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
package com.warxim.vucsa.common.message.enumeration;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.Arrays;
import java.util.Optional;

/**
 * Status describing login result
 */
@Getter
@RequiredArgsConstructor
public enum LoginStatus {
    SUCCESS(1),
    WRONG_USERNAME(2),
    WRONG_PASSWORD(3);

    private final int value;

    /**
     * Converts int value to login status.
     * @return Login status or empty optional if the value is not representing any status
     */
    public static Optional<LoginStatus> of(int value) {
        return Arrays.stream(values())
                .filter(loginStatus -> loginStatus.value == value)
                .findAny();
    }
}