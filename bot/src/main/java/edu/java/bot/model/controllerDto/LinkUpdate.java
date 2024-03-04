package edu.java.bot.model.controllerDto;

import java.net.URI;
import java.util.List;

public record LinkUpdate(
    Long id,
    URI url,
    String description,
    List<Long> tgChatIds) {
}
