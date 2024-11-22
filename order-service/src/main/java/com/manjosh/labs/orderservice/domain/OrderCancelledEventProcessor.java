package com.manjosh.labs.orderservice.domain;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.manjosh.labs.orderservice.domain.models.OrderCancelledEvent;
import org.springframework.stereotype.Component;

@Component
class OrderCancelledEventProcessor implements OrderEventProcessor<OrderCancelledEvent> {
    @Override
    public void process(OrderEventEntity event, OrderEventPublisher orderEventPublisher, ObjectMapper objectMapper) {
        OrderCancelledEvent orderCancelledEvent =
                fromJsonPayload(event.getPayload(), objectMapper, OrderCancelledEvent.class);
        orderEventPublisher.publish(orderCancelledEvent);
    }
}
