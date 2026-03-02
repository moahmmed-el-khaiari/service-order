package com.restaurant.orderservice.services;
import com.restaurant.orderservice.client.MenuClient;
import com.restaurant.orderservice.client.ProductClient;
import com.restaurant.orderservice.client.SauceClient;
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
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;

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
    private final SauceClient sauceClient;

    @Override
    public OrderResponseDTO createOrder(CreateOrderRequest request) {

        Order order = Order.builder()
                .createdAt(LocalDateTime.now())
                .status(OrderStatus.CREATED)
                .customerPhone(request.getCustomerPhone())
                .totalAmount(0.0)

                .build();

        List<OrderItem> items = new ArrayList<>();
        double total = 0.0;

        // 🔥 PRODUCTS
        // 🔥 PRODUCTS
        if (request.getProducts() != null) {

            for (ProductItemRequest p : request.getProducts()) {

                ProductDto product = productClient.getProductById(p.getProductId());

                if (product == null)
                    throw new RuntimeException("Product not found");

                if (product.getSizes() == null || product.getSizes().isEmpty())
                    throw new RuntimeException("Product has no sizes configured");

                // ✅ 1️⃣ Trouver prix selon taille
                Double unitPrice = product.getSizes().stream()
                        .filter(s -> s.getSize().equalsIgnoreCase(p.getSize()))
                        .findFirst()
                        .orElseThrow(() -> new RuntimeException("Size not found"))
                        .getPrice();



                double sauceExtraTotal = 0.0;

                // ✅ 2️⃣ Gérer sauces supplémentaires
                if (p.getExtraSauceIds() != null && !p.getExtraSauceIds().isEmpty()) {

                    int FREE_SAUCE_LIMIT = 2;

                    if (p.getExtraSauceIds().size() > FREE_SAUCE_LIMIT) {

                        List<Long> paidSauces =
                                p.getExtraSauceIds()
                                        .subList(FREE_SAUCE_LIMIT, p.getExtraSauceIds().size());

                        for (Long sauceId : paidSauces) {

                            SauceDto sauce = sauceClient.getSauceById(sauceId);

                            if (sauce == null || sauce.getExtraPrice() == null)
                                throw new RuntimeException("Invalid sauce price");

                            sauceExtraTotal += sauce.getExtraPrice();
                        }
                    }
                }

                // ✅ 3️⃣ Calcul total ligne
                double lineTotal =
                        (unitPrice * p.getQuantity())
                                + (sauceExtraTotal * p.getQuantity());

                OrderItem item = OrderItem.builder()
                        .productId(product.getId())
                        .productName(product.getName() + " - " + p.getSize())
                        .price(unitPrice)
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

    @Transactional
    public OrderResponseDTO markAsPending(Long id) {

        Order order = orderRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Order not found"));

        if(order.getStatus() != OrderStatus.CREATED)
            throw new RuntimeException("Invalid state transition");

        order.setStatus(OrderStatus.PENDING);

        return orderMapper.order_to_Dto(orderRepository.save(order));
    }
    @Transactional
    public OrderResponseDTO markAsPaid(Long id) {

        Order order = orderRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Order not found"));

        if(order.getStatus() == OrderStatus.PAID)
            return orderMapper.order_to_Dto(order);

        if(order.getStatus() == OrderStatus.EXPIRED)
            throw new RuntimeException("Order expired");

        order.setStatus(OrderStatus.PAID);

        return orderMapper.order_to_Dto(orderRepository.save(order));
    }


    @Scheduled(fixedRate = 60000)
    @Transactional
    public void expireOrders() {

        List<Order> orders = orderRepository.findByStatusAndCreatedAtBefore(
                OrderStatus.PENDING,
                LocalDateTime.now().minusMinutes(5)
        );

        if(!orders.isEmpty()) {
            for(Order order : orders) {
                order.setStatus(OrderStatus.EXPIRED);
            }

            orderRepository.saveAll(orders);
        }
    }
    @Scheduled(fixedRate = 60000)
    @Transactional
    public void deleteExpiredOrders() {

        List<Order> orders = orderRepository.findByStatusAndCreatedAtBefore(
                OrderStatus.EXPIRED,
                LocalDateTime.now().minusMinutes(10)
        );

        if(!orders.isEmpty()) {
            orderRepository.deleteAll(orders);
        }
    }
}
