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
package com.ca.resources;

import com.ca.constants.Basic;
import com.ca.constants.Position;
import com.ca.entities.controllers.BlockController;
import com.ca.errors.Logger;
import com.ca.errors.general.InputNotValid;
import com.ca.errors.general.UnknownException;
import com.ca.errors.resources.ResourceNotLoaded;
import com.ca.game.BlockManager;
import com.ca.maps.GameScene;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

/**
 * Loads the game maps.
 * @since 1.0.00
 * @author Macaluso Francesco
 */
public class MapLoader {

    /**
     * Specifies which extensions are allowed to be loaded by this class.
     */
    private static final String[] metadataExt = { ".json" };

    /**
     * Opens the given json file.
     * @param file  the file to open.
     * @return the file in form of {@link JSONObject} instance.
     */
    private static JSONObject openFile(String file) {

        // Loads the file as an input stream
        InputStream is = SpritesheetLoader.class.getResourceAsStream(file);

        if (is == null) {
            Logger.log(Logger.MODE_CRITICAL, new ResourceNotLoaded(file));
            return new JSONObject();
        }

        try {
            return (JSONObject) new JSONParser().parse(new InputStreamReader(is));
        } catch (IOException | ParseException e) {
            Logger.log(Logger.MODE_CRITICAL, new UnknownException(MetadataDeserializer.class.getName() + ": " + e.getMessage()));
        }

        // This is actually never reached since Logger.log(MODE_CRITICAL) always throws an exception
        return new JSONObject();
    }

    /**
     * Initializes the given map with the {@code .json} file.
     * @param target the target {@link GameScene} to initialize.
     * @param resource the world {@code .json} file to use for the map's initialization.
     */
    public static void initialize(GameScene target, String resource) {

        Utility.checkExtension(resource, metadataExt);

        JSONObject jsonFile = openFile(resource);
        Logger.log(Logger.MESSAGE, "Starting loading map ('" + resource + "') - " + jsonFile.get("_comment"));

        target.setName(jsonFile.get("name").toString());

        JSONArray jsonMapArray = (JSONArray) jsonFile.get("data");
        int mapBlockCount = 0;

        for(Object o : jsonMapArray){

            if(o == null){
                Logger.log(Logger.WARNING, new InputNotValid(JSONObject.class, "null"));
                continue;
            }

            JSONObject jsonBlock = (JSONObject) o;

            String id = (String) jsonBlock.get("id");
            Long defaultTexture = ((Long)jsonBlock.get("texture"));

            if (!BlockManager.has(id)) {
                Logger.log(Logger.WARNING, "The block '" + id + "' couldn't be loaded into the map.");
                continue;
            }

            JSONObject jsonPosition = (JSONObject) jsonBlock.get("position");
            Position position = new Position(((Long) jsonPosition.get("x")).intValue(), ((Long) jsonPosition.get("y")).intValue());

            BlockController block = BlockManager.get(id, position.x, position.y);

            if (block == null) {
                continue;
            }

            if (defaultTexture == null) {
                defaultTexture = Basic.DEFAULT_RANDOM_TEXTURE;
            }

            block.setDefaultTexture(defaultTexture.intValue());

            target.getScene().add(block);

            mapBlockCount++;
        }

        Logger.log(Logger.MESSAGE, "The map ('" + resource + "') has been loaded successfully!");
        Logger.log(Logger.MESSAGE, "The map has loaded %d blocks.".formatted(mapBlockCount));
    }

}
