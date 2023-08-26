package ru.clevertec.dao.util;

public class BankSQLUtil {

    public static final String SELECT_ALL_BANKS = "SELECT * FROM banks";
    public static final String SELECT_BANK_FROM_BY_ID = "SELECT * FROM banks WHERE id = ?";
    public static final String INSERT_NEW_BANK = "INSERT INTO banks (name, bic) VALUES (?, ?)";
    public static final String UPDATE_BANK = "UPDATE banks SET name = ?, bic = ? WHERE id = ?";
    public static final String DELETE_BANK_BY_ID = "DELETE FROM banks WHERE id = ?";
}
