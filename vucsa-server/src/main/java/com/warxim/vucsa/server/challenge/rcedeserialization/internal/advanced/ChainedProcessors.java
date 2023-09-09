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

import java.util.ArrayList;
import java.util.Arrays;

/**
 * Vulnerable class, part of RCE Deserialization challenge
 */
public class ChainedProcessors extends BaseProcessor {
    private final ArrayList<ChainedProcessorDescriptor> processors;

    public ChainedProcessors(ArrayList<ChainedProcessorDescriptor> processors) {
        this.processors = processors;
    }

    public Object process(Object... args) {
        if (processors.isEmpty()) {
            return null;
        }

        var first = processors.get(0);
        var previousOutput = first.getProcessor().process(first.getArgs());

        for (var i = 1; i < processors.size(); ++i) {
            var processor = processors.get(i);
            var argsCopy = Arrays.copyOf(processor.getArgs(), processor.getArgs().length);
            for (var j = 0; j < argsCopy.length; ++j) {
                if (argsCopy[j] instanceof ChainedProcessorOutputAsArgPlaceholder) {
                    argsCopy[j] = previousOutput;
                }
            }
            previousOutput = processor.getProcessor().process(argsCopy);
        }

        return previousOutput;
    }
}