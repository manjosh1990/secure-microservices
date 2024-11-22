package com.manjosh.labs.orderservice.domain;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.manjosh.labs.orderservice.domain.models.OrderErrorEvent;
import org.springframework.stereotype.Component;

@Component
class OrderErrorEventEventProcessor implements OrderEventProcessor<OrderErrorEvent> {
    @Override
    public void process(OrderEventEntity event, OrderEventPublisher orderEventPublisher, ObjectMapper objectMapper) {
        OrderErrorEvent orderErrorEvent = fromJsonPayload(event.getPayload(), objectMapper, OrderErrorEvent.class);
        orderEventPublisher.publish(orderErrorEvent);
    }
}
