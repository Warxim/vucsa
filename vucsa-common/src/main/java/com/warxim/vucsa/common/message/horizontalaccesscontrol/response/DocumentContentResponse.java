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
package com.warxim.vucsa.common.message.horizontalaccesscontrol.response;

import com.warxim.vucsa.common.message.Message;
import com.warxim.vucsa.common.message.MessageType;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Value;

/**
 * Document content response for Horizontal Access Control challenge.
 */
@Value
@EqualsAndHashCode(callSuper = true)
public class DocumentContentResponse extends Message {
    String content;

    @Builder
    public DocumentContentResponse(int target, String content) {
        super(target);
        this.content = content;
    }

    @Override
    public MessageType getType() {
        return MessageType.HORIZONTAL_ACCESS_CONTROL_DOCUMENT_CONTENT_RESPONSE;
    }
}
