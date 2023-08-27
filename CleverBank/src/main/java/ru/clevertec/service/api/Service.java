package ru.clevertec.service.api;

import ru.clevertec.entity.Transaction;

import java.math.BigDecimal;
import java.sql.SQLException;

public interface Service {
    void deposit(Long accountId, BigDecimal amount);

    void withdraw(Long accountId, BigDecimal amount);

    void transfer(Long fromAccountId, Long toAccountId, BigDecimal amount) throws SQLException;

    void accrueInterest();

    void printReceipt(Transaction transaction);
}
