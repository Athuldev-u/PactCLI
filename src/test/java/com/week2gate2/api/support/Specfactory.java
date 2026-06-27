package com.week2gate2.api.support;


import io.restassured.builder.RequestSpecBuilder;
import io.restassured.builder.ResponseSpecBuilder;
import io.restassured.http.ContentType;
import io.restassured.specification.RequestSpecification;
import io.restassured.specification.ResponseSpecification;

import static com.week2gate2.api.config.BaseConfig.BASE_URL_CONFIG;

import static io.restassured.RestAssured.given;
import static io.restassured.RestAssured.oauth2;
import static org.hamcrest.Matchers.equalToIgnoringCase;

public class Specfactory
{

    public static final String BASE_URL=BASE_URL_CONFIG;
    public static RequestSpecification login_req=new RequestSpecBuilder()
            .setBaseUri(BASE_URL)
            .setBasePath("/api/login")
            .setAccept(ContentType.JSON)
            .setContentType(ContentType.JSON)//without this code will fail
            .build();

    public static RequestSpecification Authorised (String a)
    {
        return new RequestSpecBuilder()
                .setBaseUri(BASE_URL)
                .setBasePath("/api")
                .setContentType(ContentType.JSON)
                .setAccept(ContentType.JSON)
                .setAuth(oauth2(a))
                .build();

    }
    public static RequestSpecification Auth2Tokn(String a)
    {
        return new RequestSpecBuilder()
                .setBaseUri(BASE_URL)
                .setContentType("application/json")
                .setAccept("application/json")
                .setAuth(oauth2(a))
                .build();

    }
    public static RequestSpecification order_token(String tok)
    {
        return new RequestSpecBuilder()
                .setBaseUri(BASE_URL)
                .setContentType(ContentType.JSON)
                .setAccept(ContentType.JSON)
                .setAuth(oauth2(tok))
                .build();

    }
    public static RequestSpecification Unauthor(String a)
    {
        return new RequestSpecBuilder()
            .setBaseUri(BASE_URL)
            .setBasePath("/api")
            .setContentType(ContentType.JSON)
            .setAccept(ContentType.JSON)
            .setAuth(oauth2(a))
            .build();
    }
    public static String config(String name, String fallback)
    {
        String system=System.getProperty(name);
        if(system !=null && !system.isBlank())
        {
            return system;
        }
        String envValue=System.getenv(name);
        return envValue==null||envValue.isBlank()?fallback:envValue;
    }
    public static RequestSpecification apikey(String a)
    {
            return new RequestSpecBuilder()
                    .setBaseUri(BASE_URL)
                    .setBasePath("/api")
                    .setContentType(ContentType.JSON)
                    .setContentType(ContentType.JSON)
                    .addHeader("X-API-Key",a)
                    .build();

    }
    public static ResponseSpecification ok=new ResponseSpecBuilder()
            .expectStatusCode(200)
            .expectContentType(ContentType.JSON)
            .build();

    public static RequestSpecification sec_api =  new RequestSpecBuilder()
            .setBaseUri(BASE_URL)
            .setBasePath("/api/secure/orders")
            .setContentType(ContentType.JSON)
            .setAccept("application/json")
            .build();

    public static ResponseSpecification detele=new ResponseSpecBuilder()
            .expectStatusCode(204)
            .expectContentType(ContentType.JSON)
            .build();

    public static ResponseSpecification post=new ResponseSpecBuilder()
            .expectStatusCode(201)
            .expectContentType(ContentType.JSON)
            .build();
    public static RequestSpecification invalid_api =  new RequestSpecBuilder()
            .setBaseUri(BASE_URL)
            .setContentType("application/json")
            .setAccept("application/json")
            .setAuth(oauth2("jhbyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJpc3MiOiJzZGV0LXJldGFpbC1kZW1vIiwiYXVkIjoic2RldC1yZXRhaWwtYXBpIiwic3ViIjoic3ZjLXJldGFpbC1vcHMiLCJyb2xlIjoiT1BTIiwic2NvcGUiOiJvcmRlcnM6cmVhZCBvcmRlcnM6d3JpdGUiLCJpYXQiOjE3ODExNzcxNzAsImV4cCI6MTc4MTE4MDc3MH0.PqusccM5feGVzE4EkrHqRNbMaxgT7FuVCIomFMAk7Jkd"))
            .build();


    public static String ret_token(String name,String password)
    {
        String token=
                given()
                        .baseUri(BASE_URL)
                        .contentType("application/x-www-form-urlencoded; charset=UTF-8")
                        .auth().preemptive().basic(name,password)
                        .formParam("grant_type","client_credentials")
                        .when()
                        .post("/api/oauth/token")
                        .then()
                        .statusCode(200)
                        .body("token_type",equalToIgnoringCase("Bearer"))
                        .extract().path("access_token");
        return token;

    }
}
