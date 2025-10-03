package com.demo.bank.mapper;

import com.demo.bank.entity.Customer;
import com.demo.bank.dto.CustomerDTO;

public class CustomerMapper {
    public static CustomerDTO toDTO(Customer customer) {
        return new CustomerDTO(
                customer.getCustomerId(),
                customer.getAccountNo(),
                customer.getName(),
                customer.getAddress(),
                customer.getPhone(),
                customer.getEmail(),
                customer.getPanNo(),
                customer.getAccountType()
        );

    }
}