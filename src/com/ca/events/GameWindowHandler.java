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

import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;

/**
 * Keeps track of when the window is resized. The two values {@link GameWindowHandler#windowHeight} and {@link GameWindowHandler#windowWidth} are
 * used in the entities position calculations.
 *
 * @since 1.0.00
 * @author Vidoni Pietro
 */
public class GameWindowHandler extends ComponentAdapter {

    private static int windowHeight = Basic.FRAME_HEIGHT;
    private static int windowWidth = Basic.FRAME_WIDTH;

    /**
     * @return the current window's height.
     */
    public static int getWindowHeight() {
        return windowHeight;
    }

    /**
     * @return the current window's width.
     */
    public static int getWindowWidth() {
        return windowWidth;
    }

    @Override
    public void componentShown(ComponentEvent e) {
        super.componentShown(e);
    }

    @Override
    public void componentHidden(ComponentEvent e) {
        super.componentHidden(e);
    }

    @Override
    public void componentResized(ComponentEvent e) {

        windowHeight = e.getComponent().getHeight();
        windowWidth = e.getComponent().getWidth();

        GameKeyHandler.resetInputs();
    }
}
