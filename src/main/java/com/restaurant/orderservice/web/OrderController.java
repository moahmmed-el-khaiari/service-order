package com.restaurant.orderservice.web;
import com.restaurant.orderservice.dto.CreateOrderRequest;
import com.restaurant.orderservice.dto.OrderResponseDTO;
import com.restaurant.orderservice.enums.OrderStatus;
import com.restaurant.orderservice.services.OrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
@CrossOrigin("*")
@RestController
@RequestMapping("/v1/orders")
@RequiredArgsConstructor
public class OrderController {
    private final OrderService orderService;

    @PostMapping
    public ResponseEntity<OrderResponseDTO> create(
            @RequestBody CreateOrderRequest request) {

        return ResponseEntity.ok(orderService.createOrder(request));
    }

    @GetMapping
    public ResponseEntity<List<OrderResponseDTO>> getAll() {
        return ResponseEntity.ok(orderService.getAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<OrderResponseDTO> getById(@PathVariable Long id) {
        return ResponseEntity.ok(orderService.getById(id));
    }

    @PutMapping("/{id}/status")
    public ResponseEntity<OrderResponseDTO> updateStatus(
            @PathVariable Long id,
            @RequestParam OrderStatus status) {

        return ResponseEntity.ok(orderService.updateStatus(id, status));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        orderService.delete(id);
        return ResponseEntity.ok().build();
    }
    @PutMapping("/{id}/mark-pending")
    public ResponseEntity<OrderResponseDTO> markAsPending(@PathVariable Long id) {
        return ResponseEntity.ok(orderService.markAsPending(id));
    }

    @PutMapping("/{id}/mark-paid")
    public ResponseEntity<OrderResponseDTO> markAsPaid(@PathVariable Long id) {
        return ResponseEntity.ok(orderService.markAsPaid(id));
    }
}
