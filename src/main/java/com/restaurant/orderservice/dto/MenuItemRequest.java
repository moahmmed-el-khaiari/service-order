package com.restaurant.orderservice.dto;
import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class MenuItemRequest {
    private Long menuId;
    private Integer quantity;
}
