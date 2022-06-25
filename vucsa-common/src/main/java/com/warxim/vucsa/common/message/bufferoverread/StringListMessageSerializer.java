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
package com.warxim.vucsa.common.message.bufferoverread;

import com.warxim.vucsa.common.message.MessageSerializer;
import com.warxim.vucsa.common.message.SerializedMessage;
import com.warxim.vucsa.common.message.Message;

import java.nio.ByteBuffer;
import java.util.Optional;

/**
 * Serializer for {@link StringListMessage}.
 * <p>
 *     Uses format: [length_1][item_1][length_2][item_2]
 * </p>
 */
public class StringListMessageSerializer implements MessageSerializer {
    @Override
    public Optional<SerializedMessage> serializeMessage(Message message) {
        if (!(message instanceof StringListMessage)) {
            return Optional.empty();
        }

        var stringListMessage = (StringListMessage) message;
        var items = stringListMessage.getItems();

        // Calculate size of the buffer
        var size = 0;
        for (var item : items) {
            var bytes = item.getBytes();
            size += bytes.length;
            size += 4;
        }

        // Generate the buffer
        var buffer = ByteBuffer.allocate(size);
        for (var item : items) {
            var bytes = item.getBytes();
            buffer.putInt(bytes.length);
            buffer.put(bytes);
        }

        return Optional.of(SerializedMessage.builder()
                .type(message.getType())
                .target(message.getTarget())
                .length(size)
                .payload(buffer.array())
                .build());
    }
}
