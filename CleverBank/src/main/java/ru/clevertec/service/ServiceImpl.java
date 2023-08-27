package ru.clevertec.service;

import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.PdfWriter;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import ru.clevertec.dao.api.AccountDAO;
import ru.clevertec.dao.api.TransactionDAO;
import ru.clevertec.entity.Account;
import ru.clevertec.entity.Transaction;
import ru.clevertec.exception.AccountNotFoundException;
import ru.clevertec.service.api.Service;

import javax.sql.DataSource;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Locale;
import java.util.Optional;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import static ru.clevertec.service.constants.Constants.*;

@RequiredArgsConstructor
public class ServiceImpl implements Service {
    private final AccountDAO accountDAO;
    private final TransactionDAO transactionDAO;
    private final BigDecimal interestRate;
    private final ScheduledExecutorService executor;

    public ServiceImpl(DataSource dataSource, AccountDAO accountDAO, TransactionDAO transactionDAO) {
        this.accountDAO = accountDAO;
        this.transactionDAO = transactionDAO;
        interestRate = new BigDecimal("0.01");
        executor = Executors.newSingleThreadScheduledExecutor();
        executor.scheduleAtFixedRate(this::accrueInterest, 0, 30, TimeUnit.SECONDS);
    }

    @Override
    public void deposit(@NonNull Long accountId, BigDecimal amount) {
        if (amount == null || amount.compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException(INVALID_AMOUNT);
        }
        Optional<Account> accountOptional = accountDAO.getAccountById(accountId);
        Account account = accountOptional.orElseThrow(() -> new AccountNotFoundException(accountId));
        account.setBalance(account.getBalance().add(amount));
        accountDAO.updateAccount(account);
        Transaction transaction = new Transaction(null, null, account, amount, LocalDateTime.now());
        transactionDAO.addTransaction(transaction);
        printReceipt(transaction);
    }

    @Override
    public void withdraw(@NonNull Long accountId, BigDecimal amount) {
        if (amount == null || amount.compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException(INVALID_AMOUNT);
        }
        Optional<Account> accountOptional = accountDAO.getAccountById(accountId);
        Account account = accountOptional.orElseThrow(() -> new AccountNotFoundException(accountId));
        if (account.getBalance().compareTo(amount) < 0) {
            throw new IllegalStateException(INSUFFICIENT_FUNDS);
        }
        account.setBalance(account.getBalance().subtract(amount));
        accountDAO.updateAccount(account);
        Transaction transaction = new Transaction(null, account, null, amount, LocalDateTime.now());
        transactionDAO.addTransaction(transaction);
        printReceipt(transaction);
    }

    @Override
    public void transfer(@NonNull Long fromAccountId, @NonNull Long toAccountId, BigDecimal amount) {
        synchronized (fromAccountId < toAccountId ? fromAccountId : toAccountId) {
            synchronized (fromAccountId < toAccountId ? toAccountId : fromAccountId) {
                Account fromAccount = accountDAO.getAccountById(fromAccountId)
                        .orElseThrow(() -> new AccountNotFoundException(fromAccountId));
                Account toAccount = accountDAO.getAccountById(toAccountId)
                        .orElseThrow(() -> new AccountNotFoundException(toAccountId));
                if (fromAccount.getBalance().compareTo(amount) < 0) {
                    throw new IllegalArgumentException(INVALID_AMOUNT);
                }
                fromAccount.setBalance(fromAccount.getBalance().subtract(amount));
                toAccount.setBalance(toAccount.getBalance().add(amount));
                accountDAO.updateAccount(fromAccount);
                accountDAO.updateAccount(toAccount);
                Transaction transaction = new Transaction(null, fromAccount, toAccount, amount, LocalDateTime.now());
                transactionDAO.addTransaction(transaction);
                printReceipt(transaction);
            }
        }
    }

    @Override
    public void accrueInterest() {
        List<Account> accounts = accountDAO.getAllAccounts();
        LocalDate today = LocalDate.now();
        for (Account account : accounts) {
            if (today.getDayOfMonth() == today.lengthOfMonth()) {
                BigDecimal interest = account.getBalance().multiply(interestRate);
                account.setBalance(account.getBalance().add(interest));
                accountDAO.updateAccount(account);
                Transaction transaction = new Transaction(null, null, account, interest, LocalDateTime.now());
                transactionDAO.addTransaction(transaction);
                printReceipt(transaction);
            }
        }
    }

    @Override
    public void printReceipt(Transaction transaction) {
        try (FileOutputStream fos = new FileOutputStream(new File("check" + File.separator + transaction.getId() + ".pdf"))) {
            Document document = new Document();
            String fileName = "check" + File.separator + transaction.getId() + ".pdf";
            File file = new File(fileName);
            PdfWriter.getInstance(document, fos);
            document.open();
            document.add(new Paragraph("Clever-Bank"));
            document.add(new Paragraph("Date: " + transaction.getDate().format(DateTimeFormatter.ofPattern(DATE_TIME_FORMAT).withLocale(Locale.forLanguageTag("ru")))));
            if (transaction.getFromAccount() == null) {
                document.add(new Paragraph(DEPOSIT));
                document.add(new Paragraph("To account: " + transaction.getToAccount().getNumber()));
                document.add(new Paragraph("Amount: " + transaction.getAmount()));
            } else if (transaction.getToAccount() == null) {
                document.add(new Paragraph(WITHDRAWAL));
                document.add(new Paragraph("From account: " + transaction.getFromAccount().getNumber()));
                document.add(new Paragraph("Amount: " + transaction.getAmount()));
            } else {
                document.add(new Paragraph(TRANSFER));
                document.add(new Paragraph("From account: " + transaction.getFromAccount().getNumber()));
                document.add(new Paragraph("To account: " + transaction.getToAccount().getNumber()));
                document.add(new Paragraph("Amount: " + transaction.getAmount()));
                if (transaction.getToAccount().getId() != null) {
                    document.add(new Paragraph("Reference number: " + transaction.getToAccount().getId()));
                }
            }
            document.close();
        } catch (IOException | DocumentException e) {
            e.printStackTrace();
        }
    }
}

