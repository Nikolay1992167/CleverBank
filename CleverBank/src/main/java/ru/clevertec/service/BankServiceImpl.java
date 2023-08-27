package ru.clevertec.service;

import lombok.RequiredArgsConstructor;
import org.mapstruct.factory.Mappers;
import ru.clevertec.dao.api.BankDAO;
import ru.clevertec.data.bank.request.RequestBankDto;
import ru.clevertec.data.bank.response.ResponseBankDto;
import ru.clevertec.entity.Bank;
import ru.clevertec.exception.BankNotFoundException;
import ru.clevertec.mapper.BankMapper;
import ru.clevertec.service.api.BankService;

import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
public class BankServiceImpl implements BankService {

    private final BankDAO bankDAO;
    private final BankMapper bankMapper = Mappers.getMapper(BankMapper.class);

    @Override
    public List<ResponseBankDto> getAllBanks() {
        return bankDAO.getAllBanks().stream()
                .map(bankMapper::getResponseDto)
                .toList();
    }

    @Override
    public ResponseBankDto getBankById(Long id) {
        Optional<Bank> optionalBank = bankDAO.getBankById(id);
        Bank bank = optionalBank.orElseThrow(() -> new BankNotFoundException(id));
        return bankMapper.getResponseDto(bank);
    }

    @Override
    public void addBank(RequestBankDto bankDto) {
        Bank bank = bankMapper.getBank(bankDto);
        bankDAO.addBank(bank);
    }

    @Override
    public void updateBank(Long id, RequestBankDto bankDto) {
        Bank bank = bankMapper.getBank(bankDto);
        bank.setId(id);
        bankDAO.updateBank(bank);
    }

    @Override
    public void deleteBank(Long id) {
        bankDAO.deleteBank(id);
    }
}
