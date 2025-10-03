
package com.demo.bank.controller;

import com.demo.bank.dto.AccountDTO;
import com.demo.bank.entity.Account;
import com.demo.bank.entity.Transaction;
import com.demo.bank.mapper.AccountMapper;
import com.demo.bank.service.AccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/account")
public class AccountController {

    @Autowired
    private AccountService accountService;

    @PostMapping("/add")
    public AccountDTO addAccount(@RequestBody AccountDTO accountDTO) {
        Account account = AccountMapper.toEntity(accountDTO);
        Account saved = accountService.addAccount(account);
        return AccountMapper.toDTO(saved);
    }

    @GetMapping("/{accountNo}")
    public AccountDTO getAccount(@PathVariable String accountNo) {
        Account account = accountService.getAccount(accountNo);
        return AccountMapper.toDTO(account);
    }

    @PutMapping("/update")
    public AccountDTO updateBalance(@RequestParam String accountNo, @RequestParam Double newBalance) {
        Account updated = accountService.updateBalance(accountNo, newBalance);
        return AccountMapper.toDTO(updated);
    }

    @GetMapping("/transactions/{accountNo}")
    public List<Transaction> transactionHistory(@PathVariable String accountNo) {
        return accountService.transactionHistory(accountNo);
    }

    @GetMapping("/interest")
    public Double calculateInterest(@RequestParam String accountNo, @RequestParam Double rate, @RequestParam int years) {
        return accountService.calculateInterest(accountNo, rate, years);
    }

    @PostMapping("/notify")
    public String sendNotification(@RequestParam String accountNo, @RequestParam String message) {
        return accountService.sendNotification(accountNo, message);
    }

    @PostMapping("/deposit")
    public Double deposit(@RequestParam String accountNo, @RequestParam Double amount) {
        return accountService.deposit(accountNo, amount).getBalance();
    }

    @PostMapping("/withdraw")
    public Double withdraw(@RequestParam String accountNo, @RequestParam Double amount) {
        return accountService.withdraw(accountNo, amount).getBalance();
    }
}
