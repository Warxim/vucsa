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

import com.warxim.vucsa.server.core.connection.ConnectionManager;
import com.warxim.vucsa.server.core.listener.ServerListener;
import com.warxim.vucsa.common.connection.listener.ConnectionListener;
import com.warxim.vucsa.common.message.MessageHandler;
import lombok.RequiredArgsConstructor;

import java.io.IOException;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.SocketException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Server core.
 */
@RequiredArgsConstructor
public class Server {
    /**
     * Configuration of the server.
     */
    private final ServerConfig config;

    /**
     * Server listener for reporting server events.
     */
    private final ServerListener serverListener;

    /**
     * Connection listener for reporting connection events.
     */
    private final ConnectionListener connectionListener;

    /**
     * Message handler for handling received messages.
     */
    private final MessageHandler messageHandler;

    /**
     * State of the server core.
     */
    private ServerState state;

    /**
     * Core thread for accepting connections.
     */
    private Thread thread;

    /**
     * Core server socket.
     */
    private ServerSocket serverSocket;

    /**
     * Manager of connections.
     */
    private ConnectionManager connectionManager;

    /**
     * Starts the server core.
     */
    public void start() {
        Logger.getGlobal().info("Server starting...");
        state = ServerState.STARTING;
        serverListener.beforeStart();

        connectionManager = new ConnectionManager(connectionListener, messageHandler);
        thread = new Thread(this::run);
        thread.start();
    }

    /**
     * Stops the server core.
     */
    public void stop() {
        if (thread != null) {
            thread.interrupt();
            closeSocket();
        }
    }

    /**
     * Obtains state of the server.
     * @return Server state
     */
    public ServerState getState() {
        return state;
    }

    /**
     * Runs server core and accepts connections.
     */
    private void run() {
        Logger.getGlobal().info("Server started.");
        state = ServerState.STARTED;
        serverListener.afterStart();

        try {
            serverSocket = new ServerSocket(config.getServerPort(), 4, InetAddress.getByName(config.getServerHost()));
            while (state == ServerState.STARTED) {
                var clientSocket = serverSocket.accept();
                connectionManager.create(clientSocket);
            }
        } catch (SocketException e) {
            // Socket closed
        } catch (IOException e) {
            Logger.getGlobal().log(Level.SEVERE, "IO exception occurred during server socket processing!", e);
        }

        Logger.getGlobal().info("Server stopping...");
        state = ServerState.STOPPING;
        serverListener.beforeStop();

        connectionManager.stop();

        closeSocket();

        Logger.getGlobal().info("Server stopped.");
        state = ServerState.STOPPED;
        serverListener.afterStop();
    }

    /**
     * Try to close socket.
     */
    private void closeSocket() {
        if (serverSocket == null) {
            return;
        }

        try {
            serverSocket.close();
        } catch (IOException e) {
            Logger.getGlobal().log(Level.SEVERE, "Could not close server socket!", e);
        }
    }
}
