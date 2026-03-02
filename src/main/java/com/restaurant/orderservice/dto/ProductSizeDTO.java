package com.restaurant.orderservice.dto;


import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class ProductSizeDTO {

    private Long id;
    private String size;
    private Double price;
}
