package ru.clevertec.data.transaction.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import ru.clevertec.entity.Account;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record RequestTransactionDto(
        @JsonProperty(required = true)
        Account fromAccount,
        @JsonProperty(required = true)
        Account toAccount,
        @JsonProperty(required = true)
        BigDecimal amount,
        @JsonProperty(required = true)
        LocalDateTime date
) {
}
