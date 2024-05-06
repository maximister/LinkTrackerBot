package edu.java.scrapper.service.botSender;

import edu.java.scrapper.httpClients.botClient.BotClient;
import edu.java.scrapper.model.botClientDto.LinkUpdate;
import lombok.AllArgsConstructor;

@AllArgsConstructor
public class HttpClientUpdateSender implements BotUpdateSender {
    private final BotClient botClient;

    @Override
    public void sendUpdate(LinkUpdate update) {
        botClient.sendMessage(update);
    }
}
