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

import com.ca.constants.Assets;
import com.ca.constants.Position;
import com.ca.entities.characters.Camera;
import com.ca.entities.controllers.CharacterController;
import com.ca.events.GameMouseHandler;
import com.ca.events.GameWindowHandler;
import com.ca.game.CameraManager;

import java.awt.*;
import java.awt.geom.Rectangle2D;

/**
 * Decorates the GUI and contains helper methods for the rest of the program.
 * @since 1.0.00
 * @author Macaluso Francesco
 */
public class Decorator {

    /**
     * The fade out time delay for the text.
     */
    public static final float TEXT_FADE_OUT_DELAY = 1000.f;

    private static final long DELAY_IGN_TEXT = System.currentTimeMillis();
    private static final long DELAY_TEXT = System.currentTimeMillis();

    /**
     * Draws the character IGN on top of the character's graphic.
     * @param context the graphic context in which draw the name.
     * @param c the font color.
     * @param ctrl the character controller to draw the IGN of.
     * @param centered if set to {@code true} the text will be centered relatively to the controller's
     *                 rendering space.
     */
    public static void drawIGN(Graphics context, Color c, CharacterController ctrl, boolean centered) {

        context.setFont(Assets.FONT);

        FontMetrics metrics = context.getFontMetrics(Assets.FONT);
        Rectangle2D fontBounds = metrics.getStringBounds(ctrl.getName(), context);

        int fontOffsetX = centered ? (int) ((ctrl.getWidth() - fontBounds.getWidth()) / 2) : 0;
        int fontOffsetY = 5;

        long delay = (DELAY_IGN_TEXT + 5000) - System.currentTimeMillis();

        if (delay >= 0) {
            if (delay <= TEXT_FADE_OUT_DELAY) {
                context.setColor(new Color(c.getRed(), c.getGreen(), c.getBlue(), delay / TEXT_FADE_OUT_DELAY));
            }
            else {
                context.setColor(c);
            }

            Camera cam = CameraManager.get(Camera.MAIN_CAMERA);

            context.drawString(ctrl.getName(), ctrl.getPosition().x + fontOffsetX - cam.getPosition().x + (GameWindowHandler.getWindowWidth() / (int)(2 * GameMouseHandler.defaultScale) - (ctrl.getWidth() / 2)),
                                     ctrl.getPosition().y + fontOffsetY - cam.getPosition().y + (GameWindowHandler.getWindowHeight() / (int)(2 * GameMouseHandler.defaultScale) - (ctrl.getHeight() / 2)));
        }
    }

    public static void drawText(Graphics context, String text, Position position, long duration, boolean centered) {

        new Thread(() -> {
            context.setFont(Assets.FONT.deriveFont(Font.BOLD).deriveFont((float) Assets.FONT_HUGE_SIZE));

            FontMetrics metrics = context.getFontMetrics(Assets.FONT.deriveFont(Font.BOLD).deriveFont((float) Assets.FONT_HUGE_SIZE));
            Rectangle2D fontBounds = metrics.getStringBounds(text, context);

            int fontOffsetX = centered ? (int) (-fontBounds.getWidth() / 2) : 0;
            int fontOffsetY = 5;

            long delay = (DELAY_TEXT + (duration * 1000)) - System.currentTimeMillis();

            while (delay >= 0) {
                if (delay <= TEXT_FADE_OUT_DELAY) {
                    context.setColor(Color.WHITE);
                    //context.setColor(new Color(254.f, 254.f, 254.f, delay / TEXT_FADE_OUT_DELAY));
                } else {
                    context.setColor(Color.WHITE);
                }

                Camera cam = CameraManager.get(Camera.MAIN_CAMERA);

                context.drawString(text, (int) (position.x + fontOffsetX - cam.getPosition().x + (GameWindowHandler.getWindowWidth() / (int) (2 * GameMouseHandler.defaultScale) - (fontBounds.getWidth() / 2))),
                        (int) (position.y + fontOffsetY - cam.getPosition().y + (GameWindowHandler.getWindowHeight() / (int) (2 * GameMouseHandler.defaultScale) - (fontBounds.getHeight() / 2))));

                delay = (DELAY_TEXT + (duration * 1000)) - System.currentTimeMillis();
            }
        }, "TextDecoratorT").start();
    }
}
