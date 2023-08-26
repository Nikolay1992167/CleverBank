package ru.clevertec.dao.api;

import ru.clevertec.entity.Account;
import ru.clevertec.exception.AccountNotFoundException;

import java.util.List;
import java.util.Optional;

public interface AccountDAO {
    /**
     * Find all accounts
     *
     * @return List of all products
     */
    List<Account> getAllAccounts();

    /**
     * Returns saved account
     *
     * @param id - product id
     * @return - Product if contains
     * @throws AccountNotFoundException - if not found
     */
    Optional<Account> getAccountById(Long id);

    /**
     * Save a new account
     *
     * @param account new product without id
     */
    void addAccount(Account account);

    /**
     * Update current account
     *
     * @param account - updated
     */
    void updateAccount(Account account);

    /**
     * Delete a account by ID
     *
     * @param id the account ID
     */
    void deleteAccount(Long id);
}
