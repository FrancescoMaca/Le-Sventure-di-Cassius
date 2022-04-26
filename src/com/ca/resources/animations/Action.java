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
package com.ca.resources.animations;

/**
 * Enumerator which describes the type of the animation to load. The values of each enumerator
 * matches the animation in the {@code .json} resource file.
 * @since 1.0.00
 * @author Macaluso Francesco, Brkic Emir
 */
public enum Action {
    Walking("walk"),
    KnockBack("knockBack"),
    Idling("idle"),
    Dying("die"),
//
//    /**
//     * <b>TO FIX IN THE FUTURE:</b> <br>
//     * Broken suffix is not properly done, this class in not properly done, I don't know how we kept using this without problem without
//     * seeing how bad this is. The Action should not comprehend all the game states, (e.g. the Broken could be a part of state instead of
//     * a suffix for each action. Due to the lack of time this will not be modified.
//     */
//    BrokenIdle("broken_idle"),
//    BrokenAttack("broken_attack"),
//    BrokenWalking("broken_walk"),

    AttackingSword("attackingSword"),
    AttackingHalberd("attackingHalberd"),
    AttackingFist("attackingFist"),
    Running("run"),
    Attacking("attack");

    final String type;

    Action(String type) {
        this.type = type;
    }

    /**
     * @return the action id.
     */
    public String getID() {
        return type;
    }

    /**
     * Checks if the {@link Action} is an attacking type.
     * @param a the action to scan.
     * @return {@code true} if the given action is attacking, otherwise {@code false}.
     */
    public static boolean isAttacking(Action a) {
        return a == Action.Attacking ||
                a == Action.AttackingSword ||
                a == Action.AttackingHalberd ||
                a == Action.AttackingFist;
    }
}
