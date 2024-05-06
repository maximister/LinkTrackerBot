package edu.java.bot.service.linksUpdateService;

import com.pengrad.telegrambot.request.SendMessage;
import edu.java.bot.model.controllerDto.LinkUpdate;
import edu.java.bot.sender.BotMessageSender;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

@Component
public class BotLinkUpdateService implements LinksUpdateService {
    private final BotMessageSender sender;

    public BotLinkUpdateService(BotMessageSender sender) {
        this.sender = sender;
    }

    @Override
    public ResponseEntity<Void> updateLink(LinkUpdate linkUpdateRequest) {
        for (Long chatId: linkUpdateRequest.tgChatIds()) {
            sender.sendMessage(new SendMessage(chatId, linkUpdateRequest.description()));
        }
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
