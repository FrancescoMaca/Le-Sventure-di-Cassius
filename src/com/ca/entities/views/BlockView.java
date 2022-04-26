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

import com.ca.constants.Basic;
import com.ca.constants.Direction;
import com.ca.constants.Position;
import com.ca.entities.states.BlockState;
import com.ca.errors.Logger;
import com.ca.entities.states.EntityState;
import com.ca.errors.general.UnknownException;
import com.ca.game.CollisionManager;
import com.ca.maps.GameScene;
import com.ca.resources.Utility;
import com.ca.resources.animations.BlockAnimation;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

/**
 * This class implements the rendering object for the {@link com.ca.entities.controllers.BlockController} class.
 *
 * @see EntityView
 * @see com.ca.entities.models.BlockModel
 * @since 1.0.00
 * @author Macaluso Francesco, Brkic Emir, Vidoni Pietro
 */
public class BlockView extends EntityView {

    private final List<BufferedImage> overlays = new ArrayList<>();

    private final HashMap<Direction, BufferedImage> activeOverlays = new HashMap<>();
    private boolean isPostRendered = false;
    private final boolean printOnBlock;

    private final String[] connectsWith;

    // TODO: change the last two parameters with the object Overlay in 'com.ca.entities.views.overlays'
    public BlockView(String id, List<BlockAnimation> animations, BufferedImage[] overlays, String[] connectsWith, boolean pob) {
        super("");

        String[] names = new String[animations.size()];
        for(int i = 0; i < animations.size(); i ++) {
            names[i] = id + i;
        }

        for(int i = 0; i < animations.size(); i++) {
            this.animations.put(names[i], animations.get(i));
        }

        setDefaultTexture(names[0]);

        this.msgBox = null;

        this.connectsWith = connectsWith;
        this.printOnBlock = pob;

        if(overlays != null) {
            List<BufferedImage> overlaysList = Arrays.asList(overlays);

            this.overlays.addAll(overlaysList);
        }
    }

    /**
     * @return {@code true} if the block has any overlays, otherwise {@code false}.
     */
    public boolean hasOverlays() {
        return overlays.size() != 0;
    }

    /**
     * @return {@code true} if the current block can connect with all the nearby blocks, and this is explicated
     * by using '{@code *}' in the connectsWith {@code .json} array.
     */
    public boolean isConnectionGeneric() {
        return Arrays.asList(connectsWith).contains("*");
    }

    /**
     * Sets the post render status of the current block. If the current block has already been analyzed then the {@link com.ca.gui.PostRenderer}
     * can simply skip this.
     * @param isPostRendered the new post render status.
     */
    public void isPostRendered(boolean isPostRendered) {
        this.isPostRendered = isPostRendered;
    }

    /**
     * @return {@code true} if the block has already been processed by the {@link com.ca.gui.PostRenderer#apply(GameScene)} method,
     * otherwise {@code false}.
     */
    public boolean isPostRendered() {
        return isPostRendered;
    }

    public BufferedImage[] getOverlays() {
        return overlays.toArray(new BufferedImage[0]);
    }

    /**
     * @return a string array containing the list of blocks with whom the current block can connect to.
     */
    public String[] getAllowedConnections() {
        return connectsWith;
    }

    public boolean printOnBlock() {
        return printOnBlock;
    }

    /**
     * Activates a certain overlay of the block.
     * @param direction the overlay's direction.
     */
    public void activateOverlay(Direction direction, BufferedImage overlay) {
        activeOverlays.put(direction, overlay);

        // Updates the corner flags
        if (activeOverlays.containsKey(Direction.UP) && activeOverlays.containsKey(Direction.LEFT)) {
            activeOverlays.put(Direction.UP_LEFT, overlays.get(Direction.UP_LEFT.ordinal()));
        }

        if (activeOverlays.containsKey(Direction.DOWN) && activeOverlays.containsKey(Direction.LEFT)) {
            activeOverlays.put(Direction.DOWN_LEFT, overlays.get(Direction.DOWN_LEFT.ordinal()));
        }

        if (activeOverlays.containsKey(Direction.UP) && activeOverlays.containsKey(Direction.LEFT)) {
            activeOverlays.put(Direction.UP_RIGHT, overlays.get(Direction.UP_RIGHT.ordinal()));
        }

        if (activeOverlays.containsKey(Direction.DOWN) && activeOverlays.containsKey(Direction.RIGHT)) {
            activeOverlays.put(Direction.DOWN_RIGHT, overlays.get(Direction.DOWN_RIGHT.ordinal()));
        }
    }

    protected final void loadAnimations() { }

    public void render(EntityState bState, Graphics g) {

        BlockState state;

        if (bState instanceof BlockState casted) {
            state = casted;
        }
        else {
            Logger.log(Logger.MODE_CRITICAL, new UnknownException("The block and the state differ in their type!"));
            return;
        }

        BufferedImage frame = animations.get(defaultTexture).getCurrentFrame(Basic.DEFAULT_BLOCK_DIRECTION);

        if(Utility.isBlockInView(state, CollisionManager.CHECK_RADIUS)) {
            Position p = Utility.getEntityScreenCoords(state);

            g.drawImage(frame, p.x, p.y, frame.getWidth(),frame.getHeight(), null);

            for(BufferedImage overlay : activeOverlays.values()) {
                if (overlay != null) {
                    g.drawImage(overlay, p.x, p.y, overlay.getWidth(), overlay.getHeight(), null);
                }
            }
        }
    }
}
