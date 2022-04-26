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

import com.ca.constants.Assets;
import com.ca.constants.Basic;
import com.ca.constants.Position;
import com.ca.entities.states.ItemState;
import com.ca.resources.MetadataDeserializer;

/**
 * The basic item model for each item. This model contains all logic for a game item.
 * @since 1.0.00
 * @author Macaluso Francesco, Brkic Emir, Vidoni Pietro
 */
public class ItemModel extends EntityModel {

    /**
     * The item durability can go through -1 to {@code Integer.MAX_VALUE}, each usage of the object will decrease its
     * durability, and when the object reaches 0, then you can no longer use the item.
     * In the future this can implement some way to randomize the usage of an item.
     */
    private int durability;
    private final int durabilityMax;

    /**
     * Indicates whether the item is indestructible or not.
     */
    private final boolean unbreakable;

    /**
     * The item's description that will be shown to the user.
     */
    protected String description;

    protected ItemType itemType;

    public ItemModel(Assets.Info info) {
        this.IGN = info.IGN();
        this.state = new ItemState(position, Basic.DEFAULT_ANIMATION);

        ((ItemState)state).addStatistics(MetadataDeserializer.loadItemStatistics(info.dataPath()));

        itemType = MetadataDeserializer.loadItemType(info.dataPath());


        this.durabilityMax = durability = MetadataDeserializer.loadItemDurability(info.dataPath());
        this.unbreakable = durability < 0;
    }

    /**
     * @return the in game name of the item.
     */
    public String getName() {
        return IGN;
    }

    /**
     * @return the item current state.
     */
    public ItemState getState() {
        return (ItemState) state;
    }

    public ItemType getItemType() {
        return itemType;
    }

    /**
     * @return {@code true} if the item is unbreakable, otherwise {@code false}.
     */
    public boolean isUnbreakable() {
        return unbreakable;
    }

    /**
     * @return the item's current durability.
     */
    public int getDurability() {
        return durability;
    }

    /**
     * Sets a new durability level for this item.
     * @param newDurability the new durability.
     */
    public void setDurability(int newDurability) {
        this.durability = newDurability;
    }

    /**
     * @return the item initial durability.
     */
    public int getMaxDurability() {
        return durabilityMax;
    }

    /**
     * Sets the item's absolute position.
     * @param x the x coordinate.
     * @param y the y coordinate.
     */
    public void setPosition(int x, int y) {
        position.x = x;
        position.y = y;
    }

    public void setPosition(Position p) {
        position = p;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }

    public enum ItemType{

        NO_SPECIFIED,
        WEAPON,
        POTION,
        ARMOR,
        ITEM;

        public static ItemType get(String input) {
            return switch(input) {
                case "no_specified" -> NO_SPECIFIED;
                case "armor" -> ARMOR;
                case "weapon" -> WEAPON;
                case "potion" -> POTION;
                case "item" -> ITEM;
                default -> throw new IllegalStateException("Unexpected value: " + input);
            };
        }
    }
}
