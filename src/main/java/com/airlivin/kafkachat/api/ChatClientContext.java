package com.airlivin.kafkachat.api;

public interface ChatClientContext {
    /**
     * Supply a user id of the currently active chat client
     * @return a user id
     */
    String getUserId();
}
