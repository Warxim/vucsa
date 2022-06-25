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
package com.warxim.vucsa.common.message;

import com.warxim.vucsa.common.connection.Connection;

/**
 * Endpoint for handling messages.
 * <p>Represents "endpoint" for messages.</p>
 */
public interface MessageHandler {
    /**
     * Checks whether the given message is supported by the handler.
     * @param message Message to be checked
     * @return {@code true} if the message is supported
     */
    boolean supports(Message message);

    /**
     * Handles message in the handler
     * @param connection Connection, which sent the message
     * @param message Message to be handled
     * @return {@code true} if the message has been correctly handled
     */
    boolean handleMessage(Connection connection, Message message);
}
