package br.com.fiap.postech.domain.exceptions

class OrderNotFoundException(orderId: String) : RuntimeException("Order with ID: $orderId not found.")