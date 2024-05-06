package edu.java.scrapper.service.botSender;

import edu.java.scrapper.model.botClientDto.LinkUpdate;

public interface BotUpdateSender {
    void sendUpdate(LinkUpdate update);
}
