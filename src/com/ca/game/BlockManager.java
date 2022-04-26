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
package com.ca.game;

import com.ca.constants.*;
import com.ca.entities.controllers.BlockController;
import com.ca.errors.Logger;
import com.ca.errors.resources.ResourceNotLoaded;
import com.ca.maps.GameScene;
import com.ca.resources.ResourceLoader;
import com.ca.resources.SpritesheetLoader;
import com.ca.resources.animations.BlockAnimation;
import com.ca.resources.animations.template.Animation;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Loads and handles all the creation of blocks for creating the map.
 * @since 1.0.00
 * @author Macaluso Francesco, Brkic Emir
 */
public class BlockManager {

    /**
     * Contains all the blocks in the game in form of {@link BlockData} instances. This has the benefit of not
     * storing the entire block in memory but just the information to create that block.
     */
    private static final List<BlockData> blocks = new ArrayList<>();

    /**
     * The main block spritesheet from where all the blocks are being loaded.
     */
    private static final List<Assets.Info> infoBlocks = new ArrayList<>();

    static {
        // Initializes the block spritesheet
        infoBlocks.add(Assets.BLOCKS_MATERIALS);
        infoBlocks.add(Assets.BLOCKS_NATURE);
        infoBlocks.add(Assets.BLOCKS_ROMAN);

        Logger.log(Logger.MESSAGE, "Block spritesheet loaded!");

        // Loads all the blocks
        initialize();

        Logger.log(Logger.MESSAGE, "Data blocks initialization done!");
    }

    /**
     * Loads a block animation(s).
     * @param jsonTextures  the {@code .json} text containing the animation's information.
     * @param length the animation's length.
     * @param width the animation's width.
     * @param height the animation's height.
     * @return an array containing all the blocks animations. This is used to give a different look for the same
     * blocks, without making the terrain to hand-created.
     */
    private static BlockAnimation[] loadBlockAnimation(BufferedImage img, JSONArray jsonTextures, int length, int width, int height) {

        if (jsonTextures == null) {
            Logger.log(Logger.WARNING, "The texture json array is null. Maybe the file doesn't contain the tag or it is spelled incorrectly.");
            return null;
        }

        List<BlockAnimation> animations = new ArrayList<>();

        for (Object jsonTexture : jsonTextures) {

            if (!(jsonTexture instanceof JSONObject jsonTexturePosition)) {
                Logger.log(Logger.WARNING, "The json block coordinate are not valid! Skipping block...");
                continue;
            }

            Position pos = new Position(((Long) jsonTexturePosition.get("x")).intValue(), ((Long) jsonTexturePosition.get("y")).intValue());

            BufferedImage[] animation = new BufferedImage[length];

            // Loads the texture animation, if the animation is static then the length is 1
            for(int i = 0; i < length; i++) {
                // Only the x coordinate will be modified since ALL the animations are horizontal from left to right
                animation[i] = img.getSubimage(pos.x + (width * i), pos.y, width, height);
            }

            // Creates a list of a single animation
            List<Animation> listAnimation = new ArrayList<>();

            listAnimation.add(new Animation(animation, Basic.DEFAULT_ANIMATION_SPEED));

            animations.add(new BlockAnimation(listAnimation));
        }

        return animations.toArray(new BlockAnimation[0]);
    }

