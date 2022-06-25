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
package com.warxim.vucsa.client.challenge.commandexecution;

import com.warxim.vucsa.client.Bundle;
import com.warxim.vucsa.client.challenge.ChallengeController;
import com.warxim.vucsa.common.ChallengeConstant;
import com.warxim.vucsa.common.message.commandexecution.request.PingRequest;
import javafx.application.Platform;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;

import java.net.URL;
import java.util.ResourceBundle;

/**
 * Command execution controller handles command execution challenge.
 * <p>Requests server to do PING command and shows output.</p>
 */
public class CommandExecutionController extends ChallengeController implements Initializable {
    private final CommandExecutionHandler handler = new CommandExecutionHandler(this);

    @FXML
    private TextField hostInput;
    @FXML
    private TextArea resultOutput;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        initHandler();
        hostInput.textProperty().addListener(this::onHostInputChange);
    }
    /**
     * Sets output to the text area.
     * @param output Output to be set
     */
    public void setOutput(String output) {
        Platform.runLater(() -> resultOutput.setText(output));
    }

    /**
     * Sends ping request to server.
     */
    @FXML
    private void onPingClick(ActionEvent event) {
        var message = PingRequest.builder()
                .target(ChallengeConstant.COMMAND_EXECUTION_TARGET)
                .host(hostInput.getText())
                .build();
        sendMessage(message);
    }

    /**
     * Initializes command execution message handler.
     */
    private void initHandler() {
        Bundle.getInstance().getClientManager().registerHandler(ChallengeConstant.COMMAND_EXECUTION_TARGET, handler);
    }

    /**
     * Removes invalid characters from host input on change.
     */
    private void onHostInputChange(
            ObservableValue<? extends String> observable,
            String oldValue,
            String newValue) {
        if (!newValue.matches("0-9a-zA-Z\\.-")) {
            hostInput.setText(newValue.replaceAll("[^0-9a-zA-Z.-]", ""));
        }
    }
}
