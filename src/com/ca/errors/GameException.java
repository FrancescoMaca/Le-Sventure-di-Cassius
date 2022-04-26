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
package com.ca.errors;

/**
 * The root of all game errors. This provides a useful way to detect where the error is being called from thanks to
 * the {@link Logger} class.
 *
 * @since 1.0.00
 * @author Macaluso Francesco
 */
public class GameException extends RuntimeException {

    public GameException(String message) {
        super("\n" + Logger.getLastTraceElement().getClassName() + "::" + Logger.getLastTraceElement().getMethodName() + ": " + message);
    }
}
