package com.manjosh.labs.orderservice.domain;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

interface OrderEventProcessor<T> {
    void process(OrderEventEntity event, OrderEventPublisher orderEventPublisher, ObjectMapper objectMapper);

    default T fromJsonPayload(String json, ObjectMapper objectMapper, Class<T> type) {
        try {
            return objectMapper.readValue(json, type);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }
}
