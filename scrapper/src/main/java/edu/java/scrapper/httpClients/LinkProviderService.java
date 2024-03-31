package edu.java.scrapper.httpClients;

import java.net.URI;
import java.util.List;

public interface LinkProviderService {
    List<LinkInfo> fetch(URI url);

    boolean isValid(URI url);
}
