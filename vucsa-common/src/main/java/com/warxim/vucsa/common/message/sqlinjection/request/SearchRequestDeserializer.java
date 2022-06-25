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
package com.warxim.vucsa.common.message.sqlinjection.request;

import com.warxim.vucsa.common.message.SerializedMessage;
import com.warxim.vucsa.common.message.Message;
import com.warxim.vucsa.common.message.MessageDeserializer;

import java.util.Optional;

/**
 * Deserializer for {@link SearchRequest}.
 */
public class SearchRequestDeserializer implements MessageDeserializer {
    @Override
    public Optional<Message> deserializeMessage(SerializedMessage serializedMessage) {
        return Optional.of(SearchRequest.builder()
                .target(serializedMessage.getTarget())
                .search(new String(serializedMessage.getPayload(), 0, serializedMessage.getLength()))
                .build());
    }
}
