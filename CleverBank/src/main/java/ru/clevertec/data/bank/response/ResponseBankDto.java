package ru.clevertec.data.bank.response;

public record ResponseBankDto(
        Long id,
        String name,
        String bic
) {
}
