package com.grocery.api.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.grocery.api.dto.CustomerDTO;
import com.grocery.api.exception.ResourceNotFoundException;
import com.grocery.api.service.CustomerService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Arrays;
import java.util.List;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(CustomerController.class)
class CustomerControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private CustomerService customerService;

    private CustomerDTO customerDTO;

    @BeforeEach
    void setUp() {
        customerDTO = new CustomerDTO();
        customerDTO.setId(1L);
        customerDTO.setName("John Doe");
        customerDTO.setEmail("john@example.com");
        customerDTO.setAddress("123 Main St");
        customerDTO.setPhone("555-1234");
    }

    @Test
    void getAllCustomers_ShouldReturnAllCustomers() throws Exception {
        // Arrange
        List<CustomerDTO> customers = Arrays.asList(customerDTO);
        when(customerService.getAllCustomers()).thenReturn(customers);

        // Act & Assert
        mockMvc.perform(get("/api/customers"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].id", is(1)))
                .andExpect(jsonPath("$[0].name", is("John Doe")));

        verify(customerService, times(1)).getAllCustomers();
    }

    @Test
    void getCustomerById_WithValidId_ShouldReturnCustomer() throws Exception {
        // Arrange
        when(customerService.getCustomerById(1L)).thenReturn(customerDTO);

        // Act & Assert
        mockMvc.perform(get("/api/customers/1"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id", is(1)))
                .andExpect(jsonPath("$.name", is("John Doe")));

        verify(customerService, times(1)).getCustomerById(1L);
    }

    @Test
    void getCustomerById_WithInvalidId_ShouldReturnNotFound() throws Exception {
        // Arrange
        when(customerService.getCustomerById(99L)).thenThrow(new ResourceNotFoundException("Customer not found"));

        // Act & Assert
        mockMvc.perform(get("/api/customers/99"))
                .andExpect(status().isNotFound());

        verify(customerService, times(1)).getCustomerById(99L);
    }

    @Test
    void createCustomer_WithValidData_ShouldReturnCreatedCustomer() throws Exception {
        // Arrange
        when(customerService.createCustomer(any(CustomerDTO.class))).thenReturn(customerDTO);

        // Act & Assert
        mockMvc.perform(post("/api/customers")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(customerDTO)))
                .andExpect(status().isCreated())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id", is(1)))
                .andExpect(jsonPath("$.name", is("John Doe")));

        verify(customerService, times(1)).createCustomer(any(CustomerDTO.class));
    }

    @Test
    void updateCustomer_WithValidData_ShouldReturnUpdatedCustomer() throws Exception {
        // Arrange
        when(customerService.updateCustomer(eq(1L), any(CustomerDTO.class))).thenReturn(customerDTO);

        // Act & Assert
        mockMvc.perform(put("/api/customers/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(customerDTO)))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id", is(1)))
                .andExpect(jsonPath("$.name", is("John Doe")));

        verify(customerService, times(1)).updateCustomer(eq(1L), any(CustomerDTO.class));
    }

    @Test
    void deleteCustomer_WithValidId_ShouldReturnNoContent() throws Exception {
        // Arrange
        doNothing().when(customerService).deleteCustomer(1L);

        // Act & Assert
        mockMvc.perform(delete("/api/customers/1"))
                .andExpect(status().isNoContent());

        verify(customerService, times(1)).deleteCustomer(1L);
    }
}
