package com.demo.bank.dto;


import lombok.*;
import java.time.LocalDateTime;

@Getter @Setter @ToString
@NoArgsConstructor
@AllArgsConstructor
public class TransactionDTO {
    private String accountNo;
    private Double amount;
    private String type;
    private LocalDateTime timestamp;
}

