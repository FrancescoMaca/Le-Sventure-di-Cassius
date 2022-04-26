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

import com.ca.constants.Assets;
import com.ca.constants.Basic;
import com.ca.errors.Logger;
import com.ca.errors.general.UnknownException;
import com.ca.errors.resources.ResourceNotLoaded;
import com.ca.errors.resources.SpritesheetOutOfBounds;
import com.ca.resources.animations.*;
import com.ca.resources.animations.template.Animation;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

/**
 * Loads all the game resources including animations, audio and textures.
 *
 * @see MetadataDeserializer
 * @see Animation
 * @since 1.0.00
 * @author Macaluso Francesco, Brkic Emir
 */
public class SpritesheetLoader {

    /**
     * Specifies which extensions are allowed to be loaded by this class.
     */
    private static final String[] imagesExt = { ".png", ".gif", ".bmp", ".ico" };

    /**
     * This is an empty image initialized in the static class initializer. This is used to represent all the
     * invisible images of the game, such as fists item.
     */
    public static final BufferedImage EMPTY_IMAGE;

    static {
        EMPTY_IMAGE = loadSpriteResource(Assets.PATH_OTHERS + "empty.png");
    }

    /**
     * Loads the given image-type resource into memory.
     * @param res the target resource's path.
     * @return the resource as {@link BufferedImage} instance.
     */
    public static BufferedImage loadSpriteResource(String res) {

        // If the resource doesn't match the supported formats
        Utility.checkExtension(res, imagesExt);

        // Load resources
        InputStream is = SpritesheetLoader.class.getResourceAsStream(res);

        if (is == null) {
            Logger.log(Logger.MODE_CRITICAL, new ResourceNotLoaded(res));

            return new BufferedImage(0, 0, BufferedImage.TYPE_INT_RGB);
        }

        try {
            return ImageIO.read(is);
        } catch(IOException e) {
            // All images that fail to load are considered a critical failure for the program execution. The program cannot
            // know which resources are vital for the program (such as the main block spritesheet) or not.
            Logger.log(Logger.MODE_CRITICAL, new UnknownException("The image (\"" + res + "\") couldn't be read: " + e.getMessage()));
        }

        return new BufferedImage(0, 0, BufferedImage.TYPE_INT_RGB);
    }

    /**
     * Loads an animation with the help of the {@link MetadataDeserializer} class
     * @param resource the resource where to get the animation.
     * @param action the type of animation declared in the {@link Action} class.
     * @return a new Animation with 4 directions (if the animation type supports it)
     */
    public static CharacterAnimation loadCharacterAnimation(String resource, Action action) {

        // Converts the spritesheet file path to its json metadata
        Metadata[] animationMetadata = MetadataDeserializer.loadAnimationMetadata(Utility.toJson(resource), action.getID());

        // Creates all the animation frames
        List<Animation> animations = new ArrayList<>();

        // Loads the main spritesheet
        BufferedImage spritesheet = loadSpriteResource(resource);

        // Checks if the animation spritesheet has been loaded
        if (spritesheet == null) {
            Logger.log(Logger.MODE_CRITICAL, new ResourceNotLoaded(resource));
        }

        // Loads all the animations in the metadata arraylist
        for(Metadata metadata : animationMetadata) {
            BufferedImage[] frames = new BufferedImage[metadata.getAnimationLength()];

            for(int x = metadata.getStartPosition().x, index = 0; x < metadata.getEndPosition().x && spritesheet != null; x += metadata.getWidth(), index++) {
                frames[index] = spritesheet.getSubimage(x, metadata.getStartPosition().y, metadata.getWidth(), metadata.getHeight());
            }

            animations.add(new Animation(frames, Basic.DEFAULT_ANIMATION_SPEED));
        }

        return new CharacterAnimation(animations, action);
    }

