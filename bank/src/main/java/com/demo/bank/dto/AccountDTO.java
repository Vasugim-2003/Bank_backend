package com.demo.bank.dto;

import lombok.*;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class AccountDTO {
    private String accountNo;
    private Double balance;
    private String accountType;
}
