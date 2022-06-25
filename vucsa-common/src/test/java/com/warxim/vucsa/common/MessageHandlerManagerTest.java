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
package com.warxim.vucsa.common;

import com.warxim.vucsa.common.connection.Connection;
import com.warxim.vucsa.common.message.MessageHandler;
import com.warxim.vucsa.common.message.MessageHandlerManager;
import com.warxim.vucsa.common.message.plain.PlainMessage;
import org.testng.annotations.Test;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

public class MessageHandlerManagerTest {
    @Test
    public void typeMessagePrioritizationTest() {
        var manager = new MessageHandlerManager();

        var handler1 = createMessageHandlerMock();
        var handler2 = createMessageHandlerMock();

        var message1 = PlainMessage.builder()
                .target(12)
                .length(3)
                .payload(new byte[] {0x01, 0x02, 0x03})
                .build();
        var message2 = PlainMessage.builder()
                .target(12)
                .length(5)
                .payload(new byte[] {0x05, 0x04, 0x06, 0x04, 0x05})
                .build();
        var message3 = PlainMessage.builder()
                .target(4)
                .length(5)
                .payload(new byte[] {0x05, 0x04, 0x06, 0x04, 0x05})
                .build();
        var connection = mock(Connection.class);

        manager.registerHandler(12, handler1);
        manager.registerHandler(4, handler2);

        manager.handleMessage(connection, message1);
        manager.handleMessage(connection, message2);
        manager.handleMessage(connection, message3);

        verify(handler1, times(1)).handleMessage(connection, message1);
        verify(handler1, times(1)).handleMessage(connection, message2);
        verify(handler2, times(1)).handleMessage(connection, message3);
    }

    private MessageHandler createMessageHandlerMock() {
        var handler =  mock(MessageHandler.class);
        when(handler.handleMessage(any(), any())).thenReturn(true);
        when(handler.supports(any())).thenReturn(true);
        return handler;
    }
}
