package ru.clevertec.service.api;

import ru.clevertec.entity.Account;

import java.sql.SQLException;
import java.util.List;

public interface AccountService {

    List<Account> geAllAccounts() throws SQLException, ClassNotFoundException;
    Account getAccountById(Long id) throws SQLException, ClassNotFoundException;

    void addAccount(Account account) throws SQLException, ClassNotFoundException;

    void updateAccount(Account account) throws SQLException, ClassNotFoundException;

    void deleteAccount(Account account) throws SQLException, ClassNotFoundException;
}
