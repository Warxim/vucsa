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
package com.warxim.vucsa.client.util;

import javafx.scene.Node;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;

/**
 * GUI utils.
 */
public final class GuiUtils {
    private GuiUtils() {
    }

    /**
     * Adds tab(node) to specified tab pane using the specified order.
     * (Wraps the node to a scroll pane, creates new tab and adds it to the tabPane.)
     * @param tabPane Tab pane to which to add the tab
     * @param title Text of the tab title
     * @param node Content node, which will be a content of the tab
     * @param order Order to use when adding the tab
     * @return Created tab
     */
    public static Tab addTabToTabPane(TabPane tabPane, String title, Node node, Integer order) {
        var scrollPane = new ScrollPane(node);

        scrollPane.setFitToHeight(true);
        scrollPane.setFitToWidth(true);
        var tab = new Tab(title, scrollPane);
        tab.setUserData(order);

        var tabs = tabPane.getTabs();
        var index = 0;
        for (; index < tabs.size(); ++index) {
            var currentOrder = tabs.get(index).getUserData();
            if (currentOrder == null || (Integer) currentOrder > order) {
                break;
            }
        }
        tabPane.getTabs().add(index, tab);
        return tab;
    }

}
