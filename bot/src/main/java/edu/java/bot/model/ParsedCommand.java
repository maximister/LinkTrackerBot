package edu.java.bot.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public record ParsedCommand(String command, List<String> arguments) {
    public ParsedCommand(String command, List<String> arguments) {
        this.command = command;

        if (arguments != null) {
            this.arguments = new ArrayList<>(arguments);
        } else {
            this.arguments = Collections.emptyList();
        }
    }

    @Override public List<String> arguments() {
        return Collections.unmodifiableList(arguments);
    }
}
