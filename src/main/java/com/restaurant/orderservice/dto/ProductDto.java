package com.restaurant.orderservice.dto;
import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class ProductDto {

    private Long id;
    private String name;
    private String category; // STRING car enum distant
    private Double price;
    private Boolean available;
    private String description;
    private String imageUrl;
}

