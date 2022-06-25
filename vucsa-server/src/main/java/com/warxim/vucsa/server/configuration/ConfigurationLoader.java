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

import com.google.gson.JsonParseException;
import com.google.gson.JsonParser;
import com.google.gson.stream.JsonReader;
import com.warxim.vucsa.common.util.GsonUtils;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.NoSuchFileException;
import java.util.function.Supplier;

/**
 * Static class for configuration loading.
 */
public class ConfigurationLoader {
    private ConfigurationLoader() { }

    /**
     * Loads configuration from specified path or get default and save it as configuration if it does not exist.
     * @param path Path to configuration file
     * @return Loaded configuration
     */
    public static Configuration loadOrDefault(String path, Supplier<Configuration> defaultSupplier) throws ConfigurationException {
        try (var reader = new JsonReader(new FileReader(path))) {
            return GsonUtils.getGson()
                    .fromJson(JsonParser.parseReader(reader), Configuration.class);
        } catch (JsonParseException e) {
            throw new ConfigurationException("Could not parse configuration!", e);
        } catch (NoSuchFileException | FileNotFoundException e) {
            var configuration = defaultSupplier.get();
            ConfigurationSaver.save(path, configuration);
            return configuration;
        } catch (IOException e) {
            throw new ConfigurationException("Could not load configuration!", e);
        }
    }
}
