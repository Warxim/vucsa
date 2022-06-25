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
package com.warxim.vucsa.server.configuration;

import com.warxim.vucsa.common.util.GsonUtils;

import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.NoSuchFileException;

/**
 * Static class for configuration saving.
 */
public class ConfigurationSaver {
    private ConfigurationSaver() { }

    /**
     * Saves configuration to specified path.
     * @param path Path to configuration file
     * @param configuration Configuration to be stored
     */
    public static void save(String path, Configuration configuration) throws ConfigurationException {
        var gson = GsonUtils.getGson();

        try (var writer = gson.newJsonWriter(new FileWriter(path))) {
            gson.toJson(gson.toJsonTree(configuration), writer);
        } catch (NoSuchFileException e) {
            throw new ConfigurationException("Could not found configuration!", e);
        } catch (IOException e) {
            throw new ConfigurationException("Could not save configuration!", e);
        }
    }
}
