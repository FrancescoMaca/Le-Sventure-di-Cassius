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
package com.ca.entities.controllers;

import com.ca.constants.*;
import com.ca.entities.DirectionalHitBox;
import com.ca.entities.HitBox;
import com.ca.entities.Inventory;
import com.ca.entities.characters.Camera;
import com.ca.entities.characters.PlayerStats;
import com.ca.entities.models.CharacterModel;
import com.ca.entities.models.ItemModel;
import com.ca.entities.states.CharacterState;
import com.ca.entities.views.CharacterView;
import com.ca.errors.general.InputNotValid;
import com.ca.errors.resources.SpritesheetOutOfBounds;
import com.ca.events.GameMouseHandler;
import com.ca.events.GameWindowHandler;
import com.ca.events.animations.AnimationListener;
import com.ca.game.CameraManager;
import com.ca.game.CollisionManager;
import com.ca.game.EntityManager;
import com.ca.game.FootStepManager;
import com.ca.gui.Decorator;
import com.ca.resources.MetadataDeserializer;
import com.ca.resources.Utility;
import com.ca.resources.animations.Action;
import com.ca.resources.animations.template.Animation;

import java.awt.*;
import java.util.Arrays;
import java.util.UUID;
import java.util.List;

/**
 * Merges both Model and View of the character. This class is the way to access both the functionalities of both of
 * them without mixing the graphics to the logic.
 * @since 1.0.00
 * @author Macaluso Francesco, Brkic Emir, Vidoni Pietro
 */
public class CharacterController implements AnimationListener {

    private final CharacterModel model;
    private final CharacterView view;

    private final HitBox hitBox;
    private final DirectionalHitBox interactionsHitBox;

    private Inventory inventory;

    private Direction knockbackDirection;

    /**
     * Used to identify all entities. All clones made with the copy constructors have the same UUID.
     */
    private final UUID uniqueID;

    /**
     * Default constructor, used to create characters from {@link Assets.Info} object.
     * @param info the information to create the controller.
     */
    public CharacterController(Assets.Info info) {
        this.model  = new CharacterModel(info);
        this.view   = new CharacterView(info);
        this.hitBox = MetadataDeserializer.loadHitBox(info, model.getPosition());


        // Only when you create from 0 a character it assigns a new UUID
        this.uniqueID = UUID.randomUUID();

        this.model.setPrintPriority(Basic.DEFAULT_CHARACTER_PRINT_PRIORITY);
        this.interactionsHitBox = MetadataDeserializer.loadDirectionalHitBox(info, model.getPosition(), "intersectionHitBox", getDirection());
        this.inventory = new Inventory();

        this.addAnimationListener(this,Action.Walking);
        this.addAnimationListener(this,Action.Walking);
    }

    /**
     * Copy constructor.
     * @param controller the entity's controller to copy.
     */
    public CharacterController(CharacterController controller) {
        this.model = new CharacterModel(controller.model);
        this.view = new CharacterView(controller.view);

        this.view.setDefaultTexture(controller.view.getDefaultTexture());

        this.hitBox = new HitBox(model.getPosition(), view.getFrameWidth(), view.getFrameHeight(), controller.hitBox.getOffsets());
        this.interactionsHitBox = new DirectionalHitBox(model.getPosition(), view.getFrameWidth(), view.getFrameHeight(), controller.interactionsHitBox.getOffsets(), getDirection());

        this.uniqueID = controller.uniqueID;

        this.setPosition(controller.getPosition().x, controller.getPosition().y);
        this.addAnimationListener(this,Action.Walking);
        this.addAnimationListener(this,Action.Walking);
    }

