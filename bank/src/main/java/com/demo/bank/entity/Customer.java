package com.demo.bank.entity;


import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import lombok.*;

import java.util.List;

@Entity
@Table(name = "Customer")
@Getter @Setter @ToString
@AllArgsConstructor
@NoArgsConstructor
public class Customer {

    @Id @GeneratedValue
    private int Id;

    @Column(name = "customer_id", length = 20, nullable = false, unique = true)
    private String customerId;

    @Column(name = "account_no", length = 20, nullable = false, unique = true)
    private String accountNo;

    @Column(name = "customer_name", length = 200, nullable = false)
    private String name;

    @Column(name = "Address", length = 100, nullable = false)
    private String address;

    @Column(name = "Phone", length = 10, nullable = false)
    private Long phone;

    @Email
    @Column(name = "email", length = 20, nullable = false)
    private String email;

    @Column(name = "password", length = 100, nullable = false)
    private String password;

    @Column(name = "pan_no", length = 12, nullable = false)
    private String panNo;

    @Column(name = "account_type", length = 20, nullable = false)
    private String accountType;

    @OneToMany(mappedBy = "customer", cascade = CascadeType.ALL)
    private List<Account> accounts;

}
