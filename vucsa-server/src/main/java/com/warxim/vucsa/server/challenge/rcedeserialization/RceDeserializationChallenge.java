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
package com.warxim.vucsa.server.challenge.rcedeserialization;

import com.warxim.vucsa.common.ChallengeConstant;
import com.warxim.vucsa.server.challenge.Challenge;
import com.warxim.vucsa.server.core.ServerManager;

/**
 * RCE Deserialization challenge
 */
public class RceDeserializationChallenge extends Challenge {
    @Override
    public void load(ServerManager serverManager) {
        serverManager.registerHandler(ChallengeConstant.RCE_DESERIALIZATION_TARGET, new RceDeserializationHandler());
    }

    @Override
    public void unload(ServerManager serverManager) {
        serverManager.unregisterHandler(ChallengeConstant.RCE_DESERIALIZATION_TARGET);
    }
}
