package com.week2gate2.api.test;
import com.week2gate2.api.model.Mapclass;
import com.week2gate2.database.model.OrderRow;
import com.week2gate2.database.support.DbSupport;
import io.restassured.response.Response;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import static io.restassured.matcher.RestAssuredMatchers.matchesXsdInClasspath;
import static io.restassured.RestAssured.given;
import static io.restassured.module.jsv.JsonSchemaValidator.matchesJsonSchemaInClasspath;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import static com.week2gate2.api.model.Mapclass.*;
import static com.week2gate2.api.support.Specfactory.*;
import static io.restassured.RestAssured.*;
import static com.week2gate2.fixtures.credentials.*;
import static org.hamcrest.Matchers.*;
import static org.junit.jupiter.api.Assertions.*;
public class Flowpath
{
    private static final List<Long> CreateOrders = new ArrayList<>();
    public static String ctoken;
    @BeforeAll

        static void setup() {
        baseURI = BASE_URL;
        basePath = "/api";
        ctoken = client_token;
    }
    @Test
    @DisplayName("first test")
    void api_test() throws SQLException {
        Response first_order=
                given().spec(Auth2Tokn(ctoken))
                .body(order)
                .when()
                    .post("/secure/orders")
                .then().spec(post)
                    .body("id",notNullValue())
                    .body("orderId",notNullValue())
                        .extract().response();


       Integer Order_id=first_order.path("id");
//        Integer id = first_order.path("orderId");
//        CreateOrders.add(Long.valueOf(id));
//        OrderRow row = DbSupport.findOrder(Order_id);
//        assertNotNull(row,"order must be persisted");
//        assertNotNull("orderNumber",row.OrderNumber());
//        assertEquals("CREATED",row.status());
//        assertEquals(0,row.getTotal().compareTo(first_order.jsonPath().getObject("total", BigDecimal.class)));
//        assertEquals("svc-retail-ops",row.getUserId());
//        assertNotNull(row.getCreatedAt());
//        Instant createdOrderIds = row.getCreatedAt()
//                .atZone(ZoneId.systemDefault())
//                .toInstant();
//        Instant now = Instant.now();
//        assertTrue(createdOrderIds.isAfter(now.minusSeconds(60)));
//        assertTrue(createdOrderIds.isBefore(now.plusSeconds(5)));

        Response second= given().spec(Auth2Tokn(ctoken))
                .body(order)
                .when()
                .post("/secure/orders/{id}/allocate",Order_id)
                .then().spec(ok)
                .body("id",equalTo(Order_id))
                .body("orderId",equalTo(Order_id))
                .body("orderNumber",notNullValue())
                .body("status",equalToIgnoringCase("Allocated"))
                .body(matchesJsonSchemaInClasspath("schema/json/order.schema.json")).extract().response();




        Response third=given().spec(Auth2Tokn(ctoken))
                .body(order)
                .when()
                .post("/secure/orders/{id}/ship",Order_id)
                .then().spec(ok)
                .body("id",equalTo(Order_id))
                .body("orderId",equalTo(Order_id))
                .body("orderNumber",notNullValue())
                .body("status",equalToIgnoringCase("shipped"))
                .extract().response();

        given()         //negative test case that will return 401
                .spec(sec_api)
                .when()
                .get("/{id}",5001)
                .then()
                .statusCode(401)

                .body("message",equalToIgnoringCase("Authentication required"));

        given()
                .spec(invalid_api)
                .when()
                .get("/api/secure/orders/5001")
                .then()
                .statusCode(401);

        given().spec(Auth2Tokn(viewer_token))
                .when()
                .post("/secure/orders")
                .then()
                .statusCode(403);


    }

}
