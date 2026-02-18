package com.restaurant.orderservice.dto;

import lombok.*;

import java.util.List;
@Getter
@Setter

@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CreateOrderRequest {
    private List<ProductItemRequest> products;
    private List<MenuItemRequest> menus;

}
