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

import com.ca.constants.Basic;
import com.ca.constants.Direction;
import com.ca.entities.characters.PlayerStats;
import com.ca.entities.controllers.CharacterController;
import com.ca.entities.controllers.ItemController;
import com.ca.events.GameMouseHandler;
import com.ca.events.GameWindowHandler;
import com.ca.game.EntityManager;
import com.ca.resources.SpritesheetLoader;
import com.ca.ui.GameWindow;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.List;

/**
 * @since 1.0.00
 * @author Vidoni Pietro, Macaluso Francesco
 */
public class GUI extends Container {

    private static final CharacterController subject = EntityManager.subject;

    private static final LifeOverlay loaded = new LifeOverlay();

    private static final BufferedImage inventoryIconImage;
    private static final BufferedImage InventoryImage;
    private static final BufferedImage toolBarImage;

    //private static final Inventory backPack = new Inventory(subject, getGraphics());

    static {

        inventoryIconImage = SpritesheetLoader.loadSpriteResource("spritesheets/overlays/InventoryIcon.png");
        InventoryImage = SpritesheetLoader.loadSpriteResource("spritesheets/overlays/inventory.png");
        toolBarImage = SpritesheetLoader.loadSpriteResource("spritesheets/overlays/toolbar.png");
    }

    /**
     * The render method for the inventory, statistics and other game overlays.
     * @param g the graphics
     */
    public static void render(Graphics g) {

        // Rescales the image to the default scale
        ((Graphics2D)g).scale((1 / GameMouseHandler.defaultScale) * Basic.DEFAULT_SCALE_MIN, (1 / GameMouseHandler.defaultScale) *  Basic.DEFAULT_SCALE_MIN);

        // Prints the default overlays
        paintHealth(g);
        paintToolbar(g);

        if(Basic.SHOW_FPS) {
            paintFPS(g);
        }
    }

    private static void paintFPS(Graphics g){
        g.setColor(Color.GREEN);
        g.drawString("FPS: " + GameWindow.retrieveFPS(), (GameWindowHandler.getWindowWidth() / 2) - 45, 10);
    }

    /**
     * Prints the player current health on the screen.
     * @param g the graphics where to print the overlay on.
     */
    private static void paintHealth(Graphics g){

        int distance = 0;
        double currentHealth = subject.getStats().get("health").getCurrentValue();

        final int xOffset = 18;
        final int yOffset = 8;
        for(double hearts = 0; hearts < subject.getStats().get("health").getMaxValue(); hearts++, distance++){
            if(currentHealth - hearts > 0.5) {
                g.drawImage(loaded.getOverlay().get("fullHeart"), (xOffset * (distance)), yOffset, null);
            }
            else if (currentHealth - hearts == 0.5){
                g.drawImage(loaded.getOverlay().get("halfHeart"), (xOffset * (distance)), yOffset, null);
            }
            else {
                g.drawImage(loaded.getOverlay().get("blankHeart"), (xOffset * (distance)), yOffset, null);
            }
        }
    }

    /**
     * Prints the player's statistics on the screen.
     * @param g the graphics where to print the overlay on.
     */
    private static void printStatistics(Graphics g) {

        List<PlayerStats.Statistic> stats = subject.getStats().getStatisticsList();

        int multiplierPixelDistanceX = 208;
        int statsPixelDistanceX = 78;
        int pixelOffsetY = 18;

        for(int subjectStats = 1; subjectStats < stats.size(); ++subjectStats) {
            g.drawString(stats.get(subjectStats - 1).getName().substring(0,1).toUpperCase() + stats.get(subjectStats - 1).getName().substring(1).toLowerCase() + ": " + stats.get(subjectStats).getCurrentValue(),
                    ((GameWindowHandler.getWindowWidth() - (InventoryImage.getWidth() * 2)) / 4) + statsPixelDistanceX,
                        ((GameWindowHandler.getWindowHeight() - (InventoryImage.getHeight() * 2)) / 4) + (pixelOffsetY * subjectStats));

            g.drawString("Multiplier: " + stats.get(subjectStats - 1).getMultiplier(),
                    ((GameWindowHandler.getWindowWidth() - (InventoryImage.getWidth() * 2)) / 4) + multiplierPixelDistanceX,
                    ((GameWindowHandler.getWindowHeight() - (InventoryImage.getHeight() * 2)) / 4) + (pixelOffsetY * subjectStats));
        }
    }

