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

import com.warxim.vucsa.common.message.SerializedMessage;
import com.warxim.vucsa.common.message.Message;
import com.warxim.vucsa.common.message.MessageDeserializer;

import java.io.ByteArrayInputStream;
import java.io.DataInputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.LinkedList;
import java.util.Optional;
import java.util.concurrent.ThreadLocalRandom;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Pseudo-vulnerable message deserializer for {@link StringListMessage}.
 */
public class StringListMessageDeserializer implements MessageDeserializer {
    private static final int RANDOM_DATA_LENGTH = 64;
    private static final String SECRET_DATA = "Congratulations, you have successfully exploited buffer over-read!";

    @Override
    public Optional<Message> deserializeMessage(SerializedMessage serializedMessage) {
        var payload = serializedMessage.getPayload();
        var length = serializedMessage.getLength();
        var items = new LinkedList<String>();
        var in = new DataInputStream(new ByteArrayInputStream(payload));

        try {
            var read = 0;
            while (length > read) {
                var itemLength = in.readInt();
                read += 4;

                var buffer = new byte[itemLength];
                if (itemLength > (length - read)) {
                    // Simulate read of fake memory
                    var fakeMemory = generateFakeMemory();

                    // Read as many bytes as possible
                    int actualLength = in.read(buffer);

                    // Calculate remaining length (missing bytes)
                    int remainingLength = itemLength - actualLength;

                    // Fill in the buffer using fake memory
                    System.arraycopy(fakeMemory, 0, buffer, actualLength, Math.min(remainingLength, fakeMemory.length));
                } else {
                    in.readFully(buffer);
                }
                read += itemLength;

                items.add(new String(buffer));
            }
        } catch (IOException e) {
            Logger.getGlobal().log(Level.SEVERE, "IO exception occurred during read of payload.");
        }

        return Optional.of(StringListMessage.builder()
                .target(serializedMessage.getTarget())
                .items(items)
                .build());
    }

    /**
     * Generates fake memory content bytes.
     */
    private byte[] generateFakeMemory() {
        var buffer = ByteBuffer.allocate(RANDOM_DATA_LENGTH + SECRET_DATA.getBytes().length + RANDOM_DATA_LENGTH);
        var bytes = new byte[RANDOM_DATA_LENGTH];
        ThreadLocalRandom.current().nextBytes(bytes);
        buffer.put(bytes);

        buffer.put(SECRET_DATA.getBytes());

        ThreadLocalRandom.current().nextBytes(bytes);
        buffer.put(bytes);

        return buffer.array();
    }
}
