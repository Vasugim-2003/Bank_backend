package com.demo.bank.dto;

import lombok.*;

@Getter @Setter @ToString
@NoArgsConstructor
@AllArgsConstructor
public class CustomerDTO {
    private String customerId;
    private String accountNo;
    private String name;
    private String address;
    private Long phone;
    private String email;
    private String panNo;
    private String accountType;
}
