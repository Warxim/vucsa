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
package com.warxim.vucsa.client.core;

import com.warxim.vucsa.common.connection.Connection;
import com.warxim.vucsa.common.connection.ConnectionState;
import com.warxim.vucsa.common.connection.listener.ConnectionListener;
import com.warxim.vucsa.common.connection.listener.ConnectionListenerManager;
import com.warxim.vucsa.common.message.Message;
import com.warxim.vucsa.common.message.MessageHandler;
import com.warxim.vucsa.common.message.MessageHandlerManager;

/**
 * Client manager for managing client connection.
 * <p>Wraps all required components and handles client creation, start, stop, ...</p>
 */
public class ClientManager implements ConnectionListener {
    private final ConnectionListenerManager connectionListenerManager;
    private final MessageHandlerManager messageHandlerManager;
    private Client client;

    public ClientManager() {
        this.connectionListenerManager = new ConnectionListenerManager();
        this.messageHandlerManager = new MessageHandlerManager();
        connectionListenerManager.registerListener(this);
    }

    /**
     * Creates and starts client core.
     * @param config Client configuration
     */
    public synchronized void start(ClientConfig config) {
        if (client != null) {
            return;
        }

        client = new Client(config, connectionListenerManager, messageHandlerManager);
        client.start();
    }

    /**
     * Stops client core.
     */
    public synchronized void stop() {
        if (client == null) {
            return;
        }

        client.stop();
    }

    /**
     * Obtains client core state.
     * @return Connection state
     */
    public synchronized ConnectionState getState() {
        if (client == null) {
            return ConnectionState.STOPPED;
        }

        return client.getState();
    }

    /**
     * Sends message using the client if it is available.
     * <p>If the client is not available, does nothing.</p>
     * @return {@code true} if the message was sent
     */
    public synchronized boolean sendMessage(Message message) {
        if (client != null && getState() == ConnectionState.STARTED) {
            client.sendMessage(message);
            return true;
        }
        return false;
    }

    /**
     * Registers connection listener.
     * @param clientListener Listener to be registered
     */
    public void registerConnectionListener(ConnectionListener clientListener) {
        connectionListenerManager.registerListener(clientListener);
    }

    /**
     * Unregisters connection listener.
     * @param clientListener Listener to be unregistered
     */
    public void unregisterConnectionListener(ConnectionListener clientListener) {
        connectionListenerManager.unregisterListener(clientListener);
    }

    /**
     * Registers message handler.
     * @param target Target identifier of the handler
     * @param messageHandler Handler to be registered
     */
    public void registerHandler(int target, MessageHandler messageHandler) {
        messageHandlerManager.registerHandler(target, messageHandler);
    }

    /**
     * Unregisters message handler.
     * @param target Target identifier of the handler
     */
    public void unregisterHandler(int target) {
        messageHandlerManager.unregisterHandler(target);
    }

    @Override
    public void onConnectionStop(Connection connection) {
        client = null;
    }
}
