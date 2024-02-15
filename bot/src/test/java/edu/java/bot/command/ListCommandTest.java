package edu.java.bot.command;

import com.pengrad.telegrambot.model.Chat;
import com.pengrad.telegrambot.model.Message;
import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.model.User;
import com.pengrad.telegrambot.request.SendMessage;
import edu.java.bot.comand.ListCommand;
import edu.java.bot.model.Link;
import edu.java.bot.service.linkService.LinkService;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import java.util.Collections;
import java.util.List;
import static org.assertj.core.api.Assertions.assertThat;

public class ListCommandTest {
    private static Update update;
    private static Message message;
    private static Chat chat;
    private static User user;
    private static LinkService service;

    private static final List<Link> LINKS = List.of(
        new Link(1, "https://link1"),
        new Link(2, "https://link2"),
        new Link(3, "https://link3")
    );

    private final static String DEFAULT_MESSAGE = "There are no any tracked links";
    private final static String UPDATED_MESSAGE = """
        Your links:
        — https://link1
        — https://link2
        — https://link3
        """;

    @BeforeAll
    public static void initialize() {
        update = Mockito.mock(Update.class);
        message = Mockito.mock(Message.class);
        chat = Mockito.mock(Chat.class);
        user = Mockito.mock(User.class);
        service = Mockito.mock(LinkService.class);

        Mockito.when(update.message()).thenReturn(message);
        Mockito.when(message.chat()).thenReturn(chat);
        Mockito.when(message.from()).thenReturn(user);

        Mockito.when(service.getLinks(1L)).thenReturn(Collections.emptyList());
        Mockito.when(service.getLinks(2L)).thenReturn(LINKS);
    }

    @Test
    @DisplayName("Handle message with empty link list")
    public void ListCommandTest_ShouldReturnDefaultMessage() {
        Mockito.when(user.id()).thenReturn(1L);
        Mockito.when(chat.id()).thenReturn(1L);
        SendMessage answer = new ListCommand(service).handle(update);

        assertThat(answer.getParameters().get("text").equals(DEFAULT_MESSAGE)).isTrue();
    }

    @Test
    @DisplayName("testing list command with some links in list")
    public void ListCommandTest_ShouldReturnUpdatedMessage() {
        Mockito.when(user.id()).thenReturn(2L);
        Mockito.when(chat.id()).thenReturn(2L);
        ListCommand listCommand = new ListCommand(service);

        SendMessage answer = listCommand.handle(update);
        assertThat(answer.getParameters().get("text").equals(UPDATED_MESSAGE)).isTrue();
    }

}
