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
 * This class defines the basic constants of the game.
 * @since 1.0.00
 * @author Macaluso Francesco, Brkic Emir, Pietro Vidoni
 */
public final class Basic {

    /**
     * {@code Frame} constants specific the frame settings such as width, height...
     */
    public static final int FRAME_WIDTH = 1200;
    public static final int FRAME_HEIGHT = 800;

    /**
     * {@code Game} constants describe all the default game settings.
     */
    public static final String GAME_TITLE = "Le Sventure di Cassius";
    public static final String GAME_NAME = "LeSventureDiCassiusJFrame";

    public static final String GAME_MENU_PANEL_NAME = "cassius_menu";
    public static final String GAME_SETTINGS_PANEL_NAME = "cassius_settings";
    public static final String GAME_GAME_PANEL_NAME = "cassius_game";

    public static boolean IS_FULLSCREEN = false;
    public static boolean SHOW_FPS = false;
    public static final Assets.Info SUBJECT = Assets.INFO_CASSIUS;
    public static final boolean BOT_FLAG = true;
    public static boolean BILLI_FEATURE = false;

    /**
     * The whole game is synchronized with this FPS count. <br>
     * WARNING: changing this number will speed up or slow down the whole game.
     */
    public static int GAME_FPS = 60;
    public static final boolean POST_RENDER = true;

    /**
     * Debug mode settings
     */
    public static final boolean DEBUG_MODE = false;
    public static final boolean DEBUG_RENDER_TEXTURE = false;
    public static final boolean DEBUG_RENDER_HITBOX = true;
    public static final boolean DEBUG_RENDER_INTERACT = false;
    public static final boolean DEBUG_RENDER_MAP_BORDER = false;
    public static final boolean DEBUG_RENDER_ACTION_RANGE = true;
    public static final java.awt.Color DEBUG_TEXTURE_COLOR = java.awt.Color.WHITE;
    public static final java.awt.Color DEBUG_HITBOX_COLOR = java.awt.Color.GREEN;
    public static final java.awt.Color DEBUG_MAP_BORDER_COLOR = java.awt.Color.RED;
    public static final java.awt.Color DEBUG_ACTION_RANGE_COLOR = java.awt.Color.CYAN;

    /**
     * {@code Default} constants describe all the hard-coded states/values that the game has.
     */
    public static final Direction DEFAULT_ENTITY_DIRECTION = Direction.DOWN;
    public static final Direction DEFAULT_ITEM_DIRECTION = Direction.UP;
    public static final Direction DEFAULT_BLOCK_DIRECTION = Direction.UP;

    public static final com.ca.resources.animations.Action DEFAULT_ANIMATION = com.ca.resources.animations.Action.Idling;
    public static final double DEFAULT_ANIMATION_SPEED = 0.17;

    public static final double DEFAULT_SCALE_MIN = 2;
    public static final double DEFAULT_SCALE_MAX = 8;

    public static final int DEFAULT_BLOCK_WIDTH = 32;
    public static final int DEFAULT_BLOCK_HEIGHT = 32;

    public static final int DEFAULT_PRINT_PRIORITY_MIN = 0;
    public static final int DEFAULT_PRINT_PRIORITY_MAX = 15;
    public static final int DEFAULT_ITEM_PRINT_PRIORITY = 7;
    public static final int DEFAULT_CHARACTER_PRINT_PRIORITY = 7;

    public static final String DEFAULT_SCREENSHOT_PATH = "screenshots/";
    public static final long DEFAULT_SCREENSHOT_DELAY = 2000;
    public static final long DEFAULT_ACTION_DELAY = 250;
    public static final long STEP_TIME_LIVE = 1000;

    public static final long DEFAULT_INVENTORY_DELAY = 350;
    public static final long DEFAULT_INVENTORY_DESCRIPTION_DELAY = 500;

    public static final long DEFAULT_RANDOM_TEXTURE = -1;

    public static final double DEFAULT_ANIMATION_SPEED_MIN = 0.01;
    public static final double DEFAULT_ANIMATION_SPEED_MAX = 1.00;

    /**
     * The different layer's priorities.
     */
    public static final int LAYER_BACKGROUND_LOW = 0;
    public static final int LAYER_BACKGROUND_HIGH = 3;
    public static final int LAYER_MIDDLEGROUND_LOW = 4;
    public static final int LAYER_MIDDLEGROUND_HIGH = 7;
    public static final int LAYER_FOREGROUND_LOW = 8;
    public static final int LAYER_FOREGROUND_HIGH = 11;
    public static final int LAYER_OVERLAY_LOW = 12;
    public static final int LAYER_OVERLAY_HIGH = 15;

    /**
     * Statistics and other character data.
     */
    public static final double STATS_RUN_MULTIPLIER = 1.5;
    public static final double STATS_DEFAULT_MULTIPLIER = 1.0;

    public static final int MAX_BACKPACK_ROWS = 9;
    public static final int MAX_BACKPACK_COLUMNS = 4;

    public static final int KNOCKBACK_SPEED = 5;
}
