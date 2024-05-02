package edu.java.bot.configuration;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Setter
@Getter
@ConfigurationProperties("kafka-topic")
public class KafkaTopicProperties {
    private String name;
    private Integer partitions;
    private Integer replicas;
}
