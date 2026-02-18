package com.restaurant.orderservice.dto;

import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class ProductItemRequest {
    private Long productId;
    private Integer quantity;
}
