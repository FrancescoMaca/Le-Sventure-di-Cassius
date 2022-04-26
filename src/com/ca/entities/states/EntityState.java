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

import com.ca.constants.Position;
import com.ca.resources.animations.Action;

/**
 * General state of every entity. It has the entity position and its animation. This class is made to
 * create a general object for every state.
 *
 * @see com.ca.entities.views.EntityView
 * @since 1.0.00
 * @author Macaluso Francesco, Brkic Emir
 */
public class EntityState {

    protected Position position;
    protected Action action;

    public EntityState(Position position, Action action) {
        this.position = position;
        this.action = action;
    }

    /**
     * @return the current action.
     */
    public Action getType() {
        return action;
    }

    /**
     * Sets the current {@code action}.
     * @param action the new action.
     */
    public void setType(Action action) {
        this.action = action;
    }

    /**
     * @return the entity's position.
     */
    public Position getPosition() {
        return position;
    }

    /**
     * Sets the entity's position
     * @param position the new position
     */
    public void setPosition(Position position) {
        this.position = position;
    }

}