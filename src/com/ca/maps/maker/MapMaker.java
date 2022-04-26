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
package com.ca.maps.maker;

import com.ca.maps.maker.views.MapView;

import javax.swing.*;
import java.awt.*;

/**
 * <b><h1>In progress...</h1></b>
 * @since 1.0.00
 * @author Macaluso Francesco
 */
public class MapMaker {

    private static final JFrame frame = new JFrame();

    public MapMaker() {
        frame.setResizable(false);
        frame.setLayout(new BorderLayout());
        frame.setTitle("Cassius Map Maker");
        frame.setBounds(0, 0, 800, 600);
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.add(new MapView());

        initComponents();

        frame.setVisible(true);
    }

    private void initComponents() {
        JPanel red = new JPanel();
        red.setBackground(Color.red);

        frame.add(red, BorderLayout.EAST);
    }

    public static void main(String[] args) {
        new MapMaker();
    }
}
