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
package com.warxim.vucsa.server.core.connection;

import com.warxim.vucsa.common.connection.Connection;
import com.warxim.vucsa.common.connection.listener.ConnectionListener;
import com.warxim.vucsa.common.message.MessageHandler;

import java.net.Socket;

/**
 * Server connection representing connection between client and server.
 */
public class ServerConnection extends Connection {
    public ServerConnection(int id, Socket socket, ConnectionListener listener, MessageHandler handler) {
        super(id, listener, handler);
        this.socket = socket;
    }
}

