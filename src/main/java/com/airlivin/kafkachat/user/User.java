package com.airlivin.kafkachat.user;

/**
 * User representation.
 *
 * @param name a user's name and id inside a chat.
 */
public record User(String name) {
    public static User system() {
        return new User(System.getProperty("user.name", "user"));
    }
}
