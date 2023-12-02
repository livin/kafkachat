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

1. Clone repository:
   ```bash
   git clone https://github.com/livin/kafkachat.git
   ```
2. Build with Gradle:
   ```
   cd kafkachat
   ./gradlew installDist
   ```
3. Cd into installation dir:
   ```bash
   cd build/install/kafkachat
   ```
4. Prepare your `kafka.properties` with connection parameters. it usually resides
in your cluster configuration and looks like following:
   ```
   bootstrap.servers=kafka.yourserver.com:9092
   sasl.mechanism=SCRAM-SHA-256
   security.protocol=SASL_SSL
   sasl.jaas.config=org.apache.kafka.common.security.scram.ScramLoginModule required \
      username="******" \
      password="******";
   ```
5. Put your kafka configuration `kafka.properties`
   into one of locations:
   1. Kafkachat user's config dir: `~/.kafkachat/kafka.properties`.
   2. Current working directory from which you run the app.
   
6. Run the app:
   ```bash
   bin/kafkachat
   ```
7. Optionally symlink the `kafkachat` app into one of directory in your `$PATH`, e.g.:
   ```bash
   mkdir $HOME/apps/bin
   ln -s $HOME/kafkachat/build/install/kafkachat/bin/kafkachat $HOME/apps/bin
   export PATH="$PATH:$HOME/apps/bin"
   ```