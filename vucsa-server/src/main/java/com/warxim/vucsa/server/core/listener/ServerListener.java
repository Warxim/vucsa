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
package com.warxim.vucsa.server.core.listener;

/**
 * Server listener for listening for server events.
 */
public interface ServerListener {
    /**
     * Event before start step is processed (server is starting).
     */
    default void beforeStart() {}

    /**
     * Event after start step is processed (server is running).
     */
    default void afterStart() {}

    /**
     * Event before stop step is processed (server is stopping).
     */
    default void beforeStop() {}

    /**
     * Event before stop step is processed (server is offline).
     */
    default void afterStop() {}
}
