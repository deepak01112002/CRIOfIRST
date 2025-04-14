package com.grocery.api.service;

import com.grocery.api.dto.CustomerDTO;
import com.grocery.api.exception.ResourceNotFoundException;
import com.grocery.api.model.Customer;
import com.grocery.api.repository.CustomerRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class CustomerServiceTest {

    @Mock
    private CustomerRepository customerRepository;

    @InjectMocks
    private CustomerService customerService;

    private Customer customer;
    private CustomerDTO customerDTO;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        customer = new Customer();
        customer.setId(1L);
        customer.setName("John Doe");
        customer.setEmail("john@example.com");
        customer.setAddress("123 Main St");
        customer.setPhone("555-1234");

        customerDTO = new CustomerDTO();
        customerDTO.setId(1L);
        customerDTO.setName("John Doe");
        customerDTO.setEmail("john@example.com");
        customerDTO.setAddress("123 Main St");
        customerDTO.setPhone("555-1234");
    }

    @Test
    void getAllCustomers_ShouldReturnAllCustomers() {
        // Arrange
        when(customerRepository.findAll()).thenReturn(Arrays.asList(customer));

        // Act
        List<CustomerDTO> result = customerService.getAllCustomers();

        // Assert
        assertEquals(1, result.size());
        assertEquals(customerDTO.getId(), result.get(0).getId());
        assertEquals(customerDTO.getName(), result.get(0).getName());
        verify(customerRepository, times(1)).findAll();
    }

    @Test
    void getCustomerById_WithValidId_ShouldReturnCustomer() {
        // Arrange
        when(customerRepository.findById(1L)).thenReturn(Optional.of(customer));

        // Act
        CustomerDTO result = customerService.getCustomerById(1L);

        // Assert
        assertNotNull(result);
        assertEquals(customerDTO.getId(), result.getId());
        assertEquals(customerDTO.getName(), result.getName());
        verify(customerRepository, times(1)).findById(1L);
    }

    @Test
    void getCustomerById_WithInvalidId_ShouldThrowException() {
        // Arrange
        when(customerRepository.findById(99L)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(ResourceNotFoundException.class, () -> {
            customerService.getCustomerById(99L);
        });
        verify(customerRepository, times(1)).findById(99L);
    }

    @Test
    void createCustomer_ShouldReturnCreatedCustomer() {
        // Arrange
        when(customerRepository.save(any(Customer.class))).thenReturn(customer);

        // Act
        CustomerDTO result = customerService.createCustomer(customerDTO);

        // Assert
        assertNotNull(result);
        assertEquals(customerDTO.getId(), result.getId());
        assertEquals(customerDTO.getName(), result.getName());
        verify(customerRepository, times(1)).save(any(Customer.class));
    }

    @Test
    void updateCustomer_WithValidId_ShouldReturnUpdatedCustomer() {
        // Arrange
        when(customerRepository.findById(1L)).thenReturn(Optional.of(customer));
        when(customerRepository.save(any(Customer.class))).thenReturn(customer);

        CustomerDTO updatedDTO = new CustomerDTO();
        updatedDTO.setId(1L);
        updatedDTO.setName("Updated Name");
        updatedDTO.setEmail("updated@example.com");
        updatedDTO.setAddress("456 New St");
        updatedDTO.setPhone("555-5678");

        // Act
        CustomerDTO result = customerService.updateCustomer(1L, updatedDTO);

        // Assert
        assertNotNull(result);
        assertEquals(updatedDTO.getName(), customer.getName());
        assertEquals(updatedDTO.getEmail(), customer.getEmail());
        verify(customerRepository, times(1)).findById(1L);
        verify(customerRepository, times(1)).save(customer);
    }

    @Test
    void updateCustomer_WithInvalidId_ShouldThrowException() {
        // Arrange
        when(customerRepository.findById(99L)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(ResourceNotFoundException.class, () -> {
            customerService.updateCustomer(99L, customerDTO);
        });
        verify(customerRepository, times(1)).findById(99L);
        verify(customerRepository, never()).save(any(Customer.class));
    }

    @Test
    void deleteCustomer_WithValidId_ShouldDeleteCustomer() {
        // Arrange
        when(customerRepository.existsById(1L)).thenReturn(true);
        doNothing().when(customerRepository).deleteById(1L);

        // Act
        customerService.deleteCustomer(1L);

        // Assert
        verify(customerRepository, times(1)).existsById(1L);
        verify(customerRepository, times(1)).deleteById(1L);
    }

    @Test
    void deleteCustomer_WithInvalidId_ShouldThrowException() {
        // Arrange
        when(customerRepository.existsById(99L)).thenReturn(false);

        // Act & Assert
        assertThrows(ResourceNotFoundException.class, () -> {
            customerService.deleteCustomer(99L);
        });
        verify(customerRepository, times(1)).existsById(99L);
        verify(customerRepository, never()).deleteById(anyLong());
    }
}
