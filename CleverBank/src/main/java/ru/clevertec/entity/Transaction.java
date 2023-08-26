package ru.clevertec.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Optional;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Transaction {
    private Long id;
    private Account fromAccount;
    private Account toAccount;
    private BigDecimal amount;
    private LocalDateTime date;

    public Transaction(Long id, Optional<Account> fromAccount, Optional<Account> toAccount, BigDecimal amount, LocalDateTime date) {
    }
}
