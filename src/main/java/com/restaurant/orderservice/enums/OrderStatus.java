package com.restaurant.orderservice.enums;

public enum OrderStatus {
    CREATED,
    PENDING,
    PAID,
    EXPIRED,// Commande créée (en attente)
    VALIDATED,     // Livrée au client
    CANCELLED       // Annulée
}
