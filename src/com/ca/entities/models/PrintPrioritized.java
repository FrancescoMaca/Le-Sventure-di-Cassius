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
package com.ca.entities.models;

import com.ca.maps.Scene;

/**
 * Interface for all models that can be printed in different graphics layers.
 * It also provides methods for accessing the print priority.
 *
 * @see com.ca.maps.Scene.Layer
 * @see EntityModel#printPriority
 * @since 1.0.00
 * @author Macaluso Francesco
 */
public sealed interface PrintPrioritized permits EntityModel {

    /**
     * Default print priority, used to signal that the priority has never been assigned before.
     */
    int DEFAULT = -1;

    int getPrintPriority();
    int getDefaultPrintPriority();
    Scene.Layer getLayer();
}
