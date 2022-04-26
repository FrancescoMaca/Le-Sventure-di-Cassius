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

import com.ca.events.animations.AnimationListener;

import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;

/**
 * This class contains the instance of the animation. The animation has four different
 * arrays of {@link BufferedImage} that contains the possible directions of the animation. <br>
 * The animations that have only one animation will have a single array in the List.
 * @since 1.0.00
 * @author Francesco Macaluso
 */
public class Animation {

    /**
     * The animation speed. This value should be between {@link com.ca.constants.Basic#DEFAULT_ANIMATION_SPEED_MIN} and
     * {@link com.ca.constants.Basic#DEFAULT_ANIMATION_SPEED_MAX}.
     */
    protected double currentFrame;
    protected double speed;
    private final BufferedImage[] frames;

    /**
     * Flage che indica se una animazione deve essere eseguita sola una volta
     */
    protected boolean runOne = false;

    /**
     * This list contains all the animation listeners. All listeners are notified whenever the animation
     * ends. See {@link AnimationListener} for more information, it can be also expanded to retrieve other
     * information about the animation itself.
     */
    private final List<AnimationListener> listeners = new ArrayList<>();

    /**
     * Default constructor.
     * @param frames the animation frames.
     * @param speed the speed to iterate through the frames.
     */
    public Animation(BufferedImage[] frames, double speed) {
        this.frames = frames;
        this.speed = speed;
    }

    /**
     * Sets the animation speed.
     * @param speed the new speed.
     */
    public void setSpeed(double speed) {
        this.speed = speed;
    }

    public void setRunOne(boolean runOne) {
        this.runOne = runOne;
    }

    /**
     * @return the animation's speed.
     */
    public double getSpeed() {
        return speed;
    }

    /**
     * @return all the animation's frame.
     */
    public BufferedImage[] getFrames() {
        return frames;
    }

    /**
     * @return the animation length.
     */
    public int getLength() {
        return frames.length;
    }

    /**
     * Adds the entities who want to be notified when the animation finishes.
     * @param a the listener object.
     */
    public void addAnimationStatusListener(AnimationListener a) {
        listeners.add(a);
    }


    public BufferedImage getCurrentFrame() {

        // Increases the frame counter
        currentFrame += speed;

        // Checks for animation end
        if (currentFrame >= frames.length) {
            if(runOne) {
                currentFrame -= speed;
            }
            else {
                currentFrame = 0;
            }


            // The animation just ended
            for(AnimationListener status : listeners) {
                status.animationEnded(this);
            }
        }

        return frames[(int)currentFrame];
    }


    public void setCurrentFrame(double currentFrame) {
        this.currentFrame = currentFrame;
    }



    /**
     * Notifies all the listeners that the animation is ended.
     */
    public void notifyListeners() {
        for(AnimationListener listener : listeners) {
            listener.animationEnded(this);
        }
    }

    public String toString() {
        return "Animation[Size: %d, Speed: %.2f, Listeners count: %d]".formatted(frames.length, speed, listeners.size());
    }
}
