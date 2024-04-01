package edu.java.scrapper.retry;

import java.time.Duration;
import java.util.function.Predicate;
import org.reactivestreams.Publisher;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;
import reactor.util.retry.Retry;

public class LinearRetry extends Retry {
    public final Duration minBackoff;
    public final Duration maxBackoff = Duration.ofMillis(Long.MAX_VALUE);
    public final long maxAttempts;
    public final long step;
    public final Predicate<Throwable> errorFilter;
    public final RuntimeException apiException;

    public LinearRetry(
        Duration minBackoff,
        long maxAttempts,
        long step,
        Predicate<Throwable> errorFilter,
        RuntimeException apiException
    ) {
        this.minBackoff = minBackoff;
        this.maxAttempts = maxAttempts;
        this.step = step;
        this.errorFilter = errorFilter;
        this.apiException = apiException;
    }

    @Override
    public Publisher<?> generateCompanion(Flux<RetrySignal> retrySignals) {
        return Flux.deferContextual((cv) -> retrySignals.contextWrite(cv).concatMap((retryWhenState) -> {
            RetrySignal copy = retryWhenState.copy();
            Throwable currentFailure = copy.failure();
            long iteration = copy.totalRetries();

            if (currentFailure == null) {
                return Mono.error(
                    new IllegalStateException("Retry.RetrySignal#failure() not expected to be null")
                );
            } else if (!this.errorFilter.test(currentFailure)) {
                return Mono.error(currentFailure);
            } else if (iteration >= this.maxAttempts) {
                return Mono.error(apiException);
            }

            Duration nextBackoff;
            try {
                nextBackoff = this.minBackoff.multipliedBy(iteration * step);
                if (nextBackoff.compareTo(this.maxBackoff) > 0) {
                    nextBackoff = this.maxBackoff;
                }
            } catch (ArithmeticException e) {
                nextBackoff = this.maxBackoff;
            }
            return Mono.delay(nextBackoff, Schedulers.parallel()).contextWrite(cv);

        }).onErrorStop());
    }
}
