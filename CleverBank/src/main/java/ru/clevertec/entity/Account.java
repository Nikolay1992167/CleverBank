package ru.clevertec.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Account {
    private Long id;
    private String number;
    private BigDecimal balance;
    private Bank bank;
    private User user;
}
