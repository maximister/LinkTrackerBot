package edu.java.scrapper.repository.jooq;

import edu.java.scrapper.IntegrationEnvironment;
import edu.java.scrapper.model.domainDto.Chat;
import edu.java.scrapper.repository.jooqRepository.JooqChatRepository;
import java.util.List;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;
import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class JooqChatRepositoryTest extends IntegrationEnvironment {
    @Autowired
    private JooqChatRepository repository;
    @Autowired
    private JdbcTemplate jdbcTemplate;

    @AfterEach
    @BeforeEach
    void cleanUp() {
        jdbcTemplate.update("TRUNCATE TABLE chat_link, link, chat RESTART IDENTITY CASCADE");
    }

    @Test
    @DisplayName("Testing add chat method")
    public void addChat_shouldWorkCorrectly() {
        repository.addChat(1);

        assertThat(repository.getAllChats()).isEqualTo(List.of(new Chat(1)));
    }

    @Test
    @DisplayName("delete chat test")
    public void deleteChat_shouldWorkCorrectly() {
        repository.addChat(1);
        repository.deleteChat(1);

        assertThat(repository.findChatById(1)).isNull();
    }

    @Test
    @DisplayName("get all chats test")
    public void getAllChats_shouldWorkCorrectly() {
        repository.addChat(1);
        repository.addChat(2);
        repository.addChat(3);

        assertThat(repository.getAllChats())
            .isEqualTo(List.of(new Chat(1), new Chat(2), new Chat(3)));
    }
}
