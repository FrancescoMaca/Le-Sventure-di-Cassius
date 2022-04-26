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
package com.ca.sounds;

import com.ca.resources.ResourceLoader;
import javazoom.jl.player.Player;

public class AudioManager {

    private static final Thread audioThread = new Thread(AudioManager::run, "TAudio");

    public static void initialize() {
        audioThread.start();
    }

    private static void run() {
        try {
            Player p = new Player(ResourceLoader.loadFile("audios/background.mp3"));

            //p.play();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
