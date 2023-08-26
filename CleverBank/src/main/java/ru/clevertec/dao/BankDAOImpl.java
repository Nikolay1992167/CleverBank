package ru.clevertec.dao;

import lombok.RequiredArgsConstructor;
import ru.clevertec.dao.api.BankDAO;
import ru.clevertec.entity.Bank;
import ru.clevertec.exception.ResourceSqlException;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static java.sql.Statement.RETURN_GENERATED_KEYS;
import static ru.clevertec.dao.util.BankSQLUtil.*;

@RequiredArgsConstructor
public class BankDAOImpl implements BankDAO {

    private final DataSource dataSource;

    private Bank createBankFromResultSet(ResultSet resultSet) throws SQLException {
        Long id = resultSet.getLong("id");
        String name = resultSet.getString("name");
        String bic = resultSet.getString("bic");
        return new Bank(id, name, bic);
    }

    @Override
    public List<Bank> getAllBanks() {
        try (Connection connection = dataSource.getConnection();
             PreparedStatement statement = connection.prepareStatement(SELECT_ALL_BANKS)
        ) {
            try (ResultSet resultSet = statement.executeQuery()) {
                final List<Bank> banks = new ArrayList<>();
                while (resultSet.next()) {
                    Bank bank = createBankFromResultSet(resultSet);
                    banks.add(bank);
                }
                return banks;
            }
        } catch (SQLException e) {
            throw new ResourceSqlException();
        }
    }

    @Override
    public Optional<Bank> getBankById(Long id) {
        try (Connection connection = dataSource.getConnection();
             PreparedStatement statement = connection.prepareStatement(SELECT_BANK_FROM_BY_ID)
        ) {
            statement.setLong(1, id);
            try (ResultSet resultSet = statement.executeQuery()) {
                Bank bank;
                return resultSet.next() ? Optional.of(bank = createBankFromResultSet(resultSet)) : Optional.empty();
            }
        } catch (SQLException e) {
            throw new ResourceSqlException();
        }
    }

    @Override
    public void addBank(Bank bank) {
        try (Connection connection = dataSource.getConnection();
             PreparedStatement statement = connection.prepareStatement(
                     INSERT_NEW_BANK, RETURN_GENERATED_KEYS)
        ) {
            statement.setString(1, bank.getName());
            statement.setString(2, bank.getBic());
            int count = statement.executeUpdate();
            if (count == 0) {
                throw new ResourceSqlException("Creating bank failed, no rows affected.");
            }
            ResultSet generatedKeys = statement.getGeneratedKeys();
            if (!generatedKeys.next()) {
                throw new ResourceSqlException("Creating bank failed, no ID obtained.");
            }
        } catch (SQLException e) {
            throw new ResourceSqlException();
        }
    }

    @Override
    public void updateBank(Bank bank) {
        try (Connection connection = dataSource.getConnection();
             PreparedStatement statement = connection.prepareStatement(UPDATE_BANK)
        ) {
            statement.setString(1, bank.getName());
            statement.setString(2, bank.getBic());
            statement.executeUpdate();
        } catch (SQLException e) {
            throw new ResourceSqlException();
        }
    }

    @Override
    public void deleteBank(Long id) {
        try (Connection connection = dataSource.getConnection();
             PreparedStatement statement = connection.prepareStatement(DELETE_BANK_BY_ID)
        ) {
            statement.setLong(1, id);
            statement.executeUpdate();
        } catch (SQLException e) {
            throw new ResourceSqlException();
        }
    }
}

