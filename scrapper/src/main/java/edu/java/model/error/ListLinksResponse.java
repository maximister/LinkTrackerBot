package edu.java.model.error;

import edu.java.model.LinkResponse;
import java.util.List;

public record ListLinksResponse(
    List<LinkResponse> links,
    int size
) {
}
