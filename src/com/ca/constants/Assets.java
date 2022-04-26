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
 * Assets contains all the file paths and all the game constants.
 * @since 1.0.00
 * @author Macaluso Francesco, Brkic Emir, Vidoni Pietro
 */
public final class Assets {

    /**
     * Path helpers.
     */
    public static final String PATH_BLOCKS = "spritesheets/blocks/";
    public static final String PATH_ITEMS = "spritesheets/items/";
    public static final String PATH_ENTITIES = "spritesheets/entities/";
    public static final String PATH_OTHERS = "spritesheets/others/";
    public static final String PATH_GUIS = "spritesheets/guis/";
    public static final String PATH_OVERLAY = "spritesheets/overlays/";
    public static final String PATH_SAVES = "saves/";
    public static final String PATH_WORLDS = "worlds/";

    /**
     * Worlds + array of worlds to be loaded at the game's start.
     */
    public static final String WORLD_TEST = PATH_WORLDS + "world.json";
    public static final String[] WORLD_LIST = {
            WORLD_TEST
    };

    /**
     * All character entities textures.
     */
    public static final Info INFO_CASSIUS = new Info("Cassius", PATH_ENTITIES + "Cassius.png", PATH_SAVES + "Cassius.json");
    public static final Info INFO_TIME_MAGE = new Info("Time Mage", PATH_ENTITIES + "TimeMage.png", PATH_SAVES + "TimeMage.json");
    public static final Info INFO_KING_LIZARD = new Info("King Lizard", PATH_ENTITIES + "LizardKing.png", PATH_SAVES + "LizardKing.json");

    /**
     * Blocks spritesheets path and .json path.
     */
    public static final Info BLOCKS_MATERIALS = new Info("blocks", PATH_BLOCKS + "materials.png", PATH_BLOCKS + "materials.json");
    public static final Info BLOCKS_NATURE = new Info("blocks", PATH_BLOCKS + "nature.png", PATH_BLOCKS + "nature.json");
    public static final Info BLOCKS_ROMAN = new Info("blocks", PATH_BLOCKS + "roman.png", PATH_BLOCKS + "roman.json");

    /**
     * All font-related settings for the default game font.
     */
    public static final double FONT_VERY_SMALL = 7.0;
    public static final double FONT_SMALL_SIZE = 12.0;
    public static final double FONT_MEDIUM_SIZE = 24.0;
    public static final double FONT_BIG_SIZE = 36.0;
    public static final double FONT_HUGE_SIZE = 76.0;
    public static java.awt.Font FONT = null;
    public static final String FONT_NAME = "cassius-teletype";
    public static final String FONT_NAME_DEFAULT = "Arial";

    /**
     * A {@link Info} instance of each object in the game. With this, the program can load the images of the item and
     * its properties.
     */
    public static final Info ITEM_SWORD = new Info("sword", PATH_ITEMS + "sword.png", PATH_SAVES + "Sword.json");
    public static final Info ITEM_HALBERD = new Info("halberd", PATH_ITEMS + "halberd.png", PATH_SAVES + "Halberd.json");
    public static final Info ITEM_FIST = new Info("fist", "", PATH_SAVES + "Fist.json");

    public static final Info ITEM_CHEST_PLATE = new Info("chest-plate", PATH_ITEMS + "steelChestPlate.png", PATH_SAVES + "steelChestPlate.json");

    public static final Info FOOTSTEP = new Info("footstep",PATH_OVERLAY + "footstep.png", "");

    public static final String ICON_PATH = PATH_OTHERS + "icon_app.png";

    /**
     * The {@link Info} record represent a box containing all useful information to load and create an entity.
     * It contains the in-game name of the entity (IGN), the graphical texture assets, and the {@code .json} file
     * used to read the texture.
     */
    public record Info(String IGN, String texturePath, String dataPath) { }
}
