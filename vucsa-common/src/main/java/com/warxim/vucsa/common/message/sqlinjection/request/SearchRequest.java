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

import com.warxim.vucsa.common.message.Message;
import com.warxim.vucsa.common.message.MessageType;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Value;

/**
 * Request in SQL Injection challenge for searching data.
 */
@Value
@EqualsAndHashCode(callSuper = true)
public class SearchRequest extends Message {
    /**
     * Search query
     */
    String search;

    @Builder
    public SearchRequest(int target, String search) {
        super(target);
        this.search = search;
    }

    @Override
    public MessageType getType() {
        return MessageType.SQL_INJECTION_SEARCH_REQUEST;
    }
}
