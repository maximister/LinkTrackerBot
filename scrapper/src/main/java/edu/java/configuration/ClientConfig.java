package edu.java.configuration;

import edu.java.httpClients.gitHub.GitHubWebClientService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ClientConfig {
    @Bean
    public GitHubWebClientService gitHubWebClientService() {
        return new GitHubWebClientService();
    }
}
