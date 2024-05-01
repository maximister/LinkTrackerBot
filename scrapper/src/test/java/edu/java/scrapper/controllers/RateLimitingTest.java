package edu.java.scrapper.controllers;

import edu.java.scrapper.IntegrationEnvironment;
import lombok.SneakyThrows;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@TestPropertySource(locations = {"classpath:RateLimitingTest.properties"}, properties = {"spring.cache.type="})
public class RateLimitingTest extends IntegrationEnvironment {
    @Autowired
    private MockMvc mockMvc;

    @SneakyThrows
    @Test
    @DisplayName("testing rate limiting with capacity 10")
    void rateLimitingTest() {
        for (int i = 0; i < 10; i++) {
            mockMvc.perform(post("/tg-chat/" + i)).andExpect(status().isOk());
        }

        mockMvc.perform(post("/tg-chat/" + 11)).andExpect(status().isTooManyRequests());
    }
}
