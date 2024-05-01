package edu.java.scrapper.httpClients;

import edu.java.scrapper.configuration.RetryConfig;
import edu.java.scrapper.retry.RetryPolicyFactory;
import java.time.Duration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.web.reactive.function.client.WebClient;

public abstract class LinkProviderWebService implements LinkProviderService {
    protected WebClient webClient;
    protected static final int RETRY_MAX_ATTEMPTS = 3;
    protected static final Duration RETRY_DURATION = Duration.ofMillis(100);
    protected Logger log = LoggerFactory.getLogger(LinkProviderWebService.class);

    public LinkProviderWebService(String baseUrl, RetryConfig config) {
        webClient = WebClient.builder()
            .baseUrl(baseUrl)
            .filter(RetryPolicyFactory.createFilter(RetryPolicyFactory.createRetry(getName(), config)))
            .build();
    }

    protected <T> T doRequest(String uri, Class<T> dtoClass, T onErrorValue, HttpHeaders headers) {
        return webClient.get()
            .uri(uri)
            .accept(MediaType.APPLICATION_JSON)
            .headers(h -> h.putAll(headers))
            .accept()
            .retrieve()
            .bodyToMono(dtoClass)
            .doOnError(error -> log.error("An error has occurred {}", error.getMessage()))
            .onErrorReturn(onErrorValue)
            .block();
    }

    protected abstract String getName();
}
