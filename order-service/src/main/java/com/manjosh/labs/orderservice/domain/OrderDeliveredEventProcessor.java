package com.manjosh.labs.orderservice.domain;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.manjosh.labs.orderservice.domain.models.OrderDeliveredEvent;
import org.springframework.stereotype.Component;

@Component
class OrderDeliveredEventProcessor implements OrderEventProcessor<OrderDeliveredEvent> {
    @Override
    public void process(OrderEventEntity event, OrderEventPublisher orderEventPublisher, ObjectMapper objectMapper) {
        OrderDeliveredEvent orderDeliveredEvent =
                fromJsonPayload(event.getPayload(), objectMapper, OrderDeliveredEvent.class);
        orderEventPublisher.publish(orderDeliveredEvent);
    }
}
