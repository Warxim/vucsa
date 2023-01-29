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
package com.warxim.vucsa.client.gui;

import com.sun.javafx.css.StyleManager;
import com.warxim.vucsa.client.Bundle;
import com.warxim.vucsa.common.Constant;
import com.warxim.vucsa.common.connection.ConnectionState;
import com.warxim.vucsa.client.gui.controller.ApplicationController;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import javax.imageio.ImageIO;
import java.awt.Taskbar;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Client GUI application.
 * <p>Starts Vulnerable Client GUI.</p>
 */
public class GuiApplication extends Application {
    @Override
    public void start(Stage stage) throws Exception {
        setupIcons(stage);

        Application.setUserAgentStylesheet(Application.STYLESHEET_MODENA);
        StyleManager.getInstance().addUserAgentStylesheet(getClass().getResource(GuiConstant.MAIN_CSS_PATH).toString());

        // Store host services inside GUI bundle
        GuiBundle.getInstance().setHostServices(getHostServices());

        var fxmlLoader = new FXMLLoader(getClass().getResource("/fxml/Application.fxml"));
        fxmlLoader.setController(new ApplicationController());

        Parent root = fxmlLoader.load();

        var scene = new Scene(root);
        stage.setTitle("VuCSA Client v" + Constant.VERSION);
        stage.setWidth(1200);
        stage.setHeight(900);
        stage.setScene(scene);

        stage.show();
    }

    @Override
    public void stop() throws Exception {
        super.stop();

        var clientManager = Bundle.getInstance().getClientManager();
        if (clientManager != null && clientManager.getState() != ConnectionState.STOPPED) {
            clientManager.stop();
        }

        Bundle.getInstance().destroy();
        GuiBundle.getInstance().destroy();
    }

    /**
     * Sets up icons for the application
     */
    protected void setupIcons(Stage stage) {
        // Set stage icons (WIN, LINUX)
        stage.getIcons().add(GuiBundle.getInstance().getLogo());

        // Little workaround to set icons for macOS taskbar using AWT
        try {
            if (!Taskbar.isTaskbarSupported()) {
                return;
            }
            var iconImage = ImageIO.read(getClass().getResourceAsStream(GuiConstant.ICON_PATH));
            var taskbar = Taskbar.getTaskbar();
            taskbar.setIconImage(iconImage);
        } catch (Exception e) {
            Logger.getGlobal().log(Level.SEVERE, "Could not set taskbar icon!", e);
        }
    }
}
