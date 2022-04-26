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
import com.ca.constants.Effect;
import com.ca.constants.Position;
import com.ca.resources.animations.Action;

import java.util.HashMap;

/**
 * The state of an item, this class is used in the item view to retrieve the item model's information.
 * @since 1.0.00
 * @author Brkic Emir
 */
public class ItemState extends EntityState {

    /**
     * Default item direction. The {@link Direction#UP} direction is used since its ordinal value is 0.
     */
    protected Direction direction = Basic.DEFAULT_ITEM_DIRECTION;
    private Effect[] effects;

    private boolean equipped = false;
    private boolean inUse = true;

    /**
     * Contains all the item's statistics.
     */
    protected final HashMap<String, Double> statistics = new HashMap<>();

    /**
     * Default item constructor.
     * @param position the item's position.
     * @param action the item's current action.
     */
    public ItemState(Position position, Action action) {
        super(position, action);
    }

    /**
     * @return {@code true} if the item is equipped, otherwise {@code false}.
     */
    public boolean isEquipped() {
        return equipped;
    }

    /**
     * Equips the current item, setting the flag to {@code true}.
     */
    public void equip() {
        this.equipped = true;
    }

    /**
     * Unequips the current item, setting the flag to {@code false}.
     */
    public void unEquip() {
        this.equipped = false;
    }

    /**
     * @return {@code true} if the current item is in use by any character, otherwise {@code false}.
     */
    public boolean isInUse() {
        return inUse;
    }

    /**
     * Sets the current item in use. Items in use are displayed in their character controller hands.
     * @param inUse {@code true} if the current item is being used, otherwise {@code false}.
     */
    public void setInUse(boolean inUse) {
        this.inUse = inUse;
    }

    /**
     * Adds a list of statistics to the hash map, overwriting the existing statistics.
     * @param statistics the new statistic map.
     */
    public void addStatistics(HashMap<String, Double> statistics) {
        this.statistics.putAll(statistics);
    }

    /**
     * Sets the item direction
     * @param direction the new item's direction.
     */
    public void setDirection(Direction direction) {
        this.direction = direction;
    }

    /**
     * Set the item's position.
     * @param position the new item's position.
     */
    public void setPosition(Position position) {
        this.position = position;
    }

    /**
     * Sets the item's action.
     * @param action the new item state.
     */
    public void setAction(Action action) {
        this.action = action;
    }

    /**
     * @return the current item's direction.
     */
    public Direction getDirection() {
        return direction;
    }

    /**
     * @return the current item's action.
     */
    public Action getAction() {
        return action;
    }

    public Effect[] getEffects() {
        return effects;
    }

    public HashMap<String, Double> getStatistics() {
        return statistics;
    }
}
