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
package com.ca.errors.threads;

import com.ca.errors.GameException;

/**
 * This error is thrown whenever a thread has runtime-related problems. An example is when a thread does calculations with parameters that
 * have yet no value, the operation <u>could</u> throw an exception but when the later operation are done then it might be fixed.<br>
 * If the error is not solved then an {@link UncaughtThreadException} error is thrown.
 * @since 1.0.00
 * @author Macaluso Francesco
 */
public class ThreadNotReady extends GameException {

    public ThreadNotReady() {
        super("The current thread is not ready yet. A problem has occurred but it will be fixed meanwhile the application is running.");
    }
}
