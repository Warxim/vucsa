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
import com.warxim.vucsa.common.message.MessageType;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Value;

/**
 * Text message that will be transmitted over the network in RCE Deserialization challenge
 */
@Value
@EqualsAndHashCode(callSuper = true)
public class TextMessage extends Message {
    MessageContent content;

    @Builder
    public TextMessage(int target, MessageContent content) {
        super(target);
        this.content = content;
    }

    @Override
    public MessageType getType() {
        return MessageType.RCE_DESERIALIZATION_TEXT_MESSAGE;
    }
}
