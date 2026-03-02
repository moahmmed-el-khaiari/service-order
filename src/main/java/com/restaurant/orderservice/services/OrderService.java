package com.restaurant.orderservice.services;
import com.restaurant.orderservice.dto.CreateOrderRequest;
import com.restaurant.orderservice.dto.OrderResponseDTO;
import com.restaurant.orderservice.enums.OrderStatus;

import java.util.List;
public interface OrderService {
    OrderResponseDTO createOrder(CreateOrderRequest request);

    OrderResponseDTO getById(Long id);

    List<OrderResponseDTO> getAll();

    OrderResponseDTO updateStatus(Long id, OrderStatus status);

    void delete(Long id);
    OrderResponseDTO markAsPending(Long id);
    OrderResponseDTO markAsPaid(Long id);
}
