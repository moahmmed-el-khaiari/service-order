package com.restaurant.orderservice.dto;

import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class SauceDto {

    private Long id;
    private String name;
    private Double extraPrice;
}
