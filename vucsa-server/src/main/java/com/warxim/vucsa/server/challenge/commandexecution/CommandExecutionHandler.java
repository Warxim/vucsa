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
package com.warxim.vucsa.server.challenge.commandexecution;

import com.warxim.vucsa.common.ChallengeConstant;
import com.warxim.vucsa.common.connection.Connection;
import com.warxim.vucsa.common.message.Message;
import com.warxim.vucsa.common.message.MessageHandler;
import com.warxim.vucsa.common.message.commandexecution.request.PingRequest;
import com.warxim.vucsa.common.message.commandexecution.response.PingResponse;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Handler for command execution challenge
 * <p>Executes ping command on host specified by user in vulnerable way, which allows the user to cause command execution.</p>
 */
public class CommandExecutionHandler implements MessageHandler {
    @Override
    public boolean supports(Message message) {
        return message instanceof PingRequest;
    }

    @Override
    public boolean handleMessage(Connection connection, Message message) {
        var pingRequest = (PingRequest) message;
        if (pingRequest.getHost().isBlank()) {
            connection.sendMessage(PingResponse.builder()
                    .target(ChallengeConstant.COMMAND_EXECUTION_TARGET)
                    .output("You have to specify host!")
                    .build());
            return true;
        }
        var output = ping(pingRequest.getHost());
        connection.sendMessage(PingResponse.builder()
                .target(ChallengeConstant.COMMAND_EXECUTION_TARGET)
                .output(output)
                .build());
        return true;
    }

    /**
     * Pings host and returns output.
     */
    private static String ping(String host) {
        if (isWindows()) {
            return executeCommand(new String[] {"cmd.exe", "/C", "ping " + host});
        } else {
            return executeCommand(new String[] {"sh", "-c", "ping -c 4 " + host});
        }
    }

    /**
     * Vulnerable command execution.
     */
    private static String executeCommand(String[] cmd) {
        Process process = null;
        try {
            var processBuilder = new ProcessBuilder(cmd);
            processBuilder.redirectErrorStream(true);

            process = processBuilder.start();
            if (!process.waitFor(30, TimeUnit.SECONDS)) {
                process.destroy();
                if (!process.waitFor(30, TimeUnit.SECONDS)) {
                    process.destroyForcibly();
                }
                return "Process did not finish in time!";
            }
            var builder = new StringBuilder();
            try (var reader = new BufferedReader(new InputStreamReader(process.getInputStream()))) {
                String line;
                while ((line = reader.readLine()) != null){
                    builder.append(line).append('\n');
                }
            }
            return builder.toString();
        } catch (IOException e) {
            Logger.getGlobal().log(
                    Level.SEVERE,
                    e,
                    () -> String.format("Could not execute command '%s'!", String.join(" ", cmd)));
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            Logger.getGlobal().log(
                    Level.SEVERE,
                    e,
                    () -> String.format("Interrupted command execution '%s'!", String.join(" ", cmd)));
        } finally {
            if (process != null) {
                process.destroy();
            }
        }
        return "";
    }

    /**
     * Checks, whether the current platform is Windows or not.
     */
    private static boolean isWindows() {
        return System.getProperty("os.name").toLowerCase().contains("windows");
    }
}
