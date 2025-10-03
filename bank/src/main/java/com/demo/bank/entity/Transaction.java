package com.demo.bank.entity;


import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "Transaction")
@Setter @Getter @ToString
@NoArgsConstructor
@AllArgsConstructor
public class Transaction {

    @Id @GeneratedValue
    private int Id;

    @Column(name = "account_no", length = 20, nullable = false)
    private String accountNo;

    @Column(name = "transaction_type", length = 10, nullable = false)
    private String transaction_type;

    @Column(name = "amount", nullable = false)
    private Double amount;

    @Column(name = "timestamp", nullable = false)
    private java.time.LocalDateTime timestamp;
}
