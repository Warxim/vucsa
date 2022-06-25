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
package com.warxim.vucsa.client.challenge;

import com.warxim.vucsa.client.Bundle;
import com.warxim.vucsa.common.message.Message;

/**
 * Base class of challenge controllers.
 */
public abstract class ChallengeController {
    /**
     * Sends message using client manager.
     * @param message Message to be sent
     * @return {@code true} if the message has been successfully sent
     */
    protected boolean sendMessage(Message message) {
        var clientManager = Bundle.getInstance().getClientManager();
        return clientManager.sendMessage(message);
    }
}
