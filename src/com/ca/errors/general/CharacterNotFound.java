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
package com.ca.errors.general;

import com.ca.errors.GameException;

/**
 * This exception is thrown when one character couldn't be found in the {@link com.ca.game.EntityManager}'s list.
 * @since 1.0.00
 * @author Macaluso Francesco
 */
public class CharacterNotFound extends GameException {

    public CharacterNotFound(String name) {
        super("The character '" + name + "' was not found by the entity manager.");
    }
}
