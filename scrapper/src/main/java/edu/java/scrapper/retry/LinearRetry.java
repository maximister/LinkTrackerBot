package edu.java.scrapper.retry;

import java.time.Duration;
import java.util.function.Predicate;
import org.reactivestreams.Publisher;
import reactor.core.publisher.Flux;
import reactor.util.retry.Retry;

public class LinearRetry extends Retry {
    public final Duration minBackoff;
    public final Duration maxBackoff = Duration.ofMillis(Long.MAX_VALUE);
    public final long maxAttempts;
    public final long step;
    public final Predicate<Throwable> errorFilter;

    public LinearRetry(Duration minBackoff, long maxAttempts, long step, Predicate<Throwable> errorFilter) {
        this.minBackoff = minBackoff;
        this.maxAttempts = maxAttempts;
        this.step = step;
        this.errorFilter = errorFilter;
    }

    @Override
    public Publisher<?> generateCompanion(Flux<RetrySignal> retrySignals) {
        return null;
    }
}
