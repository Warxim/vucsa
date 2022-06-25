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
package com.warxim.vucsa.client.challenge.sqlinjection;

import com.warxim.vucsa.client.Bundle;
import com.warxim.vucsa.client.challenge.ChallengeController;
import com.warxim.vucsa.common.ChallengeConstant;
import com.warxim.vucsa.common.message.sqlinjection.request.SearchRequest;
import com.warxim.vucsa.common.message.sqlinjection.FoodEntity;
import javafx.application.Platform;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;

import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

/**
 * SQL Injection Controller handles SQL injection challenge.
 * <p>Simulates food menu search and displays found food.</p>
 */
public class SqlInjectionController extends ChallengeController implements Initializable {
    private final SqlInjectionHandler handler = new SqlInjectionHandler(this);

    @FXML
    private TextField searchInput;
    @FXML
    private TableView<FoodEntity> resultTable;
    @FXML
    private TableColumn<FoodEntity, Integer> idColumn;
    @FXML
    private TableColumn<FoodEntity, String> nameColumn;
    @FXML
    private TableColumn<FoodEntity, String> descriptionColumn;
    @FXML
    private TableColumn<FoodEntity, Double> priceColumn;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        initHandler();
        initTable();
        searchInput.textProperty().addListener(this::onSearchInputChange);
    }

    /**
     * Sets items to the table.
     * @param items Items to be set
     */
    public void setItems(List<FoodEntity> items) {
        Platform.runLater(() -> resultTable.getItems().setAll(items));
    }

    @FXML
    private void onSearchClick(ActionEvent event) {
        var message = SearchRequest.builder()
                .target(ChallengeConstant.SQL_INJECTION_TARGET)
                .search(searchInput.getText())
                .build();
        sendMessage(message);
    }

    /**
     * Initializes sql injection message handler.
     */
    private void initHandler() {
        Bundle.getInstance().getClientManager().registerHandler(ChallengeConstant.SQL_INJECTION_TARGET, handler);
    }

    /**
     * Initializes table columns.
     */
    private void initTable() {
        idColumn.setCellValueFactory(new PropertyValueFactory<>("id"));
        nameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
        descriptionColumn.setCellValueFactory(new PropertyValueFactory<>("description"));
        priceColumn.setCellValueFactory(new PropertyValueFactory<>("price"));
    }

    /**
     * Removes invalid characters from search input on change.
     */
    private void onSearchInputChange(
            ObservableValue<? extends String> observable,
            String oldValue,
            String newValue) {
        if (!newValue.matches("\\s0-9a-zA-Z\\.-")) {
            searchInput.setText(newValue.replaceAll("[^\\s0-9a-zA-Z.-]", ""));
        }
    }
}
