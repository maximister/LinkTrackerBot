package edu.java.scrapper.repository.jdbc;

import edu.java.scrapper.IntegrationEnvironment;
import edu.java.scrapper.repository.ChatRepository;
import edu.java.scrapper.repository.LinkRepository;
import edu.java.scrapper.repository.jdbcRepository.JdbcChatLinkRepository;
import java.net.URI;
import java.util.List;
import lombok.SneakyThrows;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.JdbcTemplate;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class JdbcChatLinkRepositoryTest extends IntegrationEnvironment {
    @Autowired
    private JdbcChatLinkRepository repository;
    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Autowired
    private LinkRepository linkRepository;
    @Autowired
    private ChatRepository chatRepository;

    @SneakyThrows
    @BeforeEach
    private void fillTable() {
        //fill chats
        chatRepository.addChat(1);
        chatRepository.addChat(2);
        chatRepository.addChat(3);

        //fill links
        linkRepository.addLink(new URI("test1.com"));
        linkRepository.addLink(new URI("test2.com"));
        linkRepository.addLink(new URI("test3.com"));
        linkRepository.addLink(new URI("test4.com"));
        linkRepository.addLink(new URI("test5.com"));

        repository.addChatLinkConnection(1, 1);
        repository.addChatLinkConnection(1, 2);
        repository.addChatLinkConnection(1, 3);
        repository.addChatLinkConnection(2, 2);
        repository.addChatLinkConnection(3, 4);
    }

    @AfterEach
    void cleanUp() {
        jdbcTemplate.update("TRUNCATE TABLE chat_link, link, chat RESTART IDENTITY CASCADE");
    }


    @Test
    @DisplayName("testing get links by Chat id method")
    public void getLinkIdsByChatId_shouldWorkCorrectly() {
        List<Integer> result = repository.getLinkIdsByChatId(1);
        List<Integer> expected = List.of(1, 2, 3);

        assertEquals(result, expected);
    }

    @Test
    @DisplayName("testing get chats by link id method")
    public void getChatIdsByLinkId_shouldWorkCorrectly() {
        List<Integer> result = repository.getChatIdsByLinkId(2);
        List<Integer> expected = List.of(1, 2);

        assertEquals(result, expected);
    }

    @Test
    @DisplayName("testing is link tracked method with tracked link")
    public void isLinkTracked_shouldReturnTrue() {
        boolean isExists = repository.isLinkTracked(1);

        assertTrue(isExists);
    }

    @Test
    @DisplayName("testing is link tracked method with untracked link")
    public void isLinkTracked_shouldReturnFalse() {
        boolean isExists = repository.isLinkTracked(5);

        assertFalse(isExists);
    }


    @Test
    @DisplayName("testing delete connection method")
    public void deleteChatLinkConnection_shouldWorkCorrectly() {
        repository.deleteChatLinkConnection(1, 2);

        List<Integer> resultLinkList = repository.getLinkIdsByChatId(1);
        List<Integer> expectedLinkList = List.of(1, 3);

        List<Integer> resultChatList = repository.getChatIdsByLinkId(2);
        List<Integer> expectedChatList = List.of(2);

        assertAll(
            () -> assertEquals(resultChatList, expectedChatList),
            () -> assertEquals(resultLinkList, expectedLinkList)
        );
    }
}