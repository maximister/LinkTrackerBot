package edu.java.scrapper.repository.jdbcRepository;

import edu.java.scrapper.repository.ChatLinkRepository;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;

@Repository("JdbcChatLinkRepository")
public class JdbcChatLinkRepository implements ChatLinkRepository {
    private final JdbcTemplate jdbcTemplate;
    private final LinkIdRowMapper linkIdRowMapper;
    private final ChatIdRowMapper chatIdRowMapper;

    public JdbcChatLinkRepository(JdbcTemplate jdbcTemplate, LinkIdRowMapper linkIdRowMapper, ChatIdRowMapper chatIdRowMapper) {
        this.jdbcTemplate = jdbcTemplate;
        this.linkIdRowMapper = linkIdRowMapper;
        this.chatIdRowMapper = chatIdRowMapper;
    }


    @Override
    public List<Integer> getLinkIdsByChatId(long chatId) {
        String query = "SELECT link_id FROM chat_link WHERE chat_id = ?";
        return jdbcTemplate.query(query, linkIdRowMapper, chatId);
    }

    @Override
    public List<Integer> getChatIdsByLinkId(long linkId) {
        String query = "SELECT chat_id FROM chat_link WHERE link_id = ?";
        return jdbcTemplate.query(query, chatIdRowMapper, linkId);
    }

    @Override
    public boolean isLinkTracked(long linkId) {
        String query = "SELECT COUNT(*) FROM chat_link WHERE link_id = ?";
        return jdbcTemplate.queryForObject(query, Long.class, linkId).compareTo(0L) > 0;
    }

    @Override
    public void addChatLinkConnection(long chatId, long linkId) {
        String query = "INSERT INTO chat_link VALUES(?, ?)";
        jdbcTemplate.update(query, chatId, linkId);
    }

    @Override
    public void deleteChatLinkConnection(long chatId, long linkId) {
        String query = "DELETE FROM chat_link WHERE chat_id = ? AND link_id = ?";
        jdbcTemplate.update(query, chatId, linkId);
    }

    //TODO: добавить метод удаления всех ссылок связаннных с чатом
    @Component
    public static class LinkIdRowMapper implements RowMapper<Integer> {

        @Override
        public Integer mapRow(ResultSet rs, int rowNum) throws SQLException {
            return rs.getInt("link_id");
        }
    }

    @Component
    public static class ChatIdRowMapper implements RowMapper<Integer> {

        @Override
        public Integer mapRow(ResultSet rs, int rowNum) throws SQLException {
            return rs.getInt("chat_id");
        }
    }
}
