package com.manjosh.labs.orderservice.web.controllers;

import static io.restassured.RestAssured.given;
import static org.hamcrest.CoreMatchers.notNullValue;

import com.manjosh.labs.orderservice.AbstractIT;
import com.manjosh.labs.orderservice.testData.TestDataFactory;
import io.restassured.http.ContentType;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;

class OrderControllerTest extends AbstractIT {

    @Nested
    class CreateOrderTests {
        @Test
        void shouldCreateOrderSuccessfully() {
            var payload =
                    """
                    {
                        "customer" :{
                            "name":"manjosh",
                            "email":"manjosh.1990@yahoo.com",
                            "phone":"123456789"
                        },
                        "deliveryAddress":{
                            "addressLine1":"CV Raman Nagar",
                            "addressLine2":"Bengaluru",
                            "city":"Bengaluru",
                            "state":"Karnataka",
                            "zipCode":"560093",
                            "country":"India"
                        },
                        "items":[
                            {
                                "code":"P100",
                                "name":"Product 1",
                                "price":25.50,
                                "quantity":1
                            }
                        ]
                    }
                    """;
            given().contentType(ContentType.JSON)
                    .body(payload)
                    .when()
                    .post("/api/orders")
                    .then()
                    .statusCode(HttpStatus.CREATED.value())
                    .body("orderNumber", notNullValue());
        }

        @Test
        void shouldReturnBadRequestWhenMandatoryDataIsMissing() {
            var payload = TestDataFactory.createOrderRequestWithInvalidCustomer();
            given().contentType(ContentType.JSON)
                    .body(payload)
                    .when()
                    .post("/api/orders")
                    .then()
                    .statusCode(HttpStatus.BAD_REQUEST.value());
        }
    }
}
