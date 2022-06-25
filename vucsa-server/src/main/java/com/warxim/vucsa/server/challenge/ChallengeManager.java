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
package com.warxim.vucsa.server.challenge;

import com.warxim.vucsa.server.challenge.bufferoverread.BufferOverreadChallenge;
import com.warxim.vucsa.server.challenge.commandexecution.CommandExecutionChallenge;
import com.warxim.vucsa.server.challenge.enumeration.EnumerationChallenge;
import com.warxim.vucsa.server.challenge.horizontalaccesscontrol.HorizontalAccessControlChallenge;
import com.warxim.vucsa.server.challenge.sqlinjection.SqlInjectionChallenge;
import com.warxim.vucsa.server.challenge.verticalaccesscontrol.VerticalAccessControlChallenge;
import com.warxim.vucsa.server.challenge.xml.XmlChallenge;
import com.warxim.vucsa.server.core.ServerManager;
import com.warxim.vucsa.common.ChallengeConstant;

import java.io.File;
import java.util.List;
import java.util.logging.Logger;

/**
 * Simple static helper class for registering all challenge handlers.
 */
public class ChallengeManager {
    private final List<Challenge> challenges;

    public ChallengeManager() {
        initDirectory();
        challenges = List.of(
                new BufferOverreadChallenge(),
                new SqlInjectionChallenge(),
                new EnumerationChallenge(),
                new CommandExecutionChallenge(),
                new XmlChallenge(),
                new HorizontalAccessControlChallenge(),
                new VerticalAccessControlChallenge()
        );
    }

    /**
     * Loads challenges using given server manager.
     * @param serverManager Server manager
     */
    public void load(ServerManager serverManager) {
        challenges.forEach(challenge -> challenge.load(serverManager));
    }

    /**
     * Unloads challenges using given server manager.
     * @param serverManager Server manager
     */
    public void unload(ServerManager serverManager) {
        challenges.forEach(challenge -> challenge.unload(serverManager));
    }

    /**
     * Initializes directory for challenges.
     */
    private void initDirectory() {
        var directory = new File(ChallengeConstant.CHALLENGES_DIRECTORY);
        if (directory.exists()) {
            return;
        }
        if (!directory.mkdir()) {
            Logger.getGlobal().severe("Could not create challenge directory, server might not work correctly.");
        }
    }
}
