package ru.clevertec.service;

import lombok.RequiredArgsConstructor;
import ru.clevertec.dao.api.BankDAO;
import ru.clevertec.entity.Bank;
import ru.clevertec.service.api.BankService;

import java.sql.SQLException;
import java.util.List;

@RequiredArgsConstructor
public class BankServiceImpl implements BankService {

    private final BankDAO bankDAO;

    public List<Bank> getAllBanks() throws SQLException, ClassNotFoundException {
        return bankDAO.getAllBanks();
    }

    public Bank getBankById(Long id) throws SQLException, ClassNotFoundException {
        return bankDAO.getBankById(id);
    }

    public void addBank(Bank bank) throws SQLException, ClassNotFoundException {
        bankDAO.addBank(bank);
    }

    public void updateBank(Bank bank) throws SQLException, ClassNotFoundException {
        bankDAO.updateBank(bank);
    }

    public void deleteBank(Bank bank) throws SQLException, ClassNotFoundException {
        bankDAO.deleteBank(bank);
    }
}
