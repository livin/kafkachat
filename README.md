# Kafka Chat 💻💎

## What is it?
> Just run multi-user chat inside your CLI
> backed by any kafka instance with zero configuration

Here's how it looks:

[![asciicast](https://asciinema.org/a/624566.svg)](https://asciinema.org/a/624566)

### Business case
1. Learning to play with kafka.
2. Want to bring multiuser chat without any subscription or third-party application?

### Requirements
1. Kafka cluster/instance and its connection props.
    1. Local instance.
    2. Cloud-based cluster.
    3. Free (no-credit card options are available). Examples below:
       1. Upstash.com - sign up for free kafka cluster.
2. Kafka topic `chat` should be created inside your kafka cluster. Other topics supported as well. 
No extra configuration needed.
3. Java 11+ installed.

### Installation

1. Clone repository.
2. Get your kafka configuration (kafka.properties) and place in ~/.kafkachat/kafka.properties.
Optionally it can be put into current directory when running app.
3. Build with Gradle:
   ```
   ./gradlew installDist
   ```
4. Cd into installation dir:
   ```bash
   cd build/install/kafkachat
   ```
5. Run it:
   ```bash
   bin/kafkachat
   ```