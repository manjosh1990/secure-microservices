package com.manjosh.labs.orderservice.domain;

import com.manjosh.labs.orderservice.domain.models.*;
import java.time.LocalDateTime;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

class OrderEventMapper {
    static OrderCreatedEvent buildOrderCreatedEvent(OrderEntity order) {
        return new OrderCreatedEvent(
                UUID.randomUUID().toString(),
                order.getOrderNumber(),
                getOrderItems(order),
                order.getCustomer(),
                order.getDeliveryAddress(),
                LocalDateTime.now());
    }

    private static Set<OrderItem> getOrderItems(OrderEntity order) {
        return order.getItems().stream()
                .map(item -> new OrderItem(item.getCode(), item.getName(), item.getPrice(), item.getQuantity()))
                .collect(Collectors.toSet());
    }

    static OrderDeliveredEvent buildOrderDeliveredEvent(OrderEntity order) {
        return new OrderDeliveredEvent(
                UUID.randomUUID().toString(),
                order.getOrderNumber(),
                getOrderItems(order),
                order.getCustomer(),
                order.getDeliveryAddress(),
                LocalDateTime.now());
    }

    static OrderCancelledEvent buildOrderCancelledEvent(OrderEntity order, String reason) {
        return new OrderCancelledEvent(
                UUID.randomUUID().toString(),
                order.getOrderNumber(),
                getOrderItems(order),
                order.getCustomer(),
                order.getDeliveryAddress(),
                reason,
                LocalDateTime.now());
    }

    static OrderErrorEvent buildOrderErrorEvent(OrderEntity order, String reason) {
        return new OrderErrorEvent(
                UUID.randomUUID().toString(),
                order.getOrderNumber(),
                getOrderItems(order),
                order.getCustomer(),
                order.getDeliveryAddress(),
                reason,
                LocalDateTime.now());
    }
}
