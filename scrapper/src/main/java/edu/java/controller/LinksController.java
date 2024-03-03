package edu.java.controller;

import edu.java.model.AddLinkRequest;
import edu.java.model.LinkResponse;
import edu.java.model.RemoveLinkRequest;
import edu.java.model.error.ListLinksResponse;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/links")
@Slf4j
public class LinksController {

    //по идее тут везде могу вернуть только заглушки
    //дальше как я понял, с появлением бд нужно дабить слои в виде сервиса и репозитория, да?

    @GetMapping
    public ResponseEntity<ListLinksResponse> getLinks(
        @NotNull
        @RequestHeader("Tg-Chat-Id")
        Long tgChatId
    ) {
        log.info("LinkController received GET request from user {}", tgChatId);
        return new ResponseEntity<>(HttpStatus.NOT_IMPLEMENTED);
    }

    @PostMapping
    public ResponseEntity<LinkResponse> addLink(
        @NotNull
        @RequestHeader("Tg-Chat-Id")
        Long tgChatId,
        @Valid
        @RequestBody
        AddLinkRequest addLinkRequest
    ) {
        log.info("LinkController received POST request from user {} with {} body",
            tgChatId, addLinkRequest
        );
        return new ResponseEntity<>(HttpStatus.NOT_IMPLEMENTED);
    }

    @DeleteMapping
    public ResponseEntity<LinkResponse> deleteLink(
        @NotNull
        @RequestHeader(value = "Tg-Chat-Id")
        Long tgChatId,
        @Valid
        @RequestBody
        RemoveLinkRequest removeLinkRequest
    ) {
        log.info("LinkController received DELETE request from user {} with {} body",
            tgChatId, removeLinkRequest
        );
        return new ResponseEntity<>(HttpStatus.NOT_IMPLEMENTED);
    }
}
