package edu.java.bot.repository.linkRepository;

import edu.java.bot.model.Link;
import java.util.List;

public interface LinkRepository {
    void addLink(long userId, String url);

    void deleteLink(long userId, Long linkId);

    List<Link> getLinks(long userId);

    Link getLinkById(long userId, long linkId);

    Link getLinkByUrl(long userId, String url);
}
