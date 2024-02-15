package edu.java.bot.command;

import com.pengrad.telegrambot.model.Chat;
import com.pengrad.telegrambot.model.Message;
import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.request.SendMessage;
import edu.java.bot.comand.HelpCommand;
import edu.java.bot.comand.ListCommand;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import static org.assertj.core.api.Assertions.assertThat;

public class HelpCommandTest {
    private static Update update;
    private static Message message;
    private static Chat chat;

    private final static String DEFAULT_MESSAGE = """
        Welcome to Link Tracker Bot
        You can interact with me using following commands:
        """;
    private final static String UPDATED_MESSAGE = """
        Welcome to Link Tracker Bot
        You can interact with me using following commands:
        /list — Prints all tracked links
        """;

    @BeforeAll
    public static void initialize() {
        update = Mockito.mock(Update.class);
        message = Mockito.mock(Message.class);
        chat = Mockito.mock(Chat.class);

        Mockito.when(update.message()).thenReturn(message);
        Mockito.when(message.chat()).thenReturn(chat);
        Mockito.when(chat.id()).thenReturn(1L);

    }

    @Test
    @DisplayName("Handle message with empty help list")
    public void HelpCommandTest_ShouldReturnDefaultMessage() {
        SendMessage answer = new HelpCommand().handle(update);

        assertThat(answer.getParameters().get("text").equals(DEFAULT_MESSAGE)).isTrue();
        assertThat(answer.getParameters().get("chat_id").equals(1L)).isTrue();
    }

    @Test
    @DisplayName("testing update method")
    public void HelpCommandTest_ShouldUpdateDefaultMessage() {
        HelpCommand helpCommand = new HelpCommand();
        helpCommand.updateMessage(new ListCommand(null));

        SendMessage answer = helpCommand.handle(update);
        assertThat(answer.getParameters().get("text").equals(UPDATED_MESSAGE)).isTrue();
    }

}
