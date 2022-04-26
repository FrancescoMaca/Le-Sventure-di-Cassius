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

import com.ca.constants.Position;
import com.ca.entities.controllers.BlockController;
import com.ca.errors.Logger;
import com.ca.game.BlockManager;
import com.ca.game.CollisionManager;

import java.util.*;

/**
 * Represent a single scene plane. This is a rectangle made of {@link SceneSection} instances that are being initialized
 * with the default {@link com.ca.entities.controllers.CharacterController} instance loaded by the {@link BlockManager} class.
 * The default filler can be modified by the developer with the appropriate method. <br>
 * This class contains methods to retrieve information about the map itself such as the width and height of it. When
 * adding a block the map resizes itself if it needs to. All the non-added blocks by the user are by default instances of
 * the filler object.
 * @since 1.0.00
 * @author Macaluso Francesco
 */
public class BlockPlane {

    // TODO: set grid key coordinates to absolute and translate them to grid and vice versa ONLY WHEN NEEDED
    private final HashMap<Position, SceneSection> grid;

    /**
     * The upper left and the lower right corner of the map.
     */
    private Position start;
    private Position end;

    /**
     * Constructor for a basic empty map plane.
     */
    public BlockPlane() {

        grid = new HashMap<>() {
            public SceneSection get(Object key) {
                if (!(key instanceof Position position)) {
                    return null;
                }

                return values().stream().filter(sec -> sec.position.equals(position)).findFirst().orElse(null);
            }
        };
    }

    /**
     * Adds a blocks to the plane, updating the map corners and overwriting the existing blocks, if any.
     * @param block the block to add to the plane.
     */
    public void add(BlockController block) {

        if (block == null) {
            Logger.log(Logger.WARNING, "Trying to add to the map a null block!");
            return;
        }

        updateCorners(block.getPosition());

        SceneSection sec = get(Position.toGrid(block.getPosition()));

        if (sec == null) {
            return;
        }

        sec.addBlock(block);
    }

    /**
     * Finds all the nearby blocks to the given position by a {@link CollisionManager#CHECK_RADIUS} radius.
     * @param position the position where to start looking for blocks around it.
     * @return a list of all the not null blocks found.
     */
    @Deprecated
    public List<BlockController> getNearbyBlocks(Position position) {

        Position girdCenterPos = Position.toGrid(position);

        List<BlockController> blocks = new ArrayList<>();

        for(int x = girdCenterPos.x - CollisionManager.CHECK_RADIUS; x <= girdCenterPos.x + CollisionManager.CHECK_RADIUS; x++) {
            for(int y = girdCenterPos.y - CollisionManager.CHECK_RADIUS; y <= girdCenterPos.y + CollisionManager.CHECK_RADIUS; y++) {

                SceneSection sec = get(new Position(x, y));

                // If the section is valid adds all the non-null section's blocks to the list.
                if (sec != null) {
                    blocks.addAll(sec.getBlocks().stream().filter(Objects::nonNull).toList());
                }
            }
        }

        return blocks;
    }

    /**
     * @return all not null blocks in the map.
     */
    public List<BlockController> getAllBlocks() {
        List<BlockController> blocks = new ArrayList<>();

        Collection<SceneSection> sections = new ArrayList<>(grid.values());

        for(SceneSection sec : sections) {
            blocks.addAll(sec.getBlocks());
        }

        blocks.removeIf(Objects::isNull);

        return blocks;
    }

    /**
     * Finds the section at the given position.
     * @param p the {@link SceneSection} position.
     * @return the instance of the section if found, otherwise it adds a new {@link SceneSection} instance with the given
     * position and returns it.
     */
    public SceneSection get(Position p) {

        SceneSection sec = grid.get(p);

        // If the section is null then returns a new one
        if (sec == null) {
            SceneSection newSection = new SceneSection(p.x, p.y);

            grid.put(newSection.position, newSection);

            return newSection;
        }

        return sec;
    }

    /**
     * @return the coordinates in the upper left corner of the map.
     */
    public Position getGridPosition() {
        return new Position((start.x * 32), (start.y * 32));
    }

    /**
     * Updates the map bounds. This method uses ONLY grid positions.
     * To modify the parameter, call this method with {@link Position#toGrid(Position)}.
     * @param p the position of the new block
     */
    private void updateCorners(Position p) {

        Position gridPos = Position.toGrid(p);

        if(start == null){
            start = new Position(gridPos.x, gridPos.y);
        }

        if(end == null){
            end = new Position(gridPos.x, gridPos.y);

            end.x++;
            end.y++;
        }

        if (start.x > gridPos.x) {
            start.x = gridPos.x;
        }

        if (start.y > gridPos.y) {
            start.y = gridPos.y;
        }

        if (end.x < gridPos.x) {
            end.x = gridPos.x;
        }

        if (end.y < gridPos.y) {
            end.y = gridPos.y;
        }

    }

    /**
     * @return the map width in block number.
     */
    public int getWidth() {
        if (start == null || end == null) {
            Logger.log(Logger.WARNING, "Tried to access the map width when there are no blocks in it.");
            return 0;
        }

        // Adds one to count the top-most row of blocks too
        return end.x - start.x + 1;
    }

    /**
     * @return the map height in block number.
     */
    public int getHeight() {
        if (start == null || end == null) {
            Logger.log(Logger.WARNING, "Tried to access the map width when there are no blocks in it.");
            return 0;
        }

        // Adds one to count the left-most column of blocks too
        return end.y - start.y + 1;
    }

    /**
     * Represent a map square block. Each map block is made by a maximum of one  block for each {@link com.ca.maps.Scene.Layer}.
     * Each section has a position linked with the blocks on it. There is no way to have multiple blocks with the same priority
     * on the same square.
     */
    public static class SceneSection {

        private final Position position;

        private final BlockController[] blocks = new BlockController[Scene.Layer.values().length];

        /**
         * Creates an empty map section without any blocks on it.
         * @param x the x coordinate.
         * @param y the y coordinate.
         */
        public SceneSection(int x, int y) {
            position = new Position(x, y);
        }

        /**
         * Adds a block to a layer. If the layer is already occupied it overwrites the old block.
         * @param newBlock the block to add to the section.
         */
        public void addBlock(BlockController newBlock) {

            if (newBlock == null) {
                return;
            }

            blocks[newBlock.getLayer().ordinal()] = newBlock;
        }

        /**
         * @return {@code true} if all layers in the current section are empty, otherwise {@code false}.
         */
        public boolean isEmpty() {
            return Arrays.stream(blocks).allMatch(Objects::isNull);
        }

        /**
         * @return all the blocks in the section, including {@code null} blocks.
         */
        public ArrayList<BlockController> getBlocks() {
            return new ArrayList<>(Arrays.stream(blocks).toList());
        }

        public String toString() {
            return "SceneSection{" +
                    "position=" + position +
                    ", blocks=" + Arrays.toString(blocks) +
                    '}';
        }
    }
}
