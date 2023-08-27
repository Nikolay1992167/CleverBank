package ru.clevertec.service;

import lombok.RequiredArgsConstructor;
import org.mapstruct.factory.Mappers;
import ru.clevertec.dao.api.TransactionDAO;
import ru.clevertec.data.transaction.request.RequestTransactionDto;
import ru.clevertec.data.transaction.response.ResponseTransactionDto;
import ru.clevertec.entity.Transaction;
import ru.clevertec.exception.TransactionNotFoundException;
import ru.clevertec.mapper.TransactionMapper;
import ru.clevertec.service.api.TransactionService;

import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
public class TransactionServiceImpl implements TransactionService {

    private final TransactionDAO transactionDAO;
    private final TransactionMapper transactionMapper = Mappers.getMapper(TransactionMapper.class);


    @Override
    public List<ResponseTransactionDto> getAllTransactions() {
        return transactionDAO.getAllTransactions().stream()
                .map(transactionMapper::getResponseDto)
                .toList();
    }

    @Override
    public ResponseTransactionDto getTransactionById(Long id) {
        Optional<Transaction> optionalTransaction = transactionDAO.getTransactionById(id);
        Transaction transaction = optionalTransaction.orElseThrow(() -> new TransactionNotFoundException(id));
        return transactionMapper.getResponseDto(transaction);
    }

    @Override
    public void addTransaction(RequestTransactionDto transactionDto) {
        Transaction transaction = transactionMapper.getTransaction(transactionDto);
        transactionDAO.addTransaction(transaction);
    }

    @Override
    public void updateTransaction(Long id, RequestTransactionDto transactionDto) {
        Transaction transaction = transactionMapper.getTransaction(transactionDto);
        transaction.setId(id);
        transactionDAO.addTransaction(transaction);
    }

    @Override
    public void deleteTransaction(Long id) {
        transactionDAO.deleteTransaction(id);
    }
}
