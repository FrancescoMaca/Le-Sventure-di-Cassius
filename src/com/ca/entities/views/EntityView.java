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
package com.ca.entities.views;

import com.ca.entities.states.EntityState;
import com.ca.resources.animations.Action;
import com.ca.resources.animations.template.Animation;
import com.ca.resources.animations.CharacterAnimation;
import com.ca.resources.animations.template.EntityAnimation;

import java.awt.*;
import java.util.HashMap;

/**
 * This is the superclass of all Views objects. It represents the graphic implementation of each entity in the game.
 * @see CharacterView
 * @see ItemView
 * @see BlockView
 * @since 1.0.00
 * @author Macaluso Francesco, Brkic Emir, Vidoni Pietro
 */
public abstract class EntityView {

    /**
     * List of all the character common animations.
     */
    protected final HashMap<String, EntityAnimation> animations = new HashMap<>();

    protected Animation msgBox;

    /**
     * The spritesheet texture path for all objects in the game.
     */
    protected String texturePath;

    /**
     * The entity's default texture, if the entity supports it.
     */
    protected String defaultTexture = "";

    /**
     * @param texturePath this' entity's resource file.
     */
    protected EntityView(String texturePath) {
        this.texturePath = texturePath;

        loadAnimations();
    }

    /**
     * Copy constructors.
     * @param animations the animations to clone.
     * @param texturePath the animations' texture pack.
     */
    protected EntityView(HashMap<String, EntityAnimation> animations, String texturePath) {
        this.animations.putAll(animations);
        this.texturePath = texturePath;
    }

    /**
     * @return the requested animations of type {@link Animation} if the animation name is correct, otherwise it returns {@code null}.
     */
    protected CharacterAnimation getAnimation(Action target) {

        for(String string : animations.keySet()) {
            // Checks if the current animation is a character animation AND
            // the type it is the same as the requested type.
            // This is because an entity can only subscribe to character animations.
            if (animations.get(string) instanceof CharacterAnimation ca && ca.getType() == target) {
                return ca;
            }
        }

        return null;
    }

    /**
     * Sets the entity's default texture.
     * @param defaultTexture the new default texture.
     */
    public void setDefaultTexture(String defaultTexture) {
        this.defaultTexture = defaultTexture;
    }

    /**
     * @return the animation hashmap size.
     */
    public int getAnimationSize() {
        return animations.size();
    }

    /**
     * @return the default texture for each entity. This is used when there are no texture to be selected.
     */
    public String getDefaultTexture() {
        return defaultTexture;
    }

    /**
     * @return the animation current frame's width.
     */
    public int getFrameWidth() {
        if (animations.size() > 0) {
            return animations.get(defaultTexture).peekCurrentFrame().getWidth();
        }

        return 0;
    }

    /**
     * @return the animation current frame's height.
     */
    public int getFrameHeight() {
        if (animations.size() > 0) {
            return animations.get(defaultTexture).peekCurrentFrame().getHeight();
        }

        return 0;
    }

    /**
     * Renders the entity on the given graphics.
     * @param state the entity state.
     * @param g the graphics to draw on.
     */
    protected abstract void render(EntityState state, Graphics g);

    /**
     * Loads all the entity animations. This method will be overridden by its superclass based on
     * the entities and the animations it needs to load.
     */
    protected abstract void loadAnimations();
}