    /**
     * Loads a block overlays. The overlay is a portion used by the map post-processor to make it more
     * visually enjoyable printing the texture's border on top of the adjacent block.
     * @param jsonOverlays the {@code .json} text containing the overlay's information.
     * @param width the overlay's width.
     * @param height the overlay's height.
     * @return an array of images to be accessed with {@link Direction#values()}'s ordinal number.
     */
    private static BufferedImage[] loadBlockOverlay(BufferedImage img, JSONArray jsonOverlays, int width, int height) {

        if (jsonOverlays == null) {
            Logger.log(Logger.DEBUG, "The overlay json array is null. Maybe the file doesn't contain the tag or it is spelled incorrectly.");
            BufferedImage[] tmp = new BufferedImage[Direction.values().length];

            Arrays.fill(tmp,SpritesheetLoader.EMPTY_IMAGE);

            return tmp;
        }

        // Creating a list this way so the elements are actually there to be changed by the `.add(index, obj)`
        // method. 'new ArrayList(size)' was not initializing the size but the capacity. This way `.add()`
        // doesn't throw OutOfBoundsException.
        List<BufferedImage> overlays = Arrays.asList(new BufferedImage[Direction.values().length]);

        for (Object jsonOverlay : jsonOverlays) {
            if (!(jsonOverlay instanceof JSONObject jsonOverlayPosition)) {
                Logger.log(Logger.DEBUG, "The json overlay coordinate are not valid! Skipping overlay...");
                continue;
            }
            Position pos = new Position(((Long) jsonOverlayPosition.get("x")).intValue(), ((Long) jsonOverlayPosition.get("y")).intValue());

            try {
                String direction = (String) jsonOverlayPosition.get("direction");

                Direction dir = Direction.valueOf((direction.toUpperCase()));

                overlays.set(dir.ordinal(), img.getSubimage(pos.x, pos.y, width, height));
            }
            catch(IllegalArgumentException unused) {
                break;
            }
        }

        return overlays.toArray(new BufferedImage[0]);
    }

    /**
     * Loads the block effects.
     * @param jsonEffects the jsonArray containing all the effects.
     * @return an {@link Effect} array containing the block's effects.
     */
    private static Effect[] loadEffects(JSONArray jsonEffects) {

        if (jsonEffects == null) {
            Logger.log(Logger.DEBUG, "The effect json array is null. Maybe the file doesn't contain the tag or it is spelled incorrectly.");
            return null;
        }

        Effect[] effects = new Effect[jsonEffects.size()];

        for (int i = 0; i < jsonEffects.size(); i++) {
            effects[i] = Effect.valueOf(jsonEffects.get(i).toString());
        }

        return effects;
    }

    /**
     * Loads the block hitbox from a {@code .json} array object.
     * @param jsonHitBox the {@code .json} array instance.
     * @return the array with the hit box offsets.
     */
    private static int[] loadBlockHitBox(JSONArray jsonHitBox) {

        final int hitBoxSides = 4;

        if (jsonHitBox == null) {
            Logger.log(Logger.DEBUG, "The hit box json array is null. Maybe the file doesn't contain the tag or it is spelled incorrectly.");
            return null;
        }

        if (hitBoxSides != jsonHitBox.size()) {
            Logger.log(Logger.DEBUG, "The hit box size is different from the common one. It should have '" + hitBoxSides + "' values, but it has '" + jsonHitBox.size() + "'.");
            return null;
        }

        int[] hitBox = new int[hitBoxSides];

        for(int i = 0; i < jsonHitBox.size(); i++) {
            hitBox[i] = ((Long) jsonHitBox.get(i)).intValue();
        }

        return hitBox;
    }

    /**
     * Loads the blocks that connect with the current block.
     * @param jsonConnections the connection blocks {@code json} array.
     * @return a string array that containing all the block types. If the array contains '{@code *}' character
     * then the current block will connect with every block around it.
     */
    private static String[] loadBlockConnectionBlocks(JSONArray jsonConnections) {
        if (jsonConnections == null) {
            return new String[0];
        }

        List<String> blocks = new ArrayList<>();

        for(Object o : jsonConnections) {
            if (!(o instanceof String blockName)) {
                Logger.log(Logger.WARNING, "The object '" + o + "' has to be a block name");
                continue;
            }

            blocks.add(blockName);
        }

        return blocks.toArray(new String[0]);
    }

