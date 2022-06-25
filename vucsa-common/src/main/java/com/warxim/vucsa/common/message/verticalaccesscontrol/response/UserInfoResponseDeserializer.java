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
package com.warxim.vucsa.common.message.verticalaccesscontrol.response;

import com.warxim.vucsa.common.message.Message;
import com.warxim.vucsa.common.message.MessageDeserializer;
import com.warxim.vucsa.common.message.SerializedMessage;
import com.warxim.vucsa.common.message.verticalaccesscontrol.UserRole;

import java.nio.ByteBuffer;
import java.util.Arrays;
import java.util.Optional;
import java.util.logging.Logger;

/**
 * Deserializer for {@link UserInfoResponse}.
 */
public class UserInfoResponseDeserializer implements MessageDeserializer {
    @Override
    public Optional<Message> deserializeMessage(SerializedMessage serializedMessage) {
        if (serializedMessage.getLength() < 8) {
            Logger.getGlobal().severe("Serialized message does not contain enough bytes.");
            return Optional.empty();
        }

        var id = ByteBuffer.wrap(serializedMessage.getPayload(), 0, 4).getInt();
        var roleValue = ByteBuffer.wrap(serializedMessage.getPayload(), 4, 8).getInt();
        var maybeRole = UserRole.of(roleValue);
        if (maybeRole.isEmpty()) {
            Logger.getGlobal().severe("Invalid user role detected in message.");
            return Optional.empty();
        }
        var role = maybeRole.get();

        var usernameBytes = Arrays.copyOfRange(serializedMessage.getPayload(), 8, serializedMessage.getLength());
        var username = new String(usernameBytes);

        return Optional.of(UserInfoResponse.builder()
                .target(serializedMessage.getTarget())
                .id(id)
                .role(role)
                .username(username)
                .build());
    }
}
