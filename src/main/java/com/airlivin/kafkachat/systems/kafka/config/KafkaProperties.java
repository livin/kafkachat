package com.airlivin.kafkachat.systems.kafka.config;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.Properties;

public class KafkaProperties extends Properties {

    String kafkaPropsUser = System.getProperty("user.home") + "/.kafkachat/kafka.properties";
    private final List<String> kafkaPropsFiles  = List.of(
                "kafka.properties",
                kafkaPropsUser
            );

    public KafkaProperties() {
        init();
    }

    private void loadKafkaProps() {
        try {
            loadFromResource( "kafka-base.properties");
            if (!loadKafkaPropsFromFiles()) {
                throw new IllegalStateException("Can't load kafka.properties. Place it in current directory or in " +
                        kafkaPropsUser);
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        //


    }

    private boolean loadKafkaPropsFromFiles() throws IOException {
        return kafkaPropsFiles.stream()
                .map(this::loadFromFile)
                .filter(x -> x)
                .findFirst()
                .orElse(false);
    }

    private boolean loadFromFile(String filename) {
        var aFile = new File(filename);
        if (aFile.exists()) {
            try (InputStream is = new FileInputStream(aFile)) {
                    this.load(is);
            } catch (IOException e) {
                return false;
            }
            return true;
        }
        return false;
    }

    private void loadFromResource(String name) throws IOException {
        try (InputStream is = KafkaProperties.class.getResourceAsStream(name)) {
            this.load(is);
        }
    }

    private void init() {
        loadKafkaProps();
    }
}
