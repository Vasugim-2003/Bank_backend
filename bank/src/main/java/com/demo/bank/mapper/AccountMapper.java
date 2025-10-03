package com.demo.bank.mapper;

import com.demo.bank.dto.AccountDTO;
import com.demo.bank.entity.Account;

public class AccountMapper {
    public static AccountDTO toDTO(Account account) {
        AccountDTO dto = new AccountDTO();
        dto.setAccountNo(account.getAccountNo());
        dto.setBalance(account.getBalance());
        dto.setAccountType(account.getAccountType());
        return dto;
    }

    public static Account toEntity(AccountDTO dto) {
        Account account = new Account();
        account.setAccountNo(dto.getAccountNo());
        account.setBalance(dto.getBalance());
        account.setAccountType(dto.getAccountType());
        return account;
    }
}
