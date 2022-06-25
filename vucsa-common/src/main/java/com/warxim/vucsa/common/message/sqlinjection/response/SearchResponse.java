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
package com.warxim.vucsa.common.message.sqlinjection.response;

import com.warxim.vucsa.common.message.Message;
import com.warxim.vucsa.common.message.MessageType;
import com.warxim.vucsa.common.message.sqlinjection.FoodEntity;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Value;

import java.util.List;

/**
 * Response in SQL Injection challenge with list of food entities.
 */
@Value
@EqualsAndHashCode(callSuper = true)
public class SearchResponse extends Message {
    List<FoodEntity> entities;

    @Builder
    public SearchResponse(int target, List<FoodEntity> entities) {
        super(target);
        this.entities = entities;
    }

    @Override
    public MessageType getType() {
        return MessageType.SQL_INJECTION_SEARCH_RESPONSE;
    }
}
