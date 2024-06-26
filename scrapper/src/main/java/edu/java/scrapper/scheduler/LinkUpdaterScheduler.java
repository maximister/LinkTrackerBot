package edu.java.scrapper.scheduler;

import edu.java.scrapper.exceptions.UnsupportedLinkException;
import edu.java.scrapper.httpClients.LinkInfo;
import edu.java.scrapper.httpClients.LinkProviderService;
import edu.java.scrapper.model.botClientDto.LinkUpdate;
import edu.java.scrapper.model.domainDto.Link;
import edu.java.scrapper.service.LinkService;
import edu.java.scrapper.service.botSender.BotUpdateSender;
import java.time.Duration;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@EnableScheduling
@Slf4j
@Component
public class LinkUpdaterScheduler {
    //Я же правильно понимаю, что это сервисный слой тоже, поэтому
    //обращаться напрямую к репозиторию допустимо
    @Value("#{@'app-edu.java.scrapper.configuration.ApplicationConfig'.scheduler.forceCheckDelay}")
    private Duration forceCheckDelay;
    @Value("#{@'app-edu.java.scrapper.configuration.ApplicationConfig'.scheduler.enable}")
    private boolean isEnable;
    private final BotUpdateSender botUpdateSender;
    private final List<LinkProviderService> providers;
    private final LinkService linkService;

    public LinkUpdaterScheduler(
        List<LinkProviderService> providers,
        LinkService linkService,
        BotUpdateSender botUpdateSender
    ) {
        this.botUpdateSender = botUpdateSender;
        this.providers = providers;
        this.linkService = linkService;
    }

    @Scheduled(fixedDelayString = "#{@'app-edu.java.scrapper.configuration.ApplicationConfig'.scheduler.interval}")
    @Transactional
    public void update() {
        if (!isEnable) {
            log.warn("Scheduler is unavailable");
            return;
        }

        List<Link> linksForUpdate = linkService.getLinksForUpdate(forceCheckDelay);

        for (Link link : linksForUpdate) {
            LinkProviderService client = providers.stream()
                .filter(p -> p.isValid(link.url()))
                .findFirst()
                .orElseThrow(() -> {
                    log.error("Unsupported link {}. There are no clients for this type", link.url());
                    return new UnsupportedLinkException(link.url());
                });

            List<LinkInfo> updates = client.fetch(link.url());
            if (updates.isEmpty()) {
                log.warn("Scheduler has gotten empty answer");
            } else {
                for (LinkInfo update : updates) {
                    if (update.lastModified().isAfter(link.lastUpdate())) {
                        LinkUpdate botUpdate = linkService.updateLink(update);
                        botUpdateSender.sendUpdate(botUpdate);
                        log.info("Scheduler has updated link {}", link.url());
                    }
                }
            }
        }
    }
}
