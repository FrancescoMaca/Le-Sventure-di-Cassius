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

import com.ca.constants.Assets;
import com.ca.errors.Logger;
import com.ca.errors.general.UnknownException;
import com.ca.errors.resources.ResourceNotLoaded;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.awt.*;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

/**
 * This class is the core class for all the resource management of the game. It makes available different
 * methods to load different files from the resource subfolder ({@code "/resources/"}).
 * @since 1.0.00
 * @author Macaluso Francesco
 */
public final class ResourceLoader {

    private static final String fontPath = "fonts/" + Assets.FONT_NAME + ".ttf";

    static {
        Assets.FONT = loadFonts(fontPath);
    }

    /**
     * Loads the font from the default resource location.
     * @param path the {@code .ttf} font file path.
     * @return the {@link Font} instance of the given font, if the file path is not valid, or the file
     * couldn't be opened for any reasons the default font will be returned (Arial - plain - 12pt).
     */
    public static Font loadFonts(String path) {

        try {
            // Opens the font file
            InputStream is = ResourceLoader.class.getResourceAsStream(path);

            // If the file is opened it returns the font created by that file, if possible
            if (is != null) {
                return Font.createFont(Font.TRUETYPE_FONT, is).deriveFont((float) Assets.FONT_SMALL_SIZE);
            }
        } catch (IOException | FontFormatException e) {
            Logger.log(Logger.MODE_SALVAGE, new ResourceNotLoaded(path));
        }

        // Returning default font
        return new Font(Assets.FONT_NAME_DEFAULT, Font.PLAIN, (int) Assets.FONT_SMALL_SIZE);
    }


    /**
     * Opens the given {@code .json} file.
     * @param file  the file to open.
     * @return the file in form of {@link JSONObject} instance.
     */
    public static Object loadJsonFile(String file) {

        // Loads the file as an input stream
        InputStream is = SpritesheetLoader.class.getResourceAsStream(file);

        if (is == null) {
            Logger.log(Logger.MODE_CRITICAL, new ResourceNotLoaded(file));
            return new Object();
        }

        try {
            return new JSONParser().parse(new InputStreamReader(is));
        } catch (IOException | ParseException e) {
            Logger.log(Logger.MODE_CRITICAL, new UnknownException(MetadataDeserializer.class.getName() + ": " + e.getMessage()));
        }

        return new Object();
    }

    public static InputStream loadFile(String path) {
        return ResourceLoader.class.getResourceAsStream(path);
    }
}
