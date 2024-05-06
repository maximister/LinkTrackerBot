package edu.java.scrapper.retry;

import edu.java.scrapper.configuration.RetryConfig;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.function.Function;
import java.util.function.Predicate;
import lombok.experimental.UtilityClass;
import org.springframework.retry.ExhaustedRetryException;
import org.springframework.web.reactive.function.client.ExchangeFilterFunction;
import org.springframework.web.reactive.function.client.WebClientResponseException;
import reactor.core.publisher.Mono;
import reactor.util.retry.Retry;
import reactor.util.retry.RetryBackoffSpec;

@UtilityClass
public class RetryPolicyFactory {
    private final static Map<String, Function<RetryConfig.RetryInfo, Retry>> RETRIES = new HashMap<>();

    static {
        RETRIES.put(
            "fixed",
            retry -> RetryBackoffSpec.fixedDelay(retry.maxAttempts(), retry.delay())
                .filter(getErrorList(retry.codes()))
                .onRetryExhaustedThrow((retryBackoffSpec, retrySignal) -> new RuntimeException())
        );
        RETRIES.put(
            "linear",
            retry -> new LinearRetry(
                retry.delay(),
                retry.maxAttempts(),
                retry.step(),
                getErrorList(retry.codes()),
                new ExhaustedRetryException("Retry exhausted")
            )
        );
        RETRIES.put(
            "exponential",
            retry -> RetryBackoffSpec.backoff(
                    retry.maxAttempts(),
                    retry.delay()
                ).jitter(0.0)
                .filter(getErrorList(retry.codes()))
                .onRetryExhaustedThrow((retryBackoffSpec, retrySignal) -> new RuntimeException())
        );
    }

    public static ExchangeFilterFunction createFilter(Retry retry) {
        return (request, next) -> next.exchange(request)
            .flatMap(clientResponse -> Mono.just(clientResponse)
                .filter(response -> clientResponse.statusCode().isError())
                .flatMap(response -> clientResponse.createException())
                .flatMap(Mono::error)
                .thenReturn(clientResponse))
            .retryWhen(retry);
    }

    public static Retry createRetry(String client, RetryConfig retryConfig) {
        return retryConfig
            .retries()
            .stream()
            .filter(retry -> retry.client().equals(client))
            .findFirst()
            .map(retry -> RETRIES.get(retry.type()).apply(retry))
            .orElseThrow(IllegalStateException::new);
    }

    private Predicate<Throwable> getErrorList(Set<Integer> codes) {
        return t -> {
            if (t instanceof WebClientResponseException e) {
                return codes.contains(e.getStatusCode().value());
            }
            return true;
        };
    }
}
