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
package com.warxim.vucsa.common.message.horizontalaccesscontrol.request;

import com.warxim.vucsa.common.message.Message;
import com.warxim.vucsa.common.message.MessageDeserializer;
import com.warxim.vucsa.common.message.SerializedMessage;

import java.nio.ByteBuffer;
import java.util.Optional;
import java.util.logging.Logger;

/**
 * Deserializer for {@link DocumentContentRequest}.
 */
public class DocumentContentRequestDeserializer implements MessageDeserializer {
    @Override
    public Optional<Message> deserializeMessage(SerializedMessage serializedMessage) {
        if (serializedMessage.getLength() != 4) {
            Logger.getGlobal().severe("Serialized message does not contain expected number of bytes.");
            return Optional.empty();
        }
        var documentId = ByteBuffer.wrap(serializedMessage.getPayload(), 0, 4).getInt();

        return Optional.of(DocumentContentRequest.builder()
                .target(serializedMessage.getTarget())
                .documentId(documentId)
                .build());
    }
}
