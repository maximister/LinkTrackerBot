package edu.java.bot.comand;

import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.request.SendMessage;
import edu.java.bot.model.Link;
import edu.java.bot.model.ParsedCommand;
import edu.java.bot.service.linkService.LinkService;
import edu.java.bot.utils.CommandUtils;
import org.springframework.stereotype.Component;

@Component
public final class UntrackCommand extends AbstractCommand {
    private final LinkService service;

    private final static String COMMAND_NAME = "untrack";
    private final static String DESCRIPTION = "Removes link from your track list. Example of using: /untrack <link>";
    private static final String INVALID_UNTRACK_MESSAGE = "Your link is invalid. Please try again";
    private static final String NO_SUCH_LINK_MESSAGE = "There is no such link in your list. Please try again";
    private static final String CORRECT_UNTRACK_MESSAGE = "This link was successfully deleted";

    public UntrackCommand(LinkService service) {
        super(COMMAND_NAME, DESCRIPTION);
        this.service = service;
    }

    @Override
    public SendMessage handle(Update update) {
        logMessage(update);
        String userMessage = update.message().text();

        ParsedCommand parsedCommand = CommandUtils.parseCommand(userMessage);
        if (parsedCommand.arguments().isEmpty()
            || !CommandUtils.isLinkValid(parsedCommand.arguments().getFirst())) {
            return new SendMessage(update.message().chat().id(), INVALID_UNTRACK_MESSAGE);
        }

        Link link =
            service.getLinkByUrl(update.message().from().id(), parsedCommand.arguments().getFirst());
        if (link == null) {
            return new SendMessage(update.message().chat().id(), NO_SUCH_LINK_MESSAGE);
        }

        service.untrackLink(update.message().from().id(), link.linkId());
        return new SendMessage(update.message().chat().id(), CORRECT_UNTRACK_MESSAGE);
    }
}
