package com.airlivin.kafkachat.systems.kafka;

import com.airlivin.kafkachat.api.Chat;
import com.airlivin.kafkachat.api.ChatClientContext;
import com.airlivin.kafkachat.api.MessageListener;
import com.airlivin.kafkachat.systems.kafka.config.KafkaProperties;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.clients.producer.RecordMetadata;
import org.apache.kafka.common.errors.WakeupException;
import org.apache.kafka.common.header.internals.RecordHeader;

import java.time.Duration;
import java.util.List;

import static java.lang.System.err;
import static java.lang.System.out;
import static java.nio.charset.StandardCharsets.UTF_8;

/**
 * Implementation of chat system through a Kafka.
 * It uses provided kafka cluster as a chat server/storage.
 * The chat room is being represented by a kafka topic.
 * Everyone who connect to that topic reads messages from it
 * and then posts.
 */
public class KafkaChatSystem implements Chat {
    private final KafkaProducer<String, String> producer;
    private final KafkaConsumer<String, String> consumer;
    private final KafkaProperties kafkaProps;
    private boolean finished = false;
    private MessageListener messageListener;
    private Thread listenerThread;

    public KafkaChatSystem(ChatClientContext chatClientContext) {
        kafkaProps = new KafkaProperties();
        applyConfigWithUser(chatClientContext.getUserId());

        producer = new KafkaProducer<>(kafkaProps);
        consumer = new KafkaConsumer<>(kafkaProps);
    }

    private void startListen(final String userId, final String chatId) {
        var t = new Thread(() -> listen(userId, chatId), "Message Listener");
        t.setDaemon(true);
        t.start();
        listenerThread = t;
    }

    private void listen(final String userId, final String chatId) {
        consumer.subscribe(List.of(chatId));
        out.println("User " + userId + " joins " + chatId);
        while(!finished) {
            try {
                ConsumerRecords<String, String> messages = consumer.poll(Duration.ofSeconds(1));
                messages.forEach(this::receiveMessage);
            } catch (WakeupException w) {
                break;
            }
        }
    }

    private void receiveMessage(ConsumerRecord<String, String> message) {
        messageListener.receiveMessage(getSender(message), message.value());
    }

    private static String getSender(ConsumerRecord<String, String> message) {
        String sender = "<none>";
        var userHeaders = message.headers().headers("Sender").iterator();
        if (userHeaders.hasNext()) {
            sender = new String(userHeaders.next().value(), UTF_8);
        }
        return sender;
    }

    public void disconnect() {
        producer.close();

        finished = true;
    }

    @Override
    public void sendMessage(String senderId, String chatId, String message) {
        ProducerRecord<String, String> producerRecord = new ProducerRecord<String, String>(
            chatId, null,
            null, message,
            List.of(new RecordHeader("Sender", senderId.getBytes())));

        producer.send(producerRecord, this::onCompletion);
        producer.flush();
    }

    private void onCompletion(RecordMetadata metadata, Exception exception) {
        if (exception != null)
            err.println("Message error: " + exception.getMessage());

        boolean verbose = false;
        if (verbose)
            out.printf("Message sent to topic %s. %d [%d|] at %tD\n",
                    metadata.topic(),
                    metadata.partition(),
                    metadata.offset(),
                    metadata.timestamp());
    }

    @Override
    public void connect(String userId, String chatId) {
        if (listenerThread != null)
            throw new IllegalStateException("Reconnect not supported yet!");

        startListen(userId, chatId);
    }

    private void applyConfigWithUser(String userId) {
        setGroupId(userId);
        checkServers();
    }

    private void setGroupId(String userId) {
        kafkaProps.putIfAbsent("group.id", "chat-" + userId);
    }

    private void checkServers() {
        String servers = kafkaProps.getProperty("bootstrap.servers");
        if (servers == null)

            throw new IllegalArgumentException("No servers specified.");
        out.println("Servers: " + servers);
    }

    @Override
    public void addMessageListener(MessageListener messageListener) {
        this.messageListener = messageListener;
    }
}
