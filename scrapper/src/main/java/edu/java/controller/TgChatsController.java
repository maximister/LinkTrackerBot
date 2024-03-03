package edu.java.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/tg-chat")
@Slf4j
public class TgChatsController {

    @PostMapping("/{id}")
    public ResponseEntity<Void> addChat(
        @PathVariable
        Long id
    ) {
        log.info("TgChatsController received POST request from user {}", id);
        return new ResponseEntity<>(HttpStatus.NOT_IMPLEMENTED);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteChat(
        @PathVariable
        Long id
    ) {
        log.info("TgChatsController received DELETE request from user {}", id);
        return new ResponseEntity<>(HttpStatus.NOT_IMPLEMENTED);
    }
}