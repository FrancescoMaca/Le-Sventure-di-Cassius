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
package com.ca.game;

import com.ca.constants.Assets;
import com.ca.constants.Basic;
import com.ca.constants.Position;
import com.ca.entities.FootStep;
import com.ca.errors.Logger;
import com.ca.errors.general.UnknownException;
import com.ca.resources.SpritesheetLoader;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.ConcurrentModificationException;
import java.util.List;

public class FootStepManager {

    private static final List<FootStep> steps = new ArrayList<>();

    public static BufferedImage stepsTexture;

    private static final java.util.Queue<FootStep> toBeRemoved = new ArrayDeque<>();

    public static void add(Position position) {
        if(!tooNear(new FootStep(stepsTexture,position,System.currentTimeMillis()))) {
            steps.add(new FootStep(stepsTexture, position, System.currentTimeMillis()));
        }
    }

    public static boolean tooNear(FootStep fs) {
        try {
            for (FootStep step : steps) {
                if ((Math.abs(step.getPosition().x - fs.getPosition().x) <= 20) && (Math.abs(step.getPosition().y - fs.getPosition().y) <= 20)) {
                    return true;
                }
            }
        } catch(Exception ignored) {  }

        return false;
    }

    public static void removeOldStep() {
        steps.forEach(step -> {
            if (System.currentTimeMillis() - step.getLiveTime() > Basic.STEP_TIME_LIVE) {
                toBeRemoved.add(step);
            }
        });
    }

    private static void removeQueued() {
        while(!toBeRemoved.isEmpty()) {
            steps.remove(toBeRemoved.remove());
        }
    }

    public static void renderAll(Graphics g) {
        try {
            for (FootStep step : steps) {
                step.render(g);
            }
        }
        catch(ConcurrentModificationException e) {
            Logger.log(Logger.MODE_SALVAGE, new UnknownException("Concurrent modification. " + e.getMessage()));
        }

        removeQueued();
    }

    public static void initialize() {
        stepsTexture = SpritesheetLoader.loadSpriteResource(Assets.FOOTSTEP.texturePath());
    }
}
