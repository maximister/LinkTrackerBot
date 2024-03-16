package edu.java.scrapper.model.ControllerDto;

import java.net.URI;

public record RemoveLinkRequest(
    URI link
) {
}
