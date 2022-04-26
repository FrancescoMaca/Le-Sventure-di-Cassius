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
package com.ca.entities.characters;

import java.awt.Rectangle;

/**
 * Basic instance of a camera that follows the player and renders all the blocks in view.
 * @since 1.0.00
 * @author Vidoni Pietro
 */
public class Camera {

    public static final String MAIN_CAMERA = "mainCamera";
    public static final Camera NULL_CAMERA = new Camera("null");

    private Rectangle subjectSize = new Rectangle();

    private com.ca.constants.Position position;

    /**
     * Camera name, used to recognize the camera for the {@link com.ca.game.CameraManager}.
     */
    private final String name;

    /**
     * Basic camera constructor.
     * @param name the name of the new camera.
     */
    public Camera(String name) {
        this.name = name;
    }

    /**
     * Sets the position of the camera.
     * @param position the new camera's position.
     */
    public void setPosition(com.ca.constants.Position position) {
        this.position = position;
    }

    /**
     * Sets the camera's subject size.
     * @param subject the subject's size.
     */
    public void setSubjectSize(Rectangle subject) {
        this.subjectSize = subject;
    }

    /**
     * @return the subject's width.
     */
    public int getSubjectWidth() {
        return subjectSize.width;
    }

    /**
     * @return the subject's height.
     */
    public int getSubjectHeight() {
        return subjectSize.height;
    }

    /**
     * @return the camera's position in absolute coordinates.
     */
    public com.ca.constants.Position getPosition() {
        return position;
    }

    /**
     * @return the camera name.
     */
    public String getName() {
        return name;
    }

    /**
     * Checks if two camera instances are the same based on their names.
     * @param camera camera object to compare.
     * @return {@code true} if the two camera's have the same name, otherwise {@code false}.
     */
    public boolean equals(Object camera) {
        if (!(camera instanceof Camera cam)) {
            return false;
        }

        return getName().equals(cam.getName());
    }
}