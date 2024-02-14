package edu.java.bot.service.linkService;

import edu.java.bot.model.Link;
import edu.java.bot.repository.linkRepository.LinkRepository;
import java.util.List;
import org.springframework.stereotype.Service;

@Service
public final class SimpleLinkService implements LinkService {
    private final LinkRepository repository;

    public SimpleLinkService(LinkRepository repository) {
        this.repository = repository;
    }

    @Override
    public void trackLink(long userId, String url) {
        repository.addLink(userId, url);
    }

    @Override
    public void untrackLink(long userId, Long linkId) {
        repository.deleteLink(userId, linkId);
    }

    @Override
    public List<Link> getLinks(long userId) {
        return repository.getLinks(userId);
    }

    @Override
    public Link getLinkById(long userId, long linkId) {
        return repository.getLinkById(userId, linkId);
    }

    @Override
    public Link getLinkByUrl(long userId, String url) {
        return null;
    }

}
