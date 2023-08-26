package ru.clevertec.service.api;

import ru.clevertec.entity.Transaction;

import java.sql.SQLException;
import java.util.List;

public interface TransactionService {
    List<Transaction> getAllTransactions() throws SQLException, ClassNotFoundException;
    Transaction getTransactionById(Long id) throws SQLException, ClassNotFoundException;
    void addTransaction(Transaction transaction) throws SQLException, ClassNotFoundException;
    void updateTransaction(Transaction transaction) throws SQLException, ClassNotFoundException;
    void deleteTransaction(Transaction transaction) throws SQLException, ClassNotFoundException;
}
