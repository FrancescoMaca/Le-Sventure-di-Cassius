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
package com.ca.entities;

import com.ca.constants.Position;
import com.ca.entities.characters.Camera;
import com.ca.events.GameMouseHandler;
import com.ca.events.GameWindowHandler;
import com.ca.game.CameraManager;
import com.ca.game.FootStepManager;

import java.awt.*;
import java.awt.image.BufferedImage;

/**
 * This is the class representing the entity's footsteps. Whenever an entity moves, <u>if it has move enough from the last footstep</u>, it
 * prints the {@link FootStep#footStep} on the screen. After a period of time the footprint gets deleted.
 *
 * @since 1.0.00
 * @author Brkic Emir
 */
public record FootStep(BufferedImage footStep, Position position, long liveTime) {

    /**
     * Default constructor to initialize a footprint of an entity.
     *
     * @param footStep the footstep image.
     * @param position the starting position.
     * @param liveTime the time to live of the instance.
     */
    public FootStep {
    }

    /**
     * @return the footsteps' position.
     */
    public Position getPosition() {
        return position;
    }

    /**
     * @return the TTL of the instance.
     */
    public long getLiveTime() {
        return liveTime;
    }

    /**
     * Renders the footstep on screen.
     *
     * @param g the graphics where t draw the footstep on.
     */
    public void render(Graphics g) {
        Camera c = CameraManager.get(Camera.MAIN_CAMERA);

        g.drawImage(footStep, position.x - c.getPosition().x + (GameWindowHandler.getWindowWidth() / (int) (2 * GameMouseHandler.defaultScale)) - footStep.getWidth() / 2,
                position.y - c.getPosition().y + (GameWindowHandler.getWindowHeight() / (int) (2 * GameMouseHandler.defaultScale)) - footStep.getHeight() / 2,
                footStep.getWidth(), footStep.getHeight(), null);
    }
}
