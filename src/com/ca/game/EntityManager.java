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
import com.ca.constants.Direction;
import com.ca.constants.Position;
import com.ca.entities.characters.Player;
import com.ca.entities.controllers.CharacterController;
import com.ca.errors.Logger;
import com.ca.errors.general.CharacterNotFound;

import java.awt.*;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

/**
 * Manges and creates all entities in the game. This class knows about where entities are in game.
 * @since 1.0.00
 * @author Macaluso Francesco
 */
public class EntityManager {

    /**
     * Main list of all the entities of the game.
     */
    private static final List<CharacterController> entities = new ArrayList<>();

    public static final Player subject;

    static {
        subject = new Player(Basic.SUBJECT);
        subject.setPosition(-Basic.DEFAULT_BLOCK_WIDTH / 2, -Basic.DEFAULT_BLOCK_HEIGHT);

        entities.add(subject);
    }

    /**
     * Adds a character to the game. This method is used by the internal {@link EntityManager#initialize()} method.
     * @param entity the target entity to add.
     */
    public static void add(CharacterController entity) {

        if (entity == null) {
            Logger.log(Logger.WARNING, "An entity tried to be added to the EntityManager but it was null.");
            return;
        }

        Logger.log(Logger.MESSAGE, "The entity '" + entity.getName() + "' has been added to the EntityManager.");

        if (!entities.contains(entity)) {
            entities.add(entity);
        }
        else {
            Logger.log(Logger.WARNING, "The entity '" + entity.getName() + "' is already present in the EntityManager.");
        }
    }

    /**
     * Renders all the entity on the map. <br>
     * WARNING: This method has to be updated with an entity group to represent all the entities of a certain scene.
     * @param g the graphics where to draw the entities on.
     */
    public static void renderAll(Graphics g) {
        List<CharacterController> temp = new ArrayList<>(entities);

        temp.sort(Comparator.comparingInt(CharacterController::getPrintPriority));
        temp.sort(Comparator.comparingInt(c -> c.getPosition().y));

        for(var character : temp) {
            character.render(g);
        }
    }

    /**
     * Looks for the given entity name in the list.
     * @param name the entity's name.
     * @return the {@link CharacterController} instance of the requested entity. If the name wasn't found then
     * it returns {@code null}.
     */
    public static CharacterController get(String name) {

        for(CharacterController entity : entities) {
            if (entity.getName().equals(name)) {
                return entity;
            }
        }

        throw new CharacterNotFound(name);
    }

    /**
     * Looks for the given entity name in the list.
     * @param x the entity's name.
     * @param y the entity's name.
     * @return the {@link CharacterController} instance of the requested entity. If the name wasn't found then
     * it returns {@code null}.
     */
    public static CharacterController[] get(int x, int y ) {
        Position p = new Position(x, y);

        List<CharacterController> matches = new ArrayList<>();

        for(CharacterController ch : entities) {
            if ((Position.toGrid(ch.getPosition())).equals(p)) {
                matches.add(ch);
            }
        }

        return matches.toArray(new CharacterController[0]);
    }

    public static List<CharacterController> get() {
        return entities;
    }

    /**
     * Returns all the nearby blocks of a certain position
     * @param position the position to search from.
     * @return a list of all blocks around the given position that have a valid hitbox.
     */
    public static List<CharacterController> getNearbyCharacter(Position position) {

        Position girdCenterPos = Position.toGrid(position);

        List<CharacterController> character = new ArrayList<>();

        for(int x = girdCenterPos.x - CollisionManager.CHECK_RADIUS; x <= girdCenterPos.x + CollisionManager.CHECK_RADIUS; x++) {
            for(int y = girdCenterPos.y - CollisionManager.CHECK_RADIUS; y <= girdCenterPos.y + CollisionManager.CHECK_RADIUS; y++) {
                CharacterController[] characters = get(x, y);
                for (CharacterController characterController : characters) {
                    if (characters[0].getHitBox() != null) {
                        character.add(characterController);
                    }
                }
            }
        }

        return character;
    }

    public static void moveBot() {
        CharacterController ch = EntityManager.get(Assets.INFO_TIME_MAGE.IGN());

        Direction dY = getYDirectionToPlayer(subject.getPosition(), ch.getPosition());
        Direction dX = getXDirectionToPlayer(subject.getPosition(), ch.getPosition());

        if (dX != null) {
            ch.move(dX);
        }

        if (dY != null) {
            ch.move(dY);
        }
    }

    private static Direction getXDirectionToPlayer(Position p1, Position p2) {
        int xP = Position.toGrid(p1).x;
        int xC = Position.toGrid(p2).x;

        if(xP == xC) {
            return null;
        }

        if(xP > xC) {
            return Direction.RIGHT;
        }
        else {
            return Direction.LEFT;
        }
    }

    private static Direction getYDirectionToPlayer(Position p1, Position p2) {
        int yP = Position.toGrid(p1).y;
        int yC = Position.toGrid(p2).y;

        if(yP == yC) {
            return null;
        }

        if(yP > yC) {
            return Direction.DOWN;
        }
        else {
            return Direction.UP;
        }
    }

    /**
     * Initializes all the entities in the game.
     */
    public static void initialize() {
        CharacterController tempo = new CharacterController(Assets.INFO_TIME_MAGE);
        CharacterController zombie = new CharacterController(Assets.INFO_KING_LIZARD);

        zombie.setPosition(Position.toGrid(3,0));
        tempo.setPosition(Position.toGrid(10, 2));

        add(tempo);
        add(zombie);
    }
}
