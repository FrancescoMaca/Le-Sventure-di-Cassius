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
package com.ca.entities;

import com.ca.constants.Direction;
import com.ca.entities.controllers.ItemController;
import com.ca.entities.models.ItemModel;

import java.util.ArrayList;

/**
 * This class represent the logical part of the player's inventory. It holds a fixed amount of items that the
 * player can use.
 *
 * @see ItemController
 * @since 1.0
 * @author Emir Brkic
 */
public class Inventory {

    /**
     * The list containing all the items of the inventory.
     */
    private final ArrayList<ItemController> items = new ArrayList<>();

    private final ArrayList<ItemController> armors = new ArrayList<>();

    private int itemSelected;
    private int lastItemSelected;

    /**
     * @return the current item selected.
     */
    public int getItemSelected() {
        return itemSelected;
    }

    /**
     * Select the item at the given index, and it synchronizes its direction with the given one.
     * @param itemSelected the index of the selected item.
     * @param d the direction of the current player that will be passed to this item.
     */
    public void selectItem(int itemSelected, Direction d) {
        // Stop using the current item
        items.get(this.itemSelected).setInUse(false);

        // Updates indexes
        this.lastItemSelected = this.itemSelected;
        this.itemSelected = itemSelected;

        // Initializes the new item and its state
        items.get(itemSelected).setInUse(true);
        items.get(itemSelected).setDirection(d);
    }

    /**
     * Selects the last used item.
     * @param d the direction in which to set the last item to.
     */
    public void selectLastItem(Direction d) {
        items.get(lastItemSelected).setDirection(d);

        items.get(lastItemSelected).setInUse(true);
        items.get(itemSelected).setInUse(false);

        int tmp = itemSelected;
        itemSelected = lastItemSelected;
        lastItemSelected = tmp;
    }

    /**
     * Select a current item in the inventory.
     * @param item the item to select.
     */
    public void selectItem(ItemController item) {
        item.setInUse(true);

        this.lastItemSelected = itemSelected;
        this.itemSelected = items.indexOf(item);

        setUnUse(item);
    }

    /**
     * Sets all item in the inventory to unused except for the item currently selected.
     */
    private void setUnUse(ItemController item) {
       for(ItemController tempItem : items) {
           if(tempItem != item) {
               tempItem.setInUse(false);
           }
       }
    }

    /**
     * Unequips the item and therefore drops it on the ground.
     * @param d the direction of the new in-use item.
     */
    public void unequip(Direction d) {

        selectLastItem(d);

        items.get(lastItemSelected).unequip();
        items.remove(lastItemSelected);


        if(itemSelected > lastItemSelected) {
            itemSelected--;
        }

        lastItemSelected = 0;
    }

    /**
     * Adds an item to the inventory.
     * @param item the item to add.
     */
    public void add(ItemController item) {

        if(item.getItemType().equals(ItemModel.ItemType.ARMOR)) {
            armors.add(item);
            return;
        }

        items.add(item);
    }

    /**
     * @return the number of different items in the current inventory.
     */
    public int size() {
        return items.size();
    }

    /**
     * @return the {@link ItemController} instance of the item currently in use.
     */
    public ItemController getItemInUse() {
        if (itemSelected >= items.size()) {
            return null;
        }

        return items.get(itemSelected);
    }

    public ArrayList<ItemController> getItems() {
        return items;
    }

    /**
     * Checks if an item is present in the inventory.
     * @param item the item to look for.
     * @return {@code true} if any of the items in the inventory match the requested item, otherwise {@code false}.
     */
    public boolean contains(ItemController item) {
        return items.stream().noneMatch(i -> i == item);
    }
}
