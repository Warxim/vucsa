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
package com.warxim.vucsa.server.challenge.enumeration;

import com.warxim.vucsa.common.message.enumeration.LoginStatus;
import lombok.Builder;
import lombok.Getter;
import lombok.Value;

import java.security.MessageDigest;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * In-memory authentication service.
 */
public class AuthService {
    /**
     * Users mapped by username.
     */
    private Map<String, User> users;

    public AuthService() {
        var tempUsers = List.of(
                User.builder().username("guest").password("e3b0c44298fc1c149afbf4c8996fb92427ae41e4649b934ca495991b7852b855").secret("No secret.").build(),
                User.builder().username("test").password("65e84be33532fb784c48129675f9eff3a682b27168c0ea744b2cf58ee02337c5").secret("I like blue color.").build(),
                User.builder().username("admin").password("5e884898da28047151d0e56f8dc6292773603d0d6aabbdd62a11ef721d1542d8").secret("I use weak passwords.").build(),
                User.builder().username("user").password("e4ad93ca07acb8d908a3aa41e920ea4f4ef4f26e7f86cf8291c5db289780a5ae").secret("My favorite number is 42.").build(),
                User.builder().username("root").password("bcb15f821479b4d5772bd0ca866c00ad5f926e3580720659cc80d39c9d09802a").secret("I love PETEP tool.").build(),
                User.builder().username("super-alien-farmer").password("64681e853e24c842cf75a79239bf3f740064139be7592f6e3fab305f171d65cb").secret("The creatures outside looked from pig to man, and from man to pig, and from pig to man again; but already it was impossible to say which was which.").build()
        );
        users = tempUsers.stream()
                .collect(Collectors.toMap(
                        User::getUsername,
                        Function.identity())
                );
    }

    /**
     * Simulates user login.
     * @param username Username
     * @param password User password
     * @return Result of authentication
     */
    public AuthResult login(String username, String password) {
        var user = users.get(username);
        if (user == null) {
            return AuthResult.builder().status(LoginStatus.WRONG_USERNAME).build();
        }

        var passwordHash = sha256(password);
        if (!user.getPassword().equals(passwordHash)) {
            return AuthResult.builder().status(LoginStatus.WRONG_PASSWORD).build();
        }

        return AuthResult.builder().user(user).status(LoginStatus.SUCCESS).build();
    }

    /**
     * Hashes password using sha256
     */
    private static String sha256(final String password) {
        try {
            var hash = MessageDigest.getInstance("SHA-256")
                    .digest(password.getBytes());
            var hexString = new StringBuilder();
            for (var hashByte : hash) {
                var hex = Integer.toHexString(0xff & hashByte);
                if (hex.length() == 1) {
                    hexString.append('0');
                }
                hexString.append(hex);
            }
            return hexString.toString();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Authorization result
     */
    @Getter
    @Value
    @Builder
    public static class AuthResult {
        LoginStatus status;
        User user;
    }
}
