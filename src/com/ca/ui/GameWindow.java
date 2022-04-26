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
package com.ca.ui;

import com.ca.constants.Basic;
import com.ca.errors.Logger;
import com.ca.events.GameFocusHandler;
import com.ca.events.GameKeyHandler;
import com.ca.events.GameMouseHandler;
import com.ca.game.*;
import com.ca.gui.Renderer;
import com.ca.sounds.AudioManager;

import javax.swing.*;
import java.awt.*;

/**
 * This class is the main game panel implementation which draws the whole game on screen.
 *
 * @since 1.0.00
 * @author Macaluso Francesco
 */
public class GameWindow extends JPanel {

    private final Renderer renderer = new Renderer();

    static {
        SceneManager.initialize();
        ItemManager.initialize();
        EntityManager.initialize();
        AudioManager.initialize();
        FootStepManager.initialize();
    }

    public GameWindow() {

        this.setName("GamePanel");
        this.setDoubleBuffered(true);
        this.setIgnoreRepaint(true);
        this.setBackground(Color.black);
        this.setFocusable(true);

        // Adds the entity to the key listeners, this is the only entity that has to move with the key inputs.
        this.addKeyListener(new GameKeyHandler());
        this.addFocusListener(new GameFocusHandler());
    }

    /**
     * Starts the current thread.
     */
    public void start() {
        try {
            new Thread(new GameLoop(this), "TGameThread").start();
        } catch(Exception e) {
            e.printStackTrace();
        }
    }

    int frame = 0;
    long time = System.currentTimeMillis();

    static int fps;

    @Override
    public void paintComponent(Graphics g) {

        // Scales the game based on the default scaling factors
        ((Graphics2D)g).scale(GameMouseHandler.defaultScale, GameMouseHandler.defaultScale);

        // Renders the window
        super.paintComponent(g);

        // Renders everything of the game
        renderer.render(g);

        FootStepManager.removeOldStep();

        // Handles the bot movement
        if(Basic.BOT_FLAG) {
            EntityManager.moveBot();
        }

        // Gives you access once every second
        if (System.currentTimeMillis() - time >= 1000) {
            if (Basic.SHOW_FPS) {
                Logger.log(Logger.DEBUG, "FPS: " + frame);
                fps = frame;
            }

            time = System.currentTimeMillis();
            frame = 0;
        }

        frame++;
    }

    public static int retrieveFPS(){
        return fps;
    }


}
