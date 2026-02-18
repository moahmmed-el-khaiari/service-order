package com.restaurant.orderservice.dto;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class OrderItemDTO {
    private Long productId;
    private String productName;
    private Double unitPrice;
    private Integer quantity;
    private Double totalPrice;
}
