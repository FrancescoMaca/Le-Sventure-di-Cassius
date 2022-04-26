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
package com.ca.gui;

import com.ca.constants.Direction;
import com.ca.constants.Position;
import com.ca.entities.controllers.ItemController;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;

public class ArmorCells extends JPanel {

    private final int CELL_WIDTH = 25;
    private final int CELL_HEIGHT = 25;

    private Position cellPosition;

    private ItemController armor;

    public ArmorCells(){
        this.setSize(CELL_WIDTH, CELL_HEIGHT);
        cellPosition = new Position();
    }

    public ItemController getArmor() {
        return armor;
    }

    public void setArmor(ItemController armor) {
        this.armor = armor;
    }

    public void setCellPosition(int x, int y) {
        this.cellPosition = new Position(x, y);
    }

    public void render(Graphics g) {

        if (armor != null) {

            BufferedImage armorImage = armor.getIcon().getCurrentFrame(Direction.DOWN);

            g.drawImage(resize(armorImage), cellPosition.x, cellPosition.y + (CELL_WIDTH / 4), null);
        }
    }

    private BufferedImage resize(BufferedImage img) {
        Image tmp = img.getScaledInstance(CELL_WIDTH, CELL_HEIGHT, Image.SCALE_SMOOTH);
        BufferedImage dimg = new BufferedImage(CELL_WIDTH, CELL_HEIGHT, BufferedImage.TYPE_INT_ARGB);

        Graphics2D g2d = dimg.createGraphics();
        g2d.drawImage(tmp, 0, 0, null);
        g2d.dispose();

        return dimg;
    }
}
