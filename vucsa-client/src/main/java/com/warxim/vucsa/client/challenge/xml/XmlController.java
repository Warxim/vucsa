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
package com.warxim.vucsa.client.challenge.xml;

import com.warxim.vucsa.client.Bundle;
import com.warxim.vucsa.client.challenge.ChallengeController;
import com.warxim.vucsa.client.gui.dialog.Dialogs;
import com.warxim.vucsa.common.ChallengeConstant;
import com.warxim.vucsa.common.message.xml.StorageItem;
import com.warxim.vucsa.common.message.xml.StorageMessage;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;

import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

/**
 * XML controller handles XML challenge.
 * <p>This challenge acts like a simple ECHO functionality (simulates "storage"), but with vulnerable deserialization implementation.</p>
 */
public class XmlController extends ChallengeController implements Initializable {
    private final XmlHandler handler = new XmlHandler(this);

    @FXML
    private TableView<StorageItem> requestTable;
    @FXML
    private TableColumn<StorageItem, String> requestKeyColumn;
    @FXML
    private TableColumn<StorageItem, String> requestValueColumn;
    @FXML
    private TableView<StorageItem> responseTable;
    @FXML
    private TableColumn<StorageItem, String> responseKeyColumn;
    @FXML
    private TableColumn<StorageItem, String> responseValueColumn;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        initHandler();
        initDefaultItems();
        initTable();
        initContextMenu();
    }

    /**
     * Sets storage items to the GUI.
     * @param items Items to be set
     */
    public void setItems(List<StorageItem> items) {
        Platform.runLater(() -> responseTable.getItems().setAll(items));
    }

    /**
     * Sends storage items to server to "save" them.
     */
    @FXML
    private void onSendClick(ActionEvent event) {
        var message = StorageMessage.builder()
                .target(ChallengeConstant.XML_TARGET)
                .items(requestTable.getItems())
                .build();
        sendMessage(message);
    }

    /**
     * Adds new item.
     */
    @FXML
    private void onAddItemClick(ActionEvent event) {
        var maybePair = Dialogs.createTextPairDialog("Add Item", "Key", "Value");
        if (maybePair.isEmpty()) {
            return;
        }
        var pair = maybePair.get();
        requestTable.getItems().add(StorageItem.builder()
                .key(pair.getKey())
                .value(pair.getValue())
                .build());
    }

    /**
     * Removes existing item.
     */
    @FXML
    private void onRemoveItemClick(ActionEvent event) {
        var selected = requestTable.getSelectionModel().getSelectedIndex();
        if (selected < 0) {
            return;
        }

        requestTable.getItems().remove(selected);
    }

    /**
     * Initializes buffer over-read message handler.
     */
    private void initHandler() {
        Bundle.getInstance().getClientManager().registerHandler(ChallengeConstant.XML_TARGET, handler);
    }

    /**
     * Initializes default items in the challenge GUI.
     */
    private void initDefaultItems() {
        requestTable.getItems().add(StorageItem.builder().key("Item 1").value("VALUE 1").build());
        requestTable.getItems().add(StorageItem.builder().key("Item 2").value("VALUE 2").build());
        requestTable.getItems().add(StorageItem.builder().key("Item 3").value("VALUE 3").build());
    }

    /**
     * Initializes table columns.
     */
    private void initTable() {
        requestKeyColumn.setCellValueFactory(new PropertyValueFactory<>("key"));
        requestValueColumn.setCellValueFactory(new PropertyValueFactory<>("value"));
        responseKeyColumn.setCellValueFactory(new PropertyValueFactory<>("key"));
        responseValueColumn.setCellValueFactory(new PropertyValueFactory<>("value"));
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
        requestTable.setContextMenu(menu);
    }
}
