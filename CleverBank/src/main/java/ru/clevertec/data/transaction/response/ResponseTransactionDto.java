package ru.clevertec.data.transaction.response;

import ru.clevertec.entity.Account;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record ResponseTransactionDto(
        Long id,
        Account fromAccount,
        Account toAccount,
        BigDecimal amount,
        LocalDateTime date
) {
}
