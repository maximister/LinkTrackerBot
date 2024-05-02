package edu.java.bot.configuration;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "kafka.consumer")
public record KafkaConsumerProperties(
    String bootstrapServers,
    String groupId,
    String autoOffsetReset,
    Integer maxPollIntervalMs,
    boolean enableAutoCommit,
    Integer concurrency
) {}

