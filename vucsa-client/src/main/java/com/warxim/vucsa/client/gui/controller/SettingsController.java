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
package com.warxim.vucsa.client.gui.controller;

import com.warxim.vucsa.client.Bundle;
import com.warxim.vucsa.client.core.ClientConfig;
import com.warxim.vucsa.client.gui.dialog.Dialogs;
import com.warxim.vucsa.common.Constant;
import com.warxim.vucsa.common.connection.Connection;
import com.warxim.vucsa.common.connection.ConnectionState;
import com.warxim.vucsa.common.connection.listener.ConnectionListener;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;

import java.net.URL;
import java.util.Optional;
import java.util.ResourceBundle;

/**
 * Controller for settings tab.
 */
public class SettingsController implements Initializable, ConnectionListener {
    private static final String CONNECT_TEXT = "CONNECT";
    private static final String DISCONNECT_TEXT = "DISCONNECT";

    @FXML
    private TextField serverHostInput;
    @FXML
    private TextField serverPortInput;
    @FXML
    private Label statusLabel;
    @FXML
    private Label serverHostLabel;
    @FXML
    private Label serverPortLabel;
    @FXML
    private Button startStopButton;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        serverHostInput.setText(Constant.DEFAULT_SERVER_HOST);
        serverPortInput.setText(String.valueOf(Constant.DEFAULT_SERVER_PORT));
        startStopButton.setText(CONNECT_TEXT);
        onConnectionStop(null);
        var clientManager = Bundle.getInstance().getClientManager();
        clientManager.registerConnectionListener(this);
    }

    @Override
    public void onConnectionStart(Connection connection) {
        Platform.runLater(() -> {
            statusLabel.getStyleClass().add("status-connected");
            statusLabel.getStyleClass().removeAll("status-disconnected");
            statusLabel.setText("CONNECTED");
            startStopButton.setText(DISCONNECT_TEXT);
        });
    }

    @Override
    public void onConnectionStop(Connection connection) {
        Platform.runLater(() -> {
            statusLabel.getStyleClass().removeAll("status-connected");
            statusLabel.getStyleClass().add("status-disconnected");
            statusLabel.setText("DISCONNECTED");
            startStopButton.setText(CONNECT_TEXT);
        });
    }

    /**
     * Starts or stops client using client manager.
     */
    @FXML
    private void onStartStopClick(ActionEvent event) {
        var clientManager = Bundle.getInstance().getClientManager();
        var clientState = clientManager.getState();
        if (clientState == ConnectionState.STARTED) {
            clientManager.stop();
            startStopButton.setText(CONNECT_TEXT);
        } else if (clientState == ConnectionState.STOPPED) {
            var settings = validateAndGetSettings();
            if (settings.isEmpty()) {
                return;
            }
            var config = ClientConfig.builder()
                    .serverHost(serverHostInput.getText())
                    .serverPort(Integer.parseInt(serverPortInput.getText()))
                    .build();
            clientManager.start(config);
            serverHostLabel.setText(serverHostInput.getText());
            serverPortLabel.setText(serverPortInput.getText());
            startStopButton.setText(DISCONNECT_TEXT);
        }
    }

    /**
     * Checks if the settings is valid and returns the settings if it is valid.
     */
    private Optional<ClientConfig> validateAndGetSettings() {
        var serverHost = serverHostInput.getText();
        var serverPort = serverPortInput.getText();
        if (serverHost.isBlank()) {
            Dialogs.createErrorDialog(
                    "Server host required",
                    "You have to specify server host!");
            return Optional.empty();
        }

        if (serverPort.isBlank() || !isInteger(serverPort)) {
            Dialogs.createErrorDialog(
                    "Server port required",
                    "You have to specify server port!");
            return Optional.empty();
        }

        var settings = ClientConfig.builder()
                .serverHost(serverHost)
                .serverPort(Integer.parseInt(serverPort))
                .build();
        return Optional.of(settings);
    }

    /**
     * Returns true if the value is a valid integer.
     */
    private static boolean isInteger(String value) {
        try {
            Integer.parseInt(value);
            return true;
        } catch(NumberFormatException e){
            return false;
        }
    }
}
