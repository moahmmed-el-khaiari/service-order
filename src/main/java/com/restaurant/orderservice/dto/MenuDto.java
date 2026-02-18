package com.restaurant.orderservice.dto;

import lombok.*;

import java.util.List;
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Data
public class MenuDto {
    private Long id;
    private String name;
    private Double price;
    private Boolean available;
    private String description;
    private String imageUrl;

    private List<ProductDto> products;
}