    /**
     * Loads the message box animation. This animation will pop up whenever the player faces the target entity.
     * @param resource the resource path of the message box animation.
     * @return the message box animation.
     */
    public static Animation loadMsgBoxAnimation(String resource) {

        // Converts the spritesheet file path to its json metadata
        Metadata msgBoxMetadata = MetadataDeserializer.loadMsgBoxMetadata(Utility.toJson(resource));

        // Loads the main spritesheet
        BufferedImage spritesheet = loadSpriteResource(resource);

        // Checks if the icon spritesheet has been loaded
        if (spritesheet == null) {
            Logger.log(Logger.MODE_CRITICAL, new ResourceNotLoaded(resource));
        }

        // Creates all the animation frames
        BufferedImage[] animations = new BufferedImage[msgBoxMetadata.getAnimationLength()];


        for(int x = msgBoxMetadata.getStartPosition().x, index = 0; x < msgBoxMetadata.getEndPosition().x && spritesheet != null; x += msgBoxMetadata.getWidth(), index++) {

            if (x + msgBoxMetadata.getWidth() >= spritesheet.getWidth() || msgBoxMetadata.getStartPosition().y >= spritesheet.getHeight()) {
                System.out.println((x + msgBoxMetadata.getWidth() >= spritesheet.getWidth()) + " || " + (msgBoxMetadata.getStartPosition().y >= msgBoxMetadata.getHeight()));
                Logger.log(Logger.MODE_CRITICAL, new SpritesheetOutOfBounds(resource, x + msgBoxMetadata.getWidth(), spritesheet.getWidth(), msgBoxMetadata.getStartPosition().y, spritesheet.getHeight()));
            }

            animations[index] = spritesheet.getSubimage(x, msgBoxMetadata.getStartPosition().y, msgBoxMetadata.getWidth(), msgBoxMetadata.getHeight());
        }

        return new Animation(animations, Basic.DEFAULT_ANIMATION_SPEED);
    }

    /**
     * Loads the animations of an item doing a certain action.
     * @param resource the resource {@code .json} file.
     * @param action the action to load, see {@link Action#values()}.
     * @return an {@link ItemAnimation} containing all the animations (in all directions), of the required type.
     */
    public static ItemAnimation loadItemAnimation(String resource, Action action) {

        // Converts the spritesheet file path to its json metadata
        Metadata[] animationMetadata = MetadataDeserializer.loadAnimationMetadata(Utility.toJson(resource), action.getID());

        // Creates all the animation frames
        List<Animation> animations = new ArrayList<>();

        // Loads the main spritesheet
        BufferedImage spritesheet = loadSpriteResource(resource);

        // Checks if the animation spritesheet has been loaded
        if (spritesheet == null) {
            Logger.log(Logger.MODE_CRITICAL, new ResourceNotLoaded(resource));
        }

        for(Metadata metadata : animationMetadata) {
            BufferedImage[] frames = new BufferedImage[metadata.getAnimationLength()];

            for(int x = metadata.getStartPosition().x, index = 0; x < metadata.getEndPosition().x && spritesheet != null; x += metadata.getWidth(), index++) {
                frames[index] = spritesheet.getSubimage(x, metadata.getStartPosition().y, metadata.getWidth(), metadata.getHeight());
            }

            animations.add(new Animation(frames, Basic.DEFAULT_ANIMATION_SPEED));
        }

        return new ItemAnimation(animations);
    }

    /**
     * Loads the icon image as an animation from
     * @param resource the resource animation to load.
     * @return an {@link ItemAnimation} containing the frames of the icon.
     */
    public static ItemAnimation loadIconAnimation(String resource) {

        // Converts the spritesheet file path to its json metadata
        Metadata iconMetadata = MetadataDeserializer.loadIconMetadata(Utility.toJson(resource));

        // Loads the main spritesheet
        BufferedImage spritesheet = loadSpriteResource(resource);

        // Checks if the icon spritesheet has been loaded
        if (spritesheet == null) {
            Logger.log(Logger.MODE_CRITICAL, new ResourceNotLoaded(resource));
        }

        List<Animation> animationsList = new ArrayList<>();

        // Creates all the animation frames
        BufferedImage[] animations = new BufferedImage[iconMetadata.getAnimationLength()];

        for(int x = iconMetadata.getStartPosition().x, index = 0; x < iconMetadata.getEndPosition().x && spritesheet != null; x += iconMetadata.getWidth(), index++) {
            animations[index] = spritesheet.getSubimage(x, iconMetadata.getStartPosition().y, iconMetadata.getWidth(), iconMetadata.getHeight());
        }

        animationsList.add(new Animation(animations, Basic.DEFAULT_ANIMATION_SPEED));

        return new ItemAnimation(animationsList);
    }
}
