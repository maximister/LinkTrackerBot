package edu.java.bot.comand;

import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.request.SendMessage;
import edu.java.bot.model.ParsedCommand;
import edu.java.bot.service.linkService.LinkService;
import edu.java.bot.utils.CommandUtils;
import org.springframework.stereotype.Component;

@Component
public final class TrackCommand extends AbstractCommand {
    private final LinkService service;

    private final static String COMMAND_NAME = "track";
    private final static String DESCRIPTION = "Start tracking link. Example of using: /track <link>";
    private static final String  INVALID_LINK_MESSAGE = "Your link is invalid. Please try again";
    private static final String  CORRECT_LINK_MESSAGE = "Started tracking your link";

    public TrackCommand(LinkService service) {
        super(COMMAND_NAME, DESCRIPTION);
        this.service = service;
    }

    @Override
    public SendMessage handle(Update update) {
        logMessage(update);
        Long chatId = update.message().chat().id();
        String userMessage = update.message().text();

        ParsedCommand parsedCommand = CommandUtils.parseCommand(userMessage);
        if (parsedCommand.arguments().isEmpty()
            || !CommandUtils.isLinkValid(parsedCommand.arguments().getFirst())) {
            return new SendMessage(chatId, INVALID_LINK_MESSAGE);
        }

        service.trackLink(update.message().from().id(), parsedCommand.arguments().getFirst());
        return new SendMessage(chatId, CORRECT_LINK_MESSAGE);
    }
}
