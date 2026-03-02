package com.restaurant.orderservice.dto;
import com.restaurant.orderservice.enums.OrderStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
public class OrderResponseDTO {
    private Long id;
    private Double totalAmount;
    private OrderStatus status;
    private List<OrderItemDTO> items;
    private String customerPhone;
}
