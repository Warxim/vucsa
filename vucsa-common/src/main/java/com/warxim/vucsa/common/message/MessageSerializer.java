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

import java.util.Optional;

/**
 * Message serializer for serializing message into bytes.
 */
public interface MessageSerializer {
    /**
     * Serializes message into deserialized message.
     * @param message Message to be serialized
     * @return Serialized message or empty optional if the serialization failed
     */
    Optional<SerializedMessage> serializeMessage(Message message);
}
