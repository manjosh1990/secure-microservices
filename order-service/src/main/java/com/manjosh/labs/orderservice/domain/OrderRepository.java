package com.manjosh.labs.orderservice.domain;

import com.manjosh.labs.orderservice.domain.models.OrderStatus;
import com.manjosh.labs.orderservice.domain.models.OrderSummary;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

interface OrderRepository extends JpaRepository<OrderEntity, Long> {
    List<OrderEntity> findByStatus(OrderStatus orderStatus);

    Optional<OrderEntity> findByOrderNumber(String orderNumber);

    default void updateOrderStatus(String orderNumber, OrderStatus orderStatus) {
        OrderEntity orderEntity = this.findByOrderNumber(orderNumber).orElseThrow();
        orderEntity.setStatus(orderStatus);
        this.save(orderEntity);
    }

    @Query(
            """
            select new com.manjosh.labs.orderservice.domain.models.OrderSummary(o.orderNumber, o.status) from OrderEntity o
            where o.userName = :userName
            """)
    List<OrderSummary> findByUserName(String userName);

    @Query(
            """
            select distinct o from OrderEntity o
            left join fetch o.items
            where o.userName = :userName
            and o.orderNumber = :orderNumber
            """)
    Optional<OrderEntity> findByUserNameAndOrderNumber(String userName, String orderNumber);
}
