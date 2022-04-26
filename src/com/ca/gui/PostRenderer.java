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
package com.ca.gui;

import com.ca.constants.Basic;
import com.ca.constants.Direction;
import com.ca.constants.Position;
import com.ca.entities.controllers.BlockController;
import com.ca.errors.Logger;
import com.ca.maps.GameScene;
import com.ca.maps.Scene;

import java.util.Arrays;

/**
 * This class handles the post render logic of the map rendering, meaning that by default all the blocks have a single
 * look, which is not 'eye pleasing'. The role of this class is to check which blocks touch which other blocks and
 * based on that it tells the block to also render a certain layer. <br>
 * <b>IMPORTANT: </b>The post rendering happens <u>once</u>, and since <u>the map is not dynamic</u> the post render
 * action should not be needed more than once.
 * @since 1.0.00
 * @author Macaluso Francesco
 */
public class PostRenderer {

    /**
     * Applies the post rendering to a given game scene.
     * @param target the game scene to post render.
     */
    public static void apply(GameScene target) {

        // If the post rendering is off then it ends the call
        if (!Basic.POST_RENDER) {
            return;
        }

        if (target == null) {
            Logger.log(Logger.WARNING, "Post rendering has been interrupted! The game scene was null.");
            return;
        }

        Logger.log(Logger.MESSAGE, "Starting post render on '" + target.getName() + "'...");

        for(Scene.Layer currentLayer : Scene.Layer.values()) {
            for (BlockController block : target.getScene().getLayer(currentLayer)) {

                if (!block.hasOverlays() || block.isPostRendered()) {
                    continue;
                }

                // Gets the adjacent blocks
                Position bp = Position.toGrid(block.getPosition());

                // Checks all directions from the current block
                for(Direction d : Direction.values()) {
                    Position vector = Direction.convert(d, 1);

                    BlockController b = target.getScene().get(bp.x + vector.x, bp.y + vector.y).getBlocks().get(currentLayer.ordinal());

                    if (b == null) {
                        continue;
                    }

                    if (isConnectionValid(block, b) && (b.getDefaultPrintPriority() <= block.getDefaultPrintPriority())) {
                        b.paintOverlay(d, block.getOverlay(b.printsOnBlock() ? Direction.getOpposite(d) : d));
                    }
                }

                block.isPostRendered(true);
            }
        }
        Logger.log(Logger.MESSAGE, "Post render finished successfully for '" + target.getName() + "'.");
    }

    /**
     * Checks if the connection exists between two blocks.
     * @param b1 the first block to compare.
     * @param b2 the second block to compare.
     * @return {@code true} if both blocks have the ability to connect together.
     */
    private static boolean isConnectionValid(BlockController b1, BlockController b2) {
        return (canConnect(b1, b2) && !b1.getName().equals(b2.getName())) || (isConnectionAllowed(b1, b2));
    }

    /**
     * Checks if the given blocks share the same connection type.
     * @param b1 the first block to compare.
     * @param b2 the second block to compare.
     * @return {@code true} if the connection is generic, meaning that both blocks can connect to every
     * other blocks, otherwise {@code false}.
     */
    private static boolean canConnect(BlockController b1, BlockController b2) {
        return b1.isConnectionGeneric() && b2.isConnectionGeneric();
    }

    /**
     * Checks if a not generic block connection type can connect to a given block.
     * @param source the allowed block connection list's source.
     * @param target the block to check in the list.
     * @return {@code true} if the target block is contained in the source's connection list,
     * otherwise {@code false}.
     */
    private static boolean isConnectionAllowed(BlockController source, BlockController target) {
        return Arrays.stream(source.getAllowedBlockConnections()).anyMatch(s -> s.equals(target.getName()));
    }
}