    /**
     * Reads all blocks from the {@code .json} files and then loads them into this class. The blocks
     * will have only a {@link BlockData} reference, from which it can retrieve the {@link BlockController}
     * instance.
     */
    private static void initialize() {

        for (Assets.Info iBlock : infoBlocks) {

            JSONArray jsonBlockFile = (JSONArray) ResourceLoader.loadJsonFile(iBlock.dataPath());

            if (jsonBlockFile == null) {
                Logger.log(Logger.MODE_CRITICAL, new ResourceNotLoaded(iBlock.dataPath()));
                return;
            }

            BufferedImage targetSheet = SpritesheetLoader.loadSpriteResource(iBlock.texturePath());

            for(Object jsonObject : jsonBlockFile) {
                if (!(jsonObject instanceof JSONObject jsonBlock)) {
                    continue;
                }

                String name = (String) jsonBlock.get("id");

                int printPriority = ((Long)jsonBlock.get("priority")).intValue();
                int width = ((Long)jsonBlock.get("width")).intValue();
                int height = ((Long)jsonBlock.get("height")).intValue();
                int count = ((Long)jsonBlock.get("count")).intValue();

                int[] hitBox = loadBlockHitBox((JSONArray) jsonBlock.get("hitbox"));

                String[] connectsWith = loadBlockConnectionBlocks((JSONArray) jsonBlock.get("connectsWith"));
                BlockAnimation[] animation = loadBlockAnimation(targetSheet, (JSONArray) jsonBlock.get("textures"), count, width, height);
                BufferedImage[] overlays = loadBlockOverlay(targetSheet, (JSONArray) jsonBlock.get("connections"), width, height);
                boolean pob = jsonBlock.get("printOnBlock") != null && (boolean) jsonBlock.get("printOnBlock");
                Effect[] effects = loadEffects((JSONArray) jsonBlock.get("effects"));

                blocks.add(new BlockData(name, animation, overlays, connectsWith, hitBox, effects, printPriority, pob));
            }
        }
    }

    /**
     * Creates a {@link BlockController} instance from the name, and it sets its position to the give
     * coordinates.
     * @param name the block name.
     * @param x the x grid coordinate to assign to the block.
     * @param y the y grid coordinate to assign to the block.
     * @return the instance of the block controller.
     */
    public static BlockController get(String name, int x, int y) {

        BlockData target = null;
        boolean found = false;

        for(BlockData data : blocks) {
            if (data.id.equals(name)) {
                target = data;
                found = true;
            }
        }

        if (!found) {
            Logger.log(Logger.WARNING, "Block called '" + name + "' no found.");
            return null;
        }

        BlockController block = new BlockController(target);

        block.setGridPosition(x, y);
        block.setPrintPriority(target.printPriority());

        if (target.hitBox != null) {
            block.setHitBox(target.hitBox);
        }

        return block;
    }

    /**
     * Checks if the {@link BlockManager} class has the requested block id.
     * @param id the block name.
     * @return {@code true} if it contains the requested block, otherwise {@code false}.
     */
    public static boolean has(String id) {
        return blocks.stream().anyMatch(block -> block.id.equals(id));
    }

    /**
     * Represent a {@code .json} block data. This class contains information about the block's texture and other texture
     * types used whenever the {@link com.ca.gui.PostRenderer#apply(GameScene)} class is called.
     * @param id the block's name.
     * @param animation the block animation.
     * @param overlays the images used in {@link com.ca.gui.PostRenderer} when the current block is adjacent to other blocks
     *                 with whom it can connect to.
     * @param connectsWith the list of blocks that the current block can connect to. This list contains blocks IDs, or it can
     *                     contain '{@code *}' meaning that the <u>connection is generic</u>, and it can connect to every block (if the
     *                     other block's connection is also generic, or it has the current block in its connection list).
     * @param hitBox the block's hitbox.
     * @param effects <b>not used</b>
     * @param printPriority the block's print priority.
     * @param printOnBlock set to {@code true} if the block overlay have to be printed inside the block rendering space. This
     *                     flag will tell the {@link com.ca.gui.PostRenderer} class that all the overlay of the block will be
     *                     facing the opposite direction of the block.
     */
    public record BlockData(String id, BlockAnimation[] animation, BufferedImage[] overlays, String[] connectsWith, int[] hitBox,
                            Effect[] effects, int printPriority, boolean printOnBlock) { }
}
