package edu.java.bot;

import com.pengrad.telegrambot.model.BotCommand;
import com.pengrad.telegrambot.request.SetMyCommands;
import edu.java.bot.comand.Command;
import edu.java.bot.comand.CommandHandler;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class CommandMenuBuilder {
    private final CommandHandler commandHandler;

    public CommandMenuBuilder(CommandHandler commandHandler) {
        this.commandHandler = commandHandler;
    }

    public SetMyCommands buildCommandMenu() {
        return new SetMyCommands(
            commandHandler.getCommands().stream()
                .map(Command::toApiCommand)
                .toArray(BotCommand[]::new)
        );
    }
}
