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
import lombok.RequiredArgsConstructor;

import java.net.Socket;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Connection manager for handling all active connections.
 */
@RequiredArgsConstructor
public class ConnectionManager implements ConnectionListener {
    /**
     * External connection listener for reporting connection events.
     */
    private final ConnectionListener connectionListener;

    /**
     * Message handler for connections.
     */
    private final MessageHandler messageHandler;

    /**
     * Internal counter for generating connection identifiers.
     */
    private final AtomicInteger counter = new AtomicInteger();

    /**
     * Map of connections (mapped by identifier).
     */
    private final ConcurrentHashMap<Integer, ServerConnection> connections = new ConcurrentHashMap<>();

    /**
     * Obtains connection with given identifier.
     * @return Connection or empty optional if it does not exist
     */
    public Optional<ServerConnection> get(int id) {
        return Optional.ofNullable(connections.get(id));
    }

    /**
     * Creates server connection and persists it in the manager.
     * @param socket Client socket
     * @return Created server connection
     */
    public ServerConnection create(Socket socket) {
        var connection = new ServerConnection(generateId(), socket, this, messageHandler);
        connection.start();
        connections.put(connection.getId(), connection);
        connectionListener.onConnectionStart(connection);
        return connection;
    }

    /**
     * Stops connection manager with all its connections.
     */
    public void stop() {
        connections.values().forEach(ServerConnection::stop);
    }

    @Override
    public void onConnectionStop(Connection connection) {
        connections.remove(connection.getId());
        connectionListener.onConnectionStop(connection);
    }

    /**
     * Generates new connection identifier.
     */
    private int generateId() {
        return counter.incrementAndGet();
    }
}
