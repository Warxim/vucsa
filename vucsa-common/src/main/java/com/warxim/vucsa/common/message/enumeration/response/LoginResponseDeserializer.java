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
package com.warxim.vucsa.common.message.enumeration.response;

import com.warxim.vucsa.common.message.SerializedMessage;
import com.warxim.vucsa.common.message.Message;
import com.warxim.vucsa.common.message.MessageDeserializer;
import com.warxim.vucsa.common.message.enumeration.LoginStatus;

import java.nio.ByteBuffer;
import java.util.Arrays;
import java.util.Optional;
import java.util.logging.Logger;

/**
 * Deserializer for {@link LoginResponse}.
 */
public class LoginResponseDeserializer implements MessageDeserializer {
    @Override
    public Optional<Message> deserializeMessage(SerializedMessage serializedMessage) {
        if (serializedMessage.getLength() < 4) {
            Logger.getGlobal().severe("Serialized message does not contain enough bytes.");
            return Optional.empty();
        }

        var value = ByteBuffer.wrap(serializedMessage.getPayload(), 0, 4).getInt();
        var maybeStatus = LoginStatus.of(value);
        if (maybeStatus.isEmpty()) {
            Logger.getGlobal().severe("Invalid login status detected in message.");
            return Optional.empty();
        }
        var status = maybeStatus.get();

        var secretBytes = Arrays.copyOfRange(serializedMessage.getPayload(), 4, serializedMessage.getLength());
        var secret = new String(secretBytes);

        return Optional.of(LoginResponse.builder()
                .target(serializedMessage.getTarget())
                .status(status)
                .userSecret(secret)
                .build());
    }
}
