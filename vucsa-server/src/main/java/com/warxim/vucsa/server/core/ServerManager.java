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
package com.warxim.vucsa.server.core;

import com.warxim.vucsa.server.core.listener.ServerListenerManager;
import com.warxim.vucsa.common.connection.listener.ConnectionListenerManager;
import com.warxim.vucsa.common.message.MessageHandler;
import com.warxim.vucsa.common.message.MessageHandlerManager;
import com.warxim.vucsa.server.core.listener.ServerListener;

/**
 * Server manager for managing server core.
 * <p>Wraps all required components and handles server creation, start, stop, ...</p>
 */
public class ServerManager implements ServerListener {
    private final ServerListenerManager serverListenerManager;
    private final ConnectionListenerManager connectionListenerManager;
    private final MessageHandlerManager messageHandlerManager;
    private Server server;

    public ServerManager() {
        this.serverListenerManager = new ServerListenerManager();
        this.connectionListenerManager = new ConnectionListenerManager();
        this.messageHandlerManager = new MessageHandlerManager();
        serverListenerManager.registerListener(this);
    }

    /**
     * Creates and starts server core.
     * @param config Server configuration
     */
    public synchronized void start(ServerConfig config) {
        if (server != null) {
            return;
        }

        server = new Server(
                config,
                serverListenerManager,
                connectionListenerManager,
                messageHandlerManager
        );
        server.start();
    }

    /**
     * Stops server core.
     */
    public synchronized void stop() {
        if (server == null) {
            return;
        }

        server.stop();
    }

    /**
     * Obtains server core state.
     * @return Connection state
     */
    public synchronized ServerState getState() {
        if (server == null) {
            return ServerState.STOPPED;
        }

        return server.getState();
    }

    /**
     * Registers server listener.
     * @param serverListener Listener to be registered
     */
    public void registerServerListener(ServerListener serverListener) {
        serverListenerManager.registerListener(serverListener);
    }

    /**
     * Unregisters server listener.
     * @param serverListener Listener to be unregistered
     */
    public void unregisterServerListener(ServerListener serverListener) {
        serverListenerManager.unregisterListener(serverListener);
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
    public void afterStop() {
        server = null;
    }
}
