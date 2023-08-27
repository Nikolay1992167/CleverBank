package ru.clevertec.service.api;

import ru.clevertec.data.transaction.request.RequestTransactionDto;
import ru.clevertec.data.transaction.response.ResponseTransactionDto;

import java.util.List;

public interface TransactionService {
    List<ResponseTransactionDto> getAllTransactions();

    ResponseTransactionDto getTransactionById(Long id);

    void addTransaction(RequestTransactionDto transactionDto);

    void updateTransaction(Long id, RequestTransactionDto transactionDto);

    void deleteTransaction(Long id);
}
