package ru.clevertec.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Transaction {
    private Long id;
    private Account fromAccount;
    private Account toAccount;
    private BigDecimal amount;
    private LocalDateTime date;
}
