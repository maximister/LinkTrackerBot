package edu.java.scrapper.repository;

import edu.java.scrapper.httpClients.LinkInfo;
import edu.java.scrapper.model.domainDto.Link;
import java.net.URI;
import java.util.List;

public interface LinkRepository {
    List<Link> getLinks(List<Integer> linksId);

    Link addLink(URI url);

    Link deleteLink(long linkId);

    void updateLink(LinkInfo linkInfo);
}
