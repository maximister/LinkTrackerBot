package edu.java.bot.comand;

import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.request.SendMessage;
import edu.java.bot.service.userService.UserService;
import org.springframework.stereotype.Component;

@Component
public final class StartCommand extends AbstractCommand {

    private final UserService service;

    public StartCommand(UserService userService) {
        super(COMMAND_NAME, DESCRIPTION);
        this.service = userService;
    }

    private final static String COMMAND_NAME = "start";
    private final static String DESCRIPTION = "Starts bot work";
    private final static String DEFAULT_MESSAGE = """
        Welcome to Link Tracker Bot
        You can interact with me using commands described in the /help command
        """;

    @Override
    public SendMessage handle(Update update) {
        logMessage(update);
        service.registration(update.message().from().id());
        return new SendMessage(update.message().chat().id(), DEFAULT_MESSAGE);
    }
}
