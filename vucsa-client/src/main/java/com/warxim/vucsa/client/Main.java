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
package com.warxim.vucsa.client;

import com.warxim.vucsa.client.gui.GuiApplication;
import com.warxim.vucsa.client.gui.GuiBundle;
import com.warxim.vucsa.client.gui.dialog.Dialogs;
import javafx.application.Application;

/**
 * Main client application class.
 */
public final class Main {
    public static void main(String... args) {
        init();
        launch();
    }

    /**
     * Initializes GUI bundle.
     */
    private static void init() {
        var guiBundle = GuiBundle.getInstance();
        Dialogs.setDefaultIcon(guiBundle.getLogo());
    }

    /**
     * Launches GUI application.
     */
    private static void launch() {
        Application.launch(GuiApplication.class);
    }

    private Main() {}
}
