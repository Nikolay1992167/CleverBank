package ru.clevertec.service.api;

import ru.clevertec.entity.Transaction;

import java.math.BigDecimal;
import java.sql.SQLException;

public interface Service {
    void deposit(Long accountId, BigDecimal amount) throws SQLException, ClassNotFoundException;
    void withdraw(Long accountId, BigDecimal amount) throws SQLException, ClassNotFoundException;
    void transfer(Long fromAccountId, Long toAccountId, BigDecimal amount) throws SQLException, ClassNotFoundException;
    void accrueInterest();
    void printReceipt(Transaction transaction);

}
