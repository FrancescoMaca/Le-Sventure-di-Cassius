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

import com.ca.constants.Assets;
import com.ca.entities.controllers.CharacterController;
import com.ca.entities.controllers.ItemController;
import com.ca.resources.SpritesheetLoader;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;
import java.awt.image.BufferedImage;

public class Inventory extends JPanel{

    private final BackPackCells[][] backPackCells;

    private final ArmorLayout armorLayout;

    private final BufferedImage backpackImage;

    private Point imageCorner;

    private Point previousPoint;
    private Point lastLocationOnScreen;

    public Inventory(CharacterController subject, Graphics g){

        subject.getInventory().add(new ItemController(Assets.ITEM_SWORD));

        backPackCells = new BackPackCells[2][2];

        ItemController armor = new ItemController(Assets.ITEM_CHEST_PLATE);

        for(int i = 0; i < backPackCells.length; ++i){
            for(int j = 0; j < backPackCells[i].length; ++j){
                backPackCells[i][j] = new BackPackCells(new ItemController(Assets.ITEM_HALBERD), this);
            }
        }

        addMouseListener(new ClickListener());

        addMouseMotionListener(new DragListener());

        backpackImage = SpritesheetLoader.loadSpriteResource("spritesheets/overlays/inventory.png");

        imageCorner = new Point(200,100);

        lastLocationOnScreen = imageCorner;

        armorLayout = new ArmorLayout(imageCorner);

        armorLayout.getArmorSlots().get("chest-plate").setArmor(armor);

        initializePack();
    }

    private void initializePack(){
        for(int i = 0; i < backPackCells.length; ++i){
            for(int j = 0; j < backPackCells[i].length; ++j){
                add(backPackCells[i][j]);
                backPackCells[i][j].setPosition(new Point(imageCorner.x + 108 + (backPackCells[i][j].getCellIcon().getWidth() * i), imageCorner.y + 10 + (backPackCells[i][j].getCellIcon().getHeight() * j)));
                backPackCells[i][j].setSize(25,25);
                backPackCells[i][j].setLocation(imageCorner.x + 108 + (backPackCells[i][j].getCellIcon().getWidth() * i), imageCorner.y + 10 + (backPackCells[i][j].getCellIcon().getHeight() * j));
            }
        }
    }

    private void updateIconPosition(){
        for(int i = 0; i < backPackCells.length; ++i){
            for(int j = 0; j < backPackCells[i].length; ++j){
                if(backPackCells[i][j].inInventory && backPackCells[i][j] != null) {
                    backPackCells[i][j].setPosition(new Point(imageCorner.x + 108 + (backPackCells[i][j].getCellIcon().getWidth() * i), imageCorner.y + 10 + (backPackCells[i][j].getCellIcon().getHeight() * j)));
                    backPackCells[i][j].setLocation(imageCorner.x + 108 + (backPackCells[i][j].getCellIcon().getWidth() * i), imageCorner.y + 10 + (backPackCells[i][j].getCellIcon().getHeight() * j));
                    backPackCells[i][j].setSize(backPackCells[i][j].getCellIcon().getWidth(), backPackCells[i][j].getCellIcon().getHeight());
                }
            }
        }
    }

    public void paintComponent(Graphics g){

        super.paintComponent(g);

        g.drawImage(backpackImage, imageCorner.x, imageCorner.y, null);

        for (BackPackCells[] backPackCell : backPackCells) {
            for (BackPackCells packCells : backPackCell) {
                if (packCells != null) {
                    packCells.render(g, packCells.getPosition().x, packCells.getPosition().y);
                    packCells.setPosition(new Point(packCells.getPosition().x, packCells.getPosition().y));
                }
            }
        }

        armorLayout.paintComponent(g);
    }

    public Point getPosition() {
        return imageCorner;
    }

    public void setPosition(Point imageCorner) {
        this.imageCorner = imageCorner;
    }

    private class ClickListener extends MouseAdapter{
        public void mousePressed(MouseEvent e){
            previousPoint = e.getPoint();
        }
    }

    private class DragListener extends MouseMotionAdapter{

        public void mouseDragged(MouseEvent e){
            Point currentPoint = e.getPoint();
            imageCorner.translate(currentPoint.x - previousPoint.x, currentPoint.y - previousPoint.y);
            if(e.getLocationOnScreen() != lastLocationOnScreen) {
                updateIconPosition();
                lastLocationOnScreen = e.getLocationOnScreen();
            }
            previousPoint = currentPoint;
            repaint();
        }
    }
}
