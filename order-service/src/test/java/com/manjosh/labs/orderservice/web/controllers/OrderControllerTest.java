package com.manjosh.labs.orderservice.web.controllers;

import static io.restassured.RestAssured.given;
import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;

import com.manjosh.labs.orderservice.AbstractIT;
import com.manjosh.labs.orderservice.domain.models.OrderSummary;
import com.manjosh.labs.orderservice.testData.TestDataFactory;
import io.restassured.common.mapper.TypeRef;
import io.restassured.http.ContentType;
import java.math.BigDecimal;
import java.util.List;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.test.context.jdbc.Sql;

@Sql("/test-orders.sql")
class OrderControllerTest extends AbstractIT {

    @Nested
    class CreateOrderTests {
        @Test
        void shouldCreateOrderSuccessfully() {
            mockProductByCode("P100", "Product 1", new BigDecimal("25.50"));
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

    @Nested
    class GetOrderTests {
        @Test
        void shouldGetOrderSuccessfully() {
            List<OrderSummary> orderSummaries = given().when()
                    .get("/api/orders")
                    .then()
                    .statusCode(200)
                    .extract()
                    .body()
                    .as(new TypeRef<>() {});
            assertThat(orderSummaries).hasSize(2);
        }
    }

    @Nested
    class GetOrderByOrderNumberTests {
        String orderNumber = "order-123";

        @Test
        void shouldGetOrderSuccessfully() {
            given().when()
                    .get("/api/orders/{orderNumber}", orderNumber)
                    .then()
                    .statusCode(200)
                    .body("orderNumber", is(orderNumber))
                    .body("items.size()", is(2));
        }
    }
}
