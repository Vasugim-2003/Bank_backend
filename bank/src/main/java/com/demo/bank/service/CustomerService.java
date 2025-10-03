package com.demo.bank.service;

import com.demo.bank.entity.Customer;
import com.demo.bank.entity.Account;
import com.demo.bank.entity.Transaction;
import com.demo.bank.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CustomerService {
    @Autowired CustomerRepository customerRepo;
    @Autowired AccountRepository accountRepo;
    @Autowired TransactionRepository transactionRepo;


    public Customer login(String email, String password) {
        Customer c = customerRepo.findByEmail(email).orElseThrow(() -> new RuntimeException("Not found"));
        if (!c.getPassword().equals(password)) throw new RuntimeException("Invalid password");
        return c;
    }

    public Double checkBalance(String accountNo) {
        Account acc = accountRepo.findByAccountNo(accountNo).orElseThrow(() -> new RuntimeException("Account not found"));
        return acc.getBalance();
    }


    public Account deposit(String accountNo, Double amount) {
        Account account = accountRepo.findByAccountNo(accountNo)
                .orElseThrow(() -> new RuntimeException("Account not found"));
        account.setBalance(account.getBalance() + amount);
        accountRepo.save(account);

        Transaction transaction = new Transaction();
        transaction.setAccountNo(accountNo);
        transaction.setTransaction_type("Deposit");
        transaction.setAmount(amount);
        transaction.setTimestamp(java.time.LocalDateTime.now());
        transactionRepo.save(transaction);

        return account;
    }

    public Account withdraw(String accountNo, Double amount) {
        Account account = accountRepo.findByAccountNo(accountNo)
                .orElseThrow(() -> new RuntimeException("Account not found"));
        if (account.getBalance() < amount) {
            throw new RuntimeException("Insufficient balance");
        }
        account.setBalance(account.getBalance() - amount);
        accountRepo.save(account);

        Transaction transaction = new Transaction();
        transaction.setAccountNo(accountNo);
        transaction.setTransaction_type("Withdrawal");
        transaction.setAmount(amount);
        transaction.setTimestamp(java.time.LocalDateTime.now());
        transactionRepo.save(transaction);

        return account;
    }


    public Customer register(Customer customer) {
        customer.setCustomerId(generateCustomerId());
        customer.setAccountNo(generateAccountNumber());

        Customer savedCustomer = customerRepo.save(customer);

        Account account = new Account();
        account.setAccountNo(savedCustomer.getAccountNo());
        account.setBalance(0.0);
        account.setAccountType(
                savedCustomer.getAccountType() != null ? savedCustomer.getAccountType() : "Savings"
        );
        account.setCustomer(savedCustomer);

        accountRepo.save(account);

        return savedCustomer;
    }



    private String generateCustomerId() {
        return "CUST" + System.currentTimeMillis();
    }

    private String generateAccountNumber() {
        return "ACC" + (100000 + new java.util.Random().nextInt(900000));
    }


    public List<Customer> getAllCustomers() {
        return customerRepo.findAll();
    }

    public Customer updateCustomer(Customer customer) {
        return customerRepo.save(customer);
    }

    public void deleteCustomerByCustomerId(String customerId) {
        if (!customerRepo.existsByCustomerId(customerId)) {
            throw new RuntimeException("Customer not found with customerId: " + customerId);
        }
        customerRepo.deleteByCustomerId(customerId);
    }

}