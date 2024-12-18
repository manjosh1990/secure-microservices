package com.manjosh.labs.orderservice.clients.catalog;

import com.manjosh.labs.orderservice.ApplicationProperties;
import java.time.Duration;
import org.springframework.boot.web.client.ClientHttpRequestFactories;
import org.springframework.boot.web.client.ClientHttpRequestFactorySettings;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestClient;

@Configuration
public class CatalogServiceClientConfig {

    @Bean
    RestClient restClient(ApplicationProperties properties) {
        return RestClient.builder()
                .baseUrl(properties.catalogServiceUrl()) // url
                .requestFactory(ClientHttpRequestFactories.get( // add connect and read timeout
                        ClientHttpRequestFactorySettings.DEFAULTS
                                .withConnectTimeout(Duration.ofSeconds(5)) // timeout for connection
                                .withReadTimeout(
                                        Duration.ofSeconds(5)) // timeout to receive response once handshake is complete
                        ))
                .build();
    }
}
