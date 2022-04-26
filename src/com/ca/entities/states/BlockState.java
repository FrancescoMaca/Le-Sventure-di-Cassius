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
package com.ca.entities.states;

import com.ca.constants.Effect;
import com.ca.constants.Position;
import com.ca.resources.animations.Action;

/**
 * Represent a block state. This class is a <u>passive</u> class state, meaning that it doesn't contain all
 * the methods that other derivatives of {@link EntityState} have since the block is a passive entity.
 * @since 1.0.00
 * @author Brkic Emir
 */
public class BlockState extends EntityState {

    private Effect[] effects;

    public BlockState(Position position, Action action, Effect[] effects) {
        super(position, action);
        this.effects = effects;
    }

    public Effect[] getEffects() {
        return effects;
    }

    public void setEffects(Effect[] effects) {
        this.effects = effects;
    }

    public boolean hasEffect(Effect effect){
        for(Effect effects: effects) {
            if(effect.equals(effects)) {
                return true;
            }
        }
        return false;
    }
}
