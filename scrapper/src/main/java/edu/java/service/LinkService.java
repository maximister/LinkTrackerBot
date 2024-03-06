package edu.java.service;

import edu.java.model.AddLinkRequest;
import edu.java.model.LinkResponse;
import edu.java.model.ListLinksResponse;
import edu.java.model.RemoveLinkRequest;
import org.springframework.http.ResponseEntity;

public interface LinkService {
    ResponseEntity<ListLinksResponse> getLinks(Long id);

    ResponseEntity<LinkResponse> addLink(Long tgChatId, AddLinkRequest addLinkRequest);

    ResponseEntity<LinkResponse> deleteLink(Long tgChatId, RemoveLinkRequest removeLinkRequest);
}
