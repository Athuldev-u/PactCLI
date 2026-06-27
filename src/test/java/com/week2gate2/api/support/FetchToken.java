package com.week2gate2.api.support;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.builder.ResponseSpecBuilder;
import io.restassured.http.ContentType;
import io.restassured.specification.RequestSpecification;
import io.restassured.specification.ResponseSpecification;
import com.week2gate2.api.support.Specfactory.*;
import com.week2gate2.api.config.BaseConfig;
import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalToIgnoringCase;

public class FetchToken {
    public static String gettoken(String name,String pass){
        String a=given()
                .baseUri(BaseConfig.BASE_URL_CONFIG)
                .contentType("application/x-www-form-urlencoded; charset=UTF-8")
                .auth().preemptive().basic(name,pass)
                .formParam("grant_type","client_credentials")
                .when()
                .post("/oauth/token")
                .then()
                .statusCode(200)
                .body("token_type",equalToIgnoringCase("Bearer"))
                .extract().path("access_token");
        return a;

    }
}
