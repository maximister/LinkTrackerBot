package edu.java.bot.raleLimiting;

import java.util.Set;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class LimitService {
    private final Set<String> whitelist;

    public LimitService(@Value("${rate-limiter.whitelist}") Set<String> whitelist) {
        this.whitelist = whitelist;
    }

    public boolean isSkipped(String ip) {
        return whitelist.contains(ip);
    }
}
