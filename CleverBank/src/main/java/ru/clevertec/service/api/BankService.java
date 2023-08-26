package ru.clevertec.service.api;

import ru.clevertec.entity.Bank;

import java.sql.SQLException;
import java.util.List;

public interface BankService {
    List<Bank> getAllBanks() throws SQLException, ClassNotFoundException;
    Bank getBankById(Long id) throws SQLException, ClassNotFoundException;
    void addBank(Bank bank) throws SQLException, ClassNotFoundException;
    void updateBank(Bank bank) throws SQLException, ClassNotFoundException;
    void deleteBank(Bank bank) throws SQLException, ClassNotFoundException;
}
