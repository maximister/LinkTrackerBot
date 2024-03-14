package edu.java.scrapper.repository;

import java.util.List;

public interface ChatLinkRepository {
    List<Integer> getLinkIdsByChatId(long chatId);

    List<Integer> getChatIdsByLinkId(long linkId);

    boolean isLinkTracked(long linkId);

    void addChatLinkConnection(long chatId, long linkId);

    void deleteChatLinkConnection(long chatId, long linkId);
}
