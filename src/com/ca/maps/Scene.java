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
package com.ca.maps;

import com.ca.constants.Basic;
import com.ca.constants.Position;
import com.ca.entities.controllers.BlockController;
import com.ca.errors.general.InputNotValid;

import java.util.*;

/**
 * Defines a {@code XY} grid where x is the width of each texture and Y is the height of the block texture.
 * @since 1.0.00
 * @author Macaluso Francesco
 */
public class Scene {

    /**
     * Links all grid positions to the block controllers.
     */
    private final BlockPlane scenes = new BlockPlane();

    /**
     * Adds a block on the grid.
     * @param block the block to be added.
     */
    public void add(BlockController block) {
        scenes.add(block);
    }

    /**
     * Finds all blocks of a specific layer and returns them as a ordered {@link List<BlockController>}.
     * @param targetLayer the layer to look for in the {@link Layer} enumerator.
     * @return a list of the blocks in order of print priority. (less -> most)
     */
    public List<BlockController> getLayer(Layer targetLayer) {

        List<BlockController> ordered = getBlocks().stream().filter(b -> b.getLayer() == targetLayer).toList();

        return ordered.stream().sorted(Comparator.comparingInt(BlockController::getPrintPriority))
                .sorted(Comparator.comparingInt(o -> o.getPosition().y)).toList();
    }

    /**
     * @return the scene grid position.
     */
    public Position getGridPosition() {
        return scenes.getGridPosition();
    }

    /**
     * @return the map's width in blocks. To get the pixel width just multiply this my {@link Basic#DEFAULT_BLOCK_WIDTH}.
     */
    public int getWidth() {
        return scenes.getWidth();
    }

    /**
     * @return the map's height in blocks. To get the pixel height just multiply this my {@link Basic#DEFAULT_BLOCK_HEIGHT}.
     */
    public int getHeight() {
        return scenes.getHeight();
    }

    /**
     * @return  all the blocks in the scene.
     */
    public List<BlockController> getBlocks() {
        return scenes.getAllBlocks();
    }

    /**
     * Get the block at the given coordinate.
     * @param x the x coordinate.
     * @param y the y coordinate.
     * @return the block at the given coordinate.
     */
    public BlockPlane.SceneSection get(int x, int y) {

        Position p = new Position(x, y);

        return scenes.get(p);
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder("MapGrid{");

        for(BlockController block : scenes.getAllBlocks()) {
            sb.append(block).append(", ");
        }

        return sb.append('}').toString();
    }

    /**
     * This enumerator represents the different printing layers of the blocks and entities.
     * See also the constants in {@link Basic} with prefix '{@code LAYER_}' that contains each
     * priority range for each layer.
     */
    public enum Layer {
        BACKGROUND (Basic.LAYER_BACKGROUND_LOW, Basic.LAYER_BACKGROUND_HIGH),
        MIDDLEGROUND(Basic.LAYER_MIDDLEGROUND_LOW, Basic.LAYER_MIDDLEGROUND_HIGH),
        FOREGROUND(Basic.LAYER_FOREGROUND_LOW, Basic.LAYER_FOREGROUND_HIGH),
        OVERLAY(Basic.LAYER_OVERLAY_LOW, Basic.LAYER_OVERLAY_HIGH);

        private final int low;
        private final int high;

        /**
         * @return the lowest priority of the current layer.
         */
        public int getLow() {
            return low;
        }

        /**
         * @return the highest priority of the current layer.
         */
        public int getHigh() {
            return high;
        }

        Layer(int low, int high) {
            this.low = low;
            this.high = high;

        }

        /**
         * Checks if the given priority is part of a {@link Layer} enumerator.
         * @param priority the given priority.
         * @throws InputNotValid if the given priority is out of range. The valid priorities are between 0 and 15 included.
         * @return a {@link Layer} enumerator of the priority.
         */
        public static Layer getLayerFromPriority(int priority) {

            // Returns the layer containing the priority
            for(Layer layer : Layer.values()) {
                if (priority >= layer.getLow() && priority <= layer.getHigh()) {
                    return layer;
                }
            }

            throw new InputNotValid("0 - 15", priority);
        }
    }
}