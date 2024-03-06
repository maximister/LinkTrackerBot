package edu.java.scrapper.controllers;


import com.fasterxml.jackson.databind.ObjectMapper;
import edu.java.controller.LinksController;
import edu.java.model.LinkResponse;
import edu.java.model.ListLinksResponse;
import edu.java.model.RemoveLinkRequest;
import edu.java.service.LinkService;
import lombok.SneakyThrows;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.web.servlet.MockMvc;
import java.net.URI;
import java.util.List;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(LinksController.class)
public class LinksControllerTest {
    @Autowired
    private MockMvc mvc;
    @MockBean
    private LinkService service;
    @Autowired
    private ObjectMapper mapper;
    private final long linkId = 1111;
    private final URI url = URI.create("yrlvdplsh.com");

    @SneakyThrows
    @Test
    @DisplayName("correct get request")
    public void getLinks_shouldReturnOk() {
        Mockito.when(service.getLinks(1L))
            .thenReturn(new ResponseEntity<>(
                new ListLinksResponse(List.of(new LinkResponse(linkId, url)), 1),
                HttpStatus.OK
            ));

        mvc.perform(
                get("/links")
                    .header("Tg-Chat-Id", 1L)
                    .contentType("application/json")
            )
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.links[0].id").value(linkId))
            .andExpect(jsonPath("$.links[0].url").value(url.toString()));
    }

    @Test
    @DisplayName("no header get request")
    @SneakyThrows
    public void getLinks_shouldReturnBadRequest_whenHeaderIsMissed() {
        mvc.perform(get("/links")
                .contentType("application/json"))
            .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("Wrong header get request")
    @SneakyThrows
    public void getLinks_shouldReturnBadRequest_whenHeaderIsWrong() {
        mvc.perform(get("/links")
                .contentType("application/json")
                .header("Tg-Chat-Id", "asd")
            )
            .andExpect(status().isBadRequest());
    }

    @Test
    @SneakyThrows
    @DisplayName("Correct delete request")
    public void deleteLink_shouldReturnOk() {
        RemoveLinkRequest removeLinkRequest = new RemoveLinkRequest(url);

        Mockito.when(service.deleteLink(1L, removeLinkRequest))
            .thenReturn(new ResponseEntity<>(new LinkResponse(linkId, url), HttpStatus.OK));

        mvc.perform(
                delete("/links")
                    .header("Tg-Chat-Id", 1L)
                    .contentType("application/json")
                    .content(mapper.writeValueAsString(removeLinkRequest)))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.id").value(linkId))
            .andExpect(jsonPath("$.url").value(url.toString()));
    }
}


