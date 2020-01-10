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
import static com.github.tomakehurst.wiremock.client.WireMock.urlPathMatching;
import static com.github.tomakehurst.wiremock.core.WireMockConfiguration.options;

@Slf4j
@Component
@Profile({"dev","prod"})
public class BackgroundApiStartUpConfig implements ApplicationRunner {
    private static int PORT = 8181;
    @Override
    public void run(ApplicationArguments args) throws Exception {
        log.info("EXECUTING : Background API Configuration");
        final WireMockServer wireMockServer = new WireMockServer(options().port(PORT));
        wireMockServer.stubFor(get(anyUrl())
                .willReturn(aResponse()
                        .withStatus(404)
                        .withHeader("Content-Type","application/json")
                        .withBody("{\"error\":\"Not Found\"," +
                                "\"message\":\"No Message available\"}")));
        wireMockServer.stubFor(get(urlPathMatching("/check/([0-9]{12})"))
                .willReturn(aResponse()
                        .withStatus(200)
                        .withHeader("Content-Type","application/json")
                        .withBody("{\"records\":[]}")));
        wireMockServer.stubFor(get(urlPathMatching("/check/([0-3]{5}[0-9]{7})"))
                .willReturn(aResponse()
                        .withStatus(200)
                        .withHeader("Content-Type","application/json")
                        .withBody("{\"records\":[" +
                                    "{\"title\":\"Smuggle\"," +
                                    "\"date\":1547153565}," +
                                    "{\"title\":\"Assault\"," +
                                    "\"date\":1576011165}" +
                                "]}")));
        wireMockServer.stubFor(get(urlPathMatching("/check/([4-6]{5}[0-9]{7})"))
                .willReturn(aResponse()
                        .withStatus(200)
                        .withHeader("Content-Type","application/json")
                        .withBody("{\"records\":[" +
                                "{\"title\":\"Drunk Driving\"," +
                                "\"date\":880923165}," +
                                "{\"title\":\"Assault\"," +
                                "\"date\":1216587165}" +
                                "{\"title\":\"Assault\"," +
                                "\"date\":1281473565}," +
                                "{\"title\":\"Assault\"," +
                                "\"date\":1445374365}" +
                                "]}")));
        wireMockServer.stubFor(get(urlPathMatching("/check/([7-9]{5}[0-9]{7})"))
                .willReturn(aResponse()
                        .withStatus(200)
                        .withHeader("Content-Type","application/json")
                        .withBody("{\"records\":[" +
                                "{\"title\":\"Smuggle\"," +
                                "\"date\":1121460765}," +
                                "{\"title\":\"Smuggle\"," +
                                "\"date\":1577307165}" +
                                "{\"title\":\"Smuggle\"," +
                                "\"date\":1455483165}," +
                                "{\"title\":\"Assault\"," +
                                "\"date\":992983965}" +
                                "{\"title\":\"Assault\"," +
                                "\"date\":1195851165}," +
                                "{\"title\":\"Murder\"," +
                                "\"date\":1273697565}" +
                                "{\"title\":\"Murder\"," +
                                "\"date\":684535965}," +
                                "{\"title\":\"Assault\"," +
                                "\"date\":1047588765}" +
                                "]}")));
        wireMockServer.start();
        WireMock.configureFor("localhost",wireMockServer.port());
        log.info(String.format("EXECUTING : BackGround API Started on port %d",PORT));
    }
}
