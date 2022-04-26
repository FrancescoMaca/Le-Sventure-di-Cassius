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
package com.ca.entities.characters;

import com.ca.constants.Assets;
import com.ca.entities.controllers.CharacterController;
import com.ca.errors.Logger;
import com.ca.game.ItemManager;
import com.ca.resources.animations.Action;

/**
 * The player instance with all the animation listeners bound to it.
 * @since 1.0.00
 * @author Macaluso Francesco
 */
public class Player extends CharacterController {

    public Player(Assets.Info characterInfo) {
        super(characterInfo);
        this.equip(ItemManager.get("fist"));

        addAnimationListener(this, Action.Attacking);
        addAnimationListener(this, Action.AttackingHalberd);
        addAnimationListener(this, Action.AttackingSword);
        addAnimationListener(this, Action.AttackingFist);
        addAnimationListener(this, Action.Walking);
    }
}
