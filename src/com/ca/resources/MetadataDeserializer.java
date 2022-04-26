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
import com.ca.constants.Direction;
import com.ca.constants.Position;
import com.ca.entities.DirectionalHitBox;
import com.ca.entities.HitBox;
import com.ca.entities.characters.PlayerStats;
import com.ca.entities.models.ItemModel;
import com.ca.errors.Logger;
import com.ca.errors.general.InputNotValid;
import com.ca.errors.resources.ResourceFormatInvalid;
import com.ca.errors.resources.ResourceNotLoaded;
import com.ca.errors.resources.ResourceOutdated;
import com.ca.resources.animations.Action;
import com.ca.resources.animations.template.Animation;
import org.json.simple.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Locale;

/**
 * Loads the {@code .json} file associated with a spritesheet. The {@code .json} file contains information
 * about the spritesheet itself and its animations.
 *
 * @see SpritesheetLoader
 * @see Metadata
 * @since 1.0.00
 * @author Macaluso Francesco, Vidoni Pietro, Brkic Emir
 */
public class MetadataDeserializer {

    /**
     * Specifies which extensions are allowed to be loaded by this class.
     */
    private static final String[] metadataExt = { ".json" };

    /**
     * Loads a single animation row. It is used to load only certain type of animations that are not
     * direction based, such as {@link Animation}.TYPE_DYING.
     * @param animationMetadata the animation in {@code .json} format
     * @return an array made of a single animation frame that
     */
    private static Metadata[] loadSingleAnimation(JSONObject animationMetadata) {

        // Reads the tile size of the animation
        long width = (long) animationMetadata.get("width");
        long height = (long) animationMetadata.get("height");

        JSONObject coords = (JSONObject) animationMetadata.get("frames");

        return new Metadata[] {
                new Metadata((long)coords.get("fromX"), (long)coords.get("fromY"), (long)coords.get("toX"), (long)coords.get("toY"), width, height)
        };
    }

    /**
     * Loads an entire action in form of Metadata, providing coordinates for the {@link SpritesheetLoader} class
     * to load all the frames correctly.
     * @param animationMetadata the animation in {@code .json} format
     * @return an array of all the animations of a given type.
     */
    private static Metadata[] loadMultipleAnimations(JSONObject animationMetadata) {

        // Loads the four types of animations
        JSONObject up = (JSONObject) animationMetadata.get(Direction.UP.toString().toLowerCase(Locale.ROOT));
        JSONObject down = (JSONObject) animationMetadata.get(Direction.DOWN.toString().toLowerCase(Locale.ROOT));
        JSONObject left = (JSONObject) animationMetadata.get(Direction.LEFT.toString().toLowerCase(Locale.ROOT));
        JSONObject right = (JSONObject) animationMetadata.get(Direction.RIGHT.toString().toLowerCase(Locale.ROOT));

        // Reads the tile size of the animation
        long width = (long) animationMetadata.get("width");
        long height = (long) animationMetadata.get("height");

        return new Metadata[] {
            new Metadata((long)up.get("fromX"), (long)up.get("fromY"), (long)up.get("toX"), (long)up.get("toY"), width, height),
            new Metadata((long)down.get("fromX"), (long)down.get("fromY"), (long)down.get("toX"), (long)down.get("toY"), width, height),
            new Metadata((long)left.get("fromX"), (long)left.get("fromY"), (long)left.get("toX"), (long)left.get("toY"), width, height),
            new Metadata((long)right.get("fromX"), (long)right.get("fromY"), (long)right.get("toX"), (long)right.get("toY"), width, height),
        };
    }

    /**
     * Loads the {@code .json} file and reads it.
     * @param res the resource's path.
     * @param animationType the type of animation to be looked for.
     * @return an array of {@link Metadata} containing the starting and ending coordinates of a single animation row.
     * @throws RuntimeException if the resource couldn't be loaded into memory for any reasons.
     */
    public static Metadata[] loadAnimationMetadata(String res, String animationType) {

        Utility.checkExtension(res, metadataExt);

        // Converts the entire file to a json object
        JSONObject jsonSpritesheet = (JSONObject) ResourceLoader.loadJsonFile(res);

        // Reads the type of animation
        JSONObject jsonAnimation = (JSONObject) jsonSpritesheet.get(animationType);

        // If the animation type is not found it means that the .json file is corrupted nor the
        // animation file is written with the old style.
        if (jsonAnimation == null) {
            Logger.log(Logger.MODE_CRITICAL, new ResourceOutdated(res));
            return new Metadata[0];
        }

        // Checks if the animation is direction based.
        if (jsonAnimation.get("frames") != null) {
            return loadSingleAnimation(jsonAnimation);
        }

        return loadMultipleAnimations(jsonAnimation);
    }

