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

import com.ca.constants.Basic;
import com.ca.constants.Direction;
import com.ca.resources.animations.template.Animation;
import com.ca.resources.animations.template.EntityAnimation;

import java.util.List;

/**
 * Animation class used by all entities for special animations. These animations have directions if the
 * animation itself supports it.
 *
 * @since 1.0.00
 * @see Direction
 * @author Macaluso Francesco
 */
public class CharacterAnimation extends EntityAnimation {

    /**
     * The animation's action, Look {@link Action}.
     */
    private Action action;

    /**
     * Creates a character animation.
     * @param animations the animation list of the current character.
     * @param action the action of the animation.
     */
    public CharacterAnimation(List<Animation> animations, Action action) {
        super(animations);

        this.action = action;
        this.lastDirection = Basic.DEFAULT_ENTITY_DIRECTION;
    }

    /**
     * @return the animation name.
     */
    public Action getType() {
        return action;
    }

    /**
     * Sets the animation action.
     * @param action the action to se the animation to.
     */
    public void setAction(Action action) {
        this.action = action;
    }

    @Override
    public String toString() {
        return "[Action: " + action + "] " + super.toString();
    }
}
