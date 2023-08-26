package ru.clevertec.dao.api;

import ru.clevertec.entity.User;
import ru.clevertec.exception.UserNotFoundException;

import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

public interface UserDAO {

    /**
     * Find all users
     *
     * @return List of all users
     */
    List<User> getAllUsers();

    /**
     * Returns saved user
     *
     * @param id - user id
     * @return - User if contains
     * @throws UserNotFoundException - if not found
     */
    Optional <User> getUserById(Long id);

    /**
     * Save a new user
     *
     * @param user new user without id
     * @return the user ID
     */
    void addUser(User user);

    /**
     * Update current user
     *
     * @param user - updated
     */
    void updateUser(User user);

    /**
     * Delete an user by ID
     *
     * @param id the user ID
     */
    void deleteUser(Long id);
}
