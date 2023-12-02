plugins {
    id("application")
    id("java")
}

group = "com.airlivin.kafkachat"
version = "1.0-SNAPSHOT"


repositories {
    mavenCentral()
}

dependencies {
    implementation("org.apache.kafka:kafka-clients:3.6.0")

    implementation("org.slf4j:slf4j-api:2.0.9")
    implementation("org.apache.logging.log4j:log4j-api:2.22.0")
    implementation("org.apache.logging.log4j:log4j-core:2.22.0")

    implementation("org.apache.logging.log4j:log4j-slf4j2-impl:2.22.0")

    //implementation("org.slf4j:slf4j-simple:2.0.9")

    testImplementation(platform("org.junit:junit-bom:5.9.1"))
    testImplementation("org.junit.jupiter:junit-jupiter")
}

application {
    mainClass = "com.airlivin.kafkachat.KafkaChat"
}

val run: JavaExec by tasks
    run.standardInput = System.`in`

tasks.test {
    useJUnitPlatform()
}