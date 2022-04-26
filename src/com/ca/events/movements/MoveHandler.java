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
package com.ca.events.movements;

import com.ca.constants.Basic;
import com.ca.constants.Clock;
import com.ca.constants.Direction;
import com.ca.entities.characters.Player;
import com.ca.entities.controllers.CharacterController;

import static com.ca.events.GameKeyHandler.isKeyPressed;

import com.ca.events.GameKeyHandler;
import com.ca.game.EntityManager;
import com.ca.resources.animations.Action;
import com.ca.ui.Framable;

import java.awt.event.KeyEvent;

/**
 * This class handles all the inputs of the player. It directly sets the actions of the {@link Player} assigned to the specific
 * move handler instance.
 *
 * @since 1.0.00
 * @author Macaluso Francesco
 */
public class MoveHandler implements Runnable {

    public static boolean inventoryFlag = false;
    public static boolean statisticsFlag = false;
    /**
     * The player instance listening the keyboard inputs.
     */
    private final Player player;

    /**
     * The frame that listens for the screenshot key.
     */
    private final Framable framable;

    /*
     * Variables to store events in a timeline, used to check for input delays.
     */
    private long lastChangeWeaponTime = 0;
    private long lastChangeUnequippedTime = 0;
    private long lastChangeEquippedTime = 0;
    private long lastScreenshotTime = 0;
    private long lastInventoryOpenTime = 0;
    private long lastStatisticOpenTime = 0;

    /**
     * Default constructor to create a basic {@link MoveHandler} instance.
     * @param player the player that listens for the keyboard inputs.
     * @param framables the frame's to be listening for screenshots.
     */
    public MoveHandler(CharacterController player, Framable framables) {
        this.player = (Player) player;
        this.framable = framables;
    }

    public void run() {

        while (!Thread.currentThread().isInterrupted() && player != null) {
            if (Clock.tick()) {

                // Takes a screenshot
                if (GameKeyHandler.isKeyPressed((char) KeyEvent.VK_F2) && System.currentTimeMillis() - lastScreenshotTime >= Basic.DEFAULT_SCREENSHOT_DELAY) {
                    framable.takeScreenshot(Basic.DEFAULT_SCREENSHOT_PATH);
                    lastScreenshotTime = System.currentTimeMillis();
                }

                if(GameKeyHandler.isKeyPressed((char) KeyEvent.VK_I) && System.currentTimeMillis() - lastInventoryOpenTime >= Basic.DEFAULT_INVENTORY_DELAY) {
                    inventoryFlag = !inventoryFlag;
                    lastInventoryOpenTime = System.currentTimeMillis();

                    if(!inventoryFlag) {
                        statisticsFlag = false;
                    }
                }

                if(GameKeyHandler.isKeyPressed((char) KeyEvent.VK_B) && System.currentTimeMillis() - lastStatisticOpenTime >= Basic.DEFAULT_INVENTORY_DELAY && inventoryFlag) {
                    statisticsFlag = !statisticsFlag;
                    lastStatisticOpenTime = System.currentTimeMillis();
                }

                if(GameKeyHandler.isKeyPressed((char) KeyEvent.VK_ESCAPE)) {
                    inventoryFlag = false;
                    statisticsFlag = false;
                }

                // Checks if the player is attacking, if so he can't do anything
                if (!Action.isAttacking(player.getStateManager().getType())) {

                    // If the player isn't moving, then its idling
                    if (!isMoving()) {
                        player.doAction(Action.Idling);
                    }
                    else {
                        player.doAction(Action.Walking);
                        checkMovements();
                    }

                    checkActions();
                }
            }
        }
    }

    /**
     * Checks all the possible action the user can do.
     */
    private void checkActions() {

        // Attacks
        if (GameKeyHandler.isKeyPressed((char) KeyEvent.VK_SPACE)) {
            player.doAction(Action.Attacking);
        }

        //  Picks up the weapon from the ground
        if (GameKeyHandler.isKeyPressed((char) KeyEvent.VK_E) && System.currentTimeMillis() - lastChangeEquippedTime >= Basic.DEFAULT_ACTION_DELAY) {
            player.interact();

            lastChangeEquippedTime = System.currentTimeMillis();
        }

        // Switches to the last weapon
        if (GameKeyHandler.isKeyPressed((char) KeyEvent.VK_Q) && System.currentTimeMillis() - lastChangeWeaponTime >= Basic.DEFAULT_ACTION_DELAY) {
            player.switchToLast();
            lastChangeWeaponTime = System.currentTimeMillis();
        }

        // Changes gun using the hotbar
        if(isChangingWeapon() && System.currentTimeMillis() - lastChangeWeaponTime >= Basic.DEFAULT_ACTION_DELAY) {
            player.switchTo(inputHotbarNumber());
            lastChangeWeaponTime = System.currentTimeMillis();
        }

        // Drops the current item
        if (GameKeyHandler.isKeyPressed((char) KeyEvent.VK_C) && System.currentTimeMillis() - lastChangeUnequippedTime >= Basic.DEFAULT_ACTION_DELAY) {
            player.unequip();
            lastChangeUnequippedTime = System.currentTimeMillis();
        }

        for(CharacterController ch : EntityManager.get()) {
            if(ch.getStats().get("knockback").getCurrentValue() > 0) {
                if(!Basic.BILLI_FEATURE)
                    ch.doKnockback();
                else
                    ch.doKnockback(EntityManager.subject.getDirection());
            }
        }
    }

    /**
     * Checks the inputs for the movements. Any type of actions stored inside this method have to me movement-related.
     */
    private void checkMovements() {

        // If the player is shifting then it increases the speed multiplier
        if (GameKeyHandler.isKeyPressed((char) KeyEvent.VK_SHIFT)) {
            player.getStats().set("multiplier_speed", Basic.STATS_RUN_MULTIPLIER);
        }
        else {
            player.getStats().set("multiplier_speed", Basic.STATS_DEFAULT_MULTIPLIER);
        }

        // Changes the state here so if the character cannot move in the given direction the player still looks in
        // that direction
        if (GameKeyHandler.isKeyPressed('W')) {
            player.move(Direction.UP);
        } else if (GameKeyHandler.isKeyPressed('S')) {
            player.move(Direction.DOWN);
        }

        if (GameKeyHandler.isKeyPressed('A')) {
            player.move(Direction.LEFT);
        } else if (GameKeyHandler.isKeyPressed('D')) {
            player.move(Direction.RIGHT);
        }
    }

    /**
     * @return true if the user is pressing the movement keys.
     */
    private boolean isMoving() {
        return isKeyPressed('W') || isKeyPressed('A') || isKeyPressed('S') || isKeyPressed('D');
    }

    /**
     * @return {@code true} if the user has pressed the keys from '{@code 0}' through '{@code 9}'.
     */
    private boolean isChangingWeapon() {

        boolean flag = false;

        //TODO: change the limit to the ToolBar static size
        for (int i = 0; i < 10; i++) {
            if (!(flag = isKeyPressed((char) ('0' + i)))) {
                continue;
            }

            break;
        }

        return flag;
    }

    /**
     * @return the number the user has pressed on the keyboard.
     */
    private int inputHotbarNumber() {
        for (int i = 0; i < 10; i++) {
            if (isKeyPressed((char) (i + '0'))) {
                return i;
            }
        }
        return 0;
    }
}
