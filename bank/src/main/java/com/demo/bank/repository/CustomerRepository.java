package com.demo.bank.repository;

import com.demo.bank.entity.Customer;
import org.springframework.data.domain.Example;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CustomerRepository extends JpaRepository<Customer, Integer> {


    Optional<Customer> findByCustomerId(String customerId);
    Optional<Customer> findByEmail(String email);
    void deleteByCustomerId(String customerId);
    boolean existsByCustomerId(String customerId);
}
