package edu.java.bot.utils;

import edu.java.bot.model.ParsedCommand;
import java.util.Arrays;
import java.util.stream.Collectors;
import lombok.experimental.UtilityClass;

@UtilityClass
public class CommandUtils {
    private static final String COMMAND_PREFIX = "/";

    public static boolean isCommand(String message) {
        return message.startsWith(COMMAND_PREFIX);
    }

    public static ParsedCommand parseCommand(String message) {
        String[] splittedMessage = message.split(" ");

        return new ParsedCommand(
            splittedMessage[0],
            Arrays.stream(splittedMessage)
                .skip(1)
                .collect(Collectors.toList())
        );
    }

    public static boolean isLinkValid(String link) {
        return link.startsWith("http://") || link.startsWith("https://");
    }
}
