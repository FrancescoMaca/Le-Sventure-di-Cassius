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
package com.ca.resources;

import com.ca.constants.Basic;
import com.ca.constants.Position;
import com.ca.entities.HitBox;
import com.ca.entities.characters.Camera;
import com.ca.entities.states.BlockState;
import com.ca.entities.states.EntityState;
import com.ca.errors.Logger;
import com.ca.errors.general.UnknownException;
import com.ca.errors.resources.ResourceFormatInvalid;
import com.ca.events.GameMouseHandler;
import com.ca.events.GameWindowHandler;
import com.ca.game.CameraManager;

import java.awt.*;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.Random;

/**
 * This class contains many methods used around the program for utility purposes. This class improves the
 * overall code readability since it compresses long calculations inside methods.
 * @since 1.0.00
 * @author Macaluso Francesco, Vidoni Pietro
 */
public class Utility {

    /**
     * Constant random instance. Used for random operations.
     */
    private static final Random rInst = new Random();

    /**
     * Checks the given string extension
     * @param resource a resource's path
     * @throws ResourceFormatInvalid if the extension is not part of the supported formats: [.json]
     */
    public static void checkExtension(String resource, String[] pool) {
        if (Arrays.stream(pool).noneMatch(resource::endsWith)) {
            Logger.log(Logger.MODE_CRITICAL, new ResourceFormatInvalid(resource, pool));
        }
    }

    /**
     * Translates the path to its relative {@code .json} file.
     * @param path the path to translate.
     * @return the translated path.
     */
    public static String toJson(String path) {
        if (path.endsWith(".json")) {
            return path;
        }

        return path.substring(0, path.lastIndexOf(".")) + ".json";
    }

    /**
     * Calculates the entity's new position based on the current active {@link Camera} and the current window size.
     * @param state the entity's state.
     * @return a new {@link Position} instance for the entity.
     */
    public static Position getEntityScreenCoords(EntityState state) {
        Camera c = CameraManager.get(Camera.MAIN_CAMERA);

        return new Position(state.getPosition().x - c.getPosition().x + (GameWindowHandler.getWindowWidth() / (int)(2 * GameMouseHandler.defaultScale) - (c.getSubjectWidth() / 2)),
                (state.getPosition().y - c.getPosition().y + (GameWindowHandler.getWindowHeight() / (int)(2 * GameMouseHandler.defaultScale) - (c.getSubjectHeight()/ 2))));
    }

    /**
     * Calculates the hitbox position based on the current active {@link Camera} and the current window size.
     * @param hb the hitbox of the entity.
     * @return a new {@link Position} instance with the new hitbox position.
     */
    public static Position getHitBoxScreenCoords(HitBox hb) {

        Camera c = CameraManager.get(Camera.MAIN_CAMERA);

        return new Position(hb.getX() - c.getPosition().x + (GameWindowHandler.getWindowWidth() / (int)(2 * GameMouseHandler.defaultScale) - (c.getSubjectWidth() / 2)),
                (hb.getY() - c.getPosition().y + (GameWindowHandler.getWindowHeight() / (int)(2 * GameMouseHandler.defaultScale) - (c.getSubjectHeight()/ 2))));
    }

    /**
     * Checks if the given block state is in the camera view range.
     * @param state the block state to check.
     * @param range the view range.
     * @return {@code true} if the block is in range, otherwise {@code false}.
     */
    public static boolean isBlockInView(BlockState state, int range) {
        Camera c = CameraManager.get(Camera.MAIN_CAMERA);

        return Math.abs(c.getPosition().x - state.getPosition().x) - range * Basic.DEFAULT_BLOCK_WIDTH <= (GameWindowHandler.getWindowWidth() / (2 * GameMouseHandler.defaultScale)) + c.getSubjectWidth() &&
                Math.abs(c.getPosition().y - state.getPosition().y ) - range * Basic.DEFAULT_BLOCK_HEIGHT <= (GameWindowHandler.getWindowHeight() / (2 * GameMouseHandler.defaultScale)) + c.getSubjectHeight();
    }

    /**
     * Checks if a given value is within range of two other numbers.
     * @param value the value to check.
     * @param from the lower end of the range.
     * @param to the higher end of the range.
     * @return {@code true} if the value is included in the given range, otherwise {@code false}.
     */
    public static boolean isInRange(int value, int from, int to) {
        return value >= from && value <= to;
    }

    /**
     * Gets a random value between {@code 0} and {@code max}. This method is used instead of {@link Random#nextInt(int)} because
     * random is based on instance creation time, if {@code new Random().nextInt(x) } is used inside a loop the random methods will
     * always return the same value, thus this class uses a static instance of {@link Random} to solve this problem.
     * @param max the upper random value.
     * @return a random value between 0 and the given range.
     */
    public static int getRandom(int max) {
        return rInst.nextInt(max);
    }

    /**
     * <b>UNSAFE - UNUSED</b><br>
     * This is a not-tested feature to set the application fullscreen from code. This feature was not
     * tested and it can be considered a first step towards a multi-platform game implementation.
     * @param window the target window to set to fullscreen.
     */
    public static void enableOSXFullscreen(Window window) {
        try {
            Class<?> util = Class.forName("com.apple.eawt.FullScreenUtilities");
            Class<?>[] params = new Class[]{Window.class, Boolean.TYPE};

            Method method = util.getMethod("setWindowCanFullScreen", params);
            method.invoke(util, window, true);
        }
        catch (Exception e) {
            Logger.log(Logger.MODE_SALVAGE, new UnknownException(e.getMessage()));
        }
    }

    /**
     * <b>UNSAFE - UNUSED</b><br>
     * Helper method of {@link Utility#requestToggleFullScreen(Window)}.
     * @param window the window to set to fullscreen.
     */
    private static void requestToggleFullScreen(Window window)
    {
        try {
            Class<?> appClass = Class.forName("com.apple.eawt.Application");
            Class<?>[] params = new Class[] { };

            Method getApplication = appClass.getMethod("getApplication", params);
            Object application = getApplication.invoke(appClass);
            Method requestToggleFullScreen = application.getClass().getMethod("requestToggleFullScreen", Window.class);

            requestToggleFullScreen.invoke(application, window);
        } catch (Exception e) {
            Logger.log(Logger.WARNING, "An exception occurred while trying to toggle full screen mode");
        }
    }
}
