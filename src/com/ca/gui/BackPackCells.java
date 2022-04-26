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

import com.ca.constants.Basic;
import com.ca.constants.Direction;
import com.ca.entities.controllers.ItemController;
import com.ca.events.GameKeyHandler;
import com.ca.resources.SpritesheetLoader;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;

public class BackPackCells extends JPanel {

    private final BufferedImage cellIcon;
    private ItemController item;

    private final BufferedImage itemImage;

    private long showIconDescription = 0;

    private final JPanel backPackPanel;

    private Point position;

    public boolean inInventory = true;

    {
        cellIcon = SpritesheetLoader.loadSpriteResource("spritesheets/overlays/InventoryIcon.png");
        this.setSize(cellIcon.getWidth(), cellIcon.getHeight());
    }

    public BackPackCells(ItemController item, JPanel backPackPanel) {
        this.item = item;

        this.backPackPanel = backPackPanel;

        this.addMouseListener(new ClickListener());

        position = new Point(0, 0);

        itemImage = resize(item.getIcon().getCurrentFrame(Direction.DOWN), cellIcon.getWidth(), cellIcon.getHeight());

        this.setSize(25,25);
        this.setVisible(true);
        this.setOpaque(false);
        this.setSize(cellIcon.getWidth(), cellIcon.getHeight());
    }

    public void render(Graphics g, int posX, int posY) {

        if(inInventory) {
            g.drawImage(cellIcon, posX, posY, null);

            if (item != null) {
                g.drawImage(resize(item.getIcon().getCurrentFrame(Direction.DOWN), cellIcon.getWidth(), cellIcon.getHeight()), posX, posY, null);
            }
        }
    }

    public BufferedImage getCellIcon() {
        return cellIcon;
    }

    private BufferedImage resize(BufferedImage img, int newW, int newH) {
        Image tmp = img.getScaledInstance(newW, newH, Image.SCALE_SMOOTH);
        BufferedImage dimg = new BufferedImage(newW, newH, BufferedImage.TYPE_INT_ARGB);

        Graphics2D g2d = dimg.createGraphics();
        g2d.drawImage(tmp, 0, 0, null);
        g2d.dispose();

        return dimg;
    }

    private class ClickListener extends MouseAdapter {

        @Override
        public void mouseEntered(MouseEvent e) {

            System.out.println("Enterted");

            if (System.currentTimeMillis() - showIconDescription >= Basic.DEFAULT_INVENTORY_DESCRIPTION_DELAY && inInventory) {
                drawSubWindow((Graphics2D) backPackPanel.getGraphics(), position.x, position.y, (getGraphics().getFontMetrics().stringWidth(item.getName()) * 2) + 20, 120, new Color(0, 0, 0), new Color(255, 255, 255, 255));
                showIconDescription = System.currentTimeMillis();
            }

            if(GameKeyHandler.isKeyPressed((char)KeyEvent.VK_Z)) {
                inInventory = false;
                item = null;
            }
        }

        @Override
        public void mouseExited(MouseEvent e) {
            backPackPanel.update(backPackPanel.getGraphics());
        }
    }

    private void drawSubWindow(Graphics2D g, int x, int y, int width, int height, Color c1, Color c2) {
        g.setColor(c1);
        g.fillRoundRect(x, y, width, height, 35, 35);

        g.setColor(c2);
        g.setStroke(new BasicStroke(3));
        g.drawRoundRect(x + 4, y + 4, width - 8, height - 8, 25, 25);

        g.setColor(new Color(214, 203, 203));
        g.drawString(item.getName().substring(0, 1).toUpperCase() + item.getName().substring(1).toLowerCase(), x + (width / 2) - 5, y + (height / 2) - 7);

        g.drawImage(itemImage, x + (width / 2) - 35, y + (height / 2) - itemImage.getHeight(), null);

        g.drawString("Damage: ", x + (width / 2) - 35, y + (height / 2) + 20);

        g.drawString("Range: ", x + (width / 2) - 35, y + (height / 2) + 35);

        item.setDescription("Lorem ipsum");

        if(!item.getDescription().isEmpty()) {
            g.drawString(item.getDescription(), x + (width / 2) - 35, y + (height / 2) + 50);

            int labelSize = g.getFontMetrics().stringWidth(String.valueOf(item.getDescription().charAt(0)));

            int descriptionSize = g.getFontMetrics().stringWidth(String.valueOf(item.getDescription()));

        }

    }

    public Point getPosition() {
        return position;
    }

    public void setPosition(Point position) {
        this.position = position;
    }

}
