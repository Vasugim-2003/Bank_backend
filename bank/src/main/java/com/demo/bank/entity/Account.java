package com.demo.bank.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "Account")
@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class Account {

    @Id
    @GeneratedValue
    private int Id;

    @Column(name = "account_no", length = 20, nullable = false, unique = true)
    private String accountNo;

    @Column(name = "balance", nullable = false)
    private Double balance;

    @Column(name = "account_type", length = 15, nullable = false)
    private String accountType;

    @ManyToOne
    @JoinColumn(name = "customer_id", nullable = false) // this links to `customer_id` in DB
    private Customer customer;
}
