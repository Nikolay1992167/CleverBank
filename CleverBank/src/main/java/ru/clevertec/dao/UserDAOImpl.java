package ru.clevertec.dao;

import lombok.RequiredArgsConstructor;
import ru.clevertec.dao.api.UserDAO;
import ru.clevertec.entity.User;
import ru.clevertec.exception.ResourceSqlException;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static java.sql.Statement.RETURN_GENERATED_KEYS;
import static ru.clevertec.dao.util.UserSqlUtil.*;

@RequiredArgsConstructor
public class UserDAOImpl implements UserDAO {

    private final DataSource dataSource;

    private User createUserFromResultSet(ResultSet resultSet) throws SQLException {
        Long id = resultSet.getLong("id");
        String name = resultSet.getString("name");
        String email = resultSet.getString("email");
        String password = resultSet.getString("password");
        return new User(id, name, email, password);
    }

    @Override
    public List<User> getAllUsers() {
        try (Connection connection = dataSource.getConnection();
             PreparedStatement statement = connection.prepareStatement(SELECT_ALL_USERS)
        ) {
            try (ResultSet resultSet = statement.executeQuery()) {
                final List<User> users = new ArrayList<>();
                while (resultSet.next()) {
                    User user = createUserFromResultSet(resultSet);
                    users.add(user);
                }
                return users;
            }
        } catch (SQLException e) {
            throw new ResourceSqlException();
        }
    }

    @Override
    public Optional<User> getUserById(Long id) {
        try (Connection connection = dataSource.getConnection();
             PreparedStatement statement = connection.prepareStatement(SELECT_USER_BY_ID)
        ) {
            statement.setLong(1, id);
            try (ResultSet resultSet = statement.executeQuery()) {
                User user;
                return resultSet.next() ? Optional.of(user = createUserFromResultSet(resultSet)) : Optional.empty();
            }
        } catch (SQLException e) {
            throw new ResourceSqlException();
        }
    }

    @Override
    public void addUser(User user) {
        try (Connection connection = dataSource.getConnection();
             PreparedStatement statement = connection.prepareStatement(
                     INSERT_NEW_USER, RETURN_GENERATED_KEYS)
        ) {
            statement.setString(1, user.getName());
            statement.setString(2, user.getEmail());
            statement.setString(3, user.getPassword());
            int count = statement.executeUpdate();
            if (count == 0) {
                throw new ResourceSqlException("Creating user failed, no rows affected.");
            }
            ResultSet generatedKeys = statement.getGeneratedKeys();
            if (!generatedKeys.next()) {
                throw new ResourceSqlException("Creating user failed, no ID obtained.");
            }
        } catch (SQLException e) {
            throw new ResourceSqlException();
        }
    }

    @Override
    public void updateUser(User user) {
        try (Connection connection = dataSource.getConnection();
             PreparedStatement statement = connection.prepareStatement(UPDATE_USER)
        ) {
            statement.setString(1, user.getName());
            statement.setString(2, user.getEmail());
            statement.setString(3, user.getPassword());
            statement.executeUpdate();
        } catch (SQLException e) {
            throw new ResourceSqlException();
        }
    }

    @Override
    public void deleteUser(Long id) {
        try (Connection connection = dataSource.getConnection();
             PreparedStatement statement = connection.prepareStatement(DELETE_USER_BY_ID)
        ) {
            statement.setLong(1, id);
            statement.executeUpdate();
        } catch (SQLException e) {
            throw new ResourceSqlException();
        }
    }
}