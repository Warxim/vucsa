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
package com.warxim.vucsa.server.challenge.sqlinjection;

import com.warxim.vucsa.common.ChallengeConstant;
import com.warxim.vucsa.common.connection.Connection;
import com.warxim.vucsa.common.message.Message;
import com.warxim.vucsa.common.message.MessageHandler;
import com.warxim.vucsa.common.message.sqlinjection.request.SearchRequest;
import com.warxim.vucsa.common.message.sqlinjection.FoodEntity;
import com.warxim.vucsa.common.message.sqlinjection.response.SearchResponse;
import lombok.RequiredArgsConstructor;

import java.sql.SQLException;
import java.util.Collections;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Handler for SQL injection challenge
 * <p>Uses SQL database to query for food menu in vulnerable way, so that user can cause SQL injection.</p>
 */
@RequiredArgsConstructor
public class SqlInjectionHandler implements MessageHandler {
    private final SqlDatabase database;

    @Override
    public boolean supports(Message message) {
        return message instanceof SearchRequest;
    }

    @Override
    public boolean handleMessage(Connection connection, Message message) {
        var sqlInjectionRequest = (SearchRequest) message;
        var search = sqlInjectionRequest.getSearch();
        // Query for food entities using vulnerable approach
        var maybeFoodEntities = database.selectList(
                "SELECT id, name, description, price FROM food WHERE name LIKE '%' || '" + search + "' || '%'",
                resultSet -> {
                    try {
                        return FoodEntity.builder()
                                .id(resultSet.getInt(1))
                                .name(resultSet.getString(2))
                                .description(resultSet.getString(3))
                                .price(resultSet.getDouble(4))
                                .build();
                    } catch (SQLException e) {
                        Logger.getGlobal().log(Level.SEVERE, "Could not search food, because SQL exception occurred!", e);
                    }
                    return null;
                }
        );
        var entities = maybeFoodEntities.orElseGet(Collections::emptyList);
        var response = SearchResponse.builder()
                .target(ChallengeConstant.SQL_INJECTION_TARGET)
                .entities(entities)
                .build();
        connection.sendMessage(response);
        return true;
    }
}