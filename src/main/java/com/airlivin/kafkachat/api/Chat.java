package com.airlivin.kafkachat.api;

/**
 * Interface represents a chat system
 * and common operations in such system
 * like user connection to a chat, sending message
 * and listening to incoming messages.
 */
public interface Chat {
    /**
     * Connects user to specific chat room.
     */
    void connect(String userId, String chatId);

    /**
     * Disconnects and cleanup.
     */
    void disconnect();

    /**
     * Sends given message to specific chat under given sender user.
     */
    void sendMessage(String senderId, String chatId, String message);

    void addMessageListener(MessageListener messageListener);
}
