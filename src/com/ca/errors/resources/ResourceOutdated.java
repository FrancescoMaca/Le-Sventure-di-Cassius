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

/**
 * The given resource was not following the current program desired syntax, therefore it could be possible that the
 * resource version is outdated.
 * @since 1.0.00
 * @author Macaluso Francesco
 */
public class ResourceOutdated extends GameException {

    public ResourceOutdated(String resource) {
        super("The resource '" + resource + "' is written with an old format style.\nThe file loading process has been interrupted.");
    }
}
