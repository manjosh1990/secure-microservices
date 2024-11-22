package com.manjosh.labs.orderservice.domain;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.manjosh.labs.orderservice.domain.models.*;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

@Service
public class OrderEventService {
    private static final Logger log = LoggerFactory.getLogger(OrderEventService.class);
    private final OrderEventRepository orderEventRepository;
    private final ObjectMapper objectMapper;
    private final OrderEventPublisher orderEventPublisher;
    private final OrderEventProcessorRegistry processorRegistry;

    public OrderEventService(
            OrderEventRepository orderEventRepository,
            ObjectMapper objectMapper,
            OrderEventPublisher orderEventPublisher,
            OrderEventProcessorRegistry processorRegistry) {
        this.orderEventRepository = orderEventRepository;
        this.objectMapper = objectMapper;
        this.orderEventPublisher = orderEventPublisher;
        this.processorRegistry = processorRegistry;
    }

    void save(OrderCreatedEvent event) {
        OrderEventEntity orderEvent = new OrderEventEntity();
        orderEvent.setEventId(event.eventId());
        orderEvent.setEventType(OrderEventType.ORDER_CREATED);
        orderEvent.setOrderNumber(event.orderNumber());
        orderEvent.setCreatedAt(event.createdAt());
        orderEvent.setPayload(toJsonPayload(event));
        this.orderEventRepository.save(orderEvent);
    }

    void save(OrderDeliveredEvent event) {
        OrderEventEntity orderEvent = new OrderEventEntity();
        orderEvent.setEventId(event.eventId());
        orderEvent.setEventType(OrderEventType.ORDER_DELIVERED);
        orderEvent.setOrderNumber(event.orderNumber());
        orderEvent.setCreatedAt(event.createdAt());
        orderEvent.setPayload(toJsonPayload(event));
        this.orderEventRepository.save(orderEvent);
    }

    void save(OrderCancelledEvent event) {
        OrderEventEntity orderEvent = new OrderEventEntity();
        orderEvent.setEventId(event.eventId());
        orderEvent.setEventType(OrderEventType.ORDER_CANCELLED);
        orderEvent.setOrderNumber(event.orderNumber());
        orderEvent.setCreatedAt(event.createdAt());
        orderEvent.setPayload(toJsonPayload(event));
        this.orderEventRepository.save(orderEvent);
    }

    void save(OrderErrorEvent event) {
        OrderEventEntity orderEvent = new OrderEventEntity();
        orderEvent.setEventId(event.eventId());
        orderEvent.setEventType(OrderEventType.ORDER_PROCESSING_FAILED);
        orderEvent.setOrderNumber(event.orderNumber());
        orderEvent.setCreatedAt(event.createdAt());
        orderEvent.setPayload(toJsonPayload(event));
        this.orderEventRepository.save(orderEvent);
    }

    private String toJsonPayload(Object object) {
        try {
            return objectMapper.writeValueAsString(object);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

    public void publishOrderEvents() {
        Sort sort = Sort.by("createdAt").ascending();
        List<OrderEventEntity> orderEvents = this.orderEventRepository.findAll(sort);
        log.info("Found {} order events to publish", orderEvents.size());
        for (OrderEventEntity orderEvent : orderEvents) {
            this.publishEvent(orderEvent);
            orderEventRepository.delete(orderEvent);
        }
    }

    private void publishEvent(OrderEventEntity event) {
        OrderEventProcessor<?> processor = processorRegistry.getProcessor(event.getEventType());
        if (processor == null) {
            log.warn("Unsupported OrderEventType: {}", event.getEventType());
            return;
        }
        processor.process(event, orderEventPublisher, objectMapper);
    }

    private <T> T fromJsonPayload(String json, Class<T> type) {
        try {
            return objectMapper.readValue(json, type);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }
}
