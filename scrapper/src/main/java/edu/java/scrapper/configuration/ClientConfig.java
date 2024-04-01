package edu.java.scrapper.configuration;

import edu.java.scrapper.httpClients.botClient.BotClient;
import edu.java.scrapper.httpClients.botClient.WebBotClient;
import edu.java.scrapper.httpClients.gitHub.GitHubWebClientService;
import edu.java.scrapper.httpClients.stackOverflow.StackOverflowWebClientService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ClientConfig {
    @Autowired
    private RetryConfig retryConfig;

    @Bean
    public BotClient botClient() {
        return new WebBotClient(retryConfig);
    }

    @Bean
    public GitHubWebClientService gitHubWebClientService() {
        return new GitHubWebClientService(retryConfig);
    }

    @Bean
    public StackOverflowWebClientService stackOverflowWebClientService() {
        return new StackOverflowWebClientService(retryConfig);
    }
}
