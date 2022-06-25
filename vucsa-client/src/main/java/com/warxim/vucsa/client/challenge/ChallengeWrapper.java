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

import com.warxim.vucsa.common.connection.listener.ConnectionListener;
import javafx.scene.Node;
import lombok.Builder;
import lombok.Value;

/**
 * Wrapper for keeping JavaFX node and controller of a challenge.
 */
@Value
@Builder
public class ChallengeWrapper implements ConnectionListener {
    /**
     * Challenge node, which is displayed in the tab.
     */
    Node node;

    /**
     * Controller for controlling the challenge GUI.
     */
    ChallengeController controller;
}