    public static Metadata loadMsgBoxMetadata(String res) {

        Utility.checkExtension(res, metadataExt);

        // Converts the entire file to a json object
        JSONObject jsonSpritesheet = (JSONObject) ResourceLoader.loadJsonFile(res);

        // Reads the type of animation
        JSONObject jsonAnimation = (JSONObject) jsonSpritesheet.get("msgBox");

        // If the animation type is not found it means that the .json file is corrupted nor the
        // animation file is written with the old style.
        if (jsonAnimation == null) {
            Logger.log(Logger.MODE_CRITICAL, new ResourceOutdated(res));
            return Metadata.EMPTY;
        }

        return loadSingleAnimation(jsonAnimation)[0];
    }


    /**
     * Returns the list of action that are find in an entity .json file
     */
    public static ArrayList<Action> entityAction(String texturePath) {

        ArrayList<Action> action = new ArrayList<>();

        texturePath = Utility.toJson(texturePath);

        // Checks file's extension
        Utility.checkExtension(texturePath, metadataExt);

        // Opens the file
        JSONObject data = (JSONObject) ResourceLoader.loadJsonFile(texturePath);

        for(Object stat : data.keySet()) {
            if(stat instanceof String statItem) {
                try {
                    for(Action a : Action.values()) {
                        if(a.getID().equals(statItem)) {
                            action.add(a);
                        }
                    }
                }
                catch (IllegalArgumentException ignored) { }
            }
        }

        return action;
    }

    /**
     * Loads the icon coordinates from the resource file. The icon is displayed when the object is not
     * equipped, meaning that is on the ground.
     * @param res the resource file path ({@code .json})
     * @return a metadata instance with the coordinates of the icon.
     */
    public static Metadata loadIconMetadata(String res) {

        Utility.checkExtension(res, metadataExt);

        // Converts the entire file to a json object
        JSONObject jsonSpritesheet = (JSONObject) ResourceLoader.loadJsonFile(res);

        // Reads the type of animation
        JSONObject jsonAnimation = (JSONObject) jsonSpritesheet.get("icon");

        // If the animation type is not found it means that the .json file is corrupted nor the
        // animation file is written with the old style.
        if (jsonAnimation == null) {
            Logger.log(Logger.MODE_CRITICAL, new ResourceOutdated(res));
            return Metadata.EMPTY;
        }

        return loadSingleAnimation(jsonAnimation)[0];
    }

    /**
     * Loads the entity's statistics data file.
     * @param res the {@code .json} file containing the player statistics.
     * @return a {@link PlayerStats} instance initialized with the file's values.
     */
    public static PlayerStats loadStatistics(String res) {

        Utility.checkExtension(res, metadataExt);

        JSONObject data = (JSONObject) ResourceLoader.loadJsonFile(res);

        JSONObject statisticsData = (JSONObject) data.get("stats");

        PlayerStats stats = new PlayerStats();

        for(String key : stats.getKeySet()) {
            var obj = statisticsData.get(key);

            if(obj == null) {
                continue;
            }

            // Checks for .json value errors (integer instead of double)
            if (obj instanceof Double stat) {
                stats.set(key, stat);
            }
            else if (obj instanceof Long err) {
                Logger.log(Logger.WARNING, "The statistic file '" + res + "' has a faulty value at the key '" + key + "'.");
                stats.set(key, err.doubleValue());
            }
            else {
                Logger.log(Logger.MODE_CRITICAL, new InputNotValid(Double.class, obj.getClass()));
            }
        }

        return stats;
    }

    /**
     * Loads automatically into a hash map all the statistics of the given item.
     * @param res the {@code .json} file containing the player statistics.
     * @return a {@link PlayerStats} instance initialized with the file's values.
     */
    public static HashMap<String, Double> loadItemStatistics(String res) {

        // Checks file's extension
        Utility.checkExtension(res, metadataExt);

        // Opens the file
        JSONObject data = (JSONObject) ResourceLoader.loadJsonFile(res);

        if (data == null) {
            Logger.log(Logger.MODE_CRITICAL, new ResourceNotLoaded(res));
            return null;
        }

        // Loads the data
        JSONObject statisticsData = (JSONObject) data.get("stats");

        // Statistics object with name and corresponding value
        HashMap<String, Double> stats = new HashMap<>();

        // Loads all the statistics, if there is a misspelling in the file
        // it will lead to possible error when accessing those statistics.
        for(Object stat : statisticsData.keySet()) {

            if(stat instanceof String statItem) {
                Object obj = statisticsData.get(statItem);

                if (obj instanceof Double valueItem) {
                    stats.put(statItem, valueItem);
                }
            }
        }

        return stats;
   }

