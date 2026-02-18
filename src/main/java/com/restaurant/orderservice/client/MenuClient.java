package com.restaurant.orderservice.client;
import com.restaurant.orderservice.dto.MenuDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

@FeignClient(
        name = "menu-service",
        url = "http://localhost:8081/v1/menus"
)
public interface MenuClient {
    @GetMapping("{id}")
    MenuDto getMenuById(@PathVariable("id") Long id);
}
