package ru.clevertec.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import ru.clevertec.data.transaction.request.RequestTransactionDto;
import ru.clevertec.data.transaction.response.ResponseTransactionDto;
import ru.clevertec.entity.Transaction;

@Mapper
public interface TransactionMapper {
    /**
     * Mapping request dto to transaction
     *
     * @param source current request dto
     * @return transaction without ID
     */
    @Mapping(target = "id", ignore = true)
    Transaction getTransaction(RequestTransactionDto source);

    /**
     * Mapping transaction to response dto
     *
     * @param source current transaction
     * @return response dto
     */
    ResponseTransactionDto getResponseDto(Transaction source);
}
