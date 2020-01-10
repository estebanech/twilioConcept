package com.example.test01.demo.config;

import com.github.tomakehurst.wiremock.WireMockServer;
import com.github.tomakehurst.wiremock.client.WireMock;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

import static com.github.tomakehurst.wiremock.client.WireMock.aResponse;
import static com.github.tomakehurst.wiremock.client.WireMock.anyUrl;
import static com.github.tomakehurst.wiremock.client.WireMock.get;
import static com.github.tomakehurst.wiremock.client.WireMock.matching;
import static com.github.tomakehurst.wiremock.client.WireMock.urlPathMatching;
import static com.github.tomakehurst.wiremock.core.WireMockConfiguration.options;

@Slf4j
@Component
@Profile({"dev","prod"})
public class IdentityApiStartUpConfig implements ApplicationRunner {
    private static int PORT = 8180;
    @Override
    public void run(ApplicationArguments args) throws Exception {
        log.info("EXECUTING : Identity API Configuration");
        final WireMockServer wireMockServer = new WireMockServer(options().port(PORT));
        wireMockServer.stubFor(get(anyUrl())
                .willReturn(aResponse()
                        .withStatus(404)
                        .withHeader("Content-Type","application/json")
                        .withBody("{\"error\":\"Not Found\"," +
                                "\"message\":\"No Message available\"}")));
        wireMockServer.stubFor(get(urlPathMatching("/compare/([0-9]{12})"))
                .withQueryParam("name",matching("[a-z|A-Z|\\s]*"))
                .withQueryParam("age",matching("[1-9]{1}[0-9]{0,2}"))
                .willReturn(aResponse()
                        .withStatus(200)
                        .withHeader("Content-Type","application/json")
                        .withBody("{\"trustful\":true}")));
        wireMockServer.stubFor(get(urlPathMatching("/compare/([0-9]{7}[0-5]{5})"))
                .withQueryParam("name",matching("[a-z|A-Z|\\s]*"))
                .withQueryParam("age",matching("[1-9]{1}[0-9]{0,2}"))
                .willReturn(aResponse()
                        .withStatus(200)
                        .withHeader("Content-Type","application/json")
                        .withBody("{\"trustful\":false}")));
        wireMockServer.start();
        WireMock.configureFor("localhost",wireMockServer.port());
        log.info(String.format("EXECUTING : BackGround API Started on port %d",PORT));
    }
}
