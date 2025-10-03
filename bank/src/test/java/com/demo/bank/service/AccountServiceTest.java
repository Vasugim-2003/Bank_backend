package com.demo.bank.service;



import com.demo.bank.entity.Account;
import com.demo.bank.entity.Transaction;
import com.demo.bank.exception.InsufficientBalanceException;
import com.demo.bank.exception.ResourceNotFoundException;
import com.demo.bank.repository.AccountRepository;
import com.demo.bank.repository.TransactionRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AccountServiceTest {

    @InjectMocks
    private AccountService accountService;

    @Mock
    private AccountRepository accountRepo;

    @Mock
    private TransactionRepository transactionRepo;

    private Account account;

    @BeforeEach
    void setUp() {
        account = new Account();
        account.setAccountNo("ACC123");
        account.setBalance(1000.0);
        account.setAccountType("Savings");
    }

    @Test
    void getAccount_Success() {
        when(accountRepo.findByAccountNo("ACC123")).thenReturn(Optional.of(account));
        Account result = accountService.getAccount("ACC123");
        assertEquals(account, result);
    }

    @Test
    void getAccount_NotFound() {
        when(accountRepo.findByAccountNo("ACC999")).thenReturn(Optional.empty());
        assertThrows(ResourceNotFoundException.class, () -> {
            accountService.getAccount("ACC999");
        });
    }

    @Test
    void deposit_Success() {
        when(accountRepo.findByAccountNo("ACC123")).thenReturn(Optional.of(account));
        when(accountRepo.save(any(Account.class))).thenAnswer(i -> i.getArgument(0));
        when(transactionRepo.save(any(Transaction.class))).thenAnswer(i -> i.getArgument(0));

        Account updatedAccount = accountService.deposit("ACC123", 500.0);
        assertEquals(1500.0, updatedAccount.getBalance());

        verify(accountRepo).save(updatedAccount);
        verify(transactionRepo).save(any(Transaction.class));
    }

    @Test
    void withdraw_Success() {
        when(accountRepo.findByAccountNo("ACC123")).thenReturn(Optional.of(account));
        when(accountRepo.save(any(Account.class))).thenAnswer(i -> i.getArgument(0));
        when(transactionRepo.save(any(Transaction.class))).thenAnswer(i -> i.getArgument(0));

        Account updatedAccount = accountService.withdraw("ACC123", 300.0);
        assertEquals(700.0, updatedAccount.getBalance());

        verify(accountRepo).save(updatedAccount);
        verify(transactionRepo).save(any(Transaction.class));
    }

    @Test
    void withdraw_InsufficientBalance() {
        when(accountRepo.findByAccountNo("ACC123")).thenReturn(Optional.of(account));
        assertThrows(InsufficientBalanceException.class, () -> {
            accountService.withdraw("ACC123", 2000.0);
        });
    }

    @Test
    void calculateInterest_Success() {
        when(accountRepo.findByAccountNo("ACC123")).thenReturn(Optional.of(account));
        Double interest = accountService.calculateInterest("ACC123", 5.0, 2);
        assertEquals(100.0, interest); // 1000 * 5 * 2 / 100
    }

    @Test
    void transactionHistory_Success() {
        Transaction tx1 = new Transaction(1, "ACC123", "Deposit", 500.0, LocalDateTime.now());
        Transaction tx2 = new Transaction(2, "ACC123", "Withdrawal", 200.0, LocalDateTime.now());

        when(transactionRepo.findByAccountNo("ACC123")).thenReturn(List.of(tx1, tx2));
        List<Transaction> history = accountService.transactionHistory("ACC123");

        assertEquals(2, history.size());
    }
}
