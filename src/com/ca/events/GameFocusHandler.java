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

import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.awt.event.KeyEvent;

/**
 * This class handles the focus with the game's focus with other window's on screen.
 * Mainly the only thing it does is that whenever the user has the game in windowed mode, and clicks outside the
 * game, the game's inputs actives are disabled. <br>
 * <b>IMPORTANT: </b>Previously when the user lost focus on the game's window, the {@link GameKeyHandler} couldn't
 * send the {@link GameKeyHandler#keyReleased(KeyEvent)} event, therefore it seemed lik the user was still pressing the
 * key(s) when the window lost focus.
 *
 * @since 1.0.00
 * @author Vidoni Pietro
 */
public class GameFocusHandler extends FocusAdapter {

    public void focusLost(FocusEvent e) {
        super.focusLost(e);
        GameKeyHandler.resetInputs();
    }

    @Override
    public void focusGained(FocusEvent e) {
        super.focusGained(e);
    }
}
