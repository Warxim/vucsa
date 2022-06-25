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
package com.warxim.vucsa.common.message.verticalaccesscontrol;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.Arrays;
import java.util.Optional;

/**
 * Role of the user
 */
@Getter
@RequiredArgsConstructor
public enum UserRole {
    GUEST(1),
    USER(3),
    ADMIN(5);

    private final int value;

    /**
     * Converts int value to user role.
     * @return User role of empty optional if the value is not used by any role
     */
    public static Optional<UserRole> of(int value) {
        return Arrays.stream(values())
                .filter(role -> role.value == value)
                .findAny();
    }
}
