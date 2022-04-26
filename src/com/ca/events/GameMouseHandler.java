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
package com.ca.events;

import com.ca.constants.Basic;

import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseWheelEvent;

/**
 * This class is only used for the zoom mouse wheel. It allows the player to zoom in and out of the window.<br>
 * <u>By default</u> the zoom is set to the minimum value.
 * @since 1.0.00
 * @author Vidoni Pietro
 */
public class GameMouseHandler extends MouseAdapter {

    public static double defaultScale = Basic.DEFAULT_SCALE_MIN;
    private static final double step = 1;

    // TODO: remove it
    public static Point mousePosition = new Point(0, 0);

    @Override
    public void mouseWheelMoved(MouseWheelEvent e) {
        if (e.isShiftDown()) {
            if (e.getWheelRotation() < 0) {
                if(defaultScale < Basic.DEFAULT_SCALE_MAX){
                    defaultScale += step;
                }
            } else {
                if(defaultScale > Basic.DEFAULT_SCALE_MIN){
                    defaultScale -= step;
                }
            }
        }
    }

    public void mouseMoved(MouseEvent e){
        mousePosition = e.getLocationOnScreen();
        System.out.println("new position");
    }

}
