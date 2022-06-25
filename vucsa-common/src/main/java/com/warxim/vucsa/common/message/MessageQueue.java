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
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

/**
 * Queue of messages.
 */
public class MessageQueue  {
    private final BlockingQueue<Message> queue;

    public MessageQueue() {
        queue = new LinkedBlockingQueue<>();
    }

    /**
     * Add message to queue.
     * @param message Message to be added to the queue
     */
    public void add(Message message) {
        queue.add(message);
    }

    /**
     * Returns message from queue (blocks until there is message).
     * @return Message from queue
     */
    public Message take() throws InterruptedException {
        return queue.take();
    }

    /**
     * Retrieves and removes the head of the queue.
     * @return Message from queue or empty optional if the queue is empty
     */
    public Optional<Message> poll() {
        return Optional.ofNullable(queue.poll());
    }

    /**
     * Get queue size.
     * @return Size of the queue
     */
    public int size() {
        return queue.size();
    }

    /**
     * Clears queue.
     */
    public void clear() {
        queue.clear();
    }

    /**
     * Checks if the queue is empty.
     * @return {@code true} if the queue is empty
     */
    public boolean isEmpty() {
        return queue.isEmpty();
    }
}
