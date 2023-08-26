package ru.clevertec.service.api;

import ru.clevertec.entity.User;

import java.sql.SQLException;
import java.util.List;

public interface UserService {

    List<User> getAllUsers() throws SQLException, ClassNotFoundException;
    User getUserById(Long id) throws SQLException, ClassNotFoundException;
    void addUser(User user) throws SQLException, ClassNotFoundException;
    void updateUser(User user) throws SQLException, ClassNotFoundException;
    void deleteUser(User user) throws SQLException, ClassNotFoundException;

}
