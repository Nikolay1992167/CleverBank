package ru.clevertec.data.bank.request;

import com.fasterxml.jackson.annotation.JsonProperty;

public record RequestBankDto(
        @JsonProperty(required = true)
        String name,
        @JsonProperty(required = true)
        String bic
) {
}
