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
 * The requested camera was not found.
 * @since 1.0.00
 * @author Macaluso Francesco
 */
public class CameraNotFound extends GameException {

    public CameraNotFound(String cameraID) {
        super("The camera with id '" + cameraID + "' couldn't be found.");
    }
}
