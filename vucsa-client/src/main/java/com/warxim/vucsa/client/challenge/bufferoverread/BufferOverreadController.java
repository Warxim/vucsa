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
package com.warxim.vucsa.client.challenge.bufferoverread;

import com.warxim.vucsa.client.Bundle;
import com.warxim.vucsa.client.challenge.ChallengeController;
import com.warxim.vucsa.client.gui.dialog.Dialogs;
import com.warxim.vucsa.common.ChallengeConstant;
import com.warxim.vucsa.common.message.bufferoverread.StringListMessage;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.ListView;
import javafx.scene.control.MenuItem;

import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

/**
 * Buffer over-read controller handles buffer over-read challenge, which uses simple echo with transformation of input
 * to simulate buffer over-read vulnerability.
 */
public class BufferOverreadController extends ChallengeController implements Initializable {
    private final BufferOverreadHandler handler = new BufferOverreadHandler(this);

    @FXML
    private ListView<String> requestInput;
    @FXML
    private ListView<String> responseInput;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        initHandler();
        initDefaultItems();
        initContextMenu();
    }

    /**
     * Sets items to the input component.
     * @param items Items to be set
     */
    public void setItems(List<String> items) {
        Platform.runLater(() -> responseInput.getItems().setAll(items));
    }

    /**
     * Sends items to server.
     */
    @FXML
    private void onSendClick(ActionEvent event) {
        var items = requestInput.getItems();
        var message = StringListMessage.builder()
                .target(ChallengeConstant.BUFFER_OVERREAD_TARGET)
                .items(items)
                .build();
        sendMessage(message);
    }

    /**
     * Adds new item.
     */
    @FXML
    private void onAddItemClick(ActionEvent event) {
        var maybeValue = Dialogs.createTextInputDialog("Add Item", "Value");
        if (maybeValue.isEmpty()) {
            return;
        }
        requestInput.getItems().add(maybeValue.get());
    }

    /**
     * Removes existing item.
     */
    @FXML
    private void onRemoveItemClick(ActionEvent event) {
        var selected = requestInput.getSelectionModel().getSelectedIndex();
        if (selected < 0) {
            return;
        }

        requestInput.getItems().remove(selected);
    }

    /**
     * Initializes buffer over-read message handler.
     */
    private void initHandler() {
        Bundle.getInstance().getClientManager().registerHandler(ChallengeConstant.BUFFER_OVERREAD_TARGET, handler);
    }

    /**
     * Initializes default items in the challenge GUI.
     */
    private void initDefaultItems() {
        requestInput.getItems().add("Item 1");
        requestInput.getItems().add("Item 2");
        requestInput.getItems().add("Item 3");
    }

    /**
     * Initializes context menu of request input.
     */
    private void initContextMenu() {
        var menu = new ContextMenu();
        var addItem = new MenuItem("Add");
        addItem.setOnAction(this::onAddItemClick);
        var removeItem = new MenuItem("Remove");
        removeItem.setOnAction(this::onRemoveItemClick);

        menu.getItems().add(addItem);
        menu.getItems().add(removeItem);
        requestInput.setContextMenu(menu);
    }
}
