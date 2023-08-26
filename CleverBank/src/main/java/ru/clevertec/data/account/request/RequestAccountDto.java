package ru.clevertec.data.account.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import ru.clevertec.entity.Bank;
import ru.clevertec.entity.User;

import java.math.BigDecimal;

public record RequestAccountDto(
        @JsonProperty(required = true)
        String number,
        @JsonProperty(required = true)
        BigDecimal balance,
        @JsonProperty(required = true)
        Bank bank,
        @JsonProperty(required = true)
        User user
) {
}
