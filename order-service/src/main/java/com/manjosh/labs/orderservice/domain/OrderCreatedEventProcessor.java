package com.manjosh.labs.orderservice.domain;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.manjosh.labs.orderservice.domain.models.OrderCreatedEvent;
import org.springframework.stereotype.Component;

@Component
class OrderCreatedEventProcessor implements OrderEventProcessor<OrderCreatedEvent> {
    @Override
    public void process(OrderEventEntity event, OrderEventPublisher orderEventPublisher, ObjectMapper objectMapper) {
        OrderCreatedEvent orderCreatedEvent =
                fromJsonPayload(event.getPayload(), objectMapper, OrderCreatedEvent.class);
        orderEventPublisher.publish(orderCreatedEvent);
    }
}
