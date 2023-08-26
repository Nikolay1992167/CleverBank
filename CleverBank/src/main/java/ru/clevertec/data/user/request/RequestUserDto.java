package ru.clevertec.data.user.request;

import com.fasterxml.jackson.annotation.JsonProperty;

public record RequestUserDto(
        @JsonProperty(required = true)
        String name,
        @JsonProperty(required = true)
        String email,
        @JsonProperty(required = true)
        String password
) {
}
