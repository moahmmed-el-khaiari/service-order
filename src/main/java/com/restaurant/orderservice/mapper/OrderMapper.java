package com.restaurant.orderservice.mapper;

import com.restaurant.orderservice.dto.CreateOrderRequest;
import com.restaurant.orderservice.dto.OrderItemDTO;
import com.restaurant.orderservice.dto.OrderResponseDTO;
import com.restaurant.orderservice.entity.Order;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
@RequiredArgsConstructor
@Component
public class OrderMapper {
    private final OrderItemMapper itemMapper;

    // ⚠️ On ne copie PAS automatiquement CreateOrderRequest vers Order
    // car Order est construit dans le service (createdAt, status, total, etc.)
    public Order Dto_to_order(CreateOrderRequest request) {
        return Order.builder().build();
    }

    public OrderResponseDTO order_to_Dto(Order order) {

        if (order == null) {
            return null;
        }

        OrderResponseDTO dto = new OrderResponseDTO();

        dto.setId(order.getId());
        dto.setTotalAmount(order.getTotalAmount());

        // 🔥 Enum -> String sécurisé
        dto.setStatus(
                order.getStatus() != null
                        ? order.getStatus()
                        : null
        );
        // 🔥 AJOUT ICI
        dto.setCustomerPhone(order.getCustomerPhone());

        // 🔥 Sécuriser la liste
        List<OrderItemDTO> items = order.getItems() != null
                ? order.getItems()
                .stream()
                .map(itemMapper::entityToDto)
                .collect(Collectors.toList())
                : Collections.emptyList();

        dto.setItems(items);

        return dto;
    }
}
