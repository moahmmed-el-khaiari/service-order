package com.restaurant.orderservice.dto;
import lombok.*;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class ProductDto {


    private Long id;
    private String name;
    private String category;
    private Boolean available;
    private String description;
    private String imageUrl;

    private List<ProductSizeDTO> sizes;
    private List<Long> sauceIds;



}

