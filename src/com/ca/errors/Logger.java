/*
 * Copyright (c) 2022 Macaluso Francesco - Le Sventure di Cassius
 *
 * License under the Apache Licence, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the Licence at:
 *
 * - http://www.apache.org/licences/LINCESE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS: BASIS,
 * WITHOUT A WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permission and
 * limitations under the Licence.
 */
package com.ca.errors;

import java.util.*;
import java.util.concurrent.LinkedBlockingQueue;

/**
 * This class is used to log all messages and have them printed on the console in real time. All the messages
 * are logged in a {@link Log} queue that prints them out with different priorities and different outcomes.<br>
 * <h2>Log usage</h2>
 * <b>IMPORTANT: </b>This class handles the different exceptions too and other logic problems, if the error is
 * superficial it can be handled this way:
 * <pre>
 *      [program logic]
 *
 *      if ( secondary problem ) {
 *          Logger.log(Logger.SALVAGE, new exc()); // Custom exceptions
 *      }
 * </pre>
 * This <u>will not</u> interrupt the program, but it will notify the programmer that a certain problem has been
 * found in the code. <br>
 * <b>WARNING: </b>All salvageable exceptions are marked as warning by default so do be careful <u>from where</u>
 * the programmer calls the {@link Logger#log(int, RuntimeException)} since the class silences the same warnings when
 * exceeded {@link Logger#WARNINGS_MAX_COUNT} times. <br>
 *
 * Although, if the problem is critical (i.e. a resource not loaded) then you use the log alternative mode {@code Logger.CRITICAL}:
 * <pre>
 *     [program logic]
 *
 *     if ( critical problem ) {
 *         Logger.log(Logger.CRITICAL, new exc()); // Custom exceptions
 *     }
 * </pre>
 * This way the program will print that a fatal error has been encountered, and it will re-throw the exception and before this,
 * log all the messages since the last tick.
 * @see GameException
 * @author Macaluso Francesco
 */
public class Logger {

    /**
     * The types of error that can be handled by the logger.<br>
     * The {@code MESSAGE} type will use the standard output stream to display all different things happening. <br>
     * The {@code WARNING} type will use the error output stream, and it is used to tell the developer that something gone wrong. <br>
     * The {@code DEBUG} type will be used only for texting/debug purposes. <br>
     */
    public final static int DEBUG = 0x300;
    public final static int WARNING = 0x200;
    public final static int MESSAGE = 0x100;

    /**
     * The modes are used when logging a logic error: <br>
     * The {@code MODE_SALVAGE} is used when the error is not a big problem and can be fixed
     * without damaging the application. <br>
     * The {@code MODE_CRITICAl} is used when the error cannot be resolved (such as loading sounds
     * or other resources). When the application calls {@code log(mode, error)} without a valid mode,
     * {@code MODE_CRITICAL} is used assuming the worst, which is a critical error.<br>
     */
    public final static int MODE_SALVAGE = 0x800;
    public final static int MODE_CRITICAL = 0x900;

    /**
     * Defaults constant for the class. These will be used when the method's inputs are not valid.
     */
    private final static int TYPE_DEFAULT = MESSAGE;
    private final static int MODE_DEFAULT = MODE_CRITICAL;

    /**
     * Maximum and minimum ranges for the log details
     */
    private final static int RANGE_TYPE_MIN = 0x100;
    private final static int RANGE_TYPE_MAX = 0x300;
    private final static int RANGE_MODE_MIN = 0x800;
    private final static int RANGE_MODE_MAX = 0x900;

    /**
     * The main container for all the messages.
     */
    private final static Queue<Log> messages = new LinkedBlockingQueue<>();

    /**
     * Keeps track of the previous warning messages.
     */
    private final static HashMap<String, Integer> warnings = new HashMap<>();

    /**
     * Contains all the messages that have been logged just once.
     */
    private final static List<String> singleMessages = new ArrayList<>();

    /**
     * The number of repeated warnings before silencing it.
     */
    private final static int WARNINGS_MAX_COUNT = 4;

    /**
     * Although this is not the most user-friendly way to approach this,
     * this interval is the time between the warnings to check for. If a
     * warning has been logged {@link Logger#WARNINGS_MAX_COUNT} times repeatedly
     * under this period of time, then the warning is silenced. <br>
     * The interval time is expressed in nano-seconds for a more precise usage.
     * {@link System#currentTimeMillis()} was not enough accurate to check
     * intervals.
     */
    private final static long WARNING_INTER_TIME = 100;
    private final static long WARNING_RESET_TIME = 1000;

    /**
     * The maximum length of the warning message when suppressed. Just to clean long messages.
     */
    private final static int WARNING_MAX_LENGTH = 50;

    /**
     * Set to {@code true} if you want to hide a warning message after {@link Logger#WARNINGS_MAX_COUNT} times
     * it is shown to the user, otherwise set it to {@code false}. <br>
     * WARNING: Setting this variable to {@code false} may cause flooding in the debug console.
     */
    private final static boolean HIDE_MULTIPLE_WARNINGS = true;
    private final static boolean HIDE_ALL_WARNINGS = false;

    /**
     * Last warning time in nano-seconds.
     */
    private static long lastWarning;

    static {
        log(WARNING, "Initializing Logger class...");
        log(WARNING, "Current Logger settings:");
        log(WARNING, " - Hide multiple warnings: " + HIDE_MULTIPLE_WARNINGS);
        log(WARNING, " - Maximum warnings: " + WARNINGS_MAX_COUNT);
        log(WARNING, " - Minimum time between warnings: " + WARNING_INTER_TIME + "ns");
        log(WARNING, "Logger class successfully initialized!");
    }

