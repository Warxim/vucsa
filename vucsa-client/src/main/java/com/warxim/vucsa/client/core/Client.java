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
import com.warxim.vucsa.common.connection.listener.ConnectionListener;
import com.warxim.vucsa.common.message.MessageHandler;

import java.io.IOException;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Client, which is used to communicate with the server.
 * <p>The client is created through ClientManager, when user starts it in the Settings tab.</p>
 */
public class Client extends Connection {
    private final ClientConfig config;

    public Client(
            ClientConfig config,
            ConnectionListener connectionListener,
            MessageHandler messageHandler) {
        super(1, connectionListener, messageHandler);
        this.config = config;
    }

    @Override
    protected boolean handleBeforeStart() {
        if (!super.handleBeforeStart()) {
            return false;
        }

        try {
            socket = new Socket(config.getServerHost(), config.getServerPort());
        } catch (IOException e) {
            Logger.getGlobal().log(Level.SEVERE, "Could not start client socket!", e);
            return false;
        }

        return true;
    }
}
