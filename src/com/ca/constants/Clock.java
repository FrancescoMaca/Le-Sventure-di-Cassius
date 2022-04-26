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
package com.ca.constants;

/**
 * Main thread clock. <br>
 * This class is used to synchronize all the threads together.
 * @since 1.0.00
 * @author Macaluso Francesco
 */
public final class Clock {

    private final static int FPS = Basic.GAME_FPS;
    private static long lastDraw = System.nanoTime();
    private static final double drawTime =  1e9 / FPS;

    /**
     * @return {@code true} if the thread can execute a tick, otherwise all game threads are
     * in a waiting state. This is used to set the game FPS to {@link Basic#GAME_FPS}.
     */
    public static boolean tick() {
        boolean canDraw = System.nanoTime() - lastDraw >= drawTime;

        if (canDraw) {
            lastDraw = System.nanoTime();
        }

        return canDraw;
    }

}
