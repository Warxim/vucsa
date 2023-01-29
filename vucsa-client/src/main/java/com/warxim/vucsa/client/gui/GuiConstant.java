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

public final class GuiConstant {
    /**
     * Path to main CSS file of the application
     */
    public static final String MAIN_CSS_PATH = "/css/Main.css";
    public static final String ICON_PATH = "/img/Logo.png";

    public static final int LOG_TAB_ORDER = 1;
    public static final int SETTINGS_TAB_ORDER = 2;
    public static final int CHALLENGES_TAB_ORDER = 10;

    private GuiConstant() {}
}
