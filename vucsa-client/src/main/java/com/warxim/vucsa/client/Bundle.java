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
package com.warxim.vucsa.client;

import com.warxim.vucsa.client.core.ClientManager;
import lombok.Getter;

/**
 * Singleton for client assets.
 */
@Getter
public final class Bundle {
    /**
     * Singleton instance.
     */
    private static volatile Bundle instance;

    /**
     * Client manager
     */
    private final ClientManager clientManager;

    private Bundle() {
        clientManager = new ClientManager();
    }

    /**
     * Creates instance of bundle or returns existing instance if it exists.
     * @return Bundle instance
     */
    public static Bundle getInstance() {
        if (instance == null) {
            synchronized(Bundle.class) {
                if (instance == null) {
                    instance = new Bundle();
                }
            }
        }

        return instance;
    }

    /**
     * Destroys the bundle.
     */
    public void destroy() {
        // nothing to destroy
    }
}