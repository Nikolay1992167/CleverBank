package ru.clevertec.dao.util;

public class TransactionSQLUtil {
    public static final String SELECT_ALL_TRANSACTION = "SELECT * FROM transaction";
    public static final String SELECT_TRANSACTION_BY_ID = "SELECT * FROM transaction WHERE id = ?";
    public static final String INSERT_NEW_TRANSACTION = "INSERT INTO transaction (from_account_id, to_account_id, amount, date) VALUES (?, ?, ?, ?)";
    public static final String UPDATE_TRANSACTION = "UPDATE transaction SET from_account_id = ?, to_account_id = ?, amount = ?, date = ? WHERE id = ?";
    public static final String DELETE_TRANSACTION_BY_ID = "DELETE FROM transaction WHERE id = ?";
}
