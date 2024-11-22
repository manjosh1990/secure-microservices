package com.manjosh.labs.orderservice.rabbitmqdemo;

// import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Service;

@Service
public class RabbitMQListner {

    //    @RabbitListener(queues = "${orders.new-orders-queue}")
    //    public void handleNewOrder(MyPayload payload) {
    //        System.out.println("New order received: " + payload.content());
    //    }
    //
    //    @RabbitListener(queues = "${orders.delivered-orders-queue}")
    //    public void handleDeliveredOrder(MyPayload payload) {
    //        System.out.println("Delivered order: " + payload.content());
    //    }
}
