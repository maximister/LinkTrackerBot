package edu.java.model;

import java.net.URI;

public record LinkResponse(
    long id,
    URI url
) {
}
