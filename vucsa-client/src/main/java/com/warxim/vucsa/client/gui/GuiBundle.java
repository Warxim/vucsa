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

import com.warxim.vucsa.client.gui.controller.ApplicationController;
import javafx.scene.image.Image;
import lombok.Getter;

import java.util.Objects;

/**
 * Singleton for client GUI assets.
 */
@Getter
public final class GuiBundle {
    /**
     * Singleton instance.
     */
    private static volatile GuiBundle instance;

    /**
     * Logo icon
     */
    private final Image logo;

    /**
     * Application controller
     */
    private ApplicationController applicationController;

    private GuiBundle() {
        logo = new Image(Objects.requireNonNull(getClass().getResourceAsStream("/img/Logo.png")));
    }

    /**
     * Creates instance of bundle or returns existing instance if it exists.
     * @return Bundle instance
     */
    public static GuiBundle getInstance() {
        if (instance == null) {
            synchronized(GuiBundle.class) {
                if (instance == null) {
                    instance = new GuiBundle();
                }
            }
        }

        return instance;
    }

    /**
     * Obtains application controller, which handles main application window UI
     * @return Application controller
     */
    public ApplicationController getApplicationController() {
        return applicationController;
    }

    /**
     * Sets application controller, which handles main application window UI
     * @param controller Application controller
     */
    public void setApplicationController(ApplicationController controller) {
        applicationController = controller;
    }

    /**
     * Destroys the bundle.
     */
    public void destroy() {
        // no action needed
    }
}