package com.demo.bank.controller;



import com.demo.bank.entity.Account;
import com.demo.bank.entity.Transaction;
import com.demo.bank.service.AccountService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;
import java.util.List;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.fasterxml.jackson.databind.ObjectMapper;

@WebMvcTest(AccountController.class)
class AccountControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private AccountService accountService;

    private ObjectMapper objectMapper = new ObjectMapper();

    private Account account;

    @BeforeEach
    void setUp() {
        account = new Account();
        account.setId(1);
        account.setAccountNo("ACC123");
        account.setBalance(1000.0);
        account.setAccountType("Savings");
    }

    @Test
    void addAccount_ShouldReturnAccount() throws Exception {
        when(accountService.addAccount(any(Account.class))).thenReturn(account);

        mockMvc.perform(post("/account/add")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(account)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.accountNo").value("ACC123"))
                .andExpect(jsonPath("$.balance").value(1000.0));

        verify(accountService).addAccount(any(Account.class));
    }

    @Test
    void getAccount_ShouldReturnAccount() throws Exception {
        when(accountService.getAccount("ACC123")).thenReturn(account);

        mockMvc.perform(get("/account/ACC123"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.accountNo").value("ACC123"))
                .andExpect(jsonPath("$.balance").value(1000.0));

        verify(accountService).getAccount("ACC123");
    }

    @Test
    void updateBalance_ShouldReturnUpdatedAccount() throws Exception {
        account.setBalance(2000.0);
        when(accountService.updateBalance("ACC123", 2000.0)).thenReturn(account);

        mockMvc.perform(put("/account/update")
                        .param("accountNo", "ACC123")
                        .param("newBalance", "2000"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.balance").value(2000.0));

        verify(accountService).updateBalance("ACC123", 2000.0);
    }

    @Test
    void transactionHistory_ShouldReturnTransactions() throws Exception {
        Transaction tx1 = new Transaction(1, "ACC123", "Deposit", 500.0, LocalDateTime.now());
        Transaction tx2 = new Transaction(2, "ACC123", "Withdrawal", 200.0, LocalDateTime.now());

        when(accountService.transactionHistory("ACC123")).thenReturn(List.of(tx1, tx2));

        mockMvc.perform(get("/account/transactions/ACC123"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(2));

        verify(accountService).transactionHistory("ACC123");
    }

    @Test
    void calculateInterest_ShouldReturnInterest() throws Exception {
        when(accountService.calculateInterest("ACC123", 5.0, 2)).thenReturn(100.0);

        mockMvc.perform(get("/account/interest")
                        .param("accountNo", "ACC123")
                        .param("rate", "5")
                        .param("years", "2"))
                .andExpect(status().isOk())
                .andExpect(content().string("100.0"));

        verify(accountService).calculateInterest("ACC123", 5.0, 2);
    }

    @Test
    void sendNotification_ShouldReturnMessage() throws Exception {
        String msg = "Notification sent to account ACC123: Hello";
        when(accountService.sendNotification("ACC123", "Hello")).thenReturn(msg);

        mockMvc.perform(post("/account/notify")
                        .param("accountNo", "ACC123")
                        .param("message", "Hello"))
                .andExpect(status().isOk())
                .andExpect(content().string(msg));

        verify(accountService).sendNotification("ACC123", "Hello");
    }

    @Test
    void deposit_ShouldReturnUpdatedBalance() throws Exception {
        account.setBalance(1500.0);
        when(accountService.deposit("ACC123", 500.0)).thenReturn(account);

        mockMvc.perform(post("/account/deposit")
                        .param("accountNo", "ACC123")
                        .param("amount", "500"))
                .andExpect(status().isOk())
                .andExpect(content().string("1500.0"));

        verify(accountService).deposit("ACC123", 500.0);
    }

    @Test
    void withdraw_ShouldReturnUpdatedBalance() throws Exception {
        account.setBalance(700.0);
        when(accountService.withdraw("ACC123", 300.0)).thenReturn(account);

        mockMvc.perform(post("/account/withdraw")
                        .param("accountNo", "ACC123")
                        .param("amount", "300"))
                .andExpect(status().isOk())
                .andExpect(content().string("700.0"));

        verify(accountService).withdraw("ACC123", 300.0);
    }
}
