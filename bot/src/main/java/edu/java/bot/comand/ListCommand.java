package edu.java.bot.comand;

import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.request.SendMessage;
import edu.java.bot.model.Link;
import edu.java.bot.service.linkService.LinkService;
import java.util.List;
import org.springframework.stereotype.Component;

@Component
public final class ListCommand extends AbstractCommand {
    private final LinkService service;

    private final static String COMMAND_NAME = "list";
    private final static String DESCRIPTION = "Prints all tracked links";
    private final static String DEFAULT_MESSAGE = "Your links:\n";
    private final static String EMPTY_LIST_MESSAGE = "There are no any tracked links";

    public ListCommand(LinkService service) {
        super(COMMAND_NAME, DESCRIPTION);
        this.service = service;
    }

    @Override
    public SendMessage handle(Update update) {
        logMessage(update);
        List<Link> links = service.getLinks(update.message().from().id());

        if (links.isEmpty()) {
            return new SendMessage(update.message().chat().id(), EMPTY_LIST_MESSAGE);
        } else {
            StringBuilder message = new StringBuilder(DEFAULT_MESSAGE);

            for (Link link: links) {
                message.append("— ")
                    .append(link.url())
                    .append('\n');
            }

            return new SendMessage(update.message().chat().id(), message.toString());
        }
    }
}
