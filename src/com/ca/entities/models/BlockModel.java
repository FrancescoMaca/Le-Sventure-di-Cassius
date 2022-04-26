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

import com.ca.constants.Effect;
import com.ca.entities.states.BlockState;

/**
 * Implements the logic of a block. A block is every solid and non-solid environment objects rendered in the map
 * and each one of them has its own set of statistics.
 * @since 1.0.00
 * @author Macaluso Francesco, Emir Brkic
 */
public class BlockModel extends EntityModel {

    /**
     * The block's statistics such as Effects etc...
     */
    protected final BlockState statistics;

    public BlockModel(String IGN, Effect[] effects) {
        statistics = new BlockState(position, null, effects);

        this.IGN = IGN;
    }

    /**
     * Set the block position.
     * @param x the x coordinate
     * @param y the y coordinate
     */
    public void setPosition(int x, int y) {
        position.x = x;
        position.y = y;
    }

    /**
     * @return the current block's state.
     */
    public BlockState getState() {
        return statistics;
    }

   public Effect[] getEffects() {
        return statistics.getEffects();
   }
}
