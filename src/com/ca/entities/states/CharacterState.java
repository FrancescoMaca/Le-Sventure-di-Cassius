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

import com.ca.constants.Basic;
import com.ca.constants.Direction;
import com.ca.constants.Position;
import com.ca.resources.animations.Action;

/**
 * This class represents the character state. It is made of an {@link Action} which describes what actions are the
 * entities making and a {@link Direction} which tells the animation's direction.
 * @since 1.0.00
 * @author Macaluso Francesco, Brkic Emir
 */
public class CharacterState extends EntityState {

    private Direction direction;

    /**
     * Creates the default state object with default {@link Action} and the {@link Direction} instances. <br>
     * The default values are {@code Basic.DEFAULT_ANIMATION} and {@code Basic.DEFAULT_DIRECTION}
     */
    public CharacterState(Position position) {
        super(position, Basic.DEFAULT_ANIMATION);

        this.direction = Basic.DEFAULT_ENTITY_DIRECTION;
    }

    /**
     * Copy constructor.
     * @param state the controller you want to clone.
     */
    public CharacterState(CharacterState state) {
        super(state.position, state.action);
    }

    /**
     * @return the current state direction as {@link Direction}
     */
    public Direction getDirection() {
        return direction;
    }

    /**
     * Sets the current direction.
     * @param direction the new direction.
     */
    public void setDirection(Direction direction) {
        this.direction = direction;
    }

    @Override
    public String toString() {
        return "CharacterState[" + action + "; " + direction + "]";
    }
}
