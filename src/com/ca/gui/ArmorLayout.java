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
package com.ca.gui;

import java.awt.*;
import java.util.HashMap;

public class ArmorLayout {

    private final HashMap<String,ArmorCells> armorSlots;

    private final Point inventoryImageCorner;

    public ArmorLayout(Point imageCorner) {
        armorSlots = new HashMap<>();

        inventoryImageCorner = imageCorner;

        armorSlots.put("helmet", new ArmorCells());
        armorSlots.put("chest-plate", new ArmorCells());
        armorSlots.put("leggings", new ArmorCells());
        armorSlots.put("shoes", new ArmorCells());
        armorSlots.put("backpack", new ArmorCells());
        armorSlots.put("equipable1", new ArmorCells());
        armorSlots.put("equipable2", new ArmorCells());

        updateArmorSlotsPosition();
    }

    public void paintComponent(Graphics g){

        updateArmorSlotsPosition();

        armorSlots.get("helmet").render(g);
        armorSlots.get("chest-plate").render(g);
        armorSlots.get("leggings").render(g);
        armorSlots.get("shoes").render(g);
        armorSlots.get("backpack").render(g);
        armorSlots.get("equipable1").render(g);
        armorSlots.get("equipable2").render(g);

    }

    private void updateArmorSlotsPosition(){
        armorSlots.get("helmet").setCellPosition(inventoryImageCorner.x + 72, inventoryImageCorner.y + 26);
        armorSlots.get("chest-plate").setCellPosition(inventoryImageCorner.x + 72, inventoryImageCorner.y + 54);
        armorSlots.get("leggings").setCellPosition(inventoryImageCorner.x + 72, inventoryImageCorner.y + 82);
        armorSlots.get("shoes").setCellPosition(inventoryImageCorner.x + 72, inventoryImageCorner.y + 110);
        armorSlots.get("backpack").setCellPosition(inventoryImageCorner.x + 9, inventoryImageCorner.y + 54);
    }

    public HashMap<String, ArmorCells> getArmorSlots() {
        return armorSlots;
    }
}
