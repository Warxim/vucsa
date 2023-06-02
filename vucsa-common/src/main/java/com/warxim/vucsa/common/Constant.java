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
package com.warxim.vucsa.common;

/**
 * Global constants.
 */
public final class Constant {
    public static final String VERSION = "1.0.1";
    public static final String WEB = "https://vucsa.warxim.com";

    public static final String DEFAULT_SERVER_HOST = "127.0.0.1";
    public static final int DEFAULT_SERVER_PORT = 8765;

    public static final String SERVER_CONFIG_PATH = "server.json";

    private Constant() {}
}
