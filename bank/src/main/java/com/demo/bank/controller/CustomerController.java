
package com.demo.bank.controller;

import com.demo.bank.dto.CustomerDTO;
import com.demo.bank.entity.Customer;
import com.demo.bank.mapper.CustomerMapper;
import com.demo.bank.service.CustomerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/customer")
public class CustomerController {

    @Autowired
    private CustomerService customerService;

    @PostMapping("/register")
    public CustomerDTO register(@RequestBody Customer customer) {
        Customer registeredCustomer = customerService.register(customer);
        return CustomerMapper.toDTO(registeredCustomer);
    }


    @PostMapping("/login")
    public CustomerDTO login(@RequestParam String email, @RequestParam String password) {
        Customer loggedInCustomer = customerService.login(email, password);
        return CustomerMapper.toDTO(loggedInCustomer);
    }


    @GetMapping("/balance/{accountNo}")
    public Double checkBalance(@PathVariable String accountNo) {
        return customerService.checkBalance(accountNo);
    }


    @PostMapping("/deposit")
    public Double deposit(@RequestParam String accountNo, @RequestParam Double amount) {
        return customerService.deposit(accountNo, amount).getBalance();
    }

    @PostMapping("/withdraw")
    public Double withdraw(@RequestParam String accountNo, @RequestParam Double amount) {
        return customerService.withdraw(accountNo, amount).getBalance();
    }


    @GetMapping("/all")
    public List<CustomerDTO> getAllCustomers() {
        return customerService.getAllCustomers()
                .stream()
                .map(CustomerMapper::toDTO)
                .collect(Collectors.toList());
    }


    @PutMapping("/{id}")
    public CustomerDTO updateCustomer(@PathVariable int id, @RequestBody Customer customer) {
        customer.setId(id);
        Customer updatedCustomer = customerService.updateCustomer(customer);
        return CustomerMapper.toDTO(updatedCustomer);
    }

    // DELETE CUSTOMER BY customerId
    @DeleteMapping("/customer/{customerId}")
    public ResponseEntity<?> deleteCustomerByCustomerId(@PathVariable String customerId) {
        customerService.deleteCustomerByCustomerId(customerId);
        return ResponseEntity.ok().build();
    }
}













































//package com.demo.bank.controller;
//
//import com.demo.bank.entity.Customer;
//import com.demo.bank.service.CustomerService;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.http.ResponseEntity;
//import org.springframework.web.bind.annotation.*;
//
//import java.util.List;
//
//@RestController
//@RequestMapping("/customer")
//public class CustomerController {
//    @Autowired CustomerService customerService;
//
//    @PostMapping("/register")
//    public Customer register(@RequestBody Customer customer) {
//        return customerService.register(customer);
//    }
//
//    @PostMapping("/login")
//    public Customer login(@RequestParam String email, @RequestParam String password) {
//        return customerService.login(email, password);
//    }
//
//    @GetMapping("/balance/{accountNo}")
//    public Double checkBalance(@PathVariable String accountNo) {
//        return customerService.checkBalance(accountNo);
//    }
//
//    @PostMapping("/deposit")
//    public Double deposit(@RequestParam String accountNo, @RequestParam Double amount) {
//        return customerService.deposit(accountNo, amount).getBalance();
//    }
//
//    @PostMapping("/withdraw")
//    public Double withdraw(@RequestParam String accountNo, @RequestParam Double amount) {
//        return customerService.withdraw(accountNo, amount).getBalance();
//    }
//
//    @GetMapping("/all")
//    public List<Customer> getAllCustomers() {
//        return customerService.getAllCustomers();
//    }
//
//    @PutMapping("/{id}")
//    public Customer updateCustomer(@PathVariable int id, @RequestBody Customer customer) {
//        customer.setId(id);
//        return customerService.updateCustomer(customer);
//    }
//
//    @DeleteMapping("/customer/{customerId}")
//    public ResponseEntity<?> deleteCustomerByCustomerId(@PathVariable String customerId) {
//        customerService.deleteCustomerByCustomerId(customerId);
//        return ResponseEntity.ok().build();
//    }
//
//}
//
//
