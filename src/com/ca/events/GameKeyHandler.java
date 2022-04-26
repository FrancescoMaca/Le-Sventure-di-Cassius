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

import com.ca.events.movements.MoveHandler;
import com.ca.game.EntityManager;
import com.ca.ui.Framable;

import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.HashMap;
import java.util.Map;

/**
 * Main game key handler for the keyboard inputs.
 *
 * @see MoveHandler
 * @since 1.0.00
 * @author Macaluso Francesco
 */
public class GameKeyHandler extends KeyAdapter {

    /**
     * Maps that controls all keyboard's key states.
     */
    private static final Map<Character, Boolean> keys = new HashMap<>();

    /**
     * All the screenshot listeners, this is not a list since there can only be one since the {@link Framable}
     * interface permits only a single class to implement it.
     */
    private static Framable listener;

    /**
     * Binds an entity with the key handler.
     */
    public GameKeyHandler() {
        // Run the main thread with the move handler
        try {
            new Thread(new MoveHandler(EntityManager.subject, listener), "MoveHandlerT").start();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void keyPressed(KeyEvent e) {
        keys.put((char) e.getKeyCode(), true);
    }

    public void keyReleased(KeyEvent e) {
        keys.put((char) e.getKeyCode(), false);
    }

    /**
     * Resets all the map inputs. This method is useful when something happens with the program itself such as when
     * a moving player changes focus of the window, never calling {@link GameKeyHandler#keyReleased(KeyEvent)}, therefore
     * the player will keep thinking that the user is still pressing those inputs.
     */
    public static void resetInputs() {
        for(Map.Entry<Character, Boolean> key : keys.entrySet()) {
            if (key.getValue()) {
                key.setValue(false);
            }
        }
    }

    /**
     * Checks if a given character is pressed. To check invisible characters {@code (CTRL,ALT,SHIFT...)},
     * you can use the {@link KeyEvent} static entries.
     * @param key the key value to check for.
     * @return {@code true} if the given character is pressed, otherwise it returns {@code false}.
     */
    public static boolean isKeyPressed(char key) {
        if (!keys.containsKey(key)) {
            return false;
        }

        return keys.get(key);
    }

    /**
     * Adds a framable {@link javax.swing.JComponent}.
     * @param frame the main game window to screenshot.
     */
    public static void addScreenshotListener(Framable frame) {
        listener = frame;
    }
}
