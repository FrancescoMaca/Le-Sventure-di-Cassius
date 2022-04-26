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
package com.ca.gui.laf;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

/**
 * The button hover animation that switches the button image when the mouse hovers on the button's surface.
 * @since 1.0.00
 * @author Vidoni Pietro
 */
public class ButtonHover extends MouseAdapter {

    /**
     * The button idle image. This image will be used whenever the button is not hovered.
     */
    private final ImageIcon btNormal;

    /**
     * The button hover image. This image will be used whenever the user hovers inside the button's
     * bounds with the cursor.
     */
    private final ImageIcon btPressed;
    private final JButton button;

    /**
     * Default constructor.
     * @param subject the button source.
     * @param btNormal the non-pressed button skin.
     * @param buttonChangedImagePath the pressed button skin.
     */
    public ButtonHover(JButton subject, ImageIcon btNormal, ImageIcon buttonChangedImagePath) {
        this.btNormal = btNormal;
        this.btPressed = buttonChangedImagePath;
        this.button = subject;
    }

    /**
     * Executes whenever the mouse enters the button's rectangle.
     * @param evt the mouse event called.
     */
    public void mouseEntered(MouseEvent evt) {
        button.setBackground(Color.GREEN);
        button.setIcon(btPressed);
    }

    /**
     * Executes whenever the mouse exits the button's rectangle.
     * @param evt the mouse event called.
     */
    public void mouseExited(MouseEvent evt) {
        button.setBackground(UIManager.getColor("control"));
        button.setIcon(btNormal);
    }

}
