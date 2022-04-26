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

import com.ca.constants.Position;
import com.ca.entities.HitBox;
import com.ca.entities.controllers.BlockController;
import com.ca.entities.controllers.CharacterController;
import com.ca.entities.controllers.ItemController;
import com.ca.errors.Logger;
import com.ca.errors.general.InputNotValid;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

/**
 * This class handles all the collisions between in game objects. It provides useful methods to interact directly with
 * the game's managers ({@link EntityManager},{@link BlockManager}...) and it's mainly used to check whether the current
 * player is in touch with any other in game objects. <br>
 * <b>IMPORTANT: </b>All 'nearby' checks are made using the {@link CollisionManager#CHECK_RADIUS} distance. Also, this
 * inspection is made from the upper left corner of each block, so the right and the lower side will have <u>{@code 1} block
 * less reach</u> than the other directions.
 * @since 1.0.00
 * @author Macaluso Francesco, Emir Brkic
 */
public class CollisionManager {

    /**
     * This rectangle is used as a temporary entity to check for collisions.
     */
    private static final Rectangle ghost = new Rectangle();

    /**
     * Radius for the methods checking the nearby blocks.
     */
    public static final int CHECK_RADIUS = 2;

    /**
     * Checks the intersection of the given hitbox with the items nearby the given position.
     * @param hitBox the item's interaction hitbox.
     * @param position the position where to check around.
     * @return the collection containing all the block near the given position. The collection is mutable.
     */
    public static List<ItemController> checkNearbyIntersectionItem(HitBox hitBox, Position position) {
        List<ItemController> itemIntersectionList = new ArrayList<>();

        for (ItemController item : ItemManager.getNearbyItem(position)) {
            if (intersect(hitBox, item.getHitBox())) {
                itemIntersectionList.add(item);
            }
        }

        return itemIntersectionList;
    }

    /**
     * Checks if the two given hitBoxes instance intersect with each others.
     * @param entity the subject of the move
     * @param target the target hitbox.
     * @return {@code true} if the entity intersects with the target hitbox, otherwise {@code false}.
     */
    public static boolean intersect(HitBox entity, HitBox target) {

        // Possible objects with no hitbox, treated as transparent
        if (target == null) {
            return false;
        }

        // if the entity's hitbox is null then it logs the error
        if (entity == null) {
            Logger.log(Logger.MODE_SALVAGE, new InputNotValid(HitBox.class, "null"));
            return false;
        }

        // Sets the ghost to the desired position
        ghost.setBounds(entity.getX(), entity.getY(), entity.getWidth(), entity.getHeight());

        return ghost.intersects(target.getX(), target.getY(), target.getWidth(), target.getHeight());
    }

    /**
     * Checks if the given entity's hitbox is colliding with other block's hitbox.
     * @param clone the clone to check the collisions for.
     * @return {@code true} if the given entity intersects any solid block, otherwise it returns {@code false}.
     */
    public static boolean blocksInteraction(CharacterController clone) {

        // Checks if the move is valid, if it is it moves the model
        for(BlockController block : SceneManager.getGrid().getBlocks()) {
            if(block != null && CollisionManager.intersect(clone.getHitBox(), block.getHitBox())) {
                return true;
            }
        }

        return false;
    }

    /**
     * Checks the collision of a given entity with all other entities.
     * @param clone the current entity to check the collisions for.
     * @return {@code true} if the hitbox of the given entity is intersecting someone else's, otherwise {@code false}.
     */
    public static boolean entitiesInteraction(CharacterController clone) {

        // Checks if the move is valid, if it is it moves the model
        for(CharacterController entity : EntityManager.getNearbyCharacter(clone.getPosition())) {
            if(intersect(clone.getHitBox(), entity.getHitBox()) && !clone.equals(entity)) {
                return true;
            }
        }

        return false;
    }
}
