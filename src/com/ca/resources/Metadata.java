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
package com.ca.resources;

import com.ca.constants.Position;
import com.ca.resources.animations.template.Animation;

/**
 * This class represent the information needed to retrieve a single animation from a spritesheet asset.
 * It holds a starting position (upper left corner) and an end position (lower right corner). These positions
 * set the bounds of the animation, which can be of multiple frames, see {@link Animation}
 * for more info. <br>
 * The {@code width} and {@code height} variables are the dimensions of <u>each frame</u>, do not confuse this
 * with the animation bounds width and height.
 * @see Animation
 * @since 1.0.00
 * @author Macaluso Francesco
 */
public class Metadata {

    /**
     * Empty instance of the class. Instead of returning {@code null} it's suggested to return this instead.
     */
    public static final Metadata EMPTY = new Metadata(0, 0, 0, 0, 0, 0);

    /**
     * The starting and ending points of the spritesheet of the animation.
     */
    private final Position start;
    private final Position end;

    /**
     * The dimensions of every frame in the animations.
     */
    private final int width;
    private final int height;

    /**
     * Constructor to create a metadata object, due to the fact that this class is just a utility
     * class, the parameters are all the class' information. <br>
     * <b>IMPORTANT: </b>This class <u>does not support</u> different sizes for different frames.
     * @param fromX The X coordinate of the starting point of the animation on the spritesheet asset.
     * @param fromY The Y coordinate of the starting point of the animation on the spritesheet asset.
     * @param toX The X coordinate of the ending point of the animation on the spritesheet asset.
     * @param toY The Y coordinate of the ending point of the animation on the spritesheet asset.
     * @param width The width of each animation frame.
     * @param height The height of each animation frame.
     */
    public Metadata(long fromX, long fromY, long toX, long toY, long width, long height) {
        start = new Position((int)fromX, (int)fromY);
        end = new Position((int)toX, (int)toY);

        this.width = (int) width;
        this.height = (int) height;
    }

    /**
     * @return the starting point of on the animation's spritesheet.
     */
    public Position getStartPosition() {
        return start;
    }

    /**
     * @return the ending point of on the animation's spritesheet.
     */
    public Position getEndPosition() {
        return end;
    }

    /**
     * @return the animation's frame width.
     */
    public int getWidth() {
        return width;
    }

    /**
     * @return the animation's frame height.
     */
    public int getHeight() {
        return height;
    }

    /**
     * @return the number of frames of the animation. <br>
     * <b>IMPORTANT: </b> The animations have to be placed horizontally in the spritesheet to be loaded by
     * the {@link SpritesheetLoader}.
     */
    public int getAnimationLength() {
        return (end.x - start.x) / width;
    }
}
