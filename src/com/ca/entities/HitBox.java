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

import com.ca.constants.Position;
import com.ca.errors.Logger;
import com.ca.errors.general.InputNotValid;

/**
 * Represent an entity hit box. These coordinates are used to check entities collisions.
 * @since 1.0.00
 * @author Macaluso Francesco, Brkic Emir
 */
public class HitBox {

    /**
     * The various offsets from the different sides.
     */
    private final int offsetLeft;
    private final int offsetRight;
    private final int offsetTop;
    private final int offsetBottom;

    /**
     * The entity position and size.
     */
    private Position position;
    private final int width;
    private final int height;

    /**
     * Creates a basic object bound to an entity coordinates.
     * @param position the entity's position.
     * @param width the entity's texture width.
     * @param height the entity's texture height.
     * @param offsets the offsets value.
     */
    public HitBox(Position position, int width, int height, int[] offsets) {
        this.position = position;
        this.width = width;
        this.height = height;

        if (offsets == null) {
            Logger.log(Logger.MODE_SALVAGE, new InputNotValid("int[]",  "null"));
            offsets = new int[4];
        }

        // TODO: This order is very important!! LEFT-RIGHT-TOP-BOTTOM
        offsetLeft = offsets[0];
        offsetRight = offsets[1];
        offsetTop = offsets[2];
        offsetBottom = offsets[3];
    }

    /**
     * @return the x coordinate of the hit box.
     */
    public int getX() {
        return position.x + offsetLeft;
    }

    /**
     * @return the y coordinate of the hit box.
     */
    public int getY() {
        return position.y + offsetTop;
    }

    /**
     * @return the width of the hit box.
     */
    public int getWidth() {
        return Math.abs(width - (offsetRight + offsetLeft));
    }

    /**
     * @return  the height of the hit box.
     */
    public int getHeight() {
        return Math.abs(height - (offsetBottom + offsetTop));
    }

    /**
     * @return the offsets in the {@link com.ca.constants.Direction} ordinal's order
     */
    public int[] getOffsets() {
        return new int[] { offsetLeft, offsetRight, offsetTop, offsetBottom };
    }

    public Position getPosition() {
         return position;
    }

    @Override
    public String toString() {
        return "[x: %d, y: %d, w: %d, h: %d]".formatted(getX(), getY(), getWidth(), getHeight());
    }

    public void setPosition(Position p) {
        position = p;
    }
}
