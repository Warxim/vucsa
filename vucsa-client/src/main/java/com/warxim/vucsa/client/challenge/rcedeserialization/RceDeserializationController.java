/*
 * Vulnerable Client-Server Application (VuCSA)
 *
 * Copyright (C) 2023 Michal Válka
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
package com.warxim.vucsa.client.challenge.rcedeserialization;

import com.warxim.vucsa.client.Bundle;
import com.warxim.vucsa.client.challenge.ChallengeController;
import com.warxim.vucsa.common.ChallengeConstant;
import com.warxim.vucsa.common.message.rcedeserialization.MessageContent;
import com.warxim.vucsa.common.message.rcedeserialization.TextMessage;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TextArea;

import java.net.URL;
import java.util.ResourceBundle;

/**
 * RCE Deserialization controller handles RCE Deserialization challenge, which acts as echo server using object serialization.
 */
public class RceDeserializationController extends ChallengeController implements Initializable {
    private final RceDeserializationHandler handler = new RceDeserializationHandler(this);

    @FXML
    private TextArea dataInput;
    @FXML
    private TextArea dataOutput;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        initHandler();
    }

    /**
     * Sets data to the output component
     * @param data Output to be set
     */
    public void setOutput(String data) {
        Platform.runLater(() -> dataOutput.setText(data));
    }

    /**
     * Sends items to the server.
     */
    @FXML
    private void onSendClick(ActionEvent event) {
        var data = dataInput.getText();
        var messageContent = new MessageContent(data);

        var message = TextMessage.builder()
                .target(ChallengeConstant.RCE_DESERIALIZATION_TARGET)
                .content(messageContent)
                .build();
        sendMessage(message);
    }

    /**
     * Initializes RCE deserialization message handler.
     */
    private void initHandler() {
        Bundle.getInstance().getClientManager().registerHandler(ChallengeConstant.RCE_DESERIALIZATION_TARGET, handler);
    }
}
