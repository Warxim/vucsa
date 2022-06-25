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

import com.warxim.vucsa.common.ChallengeConstant;
import com.warxim.vucsa.server.core.ServerManager;

import java.io.File;
import java.util.logging.Logger;

/**
 * Base class for challenges.
 */
public abstract class Challenge {
    /**
     * Loads challenge.
     * <p>Registers challenge handlers and so on.</p>
     * @param serverManager Server manager
     */
    public abstract void load(ServerManager serverManager);

    /**
     * Unloads challenge.
     * <p>Unregisters challenge handlers and so on.</p>
     * @param serverManager Server manager
     */
    public abstract void unload(ServerManager serverManager);

    /**
     * Initializes challenge directory.
     */
    protected void initChallengeDirectory() {
        var directory = new File(getChallengeDirectory());
        if (directory.exists()) {
            return;
        }
        if (!directory.mkdir()) {
            Logger.getGlobal().severe("Could not create challenge directory, server might not work correctly.");
        }
    }

    /**
     * Obtains challenge directory name.
     * <p>Example: challenges/SuperChallenge/</p>
     */
    protected String getChallengeDirectory() {
        return ChallengeConstant.CHALLENGES_DIRECTORY
                + removeSuffix(getClass().getSimpleName().toLowerCase(), "challenge")
                + "/";
    }

    private static String removeSuffix(String string, String suffix) {
        if (string != null && suffix != null && string.endsWith(suffix)) {
            return string.substring(0, string.length() - suffix.length());
        }
        return string;
    }
}
