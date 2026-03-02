package com.restaurant.orderservice.dto;

import lombok.*;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class ProductItemRequest {
    private Long productId;
    private Integer quantity;
    private String size;

    private List<Long> extraSauceIds;
}
