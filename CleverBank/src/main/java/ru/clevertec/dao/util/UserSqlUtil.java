package ru.clevertec.dao.util;

public class UserSqlUtil {
    public static final String SELECT_ALL_USERS = "SELECT * FROM users";
    public static final String SELECT_USER_BY_ID = "SELECT * FROM users WHERE id = ?";
    public static final String INSERT_NEW_USER = "INSERT INTO users (name, email, password) VALUES (?, ?, ?)";
    public static final String UPDATE_USER = "UPDATE users SET name = ?, email = ?, password = ? WHERE id = ?";
    public static final String DELETE_USER_BY_ID = "DELETE FROM users WHERE id = ?";
}
