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
package com.ca.game;

import com.ca.constants.Assets;
import com.ca.constants.Basic;
import com.ca.constants.Position;
import com.ca.entities.controllers.ItemController;
import com.ca.errors.Logger;
import com.ca.maps.Scene;
import com.ca.resources.Utility;

import java.awt.*;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * Handles all the items in the game.
 *
 * @since 1.0.00
 * @author Brkic Emir
 */
public class ItemManager {

    private static final List<ItemController> items = new ArrayList<>();

    /**
     * Adds a certain item to the list.
     * @param item the item to add.
     */
    public static void add(ItemController item) {

        if (item == null) {
            Logger.log(Logger.WARNING, "An item tried to be added to the ItemManager but it was null.");
            return;
        }

        items.add(item);

        Logger.log(Logger.MESSAGE, "The item '" + item.getName() + "' has been added to the ItemManager.");
    }

    /**
     * Looks for the given entity name in the list.
     * @param x the entity's name.
     * @param y the entity's name.
     * @return an {@link ItemController} array with all the items in the given coordinates,
     * otherwise, if there are no item it returns an empty array.
     */
    public static ItemController[] get(int x, int y ) {
        Position p = new Position(x, y);

        List<ItemController> matches = new ArrayList<>();


        for(ItemController item : items) {
            Position girdCenterPos = Position.toGrid(item.getPosition());
            if (girdCenterPos.equals(p)) {
                matches.add(item);
            }
        }

        return matches.toArray(new ItemController[0]);
    }

    /**
     * Returns all the nearby blocks of a certain position
     * @param position the position to search from.
     * @return a list of all blocks around the given position that have a valid hitbox.
     */
    public static List<ItemController> getNearbyItem(Position position) {
        final int radius = 2;

        // FIXME: there cannot be more than one block with the hitbox per block
        Position girdCenterPos = Position.toGrid(position);

        List<ItemController> item = new ArrayList<>();

        for(int x = girdCenterPos.x - radius; x <= girdCenterPos.x + radius; x++) {
            for(int y = girdCenterPos.y - radius; y <= girdCenterPos.y + radius; y++) {
                ItemController[] blockItems = get(x, y);

                for (ItemController blockItem : blockItems) {
                    if (blockItems[0].getHitBox() != null) {
                        item.add(blockItem);
                    }
                }
            }
        }

        return item;
    }

    /**
     * Renders all the item in the background, this is done before the entities render.
     * @param g the graphics to draw the items on.
     */
    public static void renderBackground(Graphics g) {

        // Removes the non-useful block layers and then sorts them by print priority. So the higher print priority
        // will be printed first
        Stream<ItemController> sort = items
                .stream()
                .filter(block -> Utility.isInRange(block.getPrintPriority(), Scene.Layer.BACKGROUND.getLow(), Scene.Layer.MIDDLEGROUND.getHigh()))
                .sorted(Comparator.comparingInt(ItemController::getPrintPriority));

         for(var item : sort.collect(Collectors.toCollection(ArrayList::new))) {
             item.render(g);
         }
    }

    /**
     * Renders all the items foreground, so with a higher priority respect to the player.
     * @param g the graphics to draw the items on.
     */
    public static void renderForeground(Graphics g) {

        // Removes the non-useful block layers and then sorts them by print priority. So the higher print priority
        // will be printed first
        Stream<ItemController> sort = items
                .stream()
                .filter(block -> Utility.isInRange(block.getPrintPriority(), Scene.Layer.FOREGROUND.getLow(), Scene.Layer.OVERLAY.getHigh()))
                .sorted(Comparator.comparingInt(ItemController::getPrintPriority));

        for(var item : sort.collect(Collectors.toCollection(ArrayList::new))) {
            item.render(g);
        }
    }

    /**
     * Gets a certain item with the given name.
     * @param name the item name to look for.
     * @return an {@link ItemController} instance if found, otherwise it returns {@code null}.
     */
    public static ItemController get(String name) {

        for(ItemController item : items) {
            if (item.getName().equals(name)) {
                return item;
            }
        }

        return null;
    }

    /**
     * Destroys the target item from the game.
     * @param item the item to destroy.
     */
    public static void destroy(ItemController item) {

        ItemController target = null;
        for(ItemController i : items) {
            if (i.getName().equals(item.getName())) {
                target = i;
                break;
            }
        }

        if (target != null) {
            target.unequip();
            target.setInUse(false);
            items.remove(target);
        }
    }

    /**
     * Initializes all the items in the game.
     */
    public static void initialize() {
        ItemController sword = new ItemController(Assets.ITEM_SWORD);
        sword.setPosition(0 , 0);


        ItemController halberd = new ItemController(Assets.ITEM_HALBERD);
        halberd.setPosition(Basic.DEFAULT_BLOCK_WIDTH * 5 , Basic.DEFAULT_BLOCK_HEIGHT * 3);

        ItemController fist = new ItemController(Assets.ITEM_FIST);
        fist.setPosition(0 , 0);


        ItemController armor = new ItemController(Assets.ITEM_CHEST_PLATE);
        armor.setPosition(-Basic.DEFAULT_BLOCK_WIDTH * 4 , -Basic.DEFAULT_BLOCK_HEIGHT * 2);

        add(sword);
        add(halberd);
        add(fist);
        add(armor);
    }
}
