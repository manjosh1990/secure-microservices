package com.manjosh.labs.orderservice.domain;

import com.manjosh.labs.orderservice.clients.catalog.Product;
import com.manjosh.labs.orderservice.clients.catalog.ProductServiceClient;
import com.manjosh.labs.orderservice.domain.models.CreateOrderRequest;
import com.manjosh.labs.orderservice.domain.models.OrderItem;
import java.util.Set;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Component
class OrderValidator {
    private static final Logger log = LoggerFactory.getLogger(OrderValidator.class);
    private final ProductServiceClient productServiceClient;

    public OrderValidator(ProductServiceClient productServiceClient) {
        this.productServiceClient = productServiceClient;
    }

    void validate(CreateOrderRequest request) {
        Set<OrderItem> items = request.items();
        for (OrderItem item : items) {
            Product product = productServiceClient
                    .getProductByCode(item.code())
                    .orElseThrow(() -> new InvalidOrderException("Invalid product code: " + item.code()));
            if (product.price().compareTo(item.price()) != 0) {
                log.error(
                        "Product price not matching. Actual price: {}, received price: {}",
                        product.price(),
                        item.price());
                throw new InvalidOrderException("Invalid price for product code: " + item.code());
            }
        }
    }
}
