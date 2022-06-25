/*
 * Vulnerable Client-Server Application (VuCSA)
 *
 * Copyright (C) 2021 Michal Válka
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
package com.warxim.vucsa.common.util;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

/**
 * Utils for using GSON library.
 */
public final class GsonUtils {
    /**
     * GSON instance for serializing/deserializing in the application.
     */
    private static final Gson GSON = new GsonBuilder()
            .setPrettyPrinting()
            .create();

    /**
     * GSON instance for serializing/deserializing in the application (for light purposes, e.g. network communication).
     */
    private static final Gson GSON_LIGHT = new GsonBuilder()
            .create();

    /**
     * Obtains GSON instance.
     * @return GSON instance used throughout the whole application
     */
    public static Gson getGson() {
        return GSON;
    }

    /**
     * Obtains GSON instance for light JSON creation (without pretty printing).
     * @return GSON instance used throughout the whole application
     */
    public static Gson getGsonLight() {
        return GSON_LIGHT;
    }

    private GsonUtils() {}
}
