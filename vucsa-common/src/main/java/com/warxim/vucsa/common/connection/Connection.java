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
package com.warxim.vucsa.common.connection;

import com.warxim.vucsa.common.message.MessageHandler;
import com.warxim.vucsa.common.connection.listener.ConnectionListener;
import com.warxim.vucsa.common.message.Message;
import com.warxim.vucsa.common.message.MessageQueue;
import com.warxim.vucsa.common.util.MessageUtils;
import lombok.RequiredArgsConstructor;

import java.io.DataInputStream;
import java.io.EOFException;
import java.io.IOException;
import java.net.Socket;
import java.net.SocketException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Connection representing single connection between client and server.
 * <p>Implementation has to set socket.</p>
 */
@RequiredArgsConstructor
public abstract class Connection {
    /**
     * Connection identifier.
     */
    protected final int id;

    /**
     * Connection listener for reporting connection start/stop events.
     */
    protected final ConnectionListener listener;

    /**
     * Message handler for handling messages, which the connection read from the input stream.
     */
    protected final MessageHandler messageHandler;

    /**
     * Executor service for running read and write threads.
     */
    protected final ExecutorService executor = Executors.newFixedThreadPool(2);

    /**
     * Flag determining if the connection is already in closing state or not.
     */
    protected final AtomicBoolean closing = new AtomicBoolean(false);

    /**
     * Queue for message, which the connection will send using the output stream.
     */
    protected final MessageQueue outgoingQueue = new MessageQueue();

    /**
     * Connection socket, which has to be provided by the implementation.
     */
    protected Socket socket;

    /**
     * Connection state.
     */
    protected ConnectionState state;

    /**
     * Starts the connection.
     * <p>If the start fails, it is automatically stopped.</p>
     */
    public void start() {
        Logger.getGlobal().info("Starting connection...");
        state = ConnectionState.STARTING;
        if (!handleBeforeStart()) {
            stop();
        }

        executor.execute(this::doRead);
        executor.execute(this::doWrite);
        executor.execute(this::stop);

        if (!handleAfterStart()) {
            stop();
        }
        state = ConnectionState.STARTED;
        Logger.getGlobal().info("Connection started.");

        listener.onConnectionStart(this);
    }

    /**
     * Stops the connection.
     */
    public void stop() {
        if (!closing.compareAndSet(false, true)) {
            return;
        }

        Logger.getGlobal().info("Stopping connection...");
        state = ConnectionState.STOPPING;
        handleBeforeStop();

        executor.shutdownNow();
        if (socket != null) {
            try {
                socket.close();
            } catch (IOException e) {
                Logger.getGlobal().log(Level.SEVERE, "Could not close connection socket.", e);
            }
        }

        state = ConnectionState.STOPPED;
        handleAfterStop();
        Logger.getGlobal().info("Connection stopped.");

        listener.onConnectionStop(this);
    }

    /**
     * Sends message using the socket.
     * @param message Message to be sent
     */
    public void sendMessage(Message message) {
        this.outgoingQueue.add(message);
    }

    /**
     * Obtains connection state.
     * @return Connection state
     */
    public ConnectionState getState() {
        return state;
    }

    /**
     * Obtains connection identifier.
     * @return Connection identifier
     */
    public int getId() {
        return id;
    }

    /**
     * Runs before the connection starts.
     * @return {@code true} if everything went well; {@code false} if the connection should be stopped
     */
    protected boolean handleBeforeStart() {
        return true;
    }

    /**
     * Runs after the connection starts.
     * @return {@code true} if everything went well; {@code false} if the connection should be stopped
     */
    protected boolean handleAfterStart() {
        return true;
    }

    /**
     * Runs before the connection stops.
     */
    protected void handleBeforeStop() {}

    /**
     * Runs after the connection stops.
     */
    protected void handleAfterStop() {}

    /**
     * Reads messages from input stream and sends them to the handler.
     */
    private void doRead() {
        try (var in = new DataInputStream(socket.getInputStream())) {
            while (!closing.get()) {
                var maybeMessage = MessageUtils.readMessageFromInputStream(in);
                if (maybeMessage.isEmpty()) {
                    continue;
                }
                var message = maybeMessage.get();
                if (messageHandler.supports(message)) {
                    messageHandler.handleMessage(this, message);
                }
            }
        } catch (SocketException | EOFException e) {
            // Connection closed
        } catch (IOException e) {
            Logger.getGlobal().log(Level.SEVERE, "IO exception occurred durring connection read.", e);
        }
    }

    /**
     * Gets messages from outgoing queue and sends them using output stream.
     */
    private void doWrite() {
        try (var out = socket.getOutputStream()) {
            Message message;
            while (!closing.get() && (message = outgoingQueue.take()) != null) {
                MessageUtils.writeMessageToOutputStream(message, out);
            }
        } catch (SocketException e) {
            // Connection closed
        } catch (IOException e) {
            Logger.getGlobal().log(Level.SEVERE, "IO exception occurred durring connection write.", e);
        } catch (InterruptedException e) {
            // Interrupted
            Thread.currentThread().interrupt();
        }
    }
}
