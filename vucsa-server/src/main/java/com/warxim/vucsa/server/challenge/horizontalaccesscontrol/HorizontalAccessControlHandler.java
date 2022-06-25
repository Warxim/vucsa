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
package com.warxim.vucsa.server.challenge.horizontalaccesscontrol;

import com.warxim.vucsa.common.ChallengeConstant;
import com.warxim.vucsa.common.connection.Connection;
import com.warxim.vucsa.common.message.Message;
import com.warxim.vucsa.common.message.MessageHandler;
import com.warxim.vucsa.common.message.horizontalaccesscontrol.request.DocumentContentRequest;
import com.warxim.vucsa.common.message.horizontalaccesscontrol.response.DocumentContentResponse;
import lombok.RequiredArgsConstructor;

import java.io.FileInputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;

/**
 * Handler for horizontal access control challenge
 * <p>Handles document management.</p>
 */
@RequiredArgsConstructor
public class HorizontalAccessControlHandler implements MessageHandler {
    private static final String DOCUMENT_PATH = "document/%d.txt";
    private final String challengeDirectory;

    @Override
    public boolean supports(Message message) {
        return message instanceof DocumentContentRequest;
    }

    @Override
    public boolean handleMessage(Connection connection, Message message) {
        var documentContentRequest = (DocumentContentRequest) message;
        var documentId = documentContentRequest.getDocumentId();
        var content = loadDocumentContent(documentId);
        connection.sendMessage(DocumentContentResponse.builder()
                .target(ChallengeConstant.HORIZONTAL_ACCESS_CONTROL_DOCUMENT_CONTENT_TARGET)
                .content(content)
                .build());
        return true;
    }

    /**
     * Loads document content by identifier.
     */
    protected String loadDocumentContent(int documentId) {
        var path = challengeDirectory + String.format(DOCUMENT_PATH, documentId);
        try (var in = new FileInputStream(path)) {
            return new String(in.readAllBytes(), StandardCharsets.UTF_8);
        } catch (IOException e) {
            return String.format("File '%s' not found!", path);
        }
    }
}
