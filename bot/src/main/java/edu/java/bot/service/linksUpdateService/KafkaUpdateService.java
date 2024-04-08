package edu.java.bot.service.linksUpdateService;

import edu.java.bot.model.controllerDto.LinkUpdate;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.kafka.annotation.DltHandler;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.annotation.RetryableTopic;
import org.springframework.kafka.retrytopic.DltStrategy;
import org.springframework.kafka.support.Acknowledgment;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Log4j2
public class KafkaUpdateService {
    private final LinksUpdateService updateService;

    @RetryableTopic(
        attempts = "1",
        dltStrategy = DltStrategy.FAIL_ON_ERROR,
        dltTopicSuffix = "_dlq")
    @KafkaListener(topics = "${kafka-topic.name}", containerFactory = "containerFactory")
    public void consume(@Payload LinkUpdate update, Acknowledgment acknowledgment) {
        log.info(String.format("#### -> Consumed message -> %s", update));
        updateService.updateLink(update);
        acknowledgment.acknowledge();
    }

    @DltHandler
    public void handleDltPayment(
        LinkUpdate linkUpdate, @Header(KafkaHeaders.RECEIVED_TOPIC) String topic
    ) {
        log.info("Event on dlt topic={}, payload={}", topic, linkUpdate);
    }
}