    /**
     * Moves the player in a certain direction.
     * @param direction the direction to move the entity to.
     */
    public void move(Direction direction) {

        CharacterController clone = new CharacterController(this);

        boolean isRunning = model.getStatistics().getMultiplier("speed") != Basic.STATS_DEFAULT_MULTIPLIER;

        clone.model.move(direction, isRunning);

        setDirection(direction);

        // Changes the direction of the current item
        if(inventory.size() > 0) {
            if(inventory.getItemInUse() != null) {
                inventory.getItemInUse().setDirection(direction);

                if (isRunning) {
                    inventory.getItemInUse().setAction(Action.Running);
                }
            }
        }

        // If the speed multiplier is not the default one it means it's running
        getStateManager().setType(isRunning ? Action.Running : Action.Walking);

        // Checks if the move is valid, if it is it moves the model
        if (CollisionManager.blocksInteraction(clone) || CollisionManager.entitiesInteraction(clone)) {
            return;
        }

        // Moves the entity
        model.move(direction, isRunning);

        FootStepManager.add(new Position(getStateManager().getPosition().x, getStateManager().getPosition().y + 19));
    }

    public void startKnockback(double knockback, Direction direction) {
        if(knockback > 0 && model.getStatistics().get("knockback").getCurrentValue() == 0) {
            model.getState().setType(Action.KnockBack);
            model.getStatistics().set("knockback",knockback);
            knockbackDirection = direction;
        }
    }

    public void doKnockback() {
        CharacterController clone = new CharacterController(this);

        clone.model.pushBack(knockbackDirection, Basic.KNOCKBACK_SPEED);

        // Checks if the move is valid, if it is it moves the model
        if (CollisionManager.blocksInteraction(clone) || CollisionManager.entitiesInteraction(clone)) {
            model.getStatistics().set("knockback", 0);
            model.getState().setType(Action.Idling);
            return;
        }

        setDirection(Direction.getOpposite(knockbackDirection));

        // The move is valid
        if(inventory.size() > 0) {
            if(inventory.getItemInUse() != null) {
                inventory.getItemInUse().setDirection(Direction.getOpposite(knockbackDirection));
            }
        }



        model.pushBack(knockbackDirection, Basic.KNOCKBACK_SPEED);
        model.getStatistics().set("knockback", (model.getStatistics().get("knockback").getCurrentValue() - 1));

        if(model.getStatistics().get("knockback").getCurrentValue() == 0) {
            model.getState().setType(Action.Idling);
        }
    }


    /**
     * Versione da usare solo nelle Basic.BILLI_FEATURE
     */
    public void doKnockback(Direction direction) {
        CharacterController clone = new CharacterController(this);

        clone.model.pushBack(knockbackDirection, Basic.KNOCKBACK_SPEED);

        // Checks if the move is valid, if it is it moves the model
        if (CollisionManager.blocksInteraction(clone) || CollisionManager.entitiesInteraction(clone)) {
            model.getStatistics().set("knockback", 0);
            model.getState().setType(Action.Idling);
            return;
        }

        setDirection(Direction.getOpposite(direction));

        // The move is valid
        if(inventory.size() > 0) {
            if(inventory.getItemInUse() != null) {
                inventory.getItemInUse().setDirection(Direction.getOpposite(direction));
            }
        }

        model.pushBack(knockbackDirection, Basic.KNOCKBACK_SPEED);
        model.getStatistics().set("knockback", (model.getStatistics().get("knockback").getCurrentValue() - 1));

        if(model.getStatistics().get("knockback").getCurrentValue() == 0) {
            model.getState().setType(Action.Idling);
        }
    }

    /**
     * Interacts with the given item on the ground, if there are any.
     */
    public void interact() {
        List<ItemController> items  = CollisionManager.checkNearbyIntersectionItem(interactionsHitBox.toHitBox(), interactionsHitBox.getPosition());

        // If there are items nearby
        if (items.size() > 0) {
            for(ItemController i : items) {
                if (inventory.contains(i)) {
                    // Equips the item on the ground and exits the loop so if there are
                    // more than 1 item in range, it won't pick up both of them
                    i.equip(this);
                    this.equip(i);
                    break;
                }
            }
        }
    }

