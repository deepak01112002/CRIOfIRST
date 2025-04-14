package com.grocery.api.dto;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

public class OrderDTO {
    
    private Long id;
    
    @NotNull(message = "Customer ID is required")
    private Long customerId;
    
    @NotEmpty(message = "Order must contain at least one item")
    private Set<Long> itemIds = new HashSet<>();
    
    private LocalDateTime orderDate;
    
    @NotNull(message = "Total price is required")
    @Positive(message = "Total price must be positive")
    private BigDecimal totalPrice;
    
    // For response only
    private String customerName;
    private Set<GroceryItemDTO> items = new HashSet<>();
    
    // Constructors
    public OrderDTO() {
        this.orderDate = LocalDateTime.now();
    }
    
    public OrderDTO(Long id, Long customerId, Set<Long> itemIds, LocalDateTime orderDate, BigDecimal totalPrice) {
        this.id = id;
        this.customerId = customerId;
        this.itemIds = itemIds;
        this.orderDate = orderDate;
        this.totalPrice = totalPrice;
    }
    
    // Getters and Setters
    public Long getId() {
        return id;
    }
    
    public void setId(Long id) {
        this.id = id;
    }
    
    public Long getCustomerId() {
        return customerId;
    }
    
    public void setCustomerId(Long customerId) {
        this.customerId = customerId;
    }
    
    public Set<Long> getItemIds() {
        return itemIds;
    }
    
    public void setItemIds(Set<Long> itemIds) {
        this.itemIds = itemIds;
    }
    
    public LocalDateTime getOrderDate() {
        return orderDate;
    }
    
    public void setOrderDate(LocalDateTime orderDate) {
        this.orderDate = orderDate;
    }
    
    public BigDecimal getTotalPrice() {
        return totalPrice;
    }
    
    public void setTotalPrice(BigDecimal totalPrice) {
        this.totalPrice = totalPrice;
    }
    
    public String getCustomerName() {
        return customerName;
    }
    
    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }
    
    public Set<GroceryItemDTO> getItems() {
        return items;
    }
    
    public void setItems(Set<GroceryItemDTO> items) {
        this.items = items;
    }
}
