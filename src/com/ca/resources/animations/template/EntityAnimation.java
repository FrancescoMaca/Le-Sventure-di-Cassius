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
package com.ca.resources.animations.template;

import com.ca.constants.Basic;
import com.ca.constants.Direction;
import com.ca.events.animations.AnimationListener;

import java.awt.image.BufferedImage;
import java.util.List;

/**
 * Basic entity animation at the base of every other animation. It provides basic functionality to access to any
 * type of animation.
 * @since 1.0.00
 * @author Macaluso Francesco
 */
public abstract class EntityAnimation {

    protected final List<Animation> animations;

    protected double currentFrame = 0;

    /**
     * The last direction used to get the animation. This is used to set the {@code currentFrame}
     * counter to {@code 0} when the user request an animation frame that has a different direction
     * from the last frame.
     */
    protected Direction lastDirection;

    public EntityAnimation(List<Animation> animations) {
        this.animations = animations;
    }

    /**
     * Sets the speed of <u>all</u> the animations objects in this entity animations.
     * @param speed the new speed value, this value should be between {@code 0.01} and {@code 1}.
     */
    public void setSpeed(double speed) {

        // Rounds the value
        speed = Math.min(speed, Basic.DEFAULT_ANIMATION_SPEED_MAX);
        speed = Math.max(Basic.DEFAULT_ANIMATION_SPEED_MIN, speed);

        double finalSpeed = speed;

        animations.forEach(a -> a.setSpeed(finalSpeed));
    }

    public double getSpeed() {
        return animations.get(0).getSpeed();
    }

    public void addAnimationListener(AnimationListener l) {
        animations.forEach(a -> a.addAnimationStatusListener(l));
    }

    /**
     * Gets the current frame of an animation with the given direction and then goes to the next frame.
     * @param d the animation direction requested.
     * @return the current animation's frame.
     */
    public BufferedImage getCurrentFrame(Direction d) {

        // If the direction is different from the last one
        // it resets the animation
        if (lastDirection != d) {
            currentFrame = 0;
        }

        if (d.ordinal() >= animations.size()) {
            d = Basic.DEFAULT_ITEM_DIRECTION;
        }

        lastDirection = d;

        Animation animation = animations.get(d.ordinal());

        // Increases the frame counter
        currentFrame += animation.getSpeed();

        // Checks for animation's end and notifies all the listeners
        if (currentFrame >= animation.getLength()) {
            currentFrame = 0;

            animation.notifyListeners();
        }

        return animation.getFrames()[((int)currentFrame)];
    }

    public double getC() {
        return  currentFrame;
    }

    public BufferedImage peekCurrentFrame() {
        return animations.get(Basic.DEFAULT_BLOCK_DIRECTION.ordinal()).getFrames()[(int)currentFrame];
    }
}
