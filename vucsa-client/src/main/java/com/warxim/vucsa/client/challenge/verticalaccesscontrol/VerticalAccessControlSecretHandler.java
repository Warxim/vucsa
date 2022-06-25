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
package com.warxim.vucsa.client.challenge.verticalaccesscontrol;

import com.warxim.vucsa.common.connection.Connection;
import com.warxim.vucsa.common.message.Message;
import com.warxim.vucsa.common.message.MessageHandler;
import com.warxim.vucsa.common.message.verticalaccesscontrol.response.SecretResponse;
import lombok.RequiredArgsConstructor;

/**
 * Handler for secret response for vertical access control challenge.
 */
@RequiredArgsConstructor
public class VerticalAccessControlSecretHandler implements MessageHandler {
    private final VerticalAccessControlController controller;

    @Override
    public boolean supports(Message message) {
        return message instanceof SecretResponse;
    }

    @Override
    public boolean handleMessage(Connection connection, Message message) {
        var secretResponse = ((SecretResponse) message);
        controller.setSecret(secretResponse.getSecret());
        return true;
    }
}