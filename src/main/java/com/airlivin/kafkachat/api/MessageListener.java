package com.airlivin.kafkachat.api;

/**
 * Provides interface to listen for incoming messages.
 */
public interface MessageListener {
    /**
     * Message is sent when new message has been received.
     *
     * @param sender - a sender id of the chat system.
     * @param message - the message body
     */
    void receiveMessage(String sender, String message);
}
