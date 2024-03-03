package edu.java.bot.comand;

import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.request.SendMessage;
import org.springframework.stereotype.Component;

@Component
public final class HelpCommand extends AbstractCommand {
    private String message;
    private final static String COMMAND_NAME = "help";
    private final static String DESCRIPTION = "Prints help message with all allowed commands";
    private final static String DEFAULT_MESSAGE = """
        Welcome to Link Tracker Bot
        You can interact with me using following commands:
        """;

    public HelpCommand() {
        super(COMMAND_NAME, DESCRIPTION);
        this.message = DEFAULT_MESSAGE;
    }

    @Override
    public SendMessage handle(Update update) {
        logMessage(update);
        return new SendMessage(update.message().chat().id(), message);
    }

    public void updateMessage(Command newCommand) {
        StringBuilder sb = new StringBuilder(message);
        sb.append(newCommand.command())
            .append(" — ")
            .append(newCommand.description())
            .append('\n');

        message = sb.toString();
    }
}