    public static ItemModel.ItemType loadItemType(String res) {

        Utility.checkExtension(res, metadataExt);

        // Opens the file
        JSONObject data = (JSONObject) ResourceLoader.loadJsonFile(res);

        if (data == null) {
            Logger.log(Logger.MODE_CRITICAL, new ResourceNotLoaded(res));
            return null;
        }

        if(data.get("type") == null) {
            Logger.log(Logger.WARNING, "No item type description");
            return ItemModel.ItemType.NO_SPECIFIED;
        }

        return ItemModel.ItemType.get(data.get("type").toString());
    }

    /**
     * Loads the item's durability from the {@code .json} file.
     * @param res the item's resource file.
     * @return the item's durability, if the durability is {@code -1} then the item is unbreakable.
     */
   public static int loadItemDurability(String res) {

       // Checks file's extension
       Utility.checkExtension(res, metadataExt);

       // Opens the file
       JSONObject data = (JSONObject) ResourceLoader.loadJsonFile(res);

       if (data == null) {
           Logger.log(Logger.MODE_CRITICAL, new ResourceNotLoaded(res));
           return 0;
       }

       return ((Long)data.get("durability")).intValue();
   }

    /**
     * Loads the 4 offsets of the hitbox. The offset have to be passed to the {@link HitBox} constructor as they are from the
     * 1st to the 4th.
     * @param info the {@code .json} resource file.
     * @return the offsets of the hitbox.
     */
    public static HitBox loadHitBox(Assets.Info info, Position boundPosition, String TypeHitBox) {

        Utility.checkExtension(info.dataPath(), metadataExt);

        JSONObject data = (JSONObject) ResourceLoader.loadJsonFile(info.dataPath());

        JSONObject hitboxData = (JSONObject) data.get(TypeHitBox);

        int oLeft = ((Long)hitboxData.get("offsetLeft")).intValue();
        int oRight = ((Long)hitboxData.get("offsetRight")).intValue();
        int oTop = ((Long)hitboxData.get("offsetTop")).intValue();
        int oBottom = ((Long)hitboxData.get("offsetBottom")).intValue();

        int width =  ((Long)hitboxData.get("width")).intValue();
        int height = ((Long)hitboxData.get("height")).intValue();


        int[] offset = new int[] { oLeft, oRight, oTop, oBottom };

        return new HitBox(boundPosition, width, height, offset);
    }


    public static DirectionalHitBox loadDirectionalHitBox(Assets.Info info, Position boundPosition, String TypeHitBox, Direction d) {
        Logger.log(Logger.MESSAGE, "Loading directional hitbox of " + info.IGN() + " (" + info.dataPath() + ")");

        Utility.checkExtension(info.dataPath(), metadataExt);

        JSONObject data = (JSONObject) ResourceLoader.loadJsonFile(info.dataPath());

        JSONObject hitboxData = (JSONObject) data.get(TypeHitBox);

        if (hitboxData == null) {
            Logger.log(Logger.MODE_CRITICAL, new ResourceOutdated(info.dataPath()));
        }

        JSONObject oUp = (JSONObject)hitboxData.get("up");
        JSONObject oDown = (JSONObject)hitboxData.get("down");
        JSONObject oLeft = (JSONObject)hitboxData.get("left");
        JSONObject oRight = (JSONObject)hitboxData.get("right");

        int[][] offset = new int[4][oUp.size()];

        offset[0][0] = ((Long)oUp.get("offsetLeft")).intValue();
        offset[0][1] = ((Long)oUp.get("offsetRight")).intValue();
        offset[0][2] = ((Long)oUp.get("offsetTop")).intValue();
        offset[0][3] = ((Long)oUp.get("offsetBottom")).intValue();

        offset[1][0] = ((Long)oDown.get("offsetLeft")).intValue();
        offset[1][1] = ((Long)oDown.get("offsetRight")).intValue();
        offset[1][2] = ((Long)oDown.get("offsetTop")).intValue();
        offset[1][3] = ((Long)oDown.get("offsetBottom")).intValue();

        offset[2][0] = ((Long)oLeft.get("offsetLeft")).intValue();
        offset[2][1] = ((Long)oLeft.get("offsetRight")).intValue();
        offset[2][2] = ((Long)oLeft.get("offsetTop")).intValue();
        offset[2][3] = ((Long)oLeft.get("offsetBottom")).intValue();

        offset[3][0] = ((Long)oRight.get("offsetLeft")).intValue();
        offset[3][1] = ((Long)oRight.get("offsetRight")).intValue();
        offset[3][2] = ((Long)oRight.get("offsetTop")).intValue();
        offset[3][3] = ((Long)oRight.get("offsetBottom")).intValue();

        int width =  ((Long)hitboxData.get("width")).intValue();
        int height = ((Long)hitboxData.get("height")).intValue();

        return new DirectionalHitBox(boundPosition, width, height, offset, d);
    }

    public static HitBox loadHitBox(Assets.Info info, Position boundPosition) {
        return loadHitBox(info, boundPosition, "hitbox");
    }
}
