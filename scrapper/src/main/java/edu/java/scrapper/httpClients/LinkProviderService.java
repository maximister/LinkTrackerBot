package edu.java.scrapper.httpClients;

import java.net.URI;

public interface LinkProviderService {
    LinkInfo fetch(URI url);
}