    /**
     * Adds an item to the inventory.
     * @param item the item to add to the inventory.
     */
    public void equip(ItemController item) {

        // If the item is not already present in the inventory then it adds it to it
        if(inventory.contains(item)) {
            // Adds the item to the inventory
            inventory.add(item);

            // Tells the item that this is its new inventory
            item.equip(this);

            if(!item.getItemType().equals(ItemModel.ItemType.ARMOR)) {
                inventory.selectItem(item);
            }
        }
    }

    /**
     * Drops the gun on the ground.
     */
    public void unequip() {
        if(inventory.size() >= 2 && inventory.getItemSelected() != 0) {
            inventory.unequip(getDirection());
        }
    }

    /**
     * Switches to the given slot.
     * @param target the index of the weapon slot to switch to.
     */
    public void switchTo(int target) {
        if ( target >= 1 && target <= inventory.size()) {
            inventory.selectItem(target - 1, getDirection());
        }
    }

    /**
     * Switch the current item to the last item used.
     */
    public void switchToLast() {
        if(inventory.size() >= 2) {
            inventory.selectLastItem(getDirection());
        }
    }

    /**
     * Makes the character do a certain action once.
     * @param action the action to do.
     */
    public void doAction(Action action) {
        if(action == Action.Attacking) {
            model.doAction(switch (inventory.getItemInUse().getName()) {
                case "sword" -> Action.AttackingSword;
                case "halberd" -> Action.AttackingHalberd;
                case "fist" -> Action.AttackingFist;
                default -> throw new InputNotValid(Arrays.toString(Action.values()), action);
            });

            inventory.getItemInUse().use();
        }
        else {
            model.doAction(action);
        }

        if(inventory.size() > 0) {
            inventory.getItemInUse().setAction(action);
        }
    }

    /**
     * Deals damage to the current entity.
     * @param direction the direction in which the damage arrives from.
     * @param damage the amount of damage.
     * @param knockback the knockback of the damage.
     */
    public void doDamage(Direction direction, double damage, double knockback) {
        model.getStatistics().set("health", model.getStatistics().get("health").getCurrentValue() - damage);
    }

    /**
     * @return the entity's state.
     */
    public CharacterState getStateManager() {
        return model.getState();
    }

    /**
     * @return the in game name of this entity.
     */
    public String getName() {
        return model.getIGN();
    }

    /**
     * @return the player statistics object ({@link PlayerStats}).
     */
    public PlayerStats getStats() {
        return model.getStatistics();
    }

    /**
     * @return the character position.
     */
    public Position getPosition() {
        return model.getPosition();
    }

    /**
     * @return the character's current direction.
     */
    public Direction getDirection() {
        return model.getState().getDirection();
    }

    /**
     * @return the character's current action.
     */
    public Action getAction() {
        return model.getState().getType();
    }

    /**
     * @return the entity's texture width.
     */
    public int getWidth() {
        return view.getFrameWidth();
    }

    /**
     * @return the entity's texture height.
     */
    public int getHeight() {
        return view.getFrameHeight();
    }

    /**
     * Sets the entity's position to a specific x and y coordinate.
     * @param x the x coordinate.
     * @param y the y coordinate.
     */
    public void setPosition(int x, int y) {
        model.getPosition().x = x;
        model.getPosition().y = y;
    }

    /**
     * Sets the entity's position to a specific x and y coordinate of the given {@link Position} instance.
     * @param pos the new entity's position.
     */
    public void setPosition(Position pos) {
        model.getPosition().x = pos.x;
        model.getPosition().y = pos.y;
    }

    public void setDirection(Direction d) {
        model.getState().setDirection(d);
        interactionsHitBox.setDirection(d);
    }

    /**
     * @return the character hit box. This is used by the game logic to identify collisions.
     */
    public HitBox getHitBox() {
        return hitBox;
    }

    public HitBox getInteractionsHitBox() {
        return interactionsHitBox.toHitBox();
    }

    /**
     * @return the current character's print priority.
     */
    public int getPrintPriority() {
        return model.getPrintPriority();
    }

    public Inventory getInventory() {
        return inventory;
    }

