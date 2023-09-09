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
package com.warxim.vucsa.common.util;

import com.warxim.vucsa.common.message.MessageType;
import com.warxim.vucsa.common.message.SerializedMessage;
import com.warxim.vucsa.common.message.Message;

import java.io.DataInputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.ByteBuffer;
import java.util.Optional;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Utils for working with messages.
 */
public final class MessageUtils {
    /**
     * Writes message to output stream.
     * <p>Serializes the message and writes it to given output stream.</p>
     * @param message Message to be written
     * @param out Output stream
     */
    public static boolean writeMessageToOutputStream(Message message, OutputStream out) throws IOException {
        var serializer = message.getType().getSerializer();
        var maybeSerializedMessage = serializer.serializeMessage(message);
        if (maybeSerializedMessage.isEmpty()) {
            return false;
        }
        var serializedMessage = maybeSerializedMessage.get();
        var buffer = ByteBuffer.allocate(4 * 3 + serializedMessage.getLength()); // 3x INT + payload length
        buffer.putInt(serializedMessage.getType().getValue());
        buffer.putInt(serializedMessage.getTarget());
        buffer.putInt(serializedMessage.getLength());
        if (serializedMessage.getPayload() != null) {
            buffer.put(serializedMessage.getPayload(), 0, serializedMessage.getLength());
        }
        out.write(buffer.array());
        return true;
    }

    /**
     * Reads message from input stream.
     * @param in Input stream
     * @return Message or empty optional if the message could not be read or deserialized
     */
    public static Optional<Message> readMessageFromInputStream(DataInputStream in) throws IOException {
        var typeValue = in.readInt();
        var target = in.readInt();
        var length = in.readInt();

        var payload = new byte[length];
        in.readFully(payload);

        var maybeType = MessageType.of(typeValue);
        if (maybeType.isEmpty()) {
            Logger.getGlobal().severe(() -> String.format("Unrecognized message type [%d]!", typeValue));
            return Optional.empty();
        }
        var type = maybeType.get();

        var serializedMessage = SerializedMessage.builder()
                .type(type)
                .target(target)
                .length(length)
                .payload(payload)
                .build();

        var deserializer = type.getDeserializer();
        try {
            return deserializer.deserializeMessage(serializedMessage);
        } catch (Exception e) {
            Logger.getGlobal().log(Level.SEVERE, "Could not deserialize message!", e);
        }
        return Optional.empty();
    }

    private MessageUtils() {}
}
