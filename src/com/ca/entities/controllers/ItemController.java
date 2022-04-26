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

import com.ca.constants.Assets;
import com.ca.constants.Basic;
import com.ca.constants.Direction;
import com.ca.constants.Position;
import com.ca.entities.DirectionalHitBox;
import com.ca.entities.HitBox;
import com.ca.entities.models.ItemModel;
import com.ca.entities.states.EntityState;
import com.ca.entities.views.ItemView;
import com.ca.errors.Logger;
import com.ca.game.CollisionManager;
import com.ca.game.EntityManager;
import com.ca.maps.Scene;
import com.ca.resources.MetadataDeserializer;
import com.ca.resources.Utility;
import com.ca.resources.animations.Action;
import com.ca.resources.animations.ItemAnimation;

import java.awt.*;
import java.util.Random;
import java.util.List;

/**
 * The controller class for all items in the game. This class is the way to access both the functionalities of both of them
 * without mixing the graphics to the logic.
 * @see ItemModel
 * @see ItemView
 * @since 1.0.00
 * @author Brkic Emir
 */
public class ItemController {

    private final ItemModel model;
    private final ItemView view;

    private final HitBox hitBox;
    private final DirectionalHitBox actionHitBox;

    public ItemController(Assets.Info info) {
        this.model = new ItemModel(info);
        this.view = new ItemView(info);

        this.hitBox = MetadataDeserializer.loadHitBox(info, model.getPosition());
        this.actionHitBox = MetadataDeserializer.loadDirectionalHitBox(info, model.getPosition(), "actionHitBox", model.getState().getDirection());

        model.setPrintPriority(Basic.DEFAULT_ITEM_PRINT_PRIORITY);
    }

    /**
     * @return the item's position.
     */
    public Position getPosition() {
        return model.getPosition();
    }

    /**
     * @return the item's state.
     */
    public EntityState getState() {
        return model.getState();
    }

    /**
     * A target entity uses the current item.
     */
    public void use() {
        List<CharacterController> character = EntityManager.getNearbyCharacter(getPosition());

        for(CharacterController ch : character) {
            if (CollisionManager.intersect(ch.getHitBox(), actionHitBox.toHitBox())) {

                if(!ch.equals(EntityManager.subject) || Basic.BILLI_FEATURE) {
                    double dmg = model.getState().getStatistics().get("damage");
                    double knc = model.getState().getStatistics().get("knockback");

                    ch.doDamage(model.getState().getDirection(), dmg, knc);
                    ch.startKnockback(knc, EntityManager.subject.getDirection());
                }
            }
        }
    }

    /**
     * Sets the item position on the grid, referencing to the {@link Basic#DEFAULT_BLOCK_HEIGHT} and {@link Basic#DEFAULT_BLOCK_HEIGHT} constants.
     * @param x the x value in grid coordinates.
     * @param y the y value in grid coordinates.
     */
    public void setPosition(int x, int y) {
        Random r = new Random();
        final int radius = 15;

        model.setPosition(x + r.nextInt(radius), y + r.nextInt(radius));
    }

    /**
     * @return the item's print priority.
     */
    public int getPrintPriority() {
        return model.getPrintPriority();
    }

    public ItemModel.ItemType getItemType(){
        return model.getItemType();
    }

    /**
     * @return the current item's layer.
     */
    public Scene.Layer getLayer() {
        return model.getLayer();
    }

    /**
     * Sets the player position.
     * @param p the item's position.
     */
    public void setPosition(Position p) {
        model.setPosition(p);
    }

    /**
     * Sets the item direction.
     * @param d the item's direction.
     */
    public void setDirection(Direction d) {
        model.getState().setDirection(d);
        actionHitBox.setDirection(d);
    }

    /**
     * Flag indicating if the item is currently in use.
     * @param inUse set to {@code true} if the item is being used, otherwise {@code false}.
     */
    public void setInUse(boolean inUse) {
        model.getState().setInUse(inUse);
    }

    /**
     * Sets the current action's of the item.
     * @param a the new Action value.
     */
    public void setAction(Action a) {
        if (Action.isAttacking(a) && model.getState().isInUse()) {
            model.setDurability(model.getDurability() - 1);
        }

        model.getState().setAction(a);
    }

    /**
     * @return the item's name.
     */
    public String getName() {
        return model.getName();
    }

    /**
     * @return the item's hitbox.
     */
    public HitBox getHitBox() {
        return hitBox;
    }

    public ItemAnimation getIcon() {
        return view.getIcon();
    }

    public String getDescription(){
        return model.getDescription();
    }

    public void setDescription(String description){
        model.setDescription(description);
    }

    /**
     * Binds the current item to a character controller.
     * @param ch the entity bound to this item.
     */
    public void equip(CharacterController ch) {

        setPosition(ch.getPosition());
        getState().setPosition(ch.getPosition());
        hitBox.setPosition(ch.getPosition());
        actionHitBox.setPosition(ch.getPosition());

        setDirection(ch.getDirection());

        setAction(ch.getAction());

        model.getState().equip();
    }

    /**
     * Unequips the current item from the character.
     */
    public void unequip() {

        this.setPosition(new Position(this.getPosition().x, this.getPosition().y));
        this.getState().setPosition(this.getPosition());
        this.hitBox.setPosition(this.getPosition());
        this.actionHitBox.setPosition(this.getPosition());

        this.setAction(Action.Idling);
        this.setDirection(Basic.DEFAULT_ITEM_DIRECTION);

        this.model.getState().unEquip();
    }

    /**
     * Updates the item's print priority based on its state (equipped or not) and on its direction.
     */
    private void updatePrintPriority() {

        CharacterController ctrl = EntityManager.subject;

        if (model.getState().getDirection() == Direction.DOWN ||
                ((model.getState().getDirection() == Direction.RIGHT || model.getState().getDirection() == Direction.LEFT) &&
                Action.isAttacking(model.getState().getAction()))) {
            model.setPrintPriority(ctrl.getPrintPriority() + 1);
        }
        else {
            model.setPrintPriority(model.getDefaultPrintPriority());
        }
    }

    /**
     * Renders the graphics to screen.
     * @param g the graphics to draw on.
     */
    public void render(Graphics g) {

        view.render(getState(), g);

        updatePrintPriority();

        if (Basic.DEBUG_MODE) {
            if (Basic.DEBUG_RENDER_TEXTURE) {
                Position rp = Utility.getEntityScreenCoords(getState());

                g.setColor(Basic.DEBUG_TEXTURE_COLOR);
                g.drawRect(rp.x, rp.y, view.getFrameWidth(), view.getFrameHeight());
            }

            if (hitBox == null) {
                Logger.log(Logger.WARNING, "The object " + this.model.getIGN() + " doesn't have an hitbox.");
            }

            if (Basic.DEBUG_RENDER_HITBOX && hitBox != null) {
                Position hp = Utility.getHitBoxScreenCoords(hitBox);

                g.setColor(Basic.DEBUG_HITBOX_COLOR);
                g.drawRect(hp.x, hp.y, hitBox.getWidth(), hitBox.getHeight());
            }

            if (Basic.DEBUG_RENDER_ACTION_RANGE) {
                Position ahp = Utility.getHitBoxScreenCoords(actionHitBox.toHitBox());

                g.setColor(Basic.DEBUG_ACTION_RANGE_COLOR);
                g.drawRect(ahp.x, ahp.y, actionHitBox.getWidth(), actionHitBox.getHeight());
            }
        }
    }
}

