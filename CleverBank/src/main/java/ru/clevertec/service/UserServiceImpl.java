package ru.clevertec.service;

import lombok.RequiredArgsConstructor;
import org.mapstruct.factory.Mappers;
import ru.clevertec.dao.api.UserDAO;
import ru.clevertec.data.user.request.RequestUserDto;
import ru.clevertec.data.user.response.ResponseUserDto;
import ru.clevertec.entity.User;
import ru.clevertec.exception.UserNotFoundException;
import ru.clevertec.mapper.UserMapper;
import ru.clevertec.service.api.UserService;

import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserDAO userDAO;
    private final UserMapper userMapper = Mappers.getMapper(UserMapper.class);

    @Override
    public List<ResponseUserDto> getAllUsers() {
        return userDAO.getAllUsers().stream()
                .map(userMapper::getResponseDto)
                .toList();
    }

    @Override
    public ResponseUserDto getUserById(Long id) {
        Optional<User> optionalUser = userDAO.getUserById(id);
        User user = optionalUser.orElseThrow(() -> new UserNotFoundException(id));
        return userMapper.getResponseDto(user);
    }

    @Override
    public void addUser(RequestUserDto userDto) {
        User user = userMapper.getUser(userDto);
        userDAO.addUser(user);
    }

    @Override
    public void updateUser(Long id, RequestUserDto userDto) {
        User user = userMapper.getUser(userDto);
        user.setId(id);
        userDAO.updateUser(user);
    }

    @Override
    public void deleteUser(Long id) {
        userDAO.deleteUser(id);
    }
}


