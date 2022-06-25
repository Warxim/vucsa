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
package com.warxim.vucsa.server.challenge.verticalaccesscontrol;

import com.warxim.vucsa.common.ChallengeConstant;
import com.warxim.vucsa.common.connection.Connection;
import com.warxim.vucsa.common.message.Message;
import com.warxim.vucsa.common.message.MessageHandler;
import com.warxim.vucsa.common.message.verticalaccesscontrol.request.SecretRequest;
import com.warxim.vucsa.common.message.verticalaccesscontrol.response.SecretResponse;

/**
 * Handler for secret for vertical access control challenge
 * <p>Sends secret response to user that requested it.</p>
 */
public class VerticalAccessControlSecretHandler implements MessageHandler {
    @Override
    public boolean supports(Message message) {
        return message instanceof SecretRequest;
    }

    @Override
    public boolean handleMessage(Connection connection, Message message) {
        connection.sendMessage(SecretResponse.builder()
                .target(ChallengeConstant.VERTICAL_ACCESS_CONTROL_SECRET_TARGET)
                .secret("Congratulations! Now you know that the secret message is: 42!")
                .build());
        return true;
    }
}
