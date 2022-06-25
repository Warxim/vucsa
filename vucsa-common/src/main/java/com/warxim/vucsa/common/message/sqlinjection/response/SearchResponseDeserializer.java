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

import com.google.gson.reflect.TypeToken;
import com.warxim.vucsa.common.message.SerializedMessage;
import com.warxim.vucsa.common.message.Message;
import com.warxim.vucsa.common.message.MessageDeserializer;
import com.warxim.vucsa.common.message.sqlinjection.FoodEntity;
import com.warxim.vucsa.common.util.GsonUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * Deserializer for {@link SearchResponse}.
 */
public class SearchResponseDeserializer implements MessageDeserializer {
    @Override
    public Optional<Message> deserializeMessage(SerializedMessage serializedMessage) {
        var gson = GsonUtils.getGson();
        var json = new String(serializedMessage.getPayload(), 0, serializedMessage.getLength());
        var entitiesType = new TypeToken<ArrayList<FoodEntity>>() {}.getType();
        List<FoodEntity> entities = gson.fromJson(json, entitiesType);
        return Optional.of(SearchResponse.builder()
                .target(serializedMessage.getTarget())
                .entities(entities)
                .build());
    }
}
