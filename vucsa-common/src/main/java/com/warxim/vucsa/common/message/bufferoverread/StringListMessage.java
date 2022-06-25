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

import com.warxim.vucsa.common.message.Message;
import com.warxim.vucsa.common.message.MessageType;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Value;

import java.util.Collections;
import java.util.List;

/**
 * Message for buffer over-read challenge containing list of items.
 */
@Value
@EqualsAndHashCode(callSuper = true)
public class StringListMessage extends Message {
    /**
     * List of strings for buffer overread challenge.
     */
    List<String> items;

    @Builder
    public StringListMessage(int target, List<String> items) {
        super(target);
        this.items = Collections.unmodifiableList(items);
    }

    @Override
    public MessageType getType() {
        return MessageType.BUFFER_OVERREAD_STRING_LIST_MESSAGE;
    }
}
