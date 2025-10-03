package com.demo.bank.controller;


import com.demo.bank.entity.Customer;
import com.demo.bank.service.CustomerService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.fasterxml.jackson.databind.ObjectMapper;

@WebMvcTest(CustomerController.class)
class CustomerControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private CustomerService customerService;

    private ObjectMapper objectMapper = new ObjectMapper();

    private Customer customer;

    @BeforeEach
    void setUp() {
        customer = new Customer();
        customer.setId(1);
        customer.setCustomerId("CUST123");
        customer.setAccountNo("ACC123");
        customer.setName("Test User");
        customer.setAddress("123 Street");
        customer.setPhone(1234567890L);
        customer.setEmail("test@example.com");
        customer.setPassword("password123");
        customer.setPanNo("ABCDE1234F");
        customer.setAccountType("Savings");
    }

    @Test
    void register_ShouldReturnCustomer() throws Exception {
        when(customerService.register(any(Customer.class))).thenReturn(customer);

        mockMvc.perform(post("/customer/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(customer)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.customerId").value("CUST123"));

        verify(customerService).register(any(Customer.class));
    }

    @Test
    void login_ShouldReturnCustomer() throws Exception {
        when(customerService.login("test@example.com", "password123")).thenReturn(customer);

        mockMvc.perform(post("/customer/login")
                        .param("email", "test@example.com")
                        .param("password", "password123"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.email").value("test@example.com"));

        verify(customerService).login("test@example.com", "password123");
    }

    @Test
    void checkBalance_ShouldReturnBalance() throws Exception {
        when(customerService.checkBalance("ACC123")).thenReturn(1000.0);

        mockMvc.perform(get("/customer/balance/ACC123"))
                .andExpect(status().isOk())
                .andExpect(content().string("1000.0"));

        verify(customerService).checkBalance("ACC123");
    }



    @Test
    void getAllCustomers_ShouldReturnList() throws Exception {
        when(customerService.getAllCustomers()).thenReturn(List.of(customer));

        mockMvc.perform(get("/customer/all"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(1));

        verify(customerService).getAllCustomers();
    }

    @Test
    void updateCustomer_ShouldReturnUpdatedCustomer() throws Exception {
        when(customerService.updateCustomer(any(Customer.class))).thenReturn(customer);

        mockMvc.perform(put("/customer/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(customer)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.customerId").value("CUST123"));

        verify(customerService).updateCustomer(any(Customer.class));
    }

    @Test
    void deleteCustomer_ShouldReturnOk() throws Exception {
        doNothing().when(customerService).deleteCustomerByCustomerId("CUST123");

        mockMvc.perform(delete("/customer/customer/CUST123"))
                .andExpect(status().isOk());

        verify(customerService).deleteCustomerByCustomerId("CUST123");
    }
}
