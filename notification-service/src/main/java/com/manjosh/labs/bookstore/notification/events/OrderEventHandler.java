package com.manjosh.labs.bookstore.notification.events;

import com.manjosh.labs.bookstore.notification.domain.NotificationService;
import com.manjosh.labs.bookstore.notification.domain.OrderEventEntity;
import com.manjosh.labs.bookstore.notification.domain.OrderEventRepository;
import com.manjosh.labs.bookstore.notification.domain.models.OrderCancelledEvent;
import com.manjosh.labs.bookstore.notification.domain.models.OrderCreatedEvent;
import com.manjosh.labs.bookstore.notification.domain.models.OrderDeliveredEvent;
import com.manjosh.labs.bookstore.notification.domain.models.OrderErrorEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

@Component
public class OrderEventHandler {
    private static final Logger log = LoggerFactory.getLogger(OrderEventHandler.class);
    private final NotificationService notificationService;
    private final OrderEventRepository orderEventRepository;

    public OrderEventHandler(NotificationService notificationService, OrderEventRepository orderEventRepository) {
        this.notificationService = notificationService;
        this.orderEventRepository = orderEventRepository;
    }

    @RabbitListener(queues = "${notifications.new-orders-queue}")
    void handleOrderCreatedEvent(OrderCreatedEvent event) {
        log.info("Order created event received: {}", event);
        if (orderEventRepository.existsByEventId(event.eventId())) {
            log.warn("Received duplicate order created event: {}", event);
            return;
        }
        notificationService.sendOrderCreatedNotification(event);
        OrderEventEntity orderEvent = new OrderEventEntity(event.eventId());
        orderEventRepository.save(orderEvent);
    }

    @RabbitListener(queues = "${notifications.delivered-orders-queue}")
    void handleOrderDeliveredEvent(OrderDeliveredEvent event) {
        log.info("Order delivered event received: {}", event);
        if (orderEventRepository.existsByEventId(event.eventId())) {
            log.warn("Received duplicate order delivered event: {}", event);
            return;
        }
        notificationService.sendOrderDeliveredNotification(event);
        OrderEventEntity orderEvent = new OrderEventEntity(event.eventId());
        orderEventRepository.save(orderEvent);
    }

    @RabbitListener(queues = "${notifications.cancelled-orders-queue}")
    void handleOrderCancelledEvent(OrderCancelledEvent event) {
        log.info("Order cancelled event received: {}", event);
        if (orderEventRepository.existsByEventId(event.eventId())) {
            log.warn("Received cancelled order delivered event: {}", event);
            return;
        }
        notificationService.sendOrderCancelledNotification(event);
        OrderEventEntity orderEvent = new OrderEventEntity(event.eventId());
        orderEventRepository.save(orderEvent);
    }

    @RabbitListener(queues = "${notifications.error-orders-queue}")
    void handleOrderErrorEvent(OrderErrorEvent event) {
        log.info("Order error event received: {}", event);
        if (orderEventRepository.existsByEventId(event.eventId())) {
            log.warn("Received error order delivered event: {}", event);
            return;
        }
        notificationService.sendOrderErrorEventNotification(event);
        OrderEventEntity orderEvent = new OrderEventEntity(event.eventId());
        orderEventRepository.save(orderEvent);
    }
}
