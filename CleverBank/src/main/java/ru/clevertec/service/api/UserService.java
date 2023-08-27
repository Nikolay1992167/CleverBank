package ru.clevertec.service.api;

import ru.clevertec.data.user.request.RequestUserDto;
import ru.clevertec.data.user.response.ResponseUserDto;

import java.util.List;

public interface UserService {

    List<ResponseUserDto> getAllUsers();

    ResponseUserDto getUserById(Long id);

    void addUser(RequestUserDto userDto);

    void updateUser(Long id, RequestUserDto userDto);

    void deleteUser(Long id);
}
