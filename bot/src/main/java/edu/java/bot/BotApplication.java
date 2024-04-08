package edu.java.bot;

import edu.java.bot.configuration.ApplicationConfig;
import edu.java.bot.configuration.KafkaConsumerProperties;
import edu.java.bot.configuration.KafkaProducerProperties;
import edu.java.bot.configuration.KafkaTopicProperties;
import edu.java.bot.configuration.RetryConfig;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.cache.annotation.EnableCaching;

@EnableConfigurationProperties({ApplicationConfig.class, RetryConfig.class, KafkaTopicProperties.class,
    KafkaProducerProperties.class, KafkaConsumerProperties.class})
@EnableCaching
@SpringBootApplication(exclude = {DataSourceAutoConfiguration.class})
public class BotApplication {
    public static void main(String[] args) {
        SpringApplication.run(BotApplication.class, args);
    }
}
