package edu.java.scrapper.scheduler;

import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@EnableScheduling
@Slf4j
@Component
public final class LinkUpdaterScheduler {
    @Scheduled(fixedDelayString = "#{@'app-edu.java.scrapper.configuration.ApplicationConfig'.scheduler.interval}")
    public void update() {
        log.info("scheduler works");
    }
}