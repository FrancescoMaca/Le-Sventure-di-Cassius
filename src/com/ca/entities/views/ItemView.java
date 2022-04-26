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

import com.ca.constants.Assets;
import com.ca.constants.Basic;
import com.ca.entities.characters.Camera;
import com.ca.entities.states.EntityState;
import com.ca.entities.states.ItemState;
import com.ca.errors.Logger;
import com.ca.events.GameMouseHandler;
import com.ca.events.GameWindowHandler;
import com.ca.game.CameraManager;
import com.ca.resources.MetadataDeserializer;
import com.ca.resources.SpritesheetLoader;
import com.ca.resources.animations.Action;
import com.ca.resources.animations.template.Animation;
import com.ca.resources.animations.ItemAnimation;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;

/**
 * The item render object. This will print the object wherever it is (ground or inventory).
 *
 * @see com.ca.entities.models.ItemModel
 * @see Animation
 * @since 1.0.00
 * @author Macaluso Francesco, Brkic Emir, Vidoni Pietro
 */
public class ItemView extends EntityView {

    /**
     * The animation of the item when it's in the inventory or when it's on the ground.
     */
    private ItemAnimation icon;

    /**
     * Default constructor for the item view.
     * @param info the item's info. Used to retrieve the item's texture path.
     */
    public ItemView(Assets.Info info) {
        super(info.texturePath());
    }

    /**
     * Loads all the animations for the current item.
     */
    protected void loadAnimations() {

        // Loads all the basic item' animation
        // Special animations are rendered apart from here in a subclass
        Logger.log(Logger.MESSAGE, "Loading animations from file ('" + texturePath + "')...");

        // if the texture path is empty then it creates an empty animation
        if (texturePath.isEmpty()) {

            Animation empty = new Animation(new BufferedImage[] {
                    SpritesheetLoader.EMPTY_IMAGE
            }, Basic.DEFAULT_ANIMATION_SPEED);

            List<Animation> emptyAnimation = new ArrayList<>();
            emptyAnimation.add(empty);

            icon = new ItemAnimation(emptyAnimation);

            return;
        }

        for(Action action : MetadataDeserializer.entityAction(texturePath)) {
            ItemAnimation a = SpritesheetLoader.loadItemAnimation(texturePath, action);
            animations.put(action.getID(), a);
        }

        this.icon = SpritesheetLoader.loadIconAnimation(texturePath);

        animations.get(Action.Running.getID()).setSpeed(0.35);
        animations.get(Action.Attacking.getID()).setSpeed(0.17);

        setDefaultTexture(Action.Idling.getID());

        Logger.log(Logger.MESSAGE, "Animations from file ('" + texturePath + "') have been successfully loaded!");
    }

    public void render(EntityState eState, Graphics g) {

        ItemState state;

        if(eState instanceof ItemState casted) {
            state = casted;
        }
        else {
            Logger.log(Logger.MODE_CRITICAL, new IllegalStateException("The item state is not valid! [Expected: " + EntityState.class + ", Given: " + eState.getClass() + "]"));
            return;
        }

        Camera c = CameraManager.get(Camera.MAIN_CAMERA);

        BufferedImage img = icon.getCurrentFrame(state.getDirection());

        if(state.isEquipped()) {

            // renders the current character onto the graphics
            ItemAnimation animation = (ItemAnimation) animations.get(state.getType().getID());

            // If the correct animation is found then it draws its image on screen
            if (animation != null) {
                img = animation.getCurrentFrame(state.getDirection());
            }
        }

        if(state.isInUse() || !state.isEquipped()) {
            g.drawImage(img, state.getPosition().x - ((img.getWidth() - Basic.DEFAULT_BLOCK_WIDTH * 2) / 2) - c.getPosition().x + (GameWindowHandler.getWindowWidth() / (int) (2 * GameMouseHandler.defaultScale)) - Basic.DEFAULT_BLOCK_WIDTH,
                    state.getPosition().y - ((img.getHeight() - Basic.DEFAULT_BLOCK_HEIGHT * 2) / 2) - c.getPosition().y + (GameWindowHandler.getWindowHeight() / (int) (2 * GameMouseHandler.defaultScale)) - Basic.DEFAULT_BLOCK_HEIGHT,
                    img.getWidth(), img.getHeight(), null);
        }
    }

    public ItemAnimation getIcon() {
        return icon;
    }
}
