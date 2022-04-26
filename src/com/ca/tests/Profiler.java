/*
 * Copyright (c) 2022 Macaluso Francesco
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
package com.ca.tests;

import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * <h2><b>This class is not mine</b></h2>
 * Small helper class to profile the code, take timing, ...
 *
 * To use this, simply call the start method with an identifier. When you want to measure the time, call the stop method
 * with the same identifier. To output statistics, simply call the toString method or the toCsv method to create a CSV
 * file with the profiler information.
 *
 * @since 1.0.00
 * @author Vincent Prat at MarvinLabs
 */
public class Profiler {

    private static Profiler singletonInstance = null;

    private final Map<String, Profile> profiles; // Fast access to profile by name
    private final List<Profile> profilesStack; // Profiles as created chronologically

    /**
     * Get access to the singleton instance (create it if necessary)
     */
    public static Profiler getInstance() {
        if (singletonInstance == null) {
            singletonInstance = new Profiler();
        }
        return singletonInstance;
    }

    /**
     * Protected constructor for singleton
     */
    protected Profiler() {
        profiles = new HashMap<>();
        profilesStack = new ArrayList<>();
    }

    /**
     * Start a profile. If the profile does not exist, it will be created. If it exists, a new round of measure is
     * taken.
     * @param name The name of the profile.
     */
    public void start(String name) {
        Profile p = profiles.get(name);

        if (p == null) {
            p = new Profile(name);
            profiles.put(name, p);
            profilesStack.add(p);
        }

        p.start();
    }

    /**
     * Stop a profile and compute some statistics about it.
     *
     * @param name
     *            The name of the profile as declared in the corresponding start method
     */
    public void stop(String name) {
        Profile p = profiles.get(name);
        if (p == null) {
            throw new RuntimeException("The profile " + name + " has not been created by a call to the start() method!");
        }
        p.stop();
    }

    /**
     * Clear all the current measures. Not to be called within any start/stop pair.
     */
    public void reset() {
        profiles.clear();
    }

    /**
     * Build a string containing all the information about the measures we have taken so far.
     */
    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder();

        for (Profile p : profilesStack) {
            sb.append(p.toString());
            sb.append("\n");
        }

        return sb.toString();
    }

    /**
     * Output the measures to an output string
     */
    public void toCsvFile(OutputStream os) throws IOException {
        Profile.writeCsvHeader(os);
        for (Profile p : profilesStack) {
            p.writeCsvLine(os);
        }
    }

    /**
     * Profile information. It stores statistics per named profile.
     *
     * @author Vincent Prat @ MarvinLabs
     */
    private static class Profile {
        private static final String CSV_HEADERS = "Name, Call Count, Total Time (ns), Average Time (ns), Min Time (ns), Max Time (ns), Delta Time (ns), Delta Ratio (%)\n";

        private static final int MAX_NAME_LENGTH = 15;
        private static final String FORMAT_STRING = "%-" + MAX_NAME_LENGTH + "."
                + MAX_NAME_LENGTH
                + "s: %3d calls, total %5d ns, avg %5d ns, min %5d ns, max %5d ns, delta %5d ns (%d%%)";

        private static final String CSV_FORMAT_STRING = "%s, %d, %d, %d, %d, %d, %d, %d\n";

        private final String name;
        private long startTime = 0;
        private long callCount = 0;
        private long totalTime = 0;
        private long minTime = Long.MAX_VALUE;
        private long maxTime = Long.MIN_VALUE;

        public Profile(String name) {
            this.name = name;
        }

        public void start() {
            startTime = System.nanoTime();
        }

        public void stop() {
            final long elapsed = (System.nanoTime() - startTime);
            if (elapsed < minTime) minTime = elapsed;
            if (elapsed > maxTime) maxTime = elapsed;
            totalTime += elapsed;
            callCount++;
        }

        private String getFormattedStats(String format) {
            final long avgTime = callCount == 0 ? 0 : totalTime / callCount;
            final long delta = maxTime - minTime;
            final double deltaRatio = avgTime == 0 ? 0 : 100.0 * (0.5 * delta / (double) avgTime);

            return String
                    .format(format, name, callCount, totalTime, avgTime, minTime, maxTime, delta, (int) deltaRatio);
        }

        @Override
        public String toString() {
            return getFormattedStats(FORMAT_STRING);
        }

        public static void writeCsvHeader(OutputStream os) throws IOException {
            os.write(CSV_HEADERS.getBytes());
        }

        public void writeCsvLine(OutputStream os) throws IOException {
            os.write(getFormattedStats(CSV_FORMAT_STRING).getBytes());
        }
    }
}