    /**
     * Logs the messages with the given stream ({@code System.err} and {@code System.out}).
     * @param type the type of the message.
     * @param message the message itself.
     */
    public static void log(int type, String message) {

        // If the type of log is not valid, the default value will be assigned
        if (type < RANGE_TYPE_MIN || type > RANGE_TYPE_MAX) {
            type = TYPE_DEFAULT;
        }

        message = (message == null) ? "Unknown" : message;

        if (type == WARNING) {
            if (HIDE_ALL_WARNINGS || (HIDE_MULTIPLE_WARNINGS && checkSuppressionAndSuppress(message))) {
                return;
            }
        }

        String source = getLastTraceElement().getClassName() + "::" + getLastTraceElement().getMethodName() + "(): ";

        messages.add(new Log(type, source + message));
    }

    /**
     * Logs all the errors thrown in the game. It re-throw the exception if its critical
     * and prints to the console all the errors before throwing the actual error.
     * @param mode the mode of the log.
     * @param error the error object to analyze.
     */
    public static void log(int mode, RuntimeException error) {

        // If the logging mode is not valid, the default application will throw back the error
        // blocking the program assuming the worst
        if (mode < RANGE_MODE_MIN || mode > RANGE_MODE_MAX) {
            mode = MODE_DEFAULT;
        }

        if (mode == MODE_CRITICAL) {
            log(WARNING, "A critical error has been thrown, printing stack trace and displaying errors...");
            see();
            throw error;
        }

        // if the log is not critical then it adds it to the message queue
        log(Logger.WARNING, error.getMessage());
    }

    /**
     * Logs a message once. The messages logged with this method <u>have to be identical</u>, otherwise
     * two slightly different messages will be treated as two different strings.
     * @param type the type of the message.
     *      * @param message the message itself.
     */
    public static void logOnce(int type, String message) {
        if (singleMessages.contains(message)) {
            return;
        }

        // Logs the message
        log(type, message);

        // Adds the message to the single message list, so it won't be logged again
        singleMessages.add(message);
    }
    /**
     * Checks whether the message has to be suppressed based on its recent reports.
     * @param message the message to be displayed.
     * @return {@code true} if the message is suppressed, otherwise {@code false}.
     */
    private static boolean checkSuppressionAndSuppress(String message) {

        int timesLogged = 0;

        if (!warnings.containsKey(message)) {
            warnings.put(message, timesLogged);
        }
        else {
            timesLogged = warnings.get(message);
        }

        if (System.nanoTime() - lastWarning >= WARNING_INTER_TIME) {
            // Updates the last time we log a warning
            lastWarning = System.nanoTime();

            // Updates the message log count
            warnings.put(message, timesLogged + 1);

            // If the message has been logged less than WARNINGS_MAX_COUNT times then it just logs it
            // once more
            if (timesLogged < WARNINGS_MAX_COUNT) {
                return false;
            }

            // This will check ONCE, that is when the limit is reached, and a message will be logged that the
            // previous message has been silenced.
            if (timesLogged == WARNINGS_MAX_COUNT) {
                warnings.put(message, timesLogged + 1);

                String formattedMsg = message.strip();

                if (formattedMsg.length() > WARNING_MAX_LENGTH) {
                    formattedMsg = formattedMsg.substring(0, WARNING_MAX_LENGTH).concat("...");
                }

                log(WARNING, "Message ('" + formattedMsg + "') was silenced due to too many repetitions. (+" + WARNINGS_MAX_COUNT + ")");
            }

            // The message is silenced and the timesLogged counter is above WARNINGS_MAX_COUNT. The
            // only way to undo this is to wait more than 30000ns before logging the warning.
            return true;
        }

        // If time has passed since the last warning then it resets the counter.
        if (System.nanoTime() - lastWarning >= WARNING_RESET_TIME) {
            warnings.put(message, 1);
        }

        return false;
    }

    /**
     * Prints all the messages present in the queue.
     */
    public static void see() {
        while(!messages.isEmpty()) {
            Log msg = messages.remove();

            msg.print();
        }
    }

    /**
     * @return the {@link StackTraceElement} object of the caller of this method. This is used when the program
     * throws an exception to get information about the class and the method.
     * @param includeLogger if set to {@code true} it counts the {@link Logger} class in it's stacktrace. By default,
     *                      this flag is set to {@code false}, so the log contains the caller method and class name.
     */
    public static StackTraceElement getLastTraceElement(boolean includeLogger) {
        // Defines the last valid method call without including the Logger calls such as this and the Logger.log().
        int currentCall = 2;

        StackTraceElement element;
        StackTraceElement[] ste = new Throwable().getStackTrace();

        Class<?> currentClass = Logger.class;

        do {
            element = ste[currentCall++];

            if (currentCall >= ste.length) {
                break;
            }

            try {
                currentClass = Class.forName(element.getClassName());
            } catch(ClassNotFoundException ignored) { }

        } while((element.getClassName().equals(Logger.class.getName()) || currentClass.isAssignableFrom(GameException.class)) && !includeLogger);

        return element;
    }

    /**
     * Helper function for {@link Logger#getLastTraceElement(boolean)}.
     */
    public static StackTraceElement getLastTraceElement() {
        return getLastTraceElement(false);
    }

    /**
     * Basic Log object used to create detailed logs about the game.
     */
    private record Log(int type, String message) {

        public int getType() {
            return type;
        }

        public void print() {
            switch (type) {
                case WARNING -> System.err.println(message);
                case MESSAGE -> System.out.println(message);
                case DEBUG -> System.out.println("DEBUG: " + message);
            }
        }
    }
}
