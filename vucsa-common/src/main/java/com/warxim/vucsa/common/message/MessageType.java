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
package com.warxim.vucsa.common.message;

import com.warxim.vucsa.common.message.commandexecution.request.PingRequestDeserializer;
import com.warxim.vucsa.common.message.commandexecution.response.PingResponseDeserializer;
import com.warxim.vucsa.common.message.commandexecution.response.PingResponseSerializer;
import com.warxim.vucsa.common.message.horizontalaccesscontrol.request.DocumentContentRequest;
import com.warxim.vucsa.common.message.horizontalaccesscontrol.response.DocumentContentResponse;
import com.warxim.vucsa.common.message.rcedeserialization.TextMessage;
import com.warxim.vucsa.common.message.rcedeserialization.TextMessageDeserializer;
import com.warxim.vucsa.common.message.rcedeserialization.TextMessageSerializer;
import com.warxim.vucsa.common.message.sqlinjection.response.SearchResponseDeserializer;
import com.warxim.vucsa.common.message.verticalaccesscontrol.request.*;
import com.warxim.vucsa.common.message.verticalaccesscontrol.response.*;
import com.warxim.vucsa.common.message.bufferoverread.StringListMessageDeserializer;
import com.warxim.vucsa.common.message.bufferoverread.StringListMessageSerializer;
import com.warxim.vucsa.common.message.bufferoverread.StringListMessage;
import com.warxim.vucsa.common.message.commandexecution.request.PingRequest;
import com.warxim.vucsa.common.message.commandexecution.request.PingRequestSerializer;
import com.warxim.vucsa.common.message.commandexecution.response.PingResponse;
import com.warxim.vucsa.common.message.enumeration.request.LoginRequest;
import com.warxim.vucsa.common.message.enumeration.request.LoginRequestDeserializer;
import com.warxim.vucsa.common.message.enumeration.request.LoginRequestSerializer;
import com.warxim.vucsa.common.message.enumeration.response.LoginResponse;
import com.warxim.vucsa.common.message.enumeration.response.LoginResponseDeserializer;
import com.warxim.vucsa.common.message.enumeration.response.LoginResponseSerializer;
import com.warxim.vucsa.common.message.horizontalaccesscontrol.request.DocumentContentRequestDeserializer;
import com.warxim.vucsa.common.message.horizontalaccesscontrol.request.DocumentContentRequestSerializer;
import com.warxim.vucsa.common.message.horizontalaccesscontrol.response.DocumentContentResponseDeserializer;
import com.warxim.vucsa.common.message.horizontalaccesscontrol.response.DocumentContentResponseSerializer;
import com.warxim.vucsa.common.message.plain.PlainMessage;
import com.warxim.vucsa.common.message.plain.PlainMessageDeserializer;
import com.warxim.vucsa.common.message.plain.PlainMessageSerializer;
import com.warxim.vucsa.common.message.sqlinjection.request.SearchRequest;
import com.warxim.vucsa.common.message.sqlinjection.request.SearchRequestDeserializer;
import com.warxim.vucsa.common.message.sqlinjection.request.SearchRequestSerializer;
import com.warxim.vucsa.common.message.sqlinjection.response.SearchResponse;
import com.warxim.vucsa.common.message.sqlinjection.response.SearchResponseSerializer;
import com.warxim.vucsa.common.message.verticalaccesscontrol.request.*;
import com.warxim.vucsa.common.message.verticalaccesscontrol.response.*;
import com.warxim.vucsa.common.message.xml.StorageMessage;
import com.warxim.vucsa.common.message.xml.StorageMessageDeserializer;
import com.warxim.vucsa.common.message.xml.StorageMessageSerializer;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.Arrays;
import java.util.Optional;

/**
 * Message type.
 */
@Getter
@RequiredArgsConstructor
public enum MessageType {
    PLAIN(1, PlainMessage.class, new PlainMessageSerializer(), new PlainMessageDeserializer()),
    BUFFER_OVERREAD_STRING_LIST_MESSAGE(2, StringListMessage.class, new StringListMessageSerializer(), new StringListMessageDeserializer()),
    SQL_INJECTION_SEARCH_REQUEST(3, SearchRequest.class, new SearchRequestSerializer(), new SearchRequestDeserializer()),
    SQL_INJECTION_SEARCH_RESPONSE(4, SearchResponse.class, new SearchResponseSerializer(), new SearchResponseDeserializer()),
    ENUMERATION_CHALLENGE_LOGIN_REQUEST(5, LoginRequest.class, new LoginRequestSerializer(), new LoginRequestDeserializer()),
    ENUMERATION_CHALLENGE_LOGIN_RESPONSE(6, LoginResponse.class, new LoginResponseSerializer(), new LoginResponseDeserializer()),
    COMMAND_EXECUTION_PING_REQUEST(7, PingRequest.class, new PingRequestSerializer(), new PingRequestDeserializer()),
    COMMAND_EXECUTION_PING_RESPONSE(8, PingResponse.class, new PingResponseSerializer(), new PingResponseDeserializer()),
    XML_STORAGE_MESSAGE(9, StorageMessage.class, new StorageMessageSerializer(), new StorageMessageDeserializer()),
    VERTICAL_ACCESS_CONTROL_USER_INFO_REQUEST(10, UserInfoRequest.class, new UserInfoRequestSerializer(), new UserInfoRequestDeserializer()),
    VERTICAL_ACCESS_CONTROL_USER_INFO_RESPONSE(11, UserInfoResponse.class, new UserInfoResponseSerializer(), new UserInfoResponseDeserializer()),
    VERTICAL_ACCESS_CONTROL_SECRET_REQUEST(12, SecretRequest.class, new SecretRequestSerializer(), new SecretRequestDeserializer()),
    VERTICAL_ACCESS_CONTROL_SECRET_RESPONSE(13, SecretResponse.class, new SecretResponseSerializer(), new SecretResponseDeserializer()),
    HORIZONTAL_ACCESS_CONTROL_DOCUMENT_CONTENT_REQUEST(14, DocumentContentRequest.class, new DocumentContentRequestSerializer(), new DocumentContentRequestDeserializer()),
    HORIZONTAL_ACCESS_CONTROL_DOCUMENT_CONTENT_RESPONSE(15, DocumentContentResponse.class, new DocumentContentResponseSerializer(), new DocumentContentResponseDeserializer()),
    RCE_DESERIALIZATION_TEXT_MESSAGE(16, TextMessage.class, new TextMessageSerializer(), new TextMessageDeserializer()),
    ;

    /**
     * Serialized value of message type (unique).
     */
    private final int value;

    /**
     * Class representing the message.
     */
    private final Class<? extends Message> clazz;

    /**
     * Serializer for serializing given message type.
     */
    private final MessageSerializer serializer;

    /**
     * Deserializer for deserializing given message type.
     */
    private final MessageDeserializer deserializer;

    /**
     * Converts serialized message type to enum message type.
     * @return Message type or empty optional if message for given type was not found
     */
    public static Optional<MessageType> of(int value) {
        return Arrays.stream(values())
                .filter(type -> type.getValue() == value)
                .findAny();
    }

}
