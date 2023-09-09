/*
 * Vulnerable Client-Server Application (VuCSA)
 *
 * Copyright (C) 2023 Michal Válka
 *
 * This program is free software: you can redistribute it and/or modify it under the terms of the
 * GNU General Public License as published by the Free Software Foundation, either version 3 of the
 * License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without
 * even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License along with this program. If
 * not, see <https://www.gnu.org/licenses/>.
 */
package com.warxim.vucsa.server.challenge.rcedeserialization.internal.advanced;

import java.util.Arrays;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Stream;

/**
 * Vulnerable class, part of RCE Deserialization challenge
 */
public class ObjectProcessor extends BaseProcessor {
    @Override
    public Object process(Object... args) {
        var object = (Object) args[0];
        var methodName = (String) args[1];
        var methodArgs = Arrays.copyOfRange(args, 2, args.length);
        var methodArgTypes = Stream.of(methodArgs)
                .map(Object::getClass)
                .toArray(Class[]::new);
        try {
            var method = object.getClass().getMethod(methodName, methodArgTypes);
            return method.invoke(object, methodArgs);
        } catch (Exception e) {
            Logger.getGlobal().log(Level.SEVERE, "Could not execute the method in object!", e);
        }
        return null;
    }
}