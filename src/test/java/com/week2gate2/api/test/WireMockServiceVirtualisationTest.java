package com.week2gate2.api.test;
import com.github.tomakehurst.wiremock.core.Options;
import com.github.tomakehurst.wiremock.core.WireMockConfiguration;
import com.github.tomakehurst.wiremock.junit5.WireMockExtension;
import io.restassured.internal.http.HTTPBuilder;
import org.apache.http.protocol.HTTP;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.RegisterExtension;

import java.net.http.HttpResponse;
import java.net.http.HttpTimeoutException;
import java.beans.Transient;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.time.Duration;

import static com.github.tomakehurst.wiremock.client.WireMock.*;
import static com.github.tomakehurst.wiremock.core.WireMockConfiguration.wireMockConfig;
import static com.github.tomakehurst.wiremock.stubbing.Scenario.STARTED;
import static io.restassured.RestAssured.baseURI;
import static io.restassured.RestAssured.given;
import static java.net.http.HttpResponse.BodyHandlers.ofString;
import static org.hamcrest.Matchers.equalTo;
import static org.junit.jupiter.api.Assertions.assertThrows;
class WireMockServiceVirtualisationTest {
    @RegisterExtension
    static WireMockExtension wm = WireMockExtension.newInstance()
            .options(wireMockConfig().dynamicPort().templatingEnabled(true))
            .build();

    private HttpClient client;

    @BeforeEach
    void ClientInitial()
    {
        io.restassured.RestAssured.baseURI = wm.baseUrl();
        client = HttpClient.newHttpClient();
    }

    @Test
    @DisplayName("get orders id and return it")
    void returnsConfirmOrderWiremock() {
        wm.stubFor(get(urlPathEqualTo("orders/123"))
                .willReturn(okJson("""
                        {"id":123,"status":"CONFIRMED","total":42.0}""")));
    }

    @Test
    @DisplayName("Stub Inventory - Success and Out Of Stock")
    void stubInventoryTwoOutcomes() {
        wm.stubFor(get(urlPathEqualTo("/inventory/SKU-9")).willReturn(aResponse()
                .withStatus(200)
                .withHeader("Content-Type", "application/json")
                .withBody("""
                        {
                        "sku":"SKU-9","qty":5
                        }
                        """)));
        wm.stubFor(get(urlPathEqualTo("/inventory/SKU-0"))
                .willReturn(aResponse()
                        .withStatus(409)
                        .withHeader("Content-Type", "application/json")
                        .withBody("""
                                 {
                                   "error":"OUT_OF_STOCK"
                                 }
                                """)));
        given()
                .when()
                .get("/inventory/SKU-9")
                .then()
                .statusCode(200)
                .body("qty", equalTo(5))
                .body("sku", equalTo("SKU-9"));
        given()
                .when()
                .get("/inventory/SKU-0")
                .then()
                .statusCode(409)
                .body("error", equalTo("OUT_OF_STOCK"));
        wm.verify(exactly(1), getRequestedFor(urlPathEqualTo("/inventory/SKU-9")));
    }

    @Test
    @DisplayName("giving explicit timeout")
    void timeout() {
        wm.stubFor(get(urlPathEqualTo("/orders/slow")).willReturn(
                ok().withFixedDelay(5000))
        );
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(wm.baseUrl() + "/orders/slow"))
                .timeout(Duration.ofSeconds(1))
                .GET()
                .build();

        given()
                .when()
                .get("/orders/slow")
                .then().statusCode(200);
        assertThrows(HttpTimeoutException.class, () -> client.send(request, ofString()));


    }

    @Test
    @DisplayName("Pending the order then confirmed")
    void pendingandConfirmed() {
        wm.stubFor(get(urlPathEqualTo("/order/42"))
                .inScenario("fulfilment")
                .whenScenarioStateIs(STARTED)
                .willReturn(okJson("""
                        {
                        "id":42,
                        "status":"PENDING"
                        
                        }
                        """))
                .willSetStateTo("CONFIRMED"));

        wm.stubFor(get(urlPathEqualTo("/order/42"))
                .inScenario("fulfilment")
                .whenScenarioStateIs("CONFIRMED")
                .willReturn(okJson("""
                        {
                        "order":42,
                        "status":"CONFIRMED"
                        }
                        """))
        );

        given()
                .when()
                .get("/order/42").then().body("status", equalTo("PENDING"));
        given()
                .when()
                .get("/order/42").then().body("status", equalTo("CONFIRMED"));


        wm.verify(exactly(2), getRequestedFor(urlPathEqualTo("/order/42")));


    }

    @Test
    @DisplayName("order creaated 201")
    void orderCreated() {
        wm.stubFor(get(urlPathEqualTo("/product/shoe/123")).willReturn(aResponse()
                .withStatus(200)
                .withHeader("Content-Type", "application/json")
                .withBody("""
                    {
                        "id":123,
                        "shoe":"puma",
                        "qty":1
                    }
                    """)));
        wm.stubFor(get(urlPathEqualTo("/cart/items"))
                .willReturn(aResponse()
                        .withStatus(201)
                        .withHeader("Content-Type", "application/json")
                        .withBody("""
                                 {
                                   "order_status":"confirmed"
                                 }
                                """)));
        given()
                .when()
                .get("/product/shoe/123").then().body("id", equalTo(123));
        given()
                .when()
                .get("/cart/items").then().body("order_status", equalTo("confirmed"));
    }

}
