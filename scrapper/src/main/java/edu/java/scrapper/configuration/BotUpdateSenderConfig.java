package edu.java.scrapper.configuration;

import edu.java.scrapper.httpClients.botClient.BotClient;
import edu.java.scrapper.model.botClientDto.LinkUpdate;
import edu.java.scrapper.service.botSender.BotUpdateSender;
import edu.java.scrapper.service.botSender.HttpClientUpdateSender;
import edu.java.scrapper.service.botSender.ScrapperLinkUpdateQueueProducer;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.core.KafkaTemplate;

@Configuration
public class BotUpdateSenderConfig {
    @Bean
    public BotUpdateSender botUpdateSender(
        ApplicationConfig config,
        KafkaTopicProperties topicProperties,
        KafkaTemplate<String, LinkUpdate> kafkaTemplate,
        @Qualifier("webBotClient") BotClient client
    ) {
        return (config.useQueue())
            ? new ScrapperLinkUpdateQueueProducer(kafkaTemplate, topicProperties)
            : new HttpClientUpdateSender(client);
    }
}
