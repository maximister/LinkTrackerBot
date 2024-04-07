package edu.java.bot.configuration;

import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.TopicBuilder;

@Configuration
public class KafkaConfig {
    @Bean
    public NewTopic topic(KafkaTopicProperties topicProperties) {
        return TopicBuilder.name(topicProperties.getName() + "_dlq")
            .partitions(topicProperties.getPartitions())
            .replicas(topicProperties.getReplicas())
            .build();
    }
}
