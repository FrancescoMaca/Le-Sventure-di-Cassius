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

import com.ca.constants.Position;
import com.ca.entities.states.EntityState;
import com.ca.maps.Scene;

import java.awt.*;

/**
 * This is the super class of all entity's models. It implements the basic logic of each subclass.
 * The "Model" suffix explicits that the class only works with the logic of the object itself. <br>
 * The graphics part of the Entity is in the file {@code [EntityName]View.java}.<br>
 *
 * @since 1.0.00
 * @author Macaluso Francesco
 */
public non-sealed class EntityModel implements PrintPrioritized {

    /**
     * The entity's position.
     */
    protected Position position;

    /**
     * The In game name of the entity, the player will see this information and this is found in the {@code LOOKUP_IGNS}
     * in the {@link com.ca.constants.Assets} class. Each texture path will have its own item name.
     */
    protected String IGN;

    /**
     * The current entity state made of a type of action (Idle, Running, Walking...) and a direction
     * (Up, Down, Left, Right) if it has one. <br>
     * If the action doesn't support multiple direction (i.e. Dying action) then the default direction will be
     * Down.
     */
    protected EntityState state;

    /**
     * This value sets the z axis position of the current block. The background has the lower priority {@code 0},
     * while the foreground has the highest {@code 16}. The player has a print priority of {@code 8}. All the other
     * priorities can be found in {@link com.ca.constants.Basic} under {@code PRIORITY} prefix. <br>
     * The default priority is just a temporary value declared in {@link PrintPrioritized#DEFAULT}.
     */
    protected int printPriority = PrintPrioritized.DEFAULT;

    /**
     * The default print priority is the first ever assigned priority to this block. This is used
     * to keep track of the original priority when calculating the priority based on the player's
     * position in {@link com.ca.maps.GameScene#render(Graphics, Scene.Layer)}.
     */
    protected int defaultPrintPriority = PrintPrioritized.DEFAULT;

    public EntityModel() {
        this.position = new Position();

        IGN = "undefined";
    }

    /**
     * @return the print priority of this object.
     */
    public int getPrintPriority() {
        return printPriority;
    }

    /**
     * @return the default priority of the model.
     */
    public int getDefaultPrintPriority() {
        return defaultPrintPriority;
    }

    /**
     * Sets the new print priority.
     */
    public void setPrintPriority(int priority) {

        // Initializing the default print priority
        if (defaultPrintPriority == -1) {
            defaultPrintPriority = priority;
        }

        printPriority = priority;
    }

    /**
     * @return the current {@link com.ca.maps.Scene.Layer} in which the entity is in.
     */
    public Scene.Layer getLayer() {
        return Scene.Layer.getLayerFromPriority(getPrintPriority());
    }

    /**
     * @return the position of the entity.
     */
    public Position getPosition() {
        return position;
    }

    /**
     * @return the in game name.
     */
    public String getIGN() {
        return IGN;
    }

    /**
     * Sets the current entity's in game name.
     * @param IGN the new in game name.
     */
    public void setIGN(String IGN) {
        this.IGN = IGN;
    }
}
