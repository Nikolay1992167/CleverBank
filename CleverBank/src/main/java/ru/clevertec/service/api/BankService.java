package ru.clevertec.service.api;

import ru.clevertec.data.bank.request.RequestBankDto;
import ru.clevertec.data.bank.response.ResponseBankDto;

import java.util.List;

public interface BankService {
    List<ResponseBankDto> getAllBanks();

    ResponseBankDto getBankById(Long id);

    void addBank(RequestBankDto bankDto);

    void updateBank(Long id, RequestBankDto bankDto);

    void deleteBank(Long id);
}
