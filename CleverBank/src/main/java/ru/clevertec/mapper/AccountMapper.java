package ru.clevertec.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import ru.clevertec.data.account.request.RequestAccountDto;
import ru.clevertec.data.account.response.ResponseAccountDto;
import ru.clevertec.entity.Account;


public interface AccountMapper {
    /**
     * Mapping request dto to account
     *
     * @param source current request dto
     * @return account without ID
     */
    @Mapping(target = "id", ignore = true)
    Account getAccount(RequestAccountDto source);

    /**
     * Mapping account to response dto
     *
     * @param source current account
     * @return response dto
     */
    ResponseAccountDto getResponseDto(Account source);
}
