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
package com.ca.game;

import com.ca.constants.Clock;
import com.ca.errors.Logger;
import com.ca.ui.GameWindow;

/**
 * This class implements the basic game loop, the refresh rate is set to a fixed amount ({@code 60}).
 * @since 1.0.00
 * @author Macaluso Francesco
 */
public class GameLoop implements Runnable {

    /**
     * The components to refresh {@code FPS} times a second.
     */
    private final GameWindow source;

    /**
     * Creates a game loop associated with the given component.
     * @param component the component to be refreshed and synchronized with the game loop.
     */
    public GameLoop(GameWindow component) {
        this.source = component;
    }

    /**
     * The main method that executes {@code nextFrame()} FPS times a second.
     */
    public void run() {
        // Removes the bottom white line on Windows 11 systems.
        source.revalidate();

        Logger.log(Logger.MESSAGE, "The game thread has started...");

        while (!Thread.currentThread().isInterrupted()) {
            if (Clock.tick()) {
                nextFrame();

                source.repaint();
            }
        }

        Logger.log(Logger.WARNING, "The thread just exited.");
    }


    /**
     * The content of this method will be called each frame.
     */
    private void nextFrame() {
        Logger.see();
    }
}
