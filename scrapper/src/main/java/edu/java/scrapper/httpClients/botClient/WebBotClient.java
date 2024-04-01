package edu.java.scrapper.httpClients.botClient;

import edu.java.scrapper.configuration.RetryConfig;
import edu.java.scrapper.model.botClientDto.LinkUpdate;
import edu.java.scrapper.retry.RetryPolicyFactory;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.util.retry.Retry;

@Slf4j
@Component
public class WebBotClient implements BotClient {
    private final WebClient webClient;
    private static final String BASE_URL = "http://localhost:8090";
    private static final String UPDATES_ENDPOINT = "/updates";
    private final Retry retry;

    public WebBotClient(String baseUrl, RetryConfig retryConfig) {
        this.webClient = WebClient.create(baseUrl);
        retry = RetryPolicyFactory.createRetry("bot", retryConfig);
    }

    @Autowired
    public WebBotClient(RetryConfig retryConfig) {
        this(BASE_URL, retryConfig);
    }

    @Override
    public void sendMessage(LinkUpdate linkUpdateRequest) {
        webClient.post()
            .uri(UPDATES_ENDPOINT)
            .accept(MediaType.APPLICATION_JSON)
            .bodyValue(linkUpdateRequest)
            .retrieve()
            .bodyToMono(void.class)
            .retryWhen(retry)
            .doOnError(error -> log.error("An error has occurred {}", error.getMessage()))
            .block();
    }
}
