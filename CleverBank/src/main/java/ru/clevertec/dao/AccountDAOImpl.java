package ru.clevertec.dao;

import lombok.RequiredArgsConstructor;
import ru.clevertec.dao.api.AccountDAO;
import ru.clevertec.dao.api.BankDAO;
import ru.clevertec.dao.api.UserDAO;
import ru.clevertec.entity.Account;
import ru.clevertec.entity.Bank;
import ru.clevertec.entity.User;
import ru.clevertec.exception.ResourceSqlException;

import javax.sql.DataSource;
import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static java.sql.Statement.RETURN_GENERATED_KEYS;
import static ru.clevertec.dao.util.AccountSQLUtil.*;

@RequiredArgsConstructor
public class AccountDAOImpl implements AccountDAO {

    private final DataSource dataSource;

    private final BankDAO bankDAO;
    private final UserDAO userDAO;

    private Account createAccountFromResultSet(ResultSet resultSet) throws SQLException {
        Long id = resultSet.getLong("id");
        String number = resultSet.getString("number");
        BigDecimal balance = resultSet.getBigDecimal("balance");
        Long bankId = resultSet.getLong("bank_id");
        Long userId = resultSet.getLong("user_id");
        Optional<Bank> bank = bankDAO.getBankById(bankId);
        Optional<User> user = userDAO.getUserById(userId);
        return new Account(id, number, balance, bank, user);
    }

    @Override
    public List<Account> getAllAccounts() {
        try (Connection connection = dataSource.getConnection();
             PreparedStatement statement = connection.prepareStatement(SELECT_ALL_ACCOUNTS)
        ) {
            try (ResultSet resultSet = statement.executeQuery()) {
                final List<Account> accounts = new ArrayList<>();
                while (resultSet.next()) {
                    Account account = createAccountFromResultSet(resultSet);
                    accounts.add(account);
                }
                return accounts;
            }
        } catch (SQLException e) {
            throw new ResourceSqlException();
        }
    }

    @Override
    public Optional<Account> getAccountById(Long id) {
        try (Connection connection = dataSource.getConnection();
             PreparedStatement statement = connection.prepareStatement(SELECT_ACCOUNT_BY_ID)
        ) {
            statement.setLong(1, id);
            try (ResultSet resultSet = statement.executeQuery()) {
                Account account;
                return resultSet.next() ? Optional.of(account = createAccountFromResultSet(resultSet)) : Optional.empty();
            }
        } catch (SQLException e) {
            throw new ResourceSqlException();
        }
    }

    @Override
    public void addAccount(Account account) {
        try (Connection connection = dataSource.getConnection();
             PreparedStatement statement = connection.prepareStatement(
                     INSERT_NEW_ACCOUNT, RETURN_GENERATED_KEYS)
        ) {
            statement.setString(1, account.getNumber());
            statement.setBigDecimal(2, account.getBalance());
            statement.setLong(3, account.getBank().getId());
            statement.setLong(4, account.getUser().getId());
            int count = statement.executeUpdate();
            if (count == 0) {
                throw new ResourceSqlException("Creating account failed, no rows affected.");
            }
            ResultSet generatedKeys = statement.getGeneratedKeys();
            if (!generatedKeys.next()) {
                throw new ResourceSqlException("Creating account failed, no ID obtained.");
            }
        } catch (SQLException e) {
            throw new ResourceSqlException();
        }
    }

    @Override
    public void updateAccount(Account account) {
        try (Connection connection = dataSource.getConnection();
             PreparedStatement statement = connection.prepareStatement(UPDATE_ACCOUNT)
        ) {
            statement.setString(1, account.getNumber());
            statement.setBigDecimal(2, account.getBalance());
            statement.setLong(3, account.getBank().getId());
            statement.setLong(4, account.getUser().getId());
            statement.executeUpdate();
        } catch (SQLException e) {
            throw new ResourceSqlException();
        }
    }

    @Override
    public void deleteAccount(Long id) {
        try (Connection connection = dataSource.getConnection();
             PreparedStatement statement = connection.prepareStatement(DELETE_ACCOUNT_BY_ID)
        ) {
            statement.setLong(1, id);
            statement.executeUpdate();
        } catch (SQLException e) {
            throw new ResourceSqlException();
        }
    }
}