    /**
     * Adds the current entity controller to the list of the given animation.
     * @param source the object to add to the listener.
     * @param action the animation type to add the source to.
     */
    public void addAnimationListener(CharacterController source, Action action) {
        view.addAnimationListener(source, action);
    }

    /**
     * Updates all the entities print priority based on the {@link Basic#SUBJECT} position. If the position is higher in the y-axis it means
     * that graphically the character is located lower than the subject, therefore his print priority has to be higher. However, if the character
     * is located higher than the subject, it will have a lower print priority.
     */
    private void updatePrintPriority() {
        // handles the print priority based on the entity position
        CharacterController subj = EntityManager.subject;

        int heightDifference = ((subj.getHitBox().getY() + subj.getHitBox().getHeight()) - (this.getHitBox().getY() + this.getHitBox().getHeight())) / this.getHitBox().getHeight();
        int newPriority = model.getDefaultPrintPriority() - heightDifference;

        if (newPriority < Basic.DEFAULT_PRINT_PRIORITY_MIN + 1) {
            model.setPrintPriority(Basic.DEFAULT_PRINT_PRIORITY_MIN + 1);
        }
        else {
            model.setPrintPriority(Math.min(newPriority, Basic.DEFAULT_PRINT_PRIORITY_MAX + 1));
        }
    }

    /**
     * It accesses the View to render the entity.
     * @param g the graphics to draw the entity on.
     */
    public void render(java.awt.Graphics g) {

        view.render(getStateManager(), g);

        if(!this.equals(EntityManager.subject)) {
            if (CollisionManager.intersect(hitBox, EntityManager.subject.getInteractionsHitBox())) {
                view.drawMsgBox(getStateManager(), g);
            }
            else {
                view.resetMsgBox();
            }
        }

        updatePrintPriority();

        // Draws this' character ign
        Decorator.drawIGN(g, Color.black, this, true);

        // Handles all the debug layers of the character
        if (Basic.DEBUG_MODE) {
            if (Basic.DEBUG_RENDER_TEXTURE) {
                Position rp = Utility.getEntityScreenCoords(getStateManager());

                g.setColor(Basic.DEBUG_TEXTURE_COLOR);
                g.drawRect(rp.x, rp.y, view.getFrameWidth(), view.getFrameHeight());
            }

            if (Basic.DEBUG_RENDER_HITBOX) {
                Position hp = Utility.getHitBoxScreenCoords(hitBox);

                g.setColor(Basic.DEBUG_HITBOX_COLOR);
                g.drawRect(hp.x, hp.y, hitBox.getWidth(), hitBox.getHeight());
            }

            if (Basic.DEBUG_RENDER_INTERACT) {
                Camera c = CameraManager.get(Camera.MAIN_CAMERA);

                g.setColor(Color.yellow);
                g.drawRect(interactionsHitBox.getX() - c.getPosition().x + GameWindowHandler.getWindowWidth() / (int)(2 * GameMouseHandler.defaultScale) - (view.getFrameWidth() / 2),
                        interactionsHitBox.getY() - c.getPosition().y + GameWindowHandler.getWindowHeight() / (int)(2 * GameMouseHandler.defaultScale) - (view.getFrameHeight() / 2), interactionsHitBox.getWidth(), interactionsHitBox.getHeight());

            }
        }
    }

    /**
     * Compares the current {@link CharacterController} object and another generic object, looking for the same {@code UUID}.
     * @param o the object to compare.
     * @return {@code true} if the given object is of the same type and has the same UUID of the current instance.
     */
    public boolean equals(Object o) {

        if (!(o instanceof CharacterController trg)) {
            return false;
        }

        return uniqueID.equals(trg.uniqueID);
    }

    /**
     * Method callback that executes whenever the animation the entity is listening terminates.
     * @param animation the animation that ended.
     */
    @Override
    public void animationEnded(Animation animation) {
        model.getState().setType(Action.Idling);
        if(inventory != null && inventory.getItemInUse() != null)
            inventory.getItemInUse().setAction(Action.Idling);
    }
}
