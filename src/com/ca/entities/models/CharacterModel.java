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
import com.ca.entities.states.CharacterState;
import com.ca.constants.Direction;
import com.ca.entities.characters.PlayerStats;
import com.ca.resources.MetadataDeserializer;
import com.ca.resources.animations.Action;

/**
 *
 * This class represent an in-game character. A character can move and do certain actions, and it has a set
 * of statistics that can be changed throughout the game.
 *
 * @since 1.0.00
 * @author Macaluso Francesco
 */
public class CharacterModel extends EntityModel {

    /**
     * The entity's statistics such as Strength, Speed, etc...
     */
    private final PlayerStats statistics;

    /**
     * Basic constructor.
     * @param ign the in game name of the character
     * @param stats the status of the character, loaded by {@link MetadataDeserializer} class.
     */
    private CharacterModel(String ign, PlayerStats stats) {
        this.IGN = ign;
        this.statistics = stats;
    }

    /**
     * Creates an instance of the character model with the given data path.
     * @param info information
     */
    public CharacterModel(Assets.Info info) {
        this(info.IGN(), MetadataDeserializer.loadStatistics(info.dataPath()));

        state = new CharacterState(position);
    }

    /**
     * Copy constructor.
     * @param model the character model to copy.
     */
    public CharacterModel(CharacterModel model) {
        this(model.IGN + " - copy", new PlayerStats(model.statistics));
        this.state = new CharacterState(model.getState());

        this.position = new Position(model.position.x, model.position.y);
        this.state = new CharacterState(model.getState());
    }

    /**
     * Moves the entity in a particular direction.
     * @param direction the move direction
     */
    public void move(Direction direction, boolean running) {


        // If the speed multiplier is not the default one it means it's running
        state.setType(running ? Action.Running : Action.Walking);

        double speed = statistics.get("speed").getCurrentValue() * statistics.get("speed").getMultiplier();

        // Moves the player in the current direction
        switch(direction) {
            case UP -> position.y -= (int)speed;
            case DOWN -> position.y += (int)speed;
            case LEFT -> position.x -= (int)speed;
            case RIGHT -> position.x += (int)speed;
        }
    }

    public void pushBack(Direction direction, double speed) {

        //double speed = statistics.get("speed").getCurrentValue() * statistics.get("speed").getMultiplier();

        // Moves the player in the current direction
        switch(direction) {
            case UP -> position.y -= (int)speed;
            case DOWN -> position.y += (int)speed;
            case LEFT -> position.x -= (int)speed;
            case RIGHT -> position.x += (int)speed;
        }
    }

    /**
     * Makes the character do the given action in the given direction.
     * @param action the action to execute
     */
    public void doAction(Action action) {
        state.setType(action);
    }

    /**
     * @return the entity's statistics.
     */
    public PlayerStats getStatistics() {
        return statistics;
    }

    /**
     * @return the current character state.
     */
    public CharacterState getState() {
        return (CharacterState) state;
    }
}
