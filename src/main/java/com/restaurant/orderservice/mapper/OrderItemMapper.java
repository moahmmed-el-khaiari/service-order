package com.restaurant.orderservice.mapper;
import com.restaurant.orderservice.dto.MenuItemRequest;
import com.restaurant.orderservice.dto.OrderItemDTO;
import com.restaurant.orderservice.dto.ProductItemRequest;
import com.restaurant.orderservice.entity.OrderItem;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Component;

@Component
public class OrderItemMapper {
    // ================================
    // From ProductItemRequest -> Entity
    // ================================
    public OrderItem productRequestToEntity(ProductItemRequest request) {
        OrderItem item = new OrderItem();
        BeanUtils.copyProperties(request, item);
        return item;
    }

    // ================================
    // From MenuItemRequest -> Entity
    // ================================
    public OrderItem menuRequestToEntity(MenuItemRequest request) {
        OrderItem item = new OrderItem();
        BeanUtils.copyProperties(request, item);
        return item;
    }

    // ================================
    // Entity -> DTO
    // ================================
    public OrderItemDTO entityToDto(OrderItem item) {

        OrderItemDTO dto = new OrderItemDTO();

        dto.setProductId(item.getProductId());
        dto.setProductName(item.getProductName());
        dto.setQuantity(item.getQuantity());

        // 🔥 mapping manuel
        dto.setUnitPrice(item.getPrice());
        dto.setTotalPrice(item.getTotal());

        return dto;
    }


}
