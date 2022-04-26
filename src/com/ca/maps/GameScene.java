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
package com.ca.maps;

import com.ca.constants.Basic;
import com.ca.constants.Position;
import com.ca.entities.characters.Camera;
import com.ca.entities.controllers.BlockController;
import com.ca.events.GameMouseHandler;
import com.ca.events.GameWindowHandler;
import com.ca.game.CameraManager;
import com.ca.game.EntityManager;
import com.ca.resources.MapLoader;

import java.awt.*;

/**
 * Represent a map with blocks of each time.
 * @since 1.0.00
 * @author Macaluso Francesco
 */
public class GameScene {

    /**
     * The hashmap containing all the map's blocks.
     */
    private final Scene scene = new Scene();

    /**
     * Map name.
     */
    private String name;

    /**
     * Creates a map loading the given map file.
     * @param mapFile the file containing the map data.
     */
    public GameScene(String mapFile) {
        MapLoader.initialize(this, mapFile);
    }

    /**
     * @return the map blocks.
     */
    public Scene getScene() {
        return scene;
    }

    /**
     * Sets the name of the current map.
     * @param name the map name.
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * @return the name of the current map.
     */
    public String getName() {
        return name;
    }

    /**
     * Renders the map on the screen.
     * @param g  the graphics to draw the map on.
     * @param layer the block layer to draw.
     */
    public void render(Graphics g, Scene.Layer layer) {

        // Prints all blocks in the given layer
        for(BlockController block : scene.getLayer(layer)) {

            // Handles the print priority based on the entity position
            if (block.getHitBox() != null) {
                int heightDifference = (EntityManager.get(Basic.SUBJECT.IGN()).getHitBox().getY() - (block.getHitBox().getY() + block.getHitBox().getHeight())) / block.getHitBox().getHeight();

                int newPriority = block.getDefaultPrintPriority() - heightDifference;

                // Maximum value that a block can reach, which is level 11
                int max = Scene.Layer.FOREGROUND.getHigh();

                // Minimum value that a block can reach, which is level 4
                int min = Scene.Layer.MIDDLEGROUND.getLow();

                // Sets the priority between ranges
                if (newPriority < min) {
                    block.setPrintPriority(min);
                }
                else {
                    block.setPrintPriority(Math.min(newPriority, max));
                }
            }

            block.render(g);
        }

        if (Basic.DEBUG_MODE && Basic.DEBUG_RENDER_MAP_BORDER) {

            g.setColor(Basic.DEBUG_MAP_BORDER_COLOR);

            Position scenePos = scene.getGridPosition();

            Camera c = CameraManager.get(Camera.MAIN_CAMERA);

            g.drawRect(scenePos.x - c.getPosition().x + (GameWindowHandler.getWindowWidth() / (int) (2 * GameMouseHandler.defaultScale)) - c.getSubjectWidth() / 2,
                    scenePos.y - c.getPosition().y + (GameWindowHandler.getWindowHeight() / (int) (2 * GameMouseHandler.defaultScale)) - c.getSubjectHeight() / 2,
                    getScene().getWidth() * 32, getScene().getHeight() * 32);
        }
    }
}
