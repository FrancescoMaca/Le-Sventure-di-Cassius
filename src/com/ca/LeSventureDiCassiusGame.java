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
package com.ca;

import com.ca.ui.LeSventureDiCassiusWindow;

/*
 * Features:
 * - Multiple cameras supported
 * - Multiple scenes supported
 * - Screenshots supported
 * - Unified tick mechanism
 * - Logger class
 *
 * Not implemented yet.
 * - External debugging console
 * - Mapmaker tool
 * - A* searching algorithm
 * - Multiplatform support
 */
/**
 * Main application entry point.
 */
public class LeSventureDiCassiusGame {
    public static void main(String[] args) {
        new LeSventureDiCassiusWindow();
    }
}