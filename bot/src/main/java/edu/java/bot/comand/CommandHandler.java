package edu.java.bot.comand;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class CommandHandler {
    private final List<Command> commands;
    private HelpCommand help;

    public CommandHandler(List<Command> commands) {
        this.commands = new ArrayList<>();

        for (Command command : commands) {
            if (!(command instanceof HelpCommand)) {
                addCommand(command);
            } else {
                this.help = (HelpCommand) command;
                addCommand(command);
            }
        }

        if (help == null) {
            help = new HelpCommand();
            commands.forEach(c -> help.updateMessage(c));
        }
    }

    public List<Command> getCommands() {
        return Collections.unmodifiableList(commands);
    }

    public void addCommand(Command command) {
        log.info("new command {} was added", command.command());
        if (!commands.contains(command)) {
            commands.add(command);
            help.updateMessage(command);
        }
    }
}
