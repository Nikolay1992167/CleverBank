package ru.clevertec.service;

import lombok.RequiredArgsConstructor;
import ru.clevertec.dao.api.TransactionDAO;
import ru.clevertec.entity.Transaction;
import ru.clevertec.service.api.TransactionService;

import java.sql.SQLException;
import java.util.List;
@RequiredArgsConstructor
public class TransactionServiceImpl implements TransactionService {

    private final TransactionDAO transactionDAO;

    public List<Transaction> getAllTransactions() throws SQLException, ClassNotFoundException {
        return transactionDAO.getAllTransactions();
    }

    public Transaction getTransactionById(Long id) throws SQLException, ClassNotFoundException {
        return transactionDAO.getTransactionById(id);
    }

    public void addTransaction(Transaction transaction) throws SQLException, ClassNotFoundException {
        transactionDAO.addTransaction(transaction);
    }

    public void updateTransaction(Transaction transaction) throws SQLException, ClassNotFoundException {
        transactionDAO.updateTransaction(transaction);
    }

    public void deleteTransaction(Transaction transaction) throws SQLException, ClassNotFoundException {
        transactionDAO.deleteTransaction(transaction);
    }
}
