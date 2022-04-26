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
package com.ca.game;

import com.ca.entities.characters.Camera;
import com.ca.errors.Logger;

/**
 * Handles all the camera in the game, this class can move camera's focus around and switch between
 * different cameras in different locations.
 *
 * @since 1.0.00
 * @author Vidoni Pietro
 */
public class CameraManager {

    /**
     * Contains all the game's cameras.
     */
    private static final Camera[] camera = new Camera[1];

    /**
     * This variable stores the last value requested by the {@link CameraManager#get(String)} method.
     */
    private static Camera cached = null;

    static {
        camera[0] = new Camera("mainCamera");
    }

    /**
     * Looks for the requested camera in the camera array.
     * @param name The camera's name to search for.
     * @return the {@link Camera} instance of the requested camera if it finds it, otherwise it returns
     * {@link Camera#NULL_CAMERA}, which is a null-representing camera instance.
     */
    public static Camera get(String name) {

        // Looks for possible cached cameras
        if (cached != null && cached.getName().equals(name)) {
            return cached;
        }

        for (Camera c : camera) {
            if (c.getName().equals(name)) {
                cached = c;

                return c;
            }
        }

        // Displaying warning since the camera didn't do its job
        Logger.log(Logger.WARNING, "The requested camera doesn't exists, returning a null camera instance.");

        return Camera.NULL_CAMERA;
    }
}
