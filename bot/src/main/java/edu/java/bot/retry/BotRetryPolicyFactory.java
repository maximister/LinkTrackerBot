package edu.java.bot.retry;

import edu.java.bot.configuration.RetryConfig;
import edu.java.bot.exceptions.ServerException;
import edu.java.scrapper.retry.LinearRetry;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.function.Function;
import java.util.function.Predicate;
import lombok.experimental.UtilityClass;
import org.springframework.http.HttpStatus;
import org.springframework.web.reactive.function.client.ExchangeFilterFunction;
import org.springframework.web.reactive.function.client.WebClientResponseException;
import reactor.core.publisher.Mono;
import reactor.util.retry.Retry;
import reactor.util.retry.RetryBackoffSpec;

@UtilityClass
public class BotRetryPolicyFactory {
    private final static Map<String, Function<RetryConfig.RetryInfo, Retry>> RETRIES = new HashMap<>();
    private static final String ERROR_MESSAGE = "Sorry, unexpected error :(";

    static {
        RETRIES.put(
            "fixed",
            retry -> RetryBackoffSpec.fixedDelay(retry.maxAttempts(), retry.delay())
                .filter(getErrorList(retry.codes()))
                .onRetryExhaustedThrow((retryBackoffSpec, retrySignal) -> new ServerException(
                    ERROR_MESSAGE,
                    "",
                    HttpStatus.INTERNAL_SERVER_ERROR
                ))
        );
        RETRIES.put(
            "linear",
            retry -> new LinearRetry(
                retry.delay(),
                retry.maxAttempts(),
                retry.step(),
                getErrorList(retry.codes()),
                new ServerException(ERROR_MESSAGE, "", HttpStatus.INTERNAL_SERVER_ERROR)
            )
        );
        RETRIES.put(
            "exponential",
            retry -> RetryBackoffSpec.backoff(
                    retry.maxAttempts(),
                    retry.delay()
                ).jitter(0.0)
                .filter(getErrorList(retry.codes()))
                .onRetryExhaustedThrow((retryBackoffSpec, retrySignal) -> new ServerException(
                    ERROR_MESSAGE,
                    "",
                    HttpStatus.INTERNAL_SERVER_ERROR
                ))
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

