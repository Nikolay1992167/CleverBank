package ru.clevertec.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import ru.clevertec.data.bank.request.RequestBankDto;
import ru.clevertec.data.bank.response.ResponseBankDto;
import ru.clevertec.entity.Bank;


public interface BankMapper {
    /**
     * Mapping request dto to bank
     *
     * @param source current request dto
     * @return bank without ID
     */
    @Mapping(target = "id", ignore = true)
    Bank getBank(RequestBankDto source);

    /**
     * Mapping bank to response dto
     *
     * @param source current bank
     * @return response dto
     */
    ResponseBankDto getResponseDto(Bank source);
}
