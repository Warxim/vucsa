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
import com.warxim.vucsa.common.message.MessageSerializer;
import com.warxim.vucsa.common.message.SerializedMessage;

import java.nio.ByteBuffer;
import java.util.Optional;

/**
 * Serializer for {@link UserInfoResponse}.
 */
public class UserInfoResponseSerializer implements MessageSerializer {
    @Override
    public Optional<SerializedMessage> serializeMessage(Message message) {
        if (!(message instanceof UserInfoResponse)) {
            return Optional.empty();
        }
        var userInfoResponse = (UserInfoResponse) message;
        var id = userInfoResponse.getId();
        var role = userInfoResponse.getRole().getValue();

        var usernameBytes = userInfoResponse.getUsername().getBytes();
        var bytes = ByteBuffer.allocate(4 + 4 + usernameBytes.length)
                .putInt(id)
                .putInt(role)
                .put(usernameBytes)
                .array();

        return Optional.of(SerializedMessage.builder()
                .type(message.getType())
                .target(message.getTarget())
                .length(bytes.length)
                .payload(bytes)
                .build());
    }
}
