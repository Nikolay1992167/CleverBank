package ru.clevertec.service.api;

import ru.clevertec.data.account.request.RequestAccountDto;
import ru.clevertec.data.account.response.ResponseAccountDto;

import java.util.List;

public interface AccountService {

    List<ResponseAccountDto> geAllAccounts();

    ResponseAccountDto getAccountById(Long id);

    void addAccount(RequestAccountDto accountDto);

    void updateAccount(Long id, RequestAccountDto accountDto);

    void deleteAccount(Long id);
}
