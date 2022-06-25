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
package com.warxim.vucsa.client.challenge.horizontalaccesscontrol;

import com.warxim.vucsa.client.Bundle;
import com.warxim.vucsa.client.challenge.ChallengeController;
import com.warxim.vucsa.common.ChallengeConstant;
import com.warxim.vucsa.common.message.horizontalaccesscontrol.request.DocumentContentRequest;
import javafx.application.Platform;
import javafx.beans.value.ObservableValue;
import javafx.fxml.Initializable;
import javafx.fxml.FXML;
import javafx.scene.control.ListView;
import javafx.scene.control.TextArea;

import java.net.URL;
import java.util.ResourceBundle;

/**
 * Horizontal access controller handles horizontal access challenge.
 * <p>Simulates document management of logged user and allows him to download document content of his documents.</p>
 */
public class HorizontalAccessControlController extends ChallengeController implements Initializable {
    private final HorizontalAccessControlHandler handler = new HorizontalAccessControlHandler(this);

    @FXML
    private ListView<DocumentItem> documentInput;
    @FXML
    private TextArea contentOutput;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        initHandler();
        initDocuments();
    }

    /**
     * Sets document content to GUI.
     * @param content Content of the document
     */
    public void setDocument(String content) {
        Platform.runLater(() -> contentOutput.setText(content));
    }

    /**
     * Initializes horizontal access control message handler.
     */
    private void initHandler() {
        Bundle.getInstance().getClientManager().registerHandler(ChallengeConstant.HORIZONTAL_ACCESS_CONTROL_DOCUMENT_CONTENT_TARGET, handler);
    }

    /**
     * Initializes document overview.
     */
    private void initDocuments() {
        documentInput.getItems().add(DocumentItem.builder()
                .id(8435)
                .name("My first secret document")
                .build());
        documentInput.getItems().add(DocumentItem.builder()
                .id(12002)
                .name("Favorite books")
                .build());
        documentInput.getSelectionModel().selectedItemProperty().addListener(this::onDocumentSelectionChange);
    }

    /**
     * Sends request for document content to the server, when document gets selected.
     */
    private void onDocumentSelectionChange(
            ObservableValue<? extends DocumentItem> observable,
            DocumentItem oldValue,
            DocumentItem newValue
    ) {
        if (newValue == null) {
            return;
        }

        sendMessage(DocumentContentRequest.builder()
                .target(ChallengeConstant.HORIZONTAL_ACCESS_CONTROL_DOCUMENT_CONTENT_TARGET)
                .documentId(newValue.getId())
                .build());
    }
}
