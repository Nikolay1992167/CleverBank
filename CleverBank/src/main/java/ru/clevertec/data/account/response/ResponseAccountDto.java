package ru.clevertec.data.account.response;

import ru.clevertec.entity.Bank;
import ru.clevertec.entity.User;

import java.math.BigDecimal;

public record ResponseAccountDto(
        Long id,
        String number,
        BigDecimal balance,
        Bank bank,
        User user
) {
}
