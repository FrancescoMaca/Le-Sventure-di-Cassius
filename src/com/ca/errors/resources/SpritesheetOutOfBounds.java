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
package com.ca.errors.resources;

import com.ca.errors.GameException;

public class SpritesheetOutOfBounds extends GameException {

    public SpritesheetOutOfBounds(String file, int currentWidth, int maxWidth, int currentHeight, int maxHeight) {
        super("The data file (.json) of the spritesheet '" + file + "' was targeting outside the image bounds.\n" +
                "Spritesheet size: %dx%d\n".formatted(maxWidth, maxHeight) +
                "Requested position: %dx%d".formatted(currentWidth, currentHeight));
    }
}
