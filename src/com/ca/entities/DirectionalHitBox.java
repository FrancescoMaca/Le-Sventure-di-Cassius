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
import com.ca.constants.Position;
import com.ca.errors.Logger;
import com.ca.errors.general.InputNotValid;

/**
 * The directional hitbox is used to detect entity's facing direction and includes four different hitbox.
 *
 * @since 1.0.00
 * @author Brkic Emir
 */
public class DirectionalHitBox {

    /**
     * The various offsets from the different sides.
     */
    private final int[] offsetLeft;
    private final int[] offsetRight;
    private final int[] offsetTop;
    private final int[] offsetBottom;

    /**
     * The entity position and size.
     */
    private Position position;
    private final int width;
    private final int height;

    /**
     * The current direction of the hitbox.
     */
    private Direction direction;

    /**
     * Creates a basic object bound to an entity coordinates.
     * @param position the entity's position.
     * @param width the entity's texture width.
     * @param height the entity's texture height.
     * @param offsets the offsets value.
     */
    public DirectionalHitBox(Position position, int width, int height, int[][] offsets, Direction d) {
        this.position = position;
        this.width = width;
        this.height = height;

        if (offsets == null) {
            Logger.log(Logger.MODE_SALVAGE, new InputNotValid(int[][].class,  "null"));
            offsets = new int[4][4];
        }

        // TODO: This order is very important!! LEFT-RIGHT-TOP-BOTTOM
        offsetLeft = new int[offsets[0].length];
        offsetRight = new int[offsets[1].length];
        offsetTop = new int[offsets[2].length];
        offsetBottom = new int[offsets[3].length];

        int j = 0;
        for(int[] i : offsets) {
            offsetLeft[j] = i[0];
            offsetRight[j] = i[1];
            offsetTop[j] = i[2];
            offsetBottom[j] = i[3];

            j ++;
        }

        this.direction = d;
    }

    /**
     * Sets the direction on the current directional hitbox.
     * @param d the new direction for the hitbox.
     */
    public void setDirection(Direction d) {
        this.direction = d;
    }

    /**
     * @return the current hitbox's direction.
     */
    public Direction getDirection() {
        return this.direction;
    }

    /**
     * @return the x coordinate of the hit box.
     */
    public int getX() {
        return position.x + offsetLeft[direction.ordinal()];
    }

    /**
     * @return the y coordinate of the hit box.
     */
    public int getY() {
        return position.y + offsetTop[direction.ordinal()];
    }

    /**
     * @return the width of the hit box.
     */
    public int getWidth() {
        return width - (offsetRight[direction.ordinal()] + offsetLeft[direction.ordinal()]);
    }

    /**
     * @return  the height of the hit box.
     */
    public int getHeight() {
        return height - (offsetBottom[direction.ordinal()] + offsetTop[direction.ordinal()]);
    }

    /**
     * @return the offsets in the {@link com.ca.constants.Direction} ordinal's order
     */
    public int[] getOffset() {
        return new int[] { offsetLeft[direction.ordinal()], offsetRight[direction.ordinal()], offsetTop[direction.ordinal()], offsetBottom[direction.ordinal()] };
    }

    /**
     * @return all the directional hitbox's offset.
     */
    public int[][] getOffsets() {
        return new int[][] { offsetLeft, offsetRight, offsetTop, offsetBottom };
    }

    /**
     * Sets the hitbox's position.
     * @param p the new position.
     */
    public void setPosition(Position p) {
        position = p;
    }

    /**
     * @return the directional hitbox's position.
     */
    public Position getPosition() {
        return position;
    }

    @Override
    public String toString() {
        return "[x: %d, y: %d, w: %d, h: %d]".formatted(getX(), getY(), getWidth(), getHeight());
    }

    /**
     * Converts the current directional hitbox to a normal hitbox.
     * @return the current active hitbox.
     */
    public HitBox toHitBox() {
        return new HitBox(getPosition(), getWidth(),getHeight(),getOffset());
    }
}
