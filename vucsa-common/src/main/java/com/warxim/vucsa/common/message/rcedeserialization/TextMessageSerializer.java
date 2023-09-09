/*
 * Vulnerable Client-Server Application (VuCSA)
 *
 * Copyright (C) 2023 Michal Válka
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
package com.warxim.vucsa.common.message.rcedeserialization;

import com.warxim.vucsa.common.message.Message;
import com.warxim.vucsa.common.message.MessageSerializer;
import com.warxim.vucsa.common.message.SerializedMessage;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.util.Optional;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Serializer for {@link TextMessage}.
 */
public class TextMessageSerializer implements MessageSerializer {
    @Override
    public Optional<SerializedMessage> serializeMessage(Message message) {
        if (!(message instanceof TextMessage)) {
            return Optional.empty();
        }
        var textMessage = (TextMessage) message;

        try (var byteStream = new ByteArrayOutputStream();
             var objectOutputStream = new ObjectOutputStream(byteStream)) {
            objectOutputStream.writeObject(textMessage.getContent());
            var payload =  byteStream.toByteArray();
            return Optional.of(SerializedMessage.builder()
                    .type(message.getType())
                    .target(message.getTarget())
                    .length(payload.length)
                    .payload(payload)
                    .build());
        } catch (IOException e) {
            Logger.getGlobal().log(Level.SEVERE, "Could not serialize text message!", e);
            return Optional.empty();
        }
    }
}
