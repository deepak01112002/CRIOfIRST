package com.grocery.api.config;

import com.grocery.api.model.Customer;
import com.grocery.api.model.GroceryItem;
import com.grocery.api.model.Order;
import com.grocery.api.repository.CustomerRepository;
import com.grocery.api.repository.GroceryItemRepository;
import com.grocery.api.repository.OrderRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

@Configuration
public class DataInitializer {

    @Bean
    public CommandLineRunner initData(
            @Autowired CustomerRepository customerRepository,
            @Autowired GroceryItemRepository groceryItemRepository,
            @Autowired OrderRepository orderRepository) {

        return args -> {
            // Create customers
            Customer customer1 = new Customer("John Doe", "john@example.com", "123 Main St, City", "555-1234");
            Customer customer2 = new Customer("Jane Smith", "jane@example.com", "456 Oak St, Town", "555-5678");
            Customer customer3 = new Customer("Bob Johnson", "bob@example.com", "789 Pine St, Village", "555-9012");

            customerRepository.save(customer1);
            customerRepository.save(customer2);
            customerRepository.save(customer3);

            // Create grocery items
            GroceryItem item1 = new GroceryItem("Apples", "Fruits", new BigDecimal("2.99"), 100);
            GroceryItem item2 = new GroceryItem("Bananas", "Fruits", new BigDecimal("1.99"), 150);
            GroceryItem item3 = new GroceryItem("Milk", "Dairy", new BigDecimal("3.49"), 50);
            GroceryItem item4 = new GroceryItem("Bread", "Bakery", new BigDecimal("2.49"), 30);
            GroceryItem item5 = new GroceryItem("Chicken", "Meat", new BigDecimal("5.99"), 20);

            groceryItemRepository.save(item1);
            groceryItemRepository.save(item2);
            groceryItemRepository.save(item3);
            groceryItemRepository.save(item4);
            groceryItemRepository.save(item5);

            // Create orders
            // Order 1
            Order order1 = new Order();
            order1.setCustomer(customer1);
            order1.setOrderDate(LocalDateTime.now().minusDays(2));
            order1.setTotalPrice(new BigDecimal("6.48"));

            Order savedOrder1 = orderRepository.save(order1);

            // Add items to order 1
            Set<GroceryItem> order1Items = new HashSet<>();
            order1Items.add(item1);
            order1Items.add(item3);
            savedOrder1.setItems(order1Items);
            orderRepository.save(savedOrder1);

            // Order 2
            Order order2 = new Order();
            order2.setCustomer(customer2);
            order2.setOrderDate(LocalDateTime.now().minusDays(1));
            order2.setTotalPrice(new BigDecimal("10.47"));

            Order savedOrder2 = orderRepository.save(order2);

            // Add items to order 2
            Set<GroceryItem> order2Items = new HashSet<>();
            order2Items.add(item2);
            order2Items.add(item4);
            order2Items.add(item5);
            savedOrder2.setItems(order2Items);
            orderRepository.save(savedOrder2);
        };
    }
}
