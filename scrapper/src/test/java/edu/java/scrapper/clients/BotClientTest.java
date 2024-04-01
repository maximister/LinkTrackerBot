package edu.java.scrapper.clients;

import com.github.tomakehurst.wiremock.WireMockServer;
import com.github.tomakehurst.wiremock.client.WireMock;
import edu.java.scrapper.configuration.RetryConfig;
import edu.java.scrapper.httpClients.botClient.WebBotClient;
import edu.java.scrapper.model.botClientDto.LinkUpdate;
import java.net.URI;
import java.time.Duration;
import java.util.Collections;
import java.util.List;
import java.util.Set;
import lombok.SneakyThrows;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import static com.github.tomakehurst.wiremock.client.WireMock.aResponse;
import static com.github.tomakehurst.wiremock.client.WireMock.recordSpec;
import static com.github.tomakehurst.wiremock.client.WireMock.urlPathMatching;
import static com.github.tomakehurst.wiremock.core.WireMockConfiguration.wireMockConfig;

public class BotClientTest {
    private static final String UPDATES_ENDPOINT = "/updates";
    private static WebBotClient client;
    private static WireMockServer server;
    private static LinkUpdate dto;

    @SneakyThrows
    @BeforeAll
    public static void start() {
        server = new WireMockServer(wireMockConfig().dynamicPort());
        server.stubFor(WireMock.post(urlPathMatching(UPDATES_ENDPOINT))
            .willReturn(aResponse()
                .withStatus(200)));

        dto = new LinkUpdate(
            1L,
            new URI("https://google.com"),
            "test",
            Collections.emptyList()
        );

        server.start();
        RetryConfig configuration = new RetryConfig(
            List.of(new RetryConfig.RetryInfo("bot", "fixed", 1, 1,
                Duration.ofSeconds(1), Set.of(500)
            )));
        client = new WebBotClient(server.baseUrl(), configuration);
    }

    @Test
    @DisplayName("Testing bot client post request")
    public void botClient_ShouldSendRequestToClientServer() {
        client.sendMessage(dto);
        server.verify(1, WireMock.postRequestedFor(WireMock.urlEqualTo(UPDATES_ENDPOINT)));
    }

    @AfterAll
    public static void shutdown() {
        server.stop();
    }
}
