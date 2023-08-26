package ru.clevertec.dao.api;

import ru.clevertec.entity.Transaction;
import ru.clevertec.exception.TransactionNotFoundException;

import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

public interface TransactionDAO {
    /**
     * Find all transaction
     *
     * @return List of all transactions
     */
    List<Transaction> getAllTransactions();

    /**
     * Returns saved product
     *
     * @param id - transaction id
     * @return - transaction if contains
     * @throws TransactionNotFoundException - if not found
     */
    Optional <Transaction> getTransactionById(Long id);

    /**
     * Save a new transactional
     *
     * @param transaction new transaction without id
     */
    void addTransaction(Transaction transaction);

    /**
     * Update current transaction
     *
     * @param transaction - updated
     */
    void updateTransaction(Transaction transaction);

    /**
     * Delete a transaction by ID
     *
     * @param id the transaction ID
     */
    void deleteTransaction(Long id);
}
