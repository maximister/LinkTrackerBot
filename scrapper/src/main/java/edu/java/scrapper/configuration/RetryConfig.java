package edu.java.scrapper.configuration;

import java.time.Duration;
import java.util.List;
import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "retry-config", ignoreUnknownFields = false)
public record RetryConfig(
    List<RetryInfo> retries
) {
    public record RetryInfo(
        String client,
        String type,
        int maxAttempts,
        long step,
        Duration delay,
        List<Integer> codes
    ) {
    }
}
