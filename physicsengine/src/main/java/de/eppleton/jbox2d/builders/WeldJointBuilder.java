/**
 * This file is part of FXGameEngine 
 * A Game Engine written in JavaFX
 * Copyright (C) 2012 Anton Epple <info@eppleton.de>
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, version 2 of the License.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program. Look for COPYING file in the top folder.
 * If not, see http://opensource.org/licenses/GPL-2.0.
 * 
 * For alternative licensing or use in closed source projects contact Anton Epple 
 * <info@eppleton.de>
 */
package de.eppleton.jbox2d.builders;

import org.jbox2d.common.Vec2;
import org.jbox2d.dynamics.Body;
import org.jbox2d.dynamics.World;
import org.jbox2d.dynamics.joints.WeldJointDef;

/**
 *
 * @author antonepple
 */
public class WeldJointBuilder extends JointBuilder<WeldJointBuilder, WeldJointDef> {

    public WeldJointBuilder(World world, Body bodyA, Body bodyB, Vec2 anchor) {
        super(world, new WeldJointDef());
        jointDef.initialize(bodyA, bodyB, anchor);
       

    }

    public WeldJointBuilder dampingRatio(float dampingRatio) {
        jointDef.dampingRatio = dampingRatio;
        return this;
    }

    public WeldJointBuilder frequencyHz(float frequencyHz) {
        jointDef.frequencyHz = frequencyHz;
        return this;
    }

    public WeldJointBuilder referenceAngle(float referenceAngle) {
        jointDef.referenceAngle = referenceAngle;
        return this;
    }
}
