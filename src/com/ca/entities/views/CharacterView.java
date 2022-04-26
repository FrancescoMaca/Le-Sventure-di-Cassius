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
import com.ca.entities.characters.Camera;
import com.ca.entities.controllers.CharacterController;
import com.ca.entities.states.CharacterState;
import com.ca.entities.states.EntityState;
import com.ca.errors.Logger;
import com.ca.errors.general.UnknownException;
import com.ca.events.GameMouseHandler;
import com.ca.events.GameWindowHandler;
import com.ca.game.CameraManager;
import com.ca.resources.MetadataDeserializer;
import com.ca.resources.Utility;
import com.ca.resources.animations.Action;
import com.ca.resources.SpritesheetLoader;
import com.ca.resources.animations.CharacterAnimation;
import com.ca.resources.animations.template.EntityAnimation;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.HashMap;

/**
 * Handles all the graphics of each character.
 * @since 1.0.00
 * @author Macaluso Francesco, Brkic Emir, Vidoni Pietro
 */
public class CharacterView extends EntityView {

    /**
     * Binds the entity with a spritesheet path. Then the class will delegate the {@link com.ca.resources.SpritesheetLoader}
     * to load and initialize all the entity animation.
     */
    public CharacterView(Assets.Info info) {
        super(info.texturePath());
    }

    /**
     * Copy constructor.
     * @param view the object to clone.
     */
    public CharacterView(CharacterView view) {
        super(new HashMap<>(view.animations), view.texturePath);
    }

    /**
     * Loads all the character animations from the given spritesheet. This method will be called once to load
     * each instance of {@link CharacterView}.
     */
    protected final void loadAnimations() {
        // Loads all the basic characters' animation
        // Special animations are rendered apart from here in a subclass
        Logger.log(Logger.MESSAGE, "Loading animations from file ('" + texturePath + "')...");

        for(Action action : MetadataDeserializer.entityAction(texturePath)) {
            var ca =  SpritesheetLoader.loadCharacterAnimation(texturePath, action);
            animations.put(action.getID(), ca);
        }

        animations.get(Action.Running.getID()).setSpeed(0.35);
        animations.get(Action.AttackingHalberd.getID()).setSpeed(0.17);
        animations.get(Action.AttackingSword.getID()).setSpeed(0.17);

        setDefaultTexture(Action.Idling.getID());

        this.msgBox = SpritesheetLoader.loadMsgBoxAnimation(this.texturePath);
        msgBox.setSpeed(0.15);
        msgBox.setRunOne(true);


        Logger.log(Logger.MESSAGE, "Animations from file ('" + texturePath + "') have been successfully loaded!");
    }

    /**
     * Adds the given controller to the animation listener list of the given animation type.
     * @param source the source to register.
     * @param action the animation type to be registered to.
     */
    public void addAnimationListener(CharacterController source, Action action) {
        EntityAnimation animation = getAnimation(action);

        if (animation == null) {
            Logger.log(Logger.WARNING, "The character controller listener cannot be added. The Animation is null.");
            return;
        }

        animation.addAnimationListener(source);
    }


    public void resetMsgBox() {
        msgBox.setCurrentFrame(0);
    }

    /**
     * Renders the entity bound with this object based on its current state.
     * @param eState the entity's state.
     * @param g the graphics to draw on.
     */
    public void render(EntityState eState, Graphics g) {

        CharacterState state;

        if (eState instanceof CharacterState casted) {
            state = casted;
        }
        else {
            Logger.log(Logger.MODE_SALVAGE, new UnknownException("The entity and the state differ in their type!"));
            return;
        }

        // renders the current character onto the graphics
        CharacterAnimation animation = (CharacterAnimation) animations.get(state.getType().getID());
        
        BufferedImage img = animation.getCurrentFrame(state.getDirection());

        g.drawImage(img, Utility.getEntityScreenCoords(eState).x, Utility.getEntityScreenCoords(eState).y, img.getWidth(), img.getHeight(), null);
    }


    public void drawMsgBox(EntityState eState, java.awt.Graphics g) {

        CharacterState state;

        if (eState instanceof CharacterState casted) {
            state = casted;
        }
        else {
            Logger.log(Logger.MODE_SALVAGE, new UnknownException("The entity and the state differ in their type!"));
            return;
        }

        BufferedImage img = msgBox.getCurrentFrame();

        Camera c = CameraManager.get(Camera.MAIN_CAMERA);

        g.drawImage(img, state.getPosition().x - c.getPosition().x + (GameWindowHandler.getWindowWidth() / (int)(2 * GameMouseHandler.defaultScale)) - getFrameWidth() / 2,
                state.getPosition().y - c.getPosition().y + (GameWindowHandler.getWindowHeight() / (int)(2 * GameMouseHandler.defaultScale)) - getFrameHeight() / 2, img.getWidth(), img.getHeight(), null);
    }
}
