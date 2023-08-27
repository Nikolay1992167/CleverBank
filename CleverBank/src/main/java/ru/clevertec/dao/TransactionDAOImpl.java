package ru.clevertec.dao;

import lombok.RequiredArgsConstructor;
import ru.clevertec.dao.api.AccountDAO;
import ru.clevertec.dao.api.TransactionDAO;
import ru.clevertec.entity.Account;
import ru.clevertec.entity.Transaction;
import ru.clevertec.exception.ResourceSqlException;

import javax.sql.DataSource;
import java.math.BigDecimal;
import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static java.sql.Statement.RETURN_GENERATED_KEYS;
import static ru.clevertec.dao.util.TransactionSQLUtil.*;

@RequiredArgsConstructor
public class TransactionDAOImpl implements TransactionDAO {

    private final DataSource dataSource;
    private final AccountDAO accountDAO;

    private Transaction createTransactionFromResultSet(ResultSet resultSet) throws SQLException, ClassNotFoundException {
        Long id = resultSet.getLong("id");
        Long fromAccountId = resultSet.getLong("from_account_id");
        Long toAccountId = resultSet.getLong("to_account_id");
        BigDecimal amount = resultSet.getBigDecimal("amount");
        LocalDateTime date = resultSet.getTimestamp("date").toLocalDateTime();
        Optional<Account> fromAccount = accountDAO.getAccountById(fromAccountId);
        Optional<Account> toAccount = accountDAO.getAccountById(toAccountId);
        return new Transaction(id, fromAccount, toAccount, amount, date);
    }

    @Override
    public List<Transaction> getAllTransactions() {
        try (Connection connection = dataSource.getConnection();
             PreparedStatement statement = connection.prepareStatement(SELECT_ALL_TRANSACTION)
        ) {
            try (ResultSet resultSet = statement.executeQuery()) {
                final List<Transaction> transactions = new ArrayList<>();
                while (resultSet.next()) {
                    Transaction transaction = createTransactionFromResultSet(resultSet);
                    transactions.add(transaction);
                }
                return transactions;
            } catch (ClassNotFoundException e) {
                throw new RuntimeException(e);
            }
        } catch (SQLException e) {
            throw new ResourceSqlException();
        }
    }

    @Override
    public Optional<Transaction> getTransactionById(Long id) {
        try (Connection connection = dataSource.getConnection();
             PreparedStatement statement = connection.prepareStatement(SELECT_TRANSACTION_BY_ID)
        ) {
            statement.setLong(1, id);
            try (ResultSet resultSet = statement.executeQuery()) {
                Transaction transaction;
                return resultSet.next() ? Optional.of(transaction = createTransactionFromResultSet(resultSet)) : Optional.empty();
            } catch (ClassNotFoundException e) {
                throw new RuntimeException(e);
            }
        } catch (SQLException e) {
            throw new ResourceSqlException();
        }
    }

    @Override
    public void addTransaction(Transaction transaction) {
        try (Connection connection = dataSource.getConnection();
             PreparedStatement statement = connection.prepareStatement(
                     INSERT_NEW_TRANSACTION, RETURN_GENERATED_KEYS)
        ) {
            statement.setLong(1, transaction.getFromAccount().getId());
            statement.setLong(2, transaction.getToAccount().getId());
            statement.setBigDecimal(3, transaction.getAmount());
            statement.setTimestamp(4, java.sql.Timestamp.valueOf(transaction.getDate()));
            int count = statement.executeUpdate();
            if (count == 0) {
                throw new ResourceSqlException("Creating transaction failed, no rows affected.");
            }
            ResultSet generatedKeys = statement.getGeneratedKeys();
            if (!generatedKeys.next()) {
                throw new ResourceSqlException("Creating transaction failed, no ID obtained.");
            }
        } catch (SQLException e) {
            throw new ResourceSqlException();
        }
    }

    @Override
    public void updateTransaction(Transaction transaction) {
        try (Connection connection = dataSource.getConnection();
             PreparedStatement statement = connection.prepareStatement(UPDATE_TRANSACTION)
        ) {
            statement.setLong(1, transaction.getFromAccount().getId());
            statement.setLong(2, transaction.getToAccount().getId());
            statement.setBigDecimal(3, transaction.getAmount());
            statement.setTimestamp(4, java.sql.Timestamp.valueOf(transaction.getDate()));
            statement.executeUpdate();
        } catch (SQLException e) {
            throw new ResourceSqlException();
        }
    }

    @Override
    public void deleteTransaction(Long id) {
        try (Connection connection = dataSource.getConnection();
             PreparedStatement statement = connection.prepareStatement(DELETE_TRANSACTION_BY_ID)
        ) {
            statement.setLong(1, id);
            statement.executeUpdate();
        } catch (SQLException e) {
            throw new ResourceSqlException();
        }
    }
}