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

import com.ca.constants.Assets;
import com.ca.constants.Position;
import com.ca.events.scenes.SceneChanged;
import com.ca.gui.Decorator;
import com.ca.gui.PostRenderer;
import com.ca.maps.GameScene;
import com.ca.maps.Scene;
import com.ca.resources.Utility;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Handles the current map and every scene to play through.
 * @since 1.0.00
 * @author Macaluso Francesco
 */
public class SceneManager {

    // Single scene for now
    private static final List<GameScene> gameScene = new ArrayList<>();
    private static final List<SceneChanged> sceneChangedListeners = new ArrayList<>();

    private static int currentScene = 0;
    private static int lastScene = -1;

    /**
     * Loads all the maps of the game, and then stores them into the map. <br>
     * <b>WARNING: </b>As of now the map storage is made of only a single map. We could
     * change that in the future.
     */
    public static void initialize() {

        for(String map : Assets.WORLD_LIST) {
            gameScene.add(new GameScene(map));
            PostRenderer.apply(gameScene.get(gameScene.size() - 1));
        }
    }

    public static void addSceneListener(SceneChanged s) {
        sceneChangedListeners.add(s);
    }

    /**
     * Set the current active scene.
     * @param index the new scene index.
     */
    public static void setCurrentScene(int index) {

        if (index < 0 || index >= gameScene.size()) {
            return;
        }

        currentScene = index;
    }

    /**
     * Renders all the background blocks.<br>
     *      * This layer includes {@link com.ca.maps.Scene.Layer#BACKGROUND} and {@link com.ca.maps.Scene.Layer#MIDDLEGROUND}.
     * @param g the graphics to draw on.
     */
    public static void renderBackground(Graphics g) {
        render(g, Scene.Layer.BACKGROUND);
        render(g, Scene.Layer.MIDDLEGROUND);
    }

    /**
     * Renders all the foreground blocks. <br>
     * This layer includes {@link com.ca.maps.Scene.Layer#FOREGROUND} and {@link com.ca.maps.Scene.Layer#OVERLAY}.
     * @param g
     */
    public static void renderForeground(Graphics g) {
        render(g, Scene.Layer.FOREGROUND);
        render(g, Scene.Layer.OVERLAY);
    }

    /**
     * Renders the map.
     * @param g the graphics where to draw the map on.
     * @param layer the block layer to render.
     */
    private static void render(java.awt.Graphics g, Scene.Layer layer) {
        gameScene.get(currentScene).render(g, layer);


        if (lastScene != currentScene) {
            lastScene = currentScene;

            // Notifies every listener that the current scene has changed
            for(SceneChanged s : sceneChangedListeners) {
                s.sceneChanged();
            }
        }
    }

    /**
     * @return the map grid.
     */
    public static Scene getGrid() {
        return gameScene.get(currentScene).getScene();
    }
}
