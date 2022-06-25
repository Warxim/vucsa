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
package com.warxim.vucsa.server.bootstrap;

import com.warxim.vucsa.server.configuration.Configuration;
import com.warxim.vucsa.server.configuration.ConfigurationException;
import com.warxim.vucsa.server.configuration.ConfigurationLoader;
import com.warxim.vucsa.server.core.ServerConfig;
import com.warxim.vucsa.server.core.ServerManager;
import com.warxim.vucsa.server.core.ServerState;
import com.warxim.vucsa.common.Constant;
import com.warxim.vucsa.server.challenge.ChallengeManager;

import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Console bootstrap for running the server in console.
 */
public class ConsoleBootstrap {
    private Configuration configuration;
    private ServerManager serverManager;

    /**
     * Starts the server and user input loop.
     */
    public void start() {
        try {
            configuration = ConfigurationLoader.loadOrDefault(
                    Constant.SERVER_CONFIG_PATH,
                    () -> Configuration.builder()
                            .network(ServerConfig.builder()
                                    .serverHost(Constant.DEFAULT_SERVER_HOST)
                                    .serverPort(Constant.DEFAULT_SERVER_PORT)
                                    .build())
                            .build());
        } catch (ConfigurationException e) {
            Logger.getGlobal().log(Level.SEVERE, "Could not load/save configuration!", e);
            return;
        }

        serverManager = new ServerManager();
        var challengeManager = new ChallengeManager();
        challengeManager.load(serverManager);
        serverManager.start(configuration.getNetwork());

        printHelp();
        runInputLoop();

        challengeManager.unload(serverManager);
        serverManager.stop();
    }

    /**
     * Runs user input loop (waits for commands and executes them).
     */
    private void runInputLoop() {
        var scanner = new Scanner(System.in);
        while (true) {
            if (!scanner.hasNextLine()) {
                try {
                    Thread.sleep(100);
                    continue;
                } catch (InterruptedException e) {
                    Logger.getGlobal().log(Level.INFO, "Interrupted, quiting...", e);
                    Thread.currentThread().interrupt();
                    return;
                }
            }

            var line = scanner.nextLine().trim();
            switch (line) {
                case "stop":
                case "quit":
                case "exit":
                case "shutdown":
                    serverManager.stop();
                    waitForServerTermination(serverManager);
                    return;
                case "restart":
                    serverManager.stop();
                    waitForServerTermination(serverManager);
                    serverManager.start(configuration.getNetwork());
                    break;
                default:
                    printHelp();
            }
        }
    }

    /**
     * Prints help to the command line.
     */
    private void printHelp() {
        var out = System.out;
        printSeparator();
        out.println("\tHELP");
        printSeparator();
        out.println("Control server");
        out.println("\trestart - restart the server");
        out.println("\tstop - close the server");
    }

    /**
     * Prints separator to the command line.
     */
    private void printSeparator() {
        System.out.println("# ------------------------------------------ #");
    }


    /**
     * Waits till the PETEP core stops.
     */
    private void waitForServerTermination(ServerManager serverManager) {
        while (serverManager.getState() != ServerState.STOPPED) {
            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                // Interrupted.
                Thread.currentThread().interrupt();
                break;
            }
        }
    }
}
