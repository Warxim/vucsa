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
import com.warxim.vucsa.common.message.MessageDeserializer;
import com.warxim.vucsa.common.message.SerializedMessage;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.util.Optional;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Deserializer for {@link TextMessage}.
 */
public class TextMessageDeserializer implements MessageDeserializer {
    @Override
    public Optional<Message> deserializeMessage(SerializedMessage serializedMessage) {
        try (var byteInputStream = new ByteArrayInputStream(serializedMessage.getPayload());
             var objectInputStream = new ObjectInputStream(byteInputStream)) {
            var messageContent = (MessageContent) objectInputStream.readObject();
            return Optional.of(TextMessage.builder()
                    .target(serializedMessage.getTarget())
                    .content(messageContent)
                    .build());
        } catch (IOException | ClassNotFoundException e) {
            Logger.getGlobal().log(Level.SEVERE, "Could not deserialize text message!", e);
            return Optional.empty();
        }
    }
}
