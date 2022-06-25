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
package com.warxim.vucsa.client.challenge.verticalaccesscontrol;

import com.warxim.vucsa.client.Bundle;
import com.warxim.vucsa.client.challenge.ChallengeController;
import com.warxim.vucsa.common.ChallengeConstant;
import com.warxim.vucsa.common.message.verticalaccesscontrol.UserRole;
import com.warxim.vucsa.common.message.verticalaccesscontrol.request.SecretRequest;
import com.warxim.vucsa.common.message.verticalaccesscontrol.request.UserInfoRequest;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;

import java.net.URL;
import java.util.ResourceBundle;

/**
 * Vertical access controller handles vertical access challenge.
 * <p>Simulates sending of user info from the server to the client about his session.</p>
 */
public class VerticalAccessControlController extends ChallengeController implements Initializable {
    private final VerticalAccessControlUserInfoHandler userInfoHandler = new VerticalAccessControlUserInfoHandler(this);
    private final VerticalAccessControlSecretHandler secretHandler = new VerticalAccessControlSecretHandler(this);

    @FXML
    private Label usernameLabel;
    @FXML
    private Label roleLabel;

    @FXML
    private AnchorPane guestPane;
    @FXML
    private AnchorPane userPane;
    @FXML
    private AnchorPane adminPane;
    @FXML
    private Label secretLabel;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        initHandler();
        initPanes();
    }

    /**
     * Sets user to the GUI.
     * @param username Name of the user
     * @param role Role of the user
     */
    public void setUser(String username, UserRole role) {
        Platform.runLater(() -> {
            usernameLabel.setText(username);
            roleLabel.setText(role.name());

            if (role.equals(UserRole.GUEST)) {
                guestPane.setVisible(true);
                userPane.setVisible(false);
                adminPane.setVisible(false);
                secretLabel.setText("-");
            } else if (role.equals(UserRole.USER)) {
                guestPane.setVisible(false);
                userPane.setVisible(true);
                adminPane.setVisible(false);
                secretLabel.setText("-");
            } else if (role.equals(UserRole.ADMIN)) {
                guestPane.setVisible(false);
                userPane.setVisible(false);
                adminPane.setVisible(true);
                secretLabel.setText("-");
            }
        });
    }

    /**
     * Sets user secret to the GUI.
     * @param secret Secret to be set
     */
    public void setSecret(String secret) {
        Platform.runLater(() -> secretLabel.setText(secret));
    }

    /**
     * Sends request for user info to the server.
     */
    @FXML
    private void onRefreshClick(ActionEvent event) {
        var message = UserInfoRequest.builder()
                .target(ChallengeConstant.VERTICAL_ACCESS_CONTROL_USER_INFO_TARGET)
                .build();
        sendMessage(message);
    }

    /**
     * Sends request for downloading secret to the server.
     */
    @FXML
    private void onDownloadSecretClick(ActionEvent event) {
        var message = SecretRequest.builder()
                .target(ChallengeConstant.VERTICAL_ACCESS_CONTROL_SECRET_TARGET)
                .build();
        sendMessage(message);
    }

    /**
     * Initializes vertical access control message handlers.
     */
    private void initHandler() {
        Bundle.getInstance().getClientManager().registerHandler(ChallengeConstant.VERTICAL_ACCESS_CONTROL_USER_INFO_TARGET, userInfoHandler);
        Bundle.getInstance().getClientManager().registerHandler(ChallengeConstant.VERTICAL_ACCESS_CONTROL_SECRET_TARGET, secretHandler);
    }

    /**
     * Hides all panes.
     */
    private void initPanes() {
        guestPane.setVisible(false);
        userPane.setVisible(false);
        adminPane.setVisible(false);
    }
}
