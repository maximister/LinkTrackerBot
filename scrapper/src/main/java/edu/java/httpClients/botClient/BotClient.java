package edu.java.httpClients.botClient;

import edu.java.model.botClientDto.LinkUpdate;

public interface BotClient {
    void sendMessage(LinkUpdate linkUpdateRequest);
}
