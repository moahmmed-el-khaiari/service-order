package com.restaurant.orderservice.repository;
import com.restaurant.orderservice.entity.Order;
import com.restaurant.orderservice.enums.OrderStatus;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
public interface OrderRepository extends JpaRepository<Order, Long>{
    //  Trouver commandes par statut
    List<Order> findByStatus(OrderStatus status);
    @Query("SELECT o FROM Order o LEFT JOIN FETCH o.items")
    List<Order> findAllWithItems();
    @EntityGraph(attributePaths = {"items"})
    List<Order> findAll();
    //  Trouver commandes triées par date (desc)
    List<Order> findAllByOrderByCreatedAtDesc();
}
