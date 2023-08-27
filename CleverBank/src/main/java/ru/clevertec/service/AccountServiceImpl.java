package ru.clevertec.service;

import lombok.RequiredArgsConstructor;
import org.mapstruct.factory.Mappers;
import ru.clevertec.dao.api.AccountDAO;
import ru.clevertec.data.account.request.RequestAccountDto;
import ru.clevertec.data.account.response.ResponseAccountDto;
import ru.clevertec.entity.Account;
import ru.clevertec.exception.AccountNotFoundException;
import ru.clevertec.mapper.AccountMapper;
import ru.clevertec.service.api.AccountService;

import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
public class AccountServiceImpl implements AccountService {

    private final AccountDAO accountDAO;

    private final AccountMapper accountMapper = Mappers.getMapper(AccountMapper.class);

    @Override
    public List<ResponseAccountDto> geAllAccounts() {
        return accountDAO.getAllAccounts().stream()
                .map(accountMapper::getResponseDto)
                .toList();
    }

    @Override
    public ResponseAccountDto getAccountById(Long id) {
        Optional<Account> optionalAccount = accountDAO.getAccountById(id);
        Account account = optionalAccount.orElseThrow(()->new AccountNotFoundException(id));
        return accountMapper.getResponseDto(account);
    }

    @Override
    public void addAccount(RequestAccountDto accountDto) {
        Account account = accountMapper.getAccount(accountDto);
        accountDAO.addAccount(account);
    }

    @Override
    public void updateAccount(Long id, RequestAccountDto accountDto) {
        Account account = accountMapper.getAccount(accountDto);
        account.setId(id);
        accountDAO.updateAccount(account);
    }

    @Override
    public void deleteAccount(Long id) {
        accountDAO.deleteAccount(id);
    }
}
