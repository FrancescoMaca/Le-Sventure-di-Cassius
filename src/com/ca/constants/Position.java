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
package com.ca.constants;

/**
 * Basic Coordinate object implementation. This class supports both a grid and an absolute
 * layout. The grid will stick to a {@code 32}x{@code 32} grid meanwhile the absolute layout can have any
 * coordinate.
 * @since 1.0.00
 * @author Macaluso Francesco
 */
public final class Position {

    public int x;
    public int y;

    public Position() { }

    public Position(int x, int y) {
        this.x = x;
        this.y = y;
    }

    /**
     * Sets the absolute position.
     * @param x the {@code x} coordinate.
     * @param y the {@code y} coordinate.
     */
    public void set(int x, int y) {
        this.x = x;
        this.y = y;
    }

    /**
     * Compares the coordinates x and y of the current object with the given one.
     * @param o the object to compare
     * @return {@code true} if both positions have the same coordinates, otherwise {@code false}.
     */
    @Override
    public boolean equals(Object o) {
        if (!(o instanceof Position position)) {
            return false;
        }

        return this.x == position.x && this.y == position.y;
    }

    @Override
    public String toString() {
        return "[%d, %d]".formatted(x, y);
    }

    /**
     * Translates a position to grid layout.
     * @param pos the position to transform.
     * @return the new position attached to a WxH layout, where W is {@link Basic#DEFAULT_BLOCK_WIDTH} and H is {@link Basic#DEFAULT_BLOCK_HEIGHT}.
     */
    public static Position toGrid(Position pos) {
        return new Position( pos.x / Basic.DEFAULT_BLOCK_WIDTH, pos.y / Basic.DEFAULT_BLOCK_HEIGHT);
    }

    /**
     * Creates an absolute position with grid coordinate inputs.
     * @param x the x coordinate.
     * @param y the y coordinate.
     * @return a new instance of {@link Position} multiplying both x and y respectively by {@link Basic#DEFAULT_BLOCK_WIDTH} and
     * {@link Basic#DEFAULT_BLOCK_HEIGHT}.
     */
    public static Position toGrid(int x, int y) {
        return new Position( x * Basic.DEFAULT_BLOCK_WIDTH, y * Basic.DEFAULT_BLOCK_HEIGHT);
    }
}
