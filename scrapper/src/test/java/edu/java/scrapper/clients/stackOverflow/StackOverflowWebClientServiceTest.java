package edu.java.scrapper.clients.stackOverflow;

import com.github.tomakehurst.wiremock.WireMockServer;
import com.github.tomakehurst.wiremock.client.WireMock;
import edu.java.scrapper.configuration.RetryConfig;
import edu.java.scrapper.httpClients.LinkInfo;
import edu.java.scrapper.httpClients.LinkProviderService;
import edu.java.scrapper.httpClients.stackOverflow.StackOverflowWebClientService;
import java.net.URI;
import java.time.Duration;
import java.time.OffsetDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Set;
import lombok.SneakyThrows;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import static com.github.tomakehurst.wiremock.client.WireMock.aResponse;
import static com.github.tomakehurst.wiremock.client.WireMock.urlPathMatching;
import static com.github.tomakehurst.wiremock.core.WireMockConfiguration.wireMockConfig;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.InstanceOfAssertFactories.COLLECTION;

public class StackOverflowWebClientServiceTest {
    private static final String CORRECT_SERVER_ENDPOINT = "/questions/53579112*";
    private static final String CORRECT_LINK =
        "https://stackoverflow.com/questions/53579112/inject-list-of-all-beans-with-a-certain-interface";
    private static final String NOT_EXISTING_QUESTION =
        "https://stackoverflow.com/questions/10000000000000000/aboba";

    private static WireMockServer server;
    private static LinkProviderService service;
    private static LinkInfo CORRECT_DTO;

    @SneakyThrows @BeforeAll
    public static void start() {
        server = new WireMockServer(wireMockConfig().dynamicPort());
        server.stubFor(WireMock.get(urlPathMatching(CORRECT_SERVER_ENDPOINT))
            .willReturn(aResponse()
                .withStatus(200)
                .withHeader("Content-Type", "application/json")
                //будет длинная неэстетичная строка, тк я не хочу обрезать случайный жсон вручную
                .withBody("""
                    {"items":[{"tags":["java","spring","dependency-injection"],"owner":{"account_id":2381622,"reputation":21432,"user_id":2083523,"user_type":"registered","accept_rate":68,"profile_image":"https://www.gravatar.com/avatar/2ce2d086884e247c69544a7bec5b79a2?s=256&d=identicon&r=PG&f=y&so-version=2","display_name":"Avi","link":"https://stackoverflow.com/users/2083523/avi"},"is_answered":true,"view_count":25154,"accepted_answer_id":53579193,"answer_count":1,"score":28,"last_activity_date":1543744914,"creation_date":1543744254,"question_id":53579112,"content_license":"CC BY-SA 4.0","link":"https://stackoverflow.com/questions/53579112/inject-list-of-all-beans-with-a-certain-interface","title":"Inject list of all beans with a certain interface"}],"has_more":false,"quota_max":300,"quota_remaining":251}""")));
        server.stubFor(WireMock.get(urlPathMatching("/questions/1000000000000000*"))
            .willReturn(aResponse()
                .withStatus(404)));

        CORRECT_DTO = new LinkInfo(
            new URI("https://stackoverflow.com/questions/53579112/inject-list-of-all-beans-with-a-certain-interface"),
            "Inject list of all beans with a certain interface",
            "view count: 25154\nanswer count: 1",
            OffsetDateTime.parse("2018-12-02T10:01:54Z", DateTimeFormatter.ISO_DATE_TIME)
        );


        server.start();
        RetryConfig configuration = new RetryConfig(
            List.of(new RetryConfig.RetryInfo("stackoverflow", "fixed", 1, 1,
                Duration.ofSeconds(1), Set.of(500)
            )));
        service = new StackOverflowWebClientService(server.baseUrl(), configuration);
    }

    @SneakyThrows
    @Test
    @DisplayName("testing client work with correct link")
    public void fetch_shouldReturnCorrectDto() {
        List<LinkInfo> result = service.fetch(new URI(CORRECT_LINK));

        assertThat(result).isEqualTo(List.of(CORRECT_DTO));
    }

    @SneakyThrows
    @Test
    @DisplayName("testing client work with non existing question")
    public void fetch_shouldReturnNull_whenQuestionDoesNotExist() {
        List<LinkInfo> result = service.fetch(new URI(NOT_EXISTING_QUESTION));

        assertThat(result).isEqualTo(Collections.emptyList());
    }

    @AfterAll
    public static void shutdown() {
        server.stop();
    }
}
