package com.restaurant.orderservice.services;
import com.restaurant.orderservice.client.MenuClient;
import com.restaurant.orderservice.client.ProductClient;
import com.restaurant.orderservice.dto.*;
import com.restaurant.orderservice.entity.Order;
import com.restaurant.orderservice.entity.OrderItem;
import com.restaurant.orderservice.enums.OrderStatus;
import com.restaurant.orderservice.mapper.OrderItemMapper;
import com.restaurant.orderservice.mapper.OrderMapper;
import com.restaurant.orderservice.repository.OrderItemRepository;
import com.restaurant.orderservice.repository.OrderRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class OrderServiceImpl implements OrderService {
    private final OrderRepository orderRepository;
    private final ProductClient productClient;
    private final MenuClient menuClient;
    private final OrderMapper orderMapper;
    private final OrderItemMapper itemMapper;

    @Override
    public OrderResponseDTO createOrder(CreateOrderRequest request) {

        Order order = Order.builder()
                .createdAt(LocalDateTime.now())
                .status(OrderStatus.PENDING)
                .totalAmount(0.0)

                .build();

        List<OrderItem> items = new ArrayList<>();
        double total = 0.0;

        // 🔥 PRODUCTS
        if (request.getProducts() != null) {
            for (ProductItemRequest p : request.getProducts()) {

                ProductDto product = productClient.getProductById(p.getProductId());

                double lineTotal = product.getPrice() * p.getQuantity();

                OrderItem item = OrderItem.builder()
                        .productId(product.getId())
                        .productName(product.getName())
                        .price(product.getPrice())
                        .quantity(p.getQuantity())
                        .total(lineTotal)
                        .order(order)
                        .build();

                items.add(item);
                total += lineTotal;
            }
        }

        // 🔥 MENUS
        if (request.getMenus() != null) {
            for (MenuItemRequest m : request.getMenus()) {

                MenuDto menu = menuClient.getMenuById(m.getMenuId());

                double lineTotal = menu.getPrice() * m.getQuantity();

                OrderItem item = OrderItem.builder()
                        .productId(menu.getId())
                        .productName(menu.getName())
                        .price(menu.getPrice())
                        .quantity(m.getQuantity())
                        .total(lineTotal)
                        .order(order)
                        .build();

                items.add(item);
                total += lineTotal;
            }
        }

        order.setItems(items);
        order.setTotalAmount(total);

        Order saved = orderRepository.save(order);

        return orderMapper.order_to_Dto(saved);
    }

    @Override
    public OrderResponseDTO getById(Long id) {
        Order order = orderRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Order not found"));
        return orderMapper.order_to_Dto(order);
    }

    @Override
    public List<OrderResponseDTO> getAll() {
        return orderRepository.findAll()
                .stream()
                .map(orderMapper::order_to_Dto)
                .toList();
    }

    @Override
    public OrderResponseDTO updateStatus(Long id, OrderStatus status) {
        Order order = orderRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Order not found"));

        order.setStatus(status);

        return orderMapper.order_to_Dto(orderRepository.save(order));
    }

    @Override
    public void delete(Long id) {
        orderRepository.deleteById(id);
    }
}
