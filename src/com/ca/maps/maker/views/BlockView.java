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
package com.ca.maps.maker.views;

import com.ca.resources.SpritesheetLoader;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;

public class BlockView extends JPanel {

    private final BufferedImage bg = SpritesheetLoader.loadSpriteResource("spritesheets/test_block.png");

    public BlockView() {
        this.setLayout(null);
        this.setBackground(Color.RED);
        this.setMinimumSize(new Dimension(100, 100));
    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
    }
}
