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
package com.ca.gui;

import com.ca.errors.Logger;
import com.ca.events.scenes.SceneChanged;
import com.ca.game.*;

import java.awt.*;

/**
 * This renders all the blocks in the game. It follows the various {@link com.ca.maps.Scene.Layer} of the
 * priorities.
 * 
 * @see SceneManager
 * @see EntityManager
 * @see ItemManager
 * @see GUI
 * @since 1.0.00
 * @author Macaluso Francesco
 */
public class Renderer implements SceneChanged {

    private static boolean sceneChanged = false;

    public Renderer() {
        SceneManager.addSceneListener(this);
    }

    /**
     * Renders in order all the game entities. <br>
     * <b>WARNING: </b>Do not move around the render positions. <br>
     * The print priority is the following: <br>
     * <li/> {@link SceneManager} (<b>Background</b>, <b>Middleground</b>)
     * <li/> {@link ItemManager} (<b>Background</b>, <b>Middleground</b>)
     * <li/> {@link FootStepManager} (The details decorations)
     * <li/> {@link EntityManager} (<b>All layers</b>).
     * <li/> ItemManager (<b>Foreground</b>, <b>Overlay</b>)
     * <li/> SceneManager (<b>Foreground</b>, <b>Overlay</b>)
     * <li/> {@link GUI} (<b>Graphic User Interface</b>)
     * <br>
     * @param g the graphics to draw the images on.
     */
    public void render(Graphics g) {
        try {
            SceneManager.renderBackground(g);
            ItemManager.renderBackground(g);

            FootStepManager.renderAll(g);
            EntityManager.renderAll(g);

            ItemManager.renderForeground(g);
            SceneManager.renderForeground(g);

            GUI.render(g);
        } catch (RuntimeException re) {
            Logger.log(Logger.WARNING, re.getMessage());
        }
    }

    @Override
    public void sceneChanged() {
        sceneChanged = true;
    }
}
