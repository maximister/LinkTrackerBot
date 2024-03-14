package edu.java.scrapper.service.jdbcService;

import edu.java.scrapper.exceptions.ChatAlreadyRegisteredException;
import edu.java.scrapper.repository.ChatRepository;
import edu.java.scrapper.service.TgChatService;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.dao.DataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
public class JdbcChatService implements TgChatService {
    @Qualifier("JdbcChatRepository")
    private final ChatRepository repository;

    public JdbcChatService(ChatRepository repository) {
        this.repository = repository;
    }

    @Override
    public ResponseEntity<Void> addChat(Long id) {
        try {
            repository.addChat(id);
            return new ResponseEntity<>(HttpStatus.OK);
        } catch (DataAccessException e) {
            throw new ChatAlreadyRegisteredException(id);
        }
    }

    @Override
    public ResponseEntity<Void> deleteChat(Long id) {
        try {
            repository.deleteChat(id);
            return new ResponseEntity<>(HttpStatus.OK);
        } catch (DataAccessException e) {
            throw new ChatAlreadyRegisteredException(id);
        }
    }
}
