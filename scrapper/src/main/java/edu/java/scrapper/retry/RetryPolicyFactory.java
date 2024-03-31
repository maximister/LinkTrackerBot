package edu.java.scrapper.retry;

import edu.java.scrapper.configuration.RetryConfig;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.function.Predicate;
import lombok.experimental.UtilityClass;
import org.springframework.web.reactive.function.client.WebClientResponseException;
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
        );
        RETRIES.put(
            "linear",
            retry -> new LinearRetry(
                retry.delay(),
                retry.maxAttempts(),
                retry.step(),
                getErrorList(retry.codes())
            )
        );
        RETRIES.put(
            "exponential",
            retry -> RetryBackoffSpec.backoff(
                    retry.maxAttempts(),
                    retry.delay()
                ).jitter(0.0)
                .filter(getErrorList(retry.codes()))
        );
    }

    private Predicate<Throwable> getErrorList(List<Integer> codes) {
        return t -> {
            if (t instanceof WebClientResponseException e) {
                return codes.contains(e.getStatusCode().value());
            }
            return true;
        };
    }
}
