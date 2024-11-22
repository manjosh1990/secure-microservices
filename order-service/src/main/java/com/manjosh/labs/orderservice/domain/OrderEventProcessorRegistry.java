package com.manjosh.labs.orderservice.domain;

import com.manjosh.labs.orderservice.domain.models.OrderEventType;
import java.util.EnumMap;
import java.util.List;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
class OrderEventProcessorRegistry {
    private final Map<OrderEventType, OrderEventProcessor<?>> processorMap = new EnumMap<>(OrderEventType.class);

    @Autowired
    public OrderEventProcessorRegistry(List<OrderEventProcessor<?>> processors) {
        processors.forEach(processor -> {
            if (processor instanceof OrderCreatedEventProcessor) {
                processorMap.put(OrderEventType.ORDER_CREATED, processor);
                return;
            }
            if (processor instanceof OrderDeliveredEventProcessor) {
                processorMap.put(OrderEventType.ORDER_DELIVERED, processor);
                return;
            }
            if (processor instanceof OrderCancelledEventProcessor) {
                processorMap.put(OrderEventType.ORDER_CANCELLED, processor);
                return;
            }
            if (processor instanceof OrderErrorEventEventProcessor) {
                processorMap.put(OrderEventType.ORDER_PROCESSING_FAILED, processor);
            }
        });
    }

    @SuppressWarnings("unchecked")
    public <T> OrderEventProcessor<T> getProcessor(OrderEventType eventType) {
        return (OrderEventProcessor<T>) processorMap.get(eventType);
    }
}
