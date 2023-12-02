package com.airlivin.kafkachat;

import com.airlivin.kafkachat.api.Chat;
import com.airlivin.kafkachat.api.ChatClientContext;
import com.airlivin.kafkachat.systems.kafka.KafkaChatSystem;
import com.airlivin.kafkachat.user.User;

import java.util.Scanner;

import static java.lang.System.out;

/**
 * Kafka Chat main CLI app driver that glues all together.
 * It incorporates a kafka chat system for chat functionality
 * and uses console as input and output.
 */
public class KafkaChat implements Thread.UncaughtExceptionHandler, ChatClientContext {
    private static final String DEFAULT_CHAT_ID = "chat";
    public static final String EXIT_COMMAND = "/exit";
    private Chat chat;
    private User user;
    private String chatId;
    private Scanner stdin;

    public void run() {
        init();
        welcome();
        connect();
        readLoop();
        finish();
    }

    private void finish() {
        close();
        bye();
    }

    private void promptUsername() {
        var systemUser = User.system();
        out.printf("What is your name? [%s]: ", systemUser.name());
        String userName = null;
        if (stdin.hasNextLine()) {
            userName = stdin.nextLine();
        }

        user = (userName == null || userName.isEmpty()) ?
                systemUser : new User(userName);
    }

    private void close() {
        chat.disconnect();
    }

    private void connect() {
        chat = new KafkaChatSystem(this);
        chat.addMessageListener(this::receiveMessage);
        chat.connect(getUserId(), getChatId());
    }
    private void init() {
        Thread.setDefaultUncaughtExceptionHandler(this);

        stdin = new Scanner(System.in);
    }

    private void bye() {
        out.println("Good bye!");
    }

    private void readLoop() {
        while (stdin.hasNextLine()) {
            String line = stdin.nextLine();

            if (line.startsWith(EXIT_COMMAND)) {
                return;
            }

            sendMessage(line);
        }
    }

    private void sendMessage(String message) {
        chat.sendMessage(user.name(), getChatId(), message);
    }

    private void receiveMessage(String sender, String message) {
        if (user.name().equals(sender))
            return;

        out.println(sender + ": " + message);
    }

    private void promptChatId() {
        out.printf("What chat you want to join? [%s]: ", DEFAULT_CHAT_ID);
        if (stdin.hasNextLine()) {
            var chatIdInput = stdin.nextLine();
            chatId = (chatIdInput != null && !chatIdInput.isEmpty()) ?
                    chatIdInput : DEFAULT_CHAT_ID;
        }
    }

    private void welcome() {
        out.println("""
                Welcome to KafkaChat 💻️💎!
                simplest kafka-backed multi-user chat
                made for education purposes
                © Vladimir Livin, 2023.
                
                Type '/exit' to finish chat.
                """);
    }

    public static void main(String[] args) {
        var chat = new KafkaChat();
        chat.run();
    }

    @Override
    public void uncaughtException(Thread t, Throwable e) {
        System.err.println(e.getMessage());
        System.exit(2);
    }

    @Override
    public String getUserId() {
        if (user == null)
            promptUsername();
        return user.name();
    }

    public String getChatId() {
        if (chatId == null)
            promptChatId();
        return chatId;
    }
}
