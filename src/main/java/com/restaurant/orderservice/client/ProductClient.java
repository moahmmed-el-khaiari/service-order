package com.restaurant.orderservice.client;

import com.restaurant.orderservice.dto.ProductDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(
        name = "product-service",
        url = "http://localhost:8081/v1/produits" // URL de  microservice filiere
)
public interface ProductClient {

    @GetMapping("{id}")
    ProductDto getProductById(@PathVariable("id") Long id);



}
