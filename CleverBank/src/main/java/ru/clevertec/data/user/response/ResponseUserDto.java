package ru.clevertec.data.user.response;

public record ResponseUserDto(
        Long id,
        String name,
        String email,
        String password
) {
}
