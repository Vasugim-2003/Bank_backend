package com.demo.bank.service;

import com.demo.bank.entity.Account;
import com.demo.bank.entity.Transaction;
import com.demo.bank.exception.InsufficientBalanceException;
import com.demo.bank.exception.ResourceNotFoundException;
import com.demo.bank.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.security.SecureRandom;
import java.util.*;

@Service
public class AccountService {
    @Autowired AccountRepository accountRepo;
    @Autowired TransactionRepository transactionRepo;

    public Account addAccount(Account account) {
        return accountRepo.save(account);
    }

    public Account getAccount(String accountNo) {
        return accountRepo.findByAccountNo(accountNo)
                .orElseThrow(() -> new ResourceNotFoundException("Account not found"));
    }

    public Account updateBalance(String accountNo, Double newBalance) {
        Account acc = getAccount(accountNo);
        acc.setBalance(newBalance);
        return accountRepo.save(acc);
    }

    public List<Transaction> transactionHistory(String accountNo) {
        return transactionRepo.findByAccountNo(accountNo);
    }

    public Double calculateInterest(String accountNo, Double rate, int years) {
        Account acc = getAccount(accountNo);
        return acc.getBalance() * rate * years / 100;
    }

    public String sendNotification(String accountNo, String message) {

        return "Notification sent to account " + accountNo + ": " + message;
    }


    public Account deposit(String accountNo, Double amount) {
        Account acc = getAccount(accountNo);
        acc.setBalance(acc.getBalance() + amount);
        accountRepo.save(acc);

        Transaction transaction = new Transaction();
        transaction.setAccountNo(accountNo);
        transaction.setTransaction_type("Deposit");
        transaction.setAmount(amount);
        transaction.setTimestamp(java.time.LocalDateTime.now());
        transactionRepo.save(transaction);

        return acc;
    }

    public Account withdraw(String accountNo, Double amount) {
        Account acc = getAccount(accountNo);
        if (acc.getBalance() < amount) {
            throw new InsufficientBalanceException("Insufficient balance for withdrawal");
        }
        acc.setBalance(acc.getBalance() - amount);
        accountRepo.save(acc);

        Transaction transaction = new Transaction();
        transaction.setAccountNo(accountNo);
        transaction.setTransaction_type("Withdrawal");
        transaction.setAmount(amount);
        transaction.setTimestamp(java.time.LocalDateTime.now());
        transactionRepo.save(transaction);

        return acc;
    }


}