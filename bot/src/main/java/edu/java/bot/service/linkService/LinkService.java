package edu.java.bot.service.linkService;

import edu.java.bot.model.Link;
import java.util.List;

public interface LinkService {

    void trackLink(long userId, String url);

    void untrackLink(long userId, Long linkId);

    List<Link> getLinks(long userId);

    Link getLinkById(long userId, long linkId);

    Link getLinkByUrl(long userId, String url);
}
