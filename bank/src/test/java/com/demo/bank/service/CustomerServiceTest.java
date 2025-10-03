package com.demo.bank.service;


import com.demo.bank.entity.Account;
import com.demo.bank.entity.Customer;
import com.demo.bank.entity.Transaction;
import com.demo.bank.repository.AccountRepository;
import com.demo.bank.repository.CustomerRepository;
import com.demo.bank.repository.TransactionRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CustomerServiceTest {

    @InjectMocks
    private CustomerService customerService;

    @Mock
    private CustomerRepository customerRepo;

    @Mock
    private AccountRepository accountRepo;

    @Mock
    private TransactionRepository transactionRepo;

    private Customer customer;
    private Account account;

    @BeforeEach
    void setUp() {
        customer = new Customer();
        customer.setEmail("test@example.com");
        customer.setPassword("password123");
        customer.setCustomerId("CUST123");
        customer.setAccountNo("ACC123");
        customer.setAccountType("Savings");
        customer.setName("Test User");
        customer.setAddress("123 Street");
        customer.setPhone(1234567890L);
        customer.setPanNo("ABCDE1234F");

        account = new Account();
        account.setAccountNo("ACC123");
        account.setBalance(1000.0);
        account.setAccountType("Savings");
    }

    @Test
    void login_Success() {
        when(customerRepo.findByEmail("test@example.com")).thenReturn(Optional.of(customer));
        Customer loggedIn = customerService.login("test@example.com", "password123");
        assertEquals(customer, loggedIn);
    }

    @Test
    void login_Failure_EmailNotFound() {
        when(customerRepo.findByEmail("wrong@example.com")).thenReturn(Optional.empty());
        RuntimeException ex = assertThrows(RuntimeException.class, () -> {
            customerService.login("wrong@example.com", "password123");
        });
        assertEquals("Not found", ex.getMessage());
    }

    @Test
    void login_Failure_WrongPassword() {
        when(customerRepo.findByEmail("test@example.com")).thenReturn(Optional.of(customer));
        RuntimeException ex = assertThrows(RuntimeException.class, () -> {
            customerService.login("test@example.com", "wrongpass");
        });
        assertEquals("Invalid password", ex.getMessage());
    }

    @Test
    void checkBalance_Success() {
        when(accountRepo.findByAccountNo("ACC123")).thenReturn(Optional.of(account));
        Double balance = customerService.checkBalance("ACC123");
        assertEquals(1000.0, balance);
    }

    @Test
    void checkBalance_AccountNotFound() {
        when(accountRepo.findByAccountNo("ACC999")).thenReturn(Optional.empty());
        RuntimeException ex = assertThrows(RuntimeException.class, () -> {
            customerService.checkBalance("ACC999");
        });
        assertEquals("Account not found", ex.getMessage());
    }

    @Test
    void deposit_Success() {
        when(accountRepo.findByAccountNo("ACC123")).thenReturn(Optional.of(account));
        when(accountRepo.save(any(Account.class))).thenAnswer(i -> i.getArgument(0));
        when(transactionRepo.save(any(Transaction.class))).thenAnswer(i -> i.getArgument(0));

        Account updatedAccount = customerService.deposit("ACC123", 500.0);

        assertEquals(1500.0, updatedAccount.getBalance());
        verify(accountRepo).save(updatedAccount);
        verify(transactionRepo).save(any(Transaction.class));
    }

    @Test
    void withdraw_Success() {
        when(accountRepo.findByAccountNo("ACC123")).thenReturn(Optional.of(account));
        when(accountRepo.save(any(Account.class))).thenAnswer(i -> i.getArgument(0));
        when(transactionRepo.save(any(Transaction.class))).thenAnswer(i -> i.getArgument(0));

        Account updatedAccount = customerService.withdraw("ACC123", 200.0);

        assertEquals(800.0, updatedAccount.getBalance());
        verify(accountRepo).save(updatedAccount);
        verify(transactionRepo).save(any(Transaction.class));
    }

    @Test
    void withdraw_InsufficientBalance() {
        when(accountRepo.findByAccountNo("ACC123")).thenReturn(Optional.of(account));

        RuntimeException ex = assertThrows(RuntimeException.class, () -> {
            customerService.withdraw("ACC123", 2000.0);
        });
        assertEquals("Insufficient balance", ex.getMessage());
    }

    @Test
    void register_Success() {
        when(customerRepo.save(any(Customer.class))).thenAnswer(i -> i.getArgument(0));
        when(accountRepo.save(any(Account.class))).thenAnswer(i -> i.getArgument(0));

        Customer newCustomer = new Customer();
        newCustomer.setEmail("new@example.com");
        newCustomer.setPassword("pass");
        newCustomer.setAccountType("Savings");
        newCustomer.setName("New User");
        newCustomer.setAddress("Address");
        newCustomer.setPhone(1234567890L);
        newCustomer.setPanNo("ABCDE1234F");

        Customer savedCustomer = customerService.register(newCustomer);

        assertNotNull(savedCustomer.getCustomerId());
        assertNotNull(savedCustomer.getAccountNo());
        verify(customerRepo).save(newCustomer);
        verify(accountRepo).save(any(Account.class));
    }
}
