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

import com.ca.constants.Basic;
import com.ca.constants.Direction;
import com.ca.constants.Effect;
import com.ca.constants.Position;
import com.ca.entities.HitBox;
import com.ca.entities.models.BlockModel;
import com.ca.entities.states.BlockState;
import com.ca.entities.views.BlockView;
import com.ca.errors.Logger;
import com.ca.game.BlockManager;
import com.ca.maps.GameScene;
import com.ca.maps.Scene;
import com.ca.resources.Utility;
import com.ca.resources.animations.BlockAnimation;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 * This controller merges the model and the view of the game's Block component.
 *
 * @see BlockView
 * @see BlockModel
 * @since 1.0.00
 * @author Macaluso Francesco
 */
public class BlockController {

    private final BlockModel model;
    private final BlockView view;

    private HitBox hitBox;

    public BlockController(BlockManager.BlockData data) {
        this.model = new BlockModel(data.id(), data.effects());

        List<BlockAnimation> blockAnimations = new ArrayList<>();
        for(BlockAnimation aTemp : data.animation()) {
            blockAnimations.add(new BlockAnimation(aTemp));
        }

        this.view = new BlockView(data.id(), blockAnimations, data.overlays(), data.connectsWith(), data.printOnBlock());

        if (data.hitBox() != null) {
            this.hitBox = new HitBox(getPosition(), view.getFrameWidth(), view.getFrameHeight(), data.hitBox());
        }
        else {
            this.hitBox = null;
        }
    }

    /**
     * @return the block name.
     */
    public String getName() {
        return model.getIGN();
    }

    /**
     * Sets the new print priority for the object.
     */
    public void setPrintPriority(int priority) {

        if (priority >= Basic.DEFAULT_PRINT_PRIORITY_MIN && priority <= Basic.DEFAULT_PRINT_PRIORITY_MAX) {
            model.setPrintPriority(priority);
        }
        else {
            Logger.log(Logger.WARNING, "Trying to set the block print priority to an invalid value (" + priority + ")...");
        }
    }

    /**
     * Set actives the overlay of the given direction.
     * @param direction the overlay's direction.
     */
    public void paintOverlay(Direction direction, BufferedImage overlay) {
        view.activateOverlay(direction, overlay);
    }

    /**
     * @return a string array containing the list of blocks with whom the current block can connect to.
     */
    public String[] getAllowedBlockConnections() {
        return view.getAllowedConnections();
    }

    /**
     * @return {@code true} if the blocks have , otherwise {@code false}.
     */
    public boolean printsOnBlock() {
        return view.printOnBlock();
    }

    /**
     * @return {@code true} if the block has already been processed by the {@link com.ca.gui.PostRenderer#apply(GameScene)} method,
     * otherwise {@code false}.
     */
    public boolean isPostRendered() {
        return view.isPostRendered();
    }

    /**
     * Sets the post render status of the current block. If the current block has already been analyzed then the {@link com.ca.gui.PostRenderer}
     * can simply skip this.
     * @param isPostRendered the new post render status.
     */
    public void isPostRendered(boolean isPostRendered) {
        view.isPostRendered(isPostRendered);
    }

    /**
     * @return {@code true} if the block has any overlays, otherwise {@code false}.
     */
    public boolean hasOverlays() {
        return view.hasOverlays();
    }

    public BufferedImage getOverlay(Direction d) {
        return view.getOverlays()[d.ordinal()];
    }

    /**
     * @return {@code true} if the current block can connect with all the nearby blocks, and this is explicated
     * by using '{@code *}' in the connectsWith {@code .json} array.
     */
    public boolean isConnectionGeneric() {
        return view.isConnectionGeneric();
    }

    /**
     * @return the print priority of the object.
     */
    public int getPrintPriority() {
        return model.getPrintPriority();
    }

    /**
     * @return the block state.
     */
    public BlockState getState() {
        return model.getState();
    }

    /**
     * @return the block's position.
     */
    public Position getPosition() {
        return model.getPosition();
    }

    /**
     * @return the texture's width.
     */
    public int getWidth() {
        return view.getFrameWidth();
    }

    /**
     * @return the texture's height.
     */
    public int getHeight() {
        return view.getFrameHeight();
    }

    /**
     * Set the default block's texture.
     * @param texture the new default texture's index.
     */
    public void setDefaultTexture(int texture) {

        if (texture < 0 || texture >= view.getAnimationSize()) {
            texture = Utility.getRandom(view.getAnimationSize());
        }

        view.setDefaultTexture(getName() + texture);
    }

    /**
     * Sets the block's hitbox following the array standard: {@code [left, right, top, bottom]}.
     * @param offsets the integer array with the hitbox' offsets.
     */
    public void setHitBox(int[] offsets) {

        if (offsets.length < 4) {
            Logger.log(Logger.WARNING, "The hitbox array was too short to initialize '" + this + "'s hitbox.");
            return;
        }

        this.hitBox = new HitBox(getPosition(), getWidth(), getHeight(), offsets);
    }

    /**
     * @return the block's hitbox.
     */
    public HitBox getHitBox() {
        return hitBox;
    }

    /**
     * Sets the block position in the cell grid.
     * @param x the x coordinate for the block.
     * @param y the y coordinate for the block.
     */
    public void setGridPosition(int x, int y) {
        model.setPosition(x * Basic.DEFAULT_BLOCK_WIDTH, y * Basic.DEFAULT_BLOCK_HEIGHT);
    }

    /**
     * Renders the block to screen with the given print priority.
     * @param g the graphics to draw on.
     */
    public void render(Graphics g) {


        view.render(getState(), g);

        if (Basic.DEBUG_MODE) {
            if (Basic.DEBUG_RENDER_TEXTURE) {
                Position rp = Utility.getEntityScreenCoords(getState());

                g.setColor(Basic.DEBUG_TEXTURE_COLOR);
                g.drawRect(rp.x, rp.y, view.getFrameWidth(), view.getFrameHeight());
            }

            if (hitBox == null) {
                Logger.logOnce(Logger.WARNING, "The object " + model.getIGN() + " doesn't have an hitbox.");
            }

            if (Basic.DEBUG_RENDER_HITBOX && hitBox != null) {
                Position hp = Utility.getHitBoxScreenCoords(hitBox);

                g.setColor(Basic.DEBUG_HITBOX_COLOR);
                g.drawRect(hp.x, hp.y, hitBox.getWidth(), hitBox.getHeight());
            }

        }
    }

    public String toString() {
        return "Block[%s, %s, d:%d-c:%d]".formatted(model.getIGN(), Position.toGrid(model.getPosition()), getDefaultPrintPriority(), getPrintPriority());
    }

    /**
     * @return the default print priority value.
     */
    public int getDefaultPrintPriority() {
        return model.getDefaultPrintPriority();
    }

    /**
     * @return the current block {@link Scene.Layer} instance where it currently is.
     */
    public Scene.Layer getLayer() {
        return Scene.Layer.getLayerFromPriority(model.getPrintPriority());
    }
}
