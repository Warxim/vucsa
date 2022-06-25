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
import lombok.RequiredArgsConstructor;

import java.util.concurrent.ConcurrentHashMap;
import java.util.logging.Logger;

/**
 * Message handler manager aggregates message managers registered for specific target identifiers.
 */
@RequiredArgsConstructor
public class MessageHandlerManager implements MessageHandler {
    /**
     * Handlers registered for certain message target.
     */
    private final ConcurrentHashMap<Integer, MessageHandler> handlers = new ConcurrentHashMap<>();

    /**
     * Registers message handler.
     * @param target Target identifier of the handler
     * @param messageHandler Handler to be registered
     */
    public void registerHandler(int target, MessageHandler messageHandler) {
        this.handlers.putIfAbsent(target, messageHandler);
    }

    /**
     * Unregisters message handler.
     * @param target Target identifier of the handler
     */
    public void unregisterHandler(int target) {
        this.handlers.remove(target);
    }

    @Override
    public boolean supports(Message message) {
        return true;
    }

    @Override
    public boolean handleMessage(Connection connection, Message message) {
        var target = message.getTarget();
        var handler = this.handlers.get(target);
        if (handler == null) {
            Logger.getGlobal().severe(() -> String.format("No handler found with id [%d]...", target));
            return false;
        }

        if (!handler.supports(message)) {
            Logger.getGlobal().severe(() -> String.format("Handler [%d] does not support message! Ignoring message...", target));
            return false;
        }

        return handler.handleMessage(connection, message);
    }
}
