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

import com.ca.constants.Position;
import com.ca.entities.controllers.CharacterController;
import com.ca.game.EntityManager;
import com.ca.resources.SpritesheetLoader;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.HashMap;

public class LifeOverlay extends JComponent {

    private final HashMap<String, BufferedImage> overlay;

    public final Position position = new Position(10, 10);
    private static final int DEFAULT_WIDTH = 100;
    private static final int DEFAULT_HEIGHT = 40;

    public LifeOverlay() {
        overlay = new HashMap<>();

        overlay.put("blankHeart", SpritesheetLoader.loadSpriteResource("spritesheets/overlays/heart_blank.png"));
        overlay.put("fullHeart", SpritesheetLoader.loadSpriteResource("spritesheets/overlays/heart_full.png"));
        overlay.put("halfHeart", SpritesheetLoader.loadSpriteResource("spritesheets/overlays/heart_half.png"));

        this.setBounds(50, 50, DEFAULT_WIDTH, DEFAULT_HEIGHT);
        this.setBackground(Color.BLUE);
        this.setVisible(true);
    }

    public HashMap<String, BufferedImage> getOverlay() {
        return overlay;
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
    }
}
