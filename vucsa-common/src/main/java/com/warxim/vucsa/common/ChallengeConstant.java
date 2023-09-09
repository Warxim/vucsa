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
package com.warxim.vucsa.common;

/**
 * Constants for challenges.
 */
public final class ChallengeConstant {
    public static final int BUFFER_OVERREAD_TARGET = 1001;
    public static final int SQL_INJECTION_TARGET = 1002;
    public static final int ENUMERATION_TARGET = 1003;
    public static final int COMMAND_EXECUTION_TARGET = 1004;
    public static final int XML_TARGET = 1005;
    public static final int VERTICAL_ACCESS_CONTROL_USER_INFO_TARGET = 1006;
    public static final int VERTICAL_ACCESS_CONTROL_SECRET_TARGET = 1007;
    public static final int HORIZONTAL_ACCESS_CONTROL_DOCUMENT_CONTENT_TARGET = 1008;
    public static final int RCE_DESERIALIZATION_TARGET = 1009;

    public static final String CHALLENGES_DIRECTORY = "server/challenge/";

    private ChallengeConstant() {}
}
