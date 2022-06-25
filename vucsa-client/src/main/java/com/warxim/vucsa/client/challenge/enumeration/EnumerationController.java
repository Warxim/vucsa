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
package com.warxim.vucsa.client.challenge.enumeration;

import com.warxim.vucsa.client.Bundle;
import com.warxim.vucsa.client.challenge.ChallengeController;
import com.warxim.vucsa.common.ChallengeConstant;
import com.warxim.vucsa.common.message.enumeration.LoginStatus;
import com.warxim.vucsa.common.message.enumeration.request.LoginRequest;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;

import java.net.URL;
import java.util.ResourceBundle;

/**
 * Enumeration controller handles enumeration challenge.
 * <p>Simulates user login and displays result.</p>
 */
public class EnumerationController extends ChallengeController implements Initializable {
    private final EnumerationHandler handler = new EnumerationHandler(this);

    @FXML
    private TextField usernameInput;
    @FXML
    private PasswordField passwordInput;
    @FXML
    private Label resultLabel;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        initHandler();
    }

    /**
     * Sets login result to the GUI.
     * @param status Login status
     * @param userSecret Secret of logged user
     */
    public void setResult(LoginStatus status, String userSecret) {
        Platform.runLater(() -> {
            switch (status) {
                case SUCCESS:
                    resultLabel.setText("Successfully logged in! Your secret is: \"" + userSecret + "\"");
                    break;
                case WRONG_USERNAME:
                    resultLabel.setText("Wrong username!");
                    break;
                case WRONG_PASSWORD:
                    resultLabel.setText("Wrong password!");
                    break;
            }
        });
    }

    /**
     * Sends login request to the server.
     */
    @FXML
    private void onLoginClick(ActionEvent event) {
        var message = LoginRequest.builder()
                .target(ChallengeConstant.ENUMERATION_TARGET)
                .username(usernameInput.getText())
                .password(passwordInput.getText())
                .build();
        sendMessage(message);
    }

    /**
     * Initializes enumeration message handler.
     */
    private void initHandler() {
        Bundle.getInstance().getClientManager().registerHandler(ChallengeConstant.ENUMERATION_TARGET, handler);
    }
}
