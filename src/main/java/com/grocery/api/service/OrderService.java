package com.grocery.api.service;

import com.grocery.api.dto.GroceryItemDTO;
import com.grocery.api.dto.OrderDTO;
import com.grocery.api.exception.ResourceNotFoundException;
import com.grocery.api.model.Customer;
import com.grocery.api.model.GroceryItem;
import com.grocery.api.model.Order;
import com.grocery.api.repository.CustomerRepository;
import com.grocery.api.repository.GroceryItemRepository;
import com.grocery.api.repository.OrderRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class OrderService {

    private final OrderRepository orderRepository;
    private final CustomerRepository customerRepository;
    private final GroceryItemRepository groceryItemRepository;

    @Autowired
    public OrderService(OrderRepository orderRepository, 
                        CustomerRepository customerRepository, 
                        GroceryItemRepository groceryItemRepository) {
        this.orderRepository = orderRepository;
        this.customerRepository = customerRepository;
        this.groceryItemRepository = groceryItemRepository;
    }

    public List<OrderDTO> getAllOrders() {
        return orderRepository.findAll().stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    public OrderDTO getOrderById(Long id) {
        Order order = orderRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Order not found with id: " + id));
        return convertToDTO(order);
    }

    public List<OrderDTO> getOrdersByCustomerId(Long customerId) {
        return orderRepository.findByCustomerId(customerId).stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    public List<OrderDTO> getOrdersByDateRange(LocalDateTime startDate, LocalDateTime endDate) {
        return orderRepository.findByOrderDateBetween(startDate, endDate).stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    @Transactional
    public OrderDTO createOrder(OrderDTO orderDTO) {
        Order order = new Order();
        
        // Set customer
        Customer customer = customerRepository.findById(orderDTO.getCustomerId())
                .orElseThrow(() -> new ResourceNotFoundException("Customer not found with id: " + orderDTO.getCustomerId()));
        order.setCustomer(customer);
        
        // Set items
        Set<GroceryItem> items = new HashSet<>();
        for (Long itemId : orderDTO.getItemIds()) {
            GroceryItem item = groceryItemRepository.findById(itemId)
                    .orElseThrow(() -> new ResourceNotFoundException("Grocery item not found with id: " + itemId));
            items.add(item);
        }
        order.setItems(items);
        
        // Set other fields
        order.setOrderDate(LocalDateTime.now());
        order.setTotalPrice(orderDTO.getTotalPrice());
        
        Order savedOrder = orderRepository.save(order);
        return convertToDTO(savedOrder);
    }

    @Transactional
    public OrderDTO updateOrder(Long id, OrderDTO orderDTO) {
        Order existingOrder = orderRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Order not found with id: " + id));
        
        // Update customer if changed
        if (!existingOrder.getCustomer().getId().equals(orderDTO.getCustomerId())) {
            Customer customer = customerRepository.findById(orderDTO.getCustomerId())
                    .orElseThrow(() -> new ResourceNotFoundException("Customer not found with id: " + orderDTO.getCustomerId()));
            existingOrder.setCustomer(customer);
        }
        
        // Update items if changed
        Set<Long> newItemIds = orderDTO.getItemIds();
        Set<Long> existingItemIds = existingOrder.getItems().stream()
                .map(GroceryItem::getId)
                .collect(Collectors.toSet());
        
        if (!newItemIds.equals(existingItemIds)) {
            Set<GroceryItem> newItems = new HashSet<>();
            for (Long itemId : newItemIds) {
                GroceryItem item = groceryItemRepository.findById(itemId)
                        .orElseThrow(() -> new ResourceNotFoundException("Grocery item not found with id: " + itemId));
                newItems.add(item);
            }
            existingOrder.setItems(newItems);
        }
        
        // Update other fields
        existingOrder.setTotalPrice(orderDTO.getTotalPrice());
        
        Order updatedOrder = orderRepository.save(existingOrder);
        return convertToDTO(updatedOrder);
    }

    public void deleteOrder(Long id) {
        if (!orderRepository.existsById(id)) {
            throw new ResourceNotFoundException("Order not found with id: " + id);
        }
        orderRepository.deleteById(id);
    }

    // Helper methods to convert between Entity and DTO
    private OrderDTO convertToDTO(Order order) {
        OrderDTO orderDTO = new OrderDTO();
        orderDTO.setId(order.getId());
        orderDTO.setCustomerId(order.getCustomer().getId());
        orderDTO.setCustomerName(order.getCustomer().getName());
        orderDTO.setOrderDate(order.getOrderDate());
        orderDTO.setTotalPrice(order.getTotalPrice());
        
        // Set item IDs
        Set<Long> itemIds = order.getItems().stream()
                .map(GroceryItem::getId)
                .collect(Collectors.toSet());
        orderDTO.setItemIds(itemIds);
        
        // Set item details
        Set<GroceryItemDTO> itemDTOs = order.getItems().stream()
                .map(item -> new GroceryItemDTO(
                        item.getId(),
                        item.getName(),
                        item.getCategory(),
                        item.getPrice(),
                        item.getQuantity()
                ))
                .collect(Collectors.toSet());
        orderDTO.setItems(itemDTOs);
        
        return orderDTO;
    }
}
