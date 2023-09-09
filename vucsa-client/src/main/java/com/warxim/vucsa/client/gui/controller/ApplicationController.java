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
import com.warxim.vucsa.client.challenge.commandexecution.CommandExecutionController;
import com.warxim.vucsa.client.challenge.enumeration.EnumerationController;
import com.warxim.vucsa.client.challenge.rcedeserialization.RceDeserializationController;
import com.warxim.vucsa.client.challenge.verticalaccesscontrol.VerticalAccessControlController;
import com.warxim.vucsa.client.challenge.ChallengeController;
import com.warxim.vucsa.client.challenge.ChallengeWrapper;
import com.warxim.vucsa.client.challenge.bufferoverread.BufferOverreadController;
import com.warxim.vucsa.client.challenge.horizontalaccesscontrol.HorizontalAccessControlController;
import com.warxim.vucsa.client.challenge.sqlinjection.SqlInjectionController;
import com.warxim.vucsa.client.challenge.xml.XmlController;
import com.warxim.vucsa.client.gui.GuiBundle;
import com.warxim.vucsa.client.gui.GuiConstant;
import com.warxim.vucsa.client.gui.dialog.AboutDialog;
import com.warxim.vucsa.client.util.GuiUtils;
import com.warxim.vucsa.common.connection.Connection;
import com.warxim.vucsa.common.connection.ConnectionState;
import com.warxim.vucsa.common.connection.listener.ConnectionListener;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.TabPane;

import java.io.IOException;
import java.net.URL;
import java.util.LinkedList;
import java.util.List;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Main application controller, which handles the whole application.
 */
public class ApplicationController implements Initializable, ConnectionListener {
    private final List<ChallengeWrapper> challengeWrappers = new LinkedList<>();

    @FXML
    private TabPane tabs;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        GuiBundle.getInstance().setApplicationController(this);
        Bundle.getInstance().getClientManager().registerConnectionListener(this);

        initApplicationTabs();
        initChallengeTabs();

        Logger.getGlobal().info("Application initialized!");
    }

    /**
     * Exits application dialog.
     */
    @FXML
    private void onExitMenuClick(ActionEvent event) {
        Platform.exit();
    }
    /**
     * Shows application about dialog.
     */

    @FXML
    private void showAbout(ActionEvent e) {
        AboutDialog.show();
    }

    @Override
    public void onConnectionStart(Connection connection) {
        Platform.runLater(() ->
                challengeWrappers.forEach(challengeWrapper ->  challengeWrapper.getNode().setDisable(false))
        );
    }

    @Override
    public void onConnectionStop(Connection connection) {
        Platform.runLater(() ->
            challengeWrappers.forEach(challengeWrapper ->  challengeWrapper.getNode().setDisable(true))
        );
    }

    /**
     * Registers new tab to main tabs.
     * @param title Title of the tab
     * @param node Node to be added as a child into the tab
     * @param order Order of the tab (where should the tab be placed)
     */
    private void registerTab(String title, Node node, int order) {
        GuiUtils.addTabToTabPane(tabs, title, node, order);
    }

    /**
     * Initializes application tab.
     */
    private void initApplicationTab(String title, String template, Object controller, int order) {
        try {
            var fxmlLoader = new FXMLLoader(getClass().getResource(template));
            fxmlLoader.setController(controller);
            registerTab(title, fxmlLoader.load(), order);
        } catch (IOException e) {
            Logger.getGlobal().log(Level.SEVERE, "Could not add settings tab", e);
        }
    }

    /**
     * Initializes challenge tab and saves it to {@code challengeWrappers}.
     */
    private void initChallengeTab(String title, String template, ChallengeController controller, int order) {
        try {
            var fxmlLoader = new FXMLLoader(getClass().getResource(template));
            fxmlLoader.setController(controller);
            var node = (Node)  fxmlLoader.load();
            var wrapper = ChallengeWrapper.builder()
                    .controller(controller)
                    .node(node)
                    .build();
            challengeWrappers.add(wrapper);
            node.setDisable(Bundle.getInstance().getClientManager().getState() == ConnectionState.STOPPED);
            registerTab(title, node, order);
        } catch (IOException e) {
            Logger.getGlobal().log(Level.SEVERE, "Could not add settings tab", e);
        }
    }

    /**
     * Initializes main application tabs.
     */
    private void initApplicationTabs() {
        initApplicationTab("Log", "/fxml/tab/LogTab.fxml", new LogController(), GuiConstant.LOG_TAB_ORDER);
        initApplicationTab("Settings", "/fxml/tab/SettingsTab.fxml", new SettingsController(), GuiConstant.SETTINGS_TAB_ORDER);
    }

    /**
     * Initializes challenge tabs.
     * <p>Creates challenges and adds them to the tab pane.</p>
     */
    private void initChallengeTabs() {
        var tabOrder = GuiConstant.CHALLENGES_TAB_ORDER;
        initChallengeTab(
                "Buffer Over-read",
                "/fxml/challenge/bufferoverread/BufferOverreadTab.fxml",
                new BufferOverreadController(),
                ++tabOrder);
        initChallengeTab(
                "SQL Injection",
                "/fxml/challenge/sqlinjection/SqlInjectionTab.fxml",
                new SqlInjectionController(),
                ++tabOrder);
        initChallengeTab(
                "Enumeration",
                "/fxml/challenge/enumeration/EnumerationTab.fxml",
                new EnumerationController(),
                ++tabOrder);
        initChallengeTab(
                "Command Execution",
                "/fxml/challenge/commandexecution/CommandExecutionTab.fxml",
                new CommandExecutionController(),
                ++tabOrder);
        initChallengeTab(
                "XML",
                "/fxml/challenge/xml/XmlTab.fxml",
                new XmlController(),
                ++tabOrder);
        initChallengeTab(
                "Horizontal Access Control",
                "/fxml/challenge/horizontalaccesscontrol/HorizontalAccessControlTab.fxml",
                new HorizontalAccessControlController(),
                ++tabOrder);
        initChallengeTab(
                "Vertical Access Control",
                "/fxml/challenge/verticalaccesscontrol/VerticalAccessControlTab.fxml",
                new VerticalAccessControlController(),
                ++tabOrder);
        initChallengeTab(
                "RCE Deserialization",
                "/fxml/challenge/rcedeserialization/RceDeserializationTab.fxml",
                new RceDeserializationController(),
                ++tabOrder);
    }
}
