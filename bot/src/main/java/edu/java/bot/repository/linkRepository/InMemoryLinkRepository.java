package edu.java.bot.repository.linkRepository;

import edu.java.bot.model.Link;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.springframework.stereotype.Repository;

@Repository
public class InMemoryLinkRepository implements LinkRepository {
    private final Map<Long, List<Link>> links;
    private long idIncrementer;

    public InMemoryLinkRepository() {
        links = new HashMap<>();
        idIncrementer = 0;
    }

    @Override
    public void addLink(long userId, String url) {
        List<Link> userLinks = links.get(userId);

        if (userLinks == null) {
            userLinks = new ArrayList<>();
            userLinks.add(new Link(idIncrementer++, url));
            links.put(userId, userLinks);
        } else {
            for (Link link : userLinks) {
                if (link.url().equals(url)) {
                    return;
                }
            }

            links.get(userId).add(new Link(idIncrementer++, url));
        }
    }

    @Override
    public void deleteLink(long userId, Long linkId) {
        List<Link> userLinks = links.get(userId);
        if (userLinks != null) {
            for (int i = 0; i < userLinks.size(); i++) {
                if (userLinks.get(i).linkId() == linkId) {
                    userLinks.remove(i);
                    break;
                }
            }
        }
    }

    @Override
    public List<Link> getLinks(long userId) {
        List<Link> userLinks = links.get(userId);
        return userLinks == null ? Collections.emptyList() : Collections.unmodifiableList(userLinks);
    }

    @Override
    public Link getLinkById(long userId, long linkId) {
        List<Link> userLinks = links.get(userId);
        if (userLinks != null) {
            for (Link userLink : userLinks) {
                if (userLink.linkId() == linkId) {
                    return userLink;
                }
            }
        }

        return null;
    }

    @Override
    public Link getLinkByUrl(long userId, String url) {
        List<Link> userLinks = links.get(userId);
        if (userLinks != null) {
            for (Link userLink : userLinks) {
                if (userLink.url().equals(url)) {
                    return userLink;
                }
            }
        }

        return null;
    }
}
