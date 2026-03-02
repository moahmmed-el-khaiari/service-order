package com.restaurant.orderservice.client;

import com.restaurant.orderservice.dto.ProductDto;
import com.restaurant.orderservice.dto.SauceDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(
        name = "sauces-service",
        url = "http://localhost:8081/v1/sauces" // URL de  microservice filiere
)
public interface SauceClient {

    @GetMapping("{id}")
    SauceDto getSauceById(@PathVariable("id") Long id);
}

