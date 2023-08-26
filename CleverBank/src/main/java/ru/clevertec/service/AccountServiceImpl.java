package ru.clevertec.service;

import lombok.RequiredArgsConstructor;
import ru.clevertec.dao.api.AccountDAO;
import ru.clevertec.entity.Account;
import ru.clevertec.service.api.AccountService;

import java.sql.SQLException;
import java.util.List;
@RequiredArgsConstructor
public class AccountServiceImpl implements AccountService {

    private final AccountDAO accountDAO;

    public List<Account> geAllAccounts() throws SQLException, ClassNotFoundException {
        return accountDAO.getAllAccounts();
    }

    public Account getAccountById(Long id) throws SQLException, ClassNotFoundException {
        return accountDAO.getAccountById(id);
    }

    public void addAccount(Account account) throws SQLException, ClassNotFoundException {
        accountDAO.addAccount(account);
    }

    public void updateAccount(Account account) throws SQLException, ClassNotFoundException {
        accountDAO.updateAccount(account);
    }

    public void deleteAccount(Account account) throws SQLException, ClassNotFoundException {
        accountDAO.deleteAccount(account);
    }
}
