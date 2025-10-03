package com.demo.bank.exception;


import com.demo.bank.controller.AccountController;
import com.demo.bank.entity.Account;
import com.demo.bank.service.AccountService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(controllers = AccountController.class)
public class GlobalExceptionHandlerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private AccountService accountService;

    @Test
    void testResourceNotFoundExceptionHandled() throws Exception {
        when(accountService.getAccount(anyString()))
                .thenThrow(new ResourceNotFoundException("Account not found"));

        mockMvc.perform(get("/account/invalidAccountNo")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andExpect(content().string("Account not found"));
    }


    @Test
    void testGenericExceptionHandled() throws Exception {
        when(accountService.getAccount(anyString()))
                .thenThrow(new RuntimeException("Something went wrong"));

        mockMvc.perform(get("/account/anyAccountNo")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(content().string("Something went wrong"));
    }
}
