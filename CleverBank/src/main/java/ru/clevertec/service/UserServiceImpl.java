package ru.clevertec.service;

import lombok.RequiredArgsConstructor;
import ru.clevertec.dao.api.UserDAO;
import ru.clevertec.entity.User;
import ru.clevertec.service.api.UserService;

import java.sql.SQLException;
import java.util.List;
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserDAO userDAO;

    public List <User> getAllUsers() throws SQLException, ClassNotFoundException {
        return userDAO.getAllUsers();
    }

    public User getUserById(Long id) throws SQLException, ClassNotFoundException {
        return userDAO.getUserById(id);
    }

    public void addUser(User user) throws SQLException, ClassNotFoundException {
        userDAO.addUser(user);
    }

    public void updateUser(User user) throws SQLException, ClassNotFoundException {
        userDAO.updateUser(user);
    }

    public void deleteUser(User user) throws SQLException, ClassNotFoundException {
        userDAO.deleteUser(user);
    }
}