    /**
     * Prints the player current toolbar on the screen.
     * @param g the graphics where to print the overlay on.
     */
    private static void paintToolbar(Graphics g){
        g.drawImage(toolBarImage,  ((GameWindowHandler.getWindowWidth() - (toolBarImage.getWidth() * 2)) / 2) - 50, ((GameWindowHandler.getWindowHeight() - (toolBarImage.getHeight() * 2)) / 2) - (toolBarImage.getHeight() / 2) - 2,null);

        BufferedImage itemInUse = resize(subject.getInventory().getItemInUse().getIcon().getCurrentFrame(Direction.DOWN), 25, 25);

        g.drawImage(itemInUse, ((GameWindowHandler.getWindowWidth() - (toolBarImage.getWidth() * 2)) / 2) - 22, ((GameWindowHandler.getWindowHeight() - (toolBarImage.getHeight() * 2)) / 2) - (toolBarImage.getHeight() / 2) + 3,null);
    }

    /**
     * Prints the player current inventory on the screen.
     * @param g the graphics where to print the overlay on.
     */
    private static void printInventory(Graphics g){

        g.drawImage(InventoryImage,  ((GameWindowHandler.getWindowWidth() - (InventoryImage.getWidth() * 2)) / 4) , ((GameWindowHandler.getWindowHeight() - (InventoryImage.getHeight()*2)) / 4),null);

        for(int i = 0; i < Basic.MAX_BACKPACK_ROWS; i++) {
            for(int j = 0; j < Basic.MAX_BACKPACK_COLUMNS; j++) {
                int posX = (GameWindowHandler.getWindowWidth() / 4) + (inventoryIconImage.getWidth() * i) - 60;
                int posY = (GameWindowHandler.getWindowHeight() / 4) + (inventoryIconImage.getHeight() * j) - 66;

                g.drawImage(inventoryIconImage, posX, posY, null);

                if(i + (Basic.MAX_BACKPACK_ROWS * j) < subject.getInventory().size() - 1 ) {
                    ItemController item = subject.getInventory().getItems().get( i + (Basic.MAX_BACKPACK_ROWS * j) + 1);
                    g.drawImage(resize(item.getIcon().getCurrentFrame(Direction.DOWN), inventoryIconImage.getWidth(), inventoryIconImage.getHeight()),posX, posY, null);
                }
            }
        }

        g.drawString(subject.getName(), (GameWindowHandler.getWindowWidth() / 4) - 140,  (GameWindowHandler.getWindowHeight() / 4) - 55);

        BufferedImage subjectIcon = SpritesheetLoader.loadSpriteResource("spritesheets/overlays/Cassius.png");

        g.drawImage(subjectIcon, (GameWindowHandler.getWindowWidth() / 4) - (subjectIcon.getWidth() * 4) + 2,
                (GameWindowHandler.getWindowHeight() / 4) - (subjectIcon.getHeight() * 4) + 170, null);
    }

    /**
     * Rescales the given image with the new coordinates.
     * @param image the image to rescale.
     * @param newW the new image width.
     * @param newH the new image height.
     */
    private static BufferedImage resize(BufferedImage image, int newW, int newH) {
        Image tmp = image.getScaledInstance(newW, newH, Image.SCALE_SMOOTH);
        BufferedImage img = new BufferedImage(newW, newH, BufferedImage.TYPE_INT_ARGB);

        Graphics2D g2d = img.createGraphics();
        g2d.drawImage(tmp, 0, 0, null);
        g2d.dispose();

        return img;
    }
}

