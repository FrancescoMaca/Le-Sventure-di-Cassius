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
 * Enumerator that defines a direction that entities' action can have. <br>
 * {@link Basic} class defines a default direction {@code Basic.DEFAULT_DIRECTION} that represent the default direction
 * of the animations that do not have multiple directions.
 * @since 1.0.00
 * @author Macaluso Francesco
 */
public enum Direction {
    UP,
    DOWN,
    LEFT,
    RIGHT,
    UP_RIGHT,
    UP_LEFT,
    DOWN_RIGHT,
    DOWN_LEFT;

    /**
     * Gets a direction based on the character input.
     * @param input the character pressed
     * @return the direction bound with that letter.
     */
    public static Direction get(char input) {
        return switch(input) {
            case 'w' -> Direction.UP;
            case 's' -> Direction.DOWN;
            case 'a' -> Direction.LEFT;
            case 'd' -> Direction.RIGHT;
            default -> throw new IllegalStateException("Unexpected value: " + input);
        };
    }

    /**
     * Converts a direction and an amplitude value to a position. with range x {@code Position(+-amplitude, 0)} and
     * range y {@code Position(0, +-amplitude)}. <br>
     * @param direction the direction of the vector.
     * @param amplitude the maximum and minimum value of the vector.
     * @return an instance of {@link Position} with the amplitude in the requested direction.
     */
    public static Position convert(Direction direction, int amplitude) {
        return switch (direction) {
            case RIGHT -> new Position(amplitude, 0);
            case LEFT -> new Position(-amplitude, 0);
            case DOWN -> new Position(0, amplitude);
            case UP -> new Position(0, -amplitude);
            case UP_LEFT -> new Position(-amplitude, -amplitude);
            case UP_RIGHT -> new Position(amplitude, -amplitude);
            case DOWN_LEFT -> new Position(-amplitude, amplitude);
            case DOWN_RIGHT -> new Position(amplitude, amplitude);
        };
    }

    public static Direction getOpposite(Direction direction) {
        return switch (direction) {
            case RIGHT -> LEFT;
            case LEFT -> RIGHT;
            case DOWN -> UP;
            case UP -> DOWN;
            case UP_LEFT -> DOWN_RIGHT;
            case UP_RIGHT -> DOWN_LEFT;
            case DOWN_LEFT -> UP_RIGHT;
            case DOWN_RIGHT -> UP_LEFT;
        };
    }
}
