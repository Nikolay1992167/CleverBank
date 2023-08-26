package ru.clevertec.dao.api;

import ru.clevertec.entity.Bank;
import ru.clevertec.exception.BankNotFoundException;

import java.util.List;
import java.util.Optional;

public interface BankDAO {
    /**
     * Find all banks
     *
     * @return List of all banks
     */
    List<Bank> getAllBanks();

    /**
     * Returns saved bank
     *
     * @param id - bank id
     * @return - Bank if contains
     * @throws BankNotFoundException - if not found
     */
    Optional<Bank> getBankById(Long id);

    /**
     * Save a new bank
     *
     * @param bank new bank without id
     */
    void addBank(Bank bank);

    /**
     * Update current bank
     *
     * @param bank - updated
     */
    void updateBank(Bank bank);

    /**
     * Delete a bank by ID
     *
     * @param id the bank ID
     */
    void deleteBank(Long id);
}
