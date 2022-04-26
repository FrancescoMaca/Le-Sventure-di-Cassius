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
package com.ca.tests;

import com.ca.constants.Clock;
import com.ca.resources.SpritesheetLoader;

import java.awt.*;
import java.awt.image.BufferStrategy;
import java.awt.image.BufferedImage;
import java.util.Random;
import javax.swing.*;

/*
 * This is an example of a simple windowed render loop
 */
public class TestClass {

    private static final Image background = new ImageIcon("C:\\Users\\franc\\Downloads\\LeSventureDiCassius\\src\\com\\ca\\resources\\test.gif").getImage();

    public static void main( String[] args ) {

        if (background == null) {
            System.err.println("problem loading image");
        }

        JFrame frame = new JFrame();

        frame.setResizable(true);
        frame.setBounds(100, 100, 800, 600);

        JPanel trgPanel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);

                g.drawImage(background,0, 0, getWidth(), getHeight(),null);
            }
        };

        frame.add(trgPanel);

        frame.setVisible(true);

        while(true) {
            frame.repaint();
        }
    }


    private static void canvasTest() {

        // Create game window...
        JFrame app = new JFrame();
        app.setIgnoreRepaint( true );
        app.setDefaultCloseOperation( JFrame.EXIT_ON_CLOSE );

        // Create canvas for painting...
        Canvas canvas = new Canvas();
        canvas.setIgnoreRepaint( true );
        canvas.setSize( 640, 480 );

        // Add canvas to game window...
        app.add( canvas );
        app.pack();
        app.setVisible( true );

        // Create BackBuffer...
        canvas.createBufferStrategy( 2 );
        BufferStrategy buffer = canvas.getBufferStrategy();

        // Get graphics configuration...
        GraphicsEnvironment ge =
                GraphicsEnvironment.getLocalGraphicsEnvironment();
        GraphicsDevice gd = ge.getDefaultScreenDevice();
        GraphicsConfiguration gc = gd.getDefaultConfiguration();

        // Create off-screen drawing surface
        BufferedImage bi = gc.createCompatibleImage( 640, 480 );

        // Objects needed for rendering...
        Graphics graphics = null;
        Graphics2D g2d = null;
        Color background = Color.BLACK;
        Random rand = new Random();

        // Variables for counting frames per seconds
        int fps = 0;
        int frames = 0;
        long totalTime = 0;
        long curTime = System.currentTimeMillis();
        long lastTime = curTime;

        while (true) {
            while (Clock.tick()) {
                try {
                    // count Frames per second...
                    lastTime = curTime;
                    curTime = System.currentTimeMillis();
                    totalTime += curTime - lastTime;
                    if (totalTime > 1000) {
                        totalTime -= 1000;
                        fps = frames;
                        frames = 0;
                    }
                    ++frames;

                    // clear back buffer...
                    g2d = bi.createGraphics();
                    g2d.setColor(background);
                    g2d.fillRect(0, 0, 639, 479);

                    // draw some rectangles...
                    for (int i = 0; i < 20; ++i) {
                        int r = rand.nextInt(256);
                        int g = rand.nextInt(256);
                        int b = rand.nextInt(256);
                        g2d.setColor(new Color(r, g, b));
                        int x = rand.nextInt(640 / 2);
                        int y = rand.nextInt(480 / 2);
                        int w = rand.nextInt(640 / 2);
                        int h = rand.nextInt(480 / 2);
                        g2d.fillRect(x, y, w, h);
                    }

                    // display frames per second...
                    g2d.setFont(new Font("Courier New", Font.PLAIN, 12));
                    g2d.setColor(Color.GREEN);
                    g2d.drawString(String.format("FPS: %s", fps), 20, 20);

                    // Blit image and flip...
                    graphics = buffer.getDrawGraphics();
                    graphics.drawImage(bi, 0, 0, null);
                    if (!buffer.contentsLost())
                        buffer.show();

                    // Let the OS have a little time...
                    Thread.yield();
                } finally {
                    // release resources
                    if (graphics != null)
                        graphics.dispose();
                    if (g2d != null)
                        g2d.dispose();
                }
            }
        }
    }
}
