package edu.java.scrapper.service.botSender;

import edu.java.scrapper.configuration.KafkaTopicProperties;
import edu.java.scrapper.model.botClientDto.LinkUpdate;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;

@AllArgsConstructor
@Slf4j
public class ScrapperLinkUpdateQueueProducer implements BotUpdateSender {
    private final KafkaTemplate<String, LinkUpdate> kafkaTemplate;
    private final KafkaTopicProperties topicProperties;

    @Override
    public void sendUpdate(LinkUpdate update) {
        try {
            kafkaTemplate.send(topicProperties.getName(), update).whenComplete(
                (stringLinkUpdateSendResult, throwable) -> {
                    if (throwable != null) {
                        log.error("Error in queue producer: ", throwable);
                    } else {
                        log.info("Successfully sent message {} to the queue", update);
                    }
                }
            );
        } catch (Exception ex) {
            log.error("Unexpected error during sending message to the queue: ", ex);
        }
    }
}
