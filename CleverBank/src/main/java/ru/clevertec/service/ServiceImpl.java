package ru.clevertec.service;

import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.PdfWriter;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import ru.clevertec.dao.api.AccountDAO;
import ru.clevertec.dao.api.TransactionDAO;
import ru.clevertec.entity.Account;
import ru.clevertec.entity.Transaction;
import ru.clevertec.service.api.Service;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
@RequiredArgsConstructor
public class ServiceImpl implements Service {
    private final AccountDAO accountDAO;
    private final TransactionDAO transactionDAO;
    private final BigDecimal interestRate;
    private final ScheduledExecutorService executor;

    // Константы для хранения строковых литералов
    static final String ACCOUNT_NOT_FOUND = "Account not found";
    static final String INVALID_AMOUNT = "Invalid amount";
    static final String INSUFFICIENT_FUNDS = "Insufficient funds";
    private static final String DATE_TIME_FORMAT = "dd.MM.yyyy HH:mm:ss";
    private static final String DEPOSIT = "Deposit";
    private static final String WITHDRAWAL = "Withdrawal";
    private static final String TRANSFER = "Transfer";

    public ServiceImpl(AccountDAO accountDAO, TransactionDAO transactionDAO) {
        this.accountDAO = accountDAO;
        this.transactionDAO = transactionDAO;
        interestRate = new BigDecimal("0.01");
        executor = Executors.newSingleThreadScheduledExecutor();
        executor.scheduleAtFixedRate(this::accrueInterest, 0, 30, TimeUnit.SECONDS);
    }

    public void close() {
        executor.shutdown();
        ConnectorDB.closeResources();
    }

    // метод для пополнения средств на счет
    public void deposit(Long accountId, BigDecimal amount) throws SQLException, ClassNotFoundException {
        if (accountId == null) {
            throw new IllegalArgumentException(ACCOUNT_NOT_FOUND);
        }
        if (amount == null || amount.compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException(INVALID_AMOUNT);
        }
        Connection connection = null;
        try {
            connection = ConnectorDB.getConnection();
            connection.setAutoCommit(false);
            Account account = accountDAO.getAccountById(accountId);
            if (account == null) {
                throw new IllegalArgumentException(ACCOUNT_NOT_FOUND);
            }
            account.setBalance(account.getBalance().add(amount));
            accountDAO.updateAccount(account);
            Transaction transaction = new Transaction(null, null, account, amount, LocalDateTime.now());
            transactionDAO.addTransaction(transaction);
            connection.commit();
            printReceipt(transaction);
        } catch (SQLException | ClassNotFoundException e) {
            if (connection != null) {
                connection.rollback();
            }
            throw e;
        } finally {
            if (connection != null) {
                connection.setAutoCommit(true);
                connection.close();
            }
        }
    }

    // метод для снятия средств со счета
    public void withdraw(Long accountId, BigDecimal amount) throws SQLException, ClassNotFoundException {
        if (accountId == null) {
            throw new IllegalArgumentException(ACCOUNT_NOT_FOUND);
        }
        if (amount == null || amount.compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException(INVALID_AMOUNT);
        }
        Connection connection = null;
        try {
            connection = ConnectorDB.getConnection();
            connection.setAutoCommit(false);
            Account account = accountDAO.getAccountById(accountId);
            if (account == null) {
                throw new IllegalArgumentException(ACCOUNT_NOT_FOUND);
            }
            if (account.getBalance().compareTo(amount) < 0) {
                throw new IllegalStateException(INSUFFICIENT_FUNDS);
            }
            account.setBalance(account.getBalance().subtract(amount));
            accountDAO.updateAccount(account);
            Transaction transaction = new Transaction(null, account, null, amount, LocalDateTime.now());
            transactionDAO.addTransaction(transaction);
            connection.commit();
            printReceipt(transaction);
        } catch (SQLException | ClassNotFoundException e) {
            if (connection != null) {
                connection.rollback();
            }
            throw e;
        } finally {
            if (connection != null) {
                connection.setAutoCommit(true);
                connection.close();
            }
        }
    }

    // метод для перевода средств между двумя счетами внутри одного банка или между разными банками
    public void transfer(Long fromAccountId, Long toAccountId, BigDecimal amount) throws SQLException, ClassNotFoundException {
        if (fromAccountId == null || toAccountId == null) {
            throw new IllegalArgumentException(ACCOUNT_NOT_FOUND);
        }
        if (amount == null || amount.compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException(INVALID_AMOUNT);
        }
        Connection connection = null;
        try {
            connection = ConnectorDB.getConnection();
            connection.setAutoCommit(false);
            Account fromAccount = accountDAO.getAccountById(fromAccountId);
            if (fromAccount == null) {
                throw new IllegalArgumentException(ACCOUNT_NOT_FOUND);
            }
            Account toAccount = accountDAO.getAccountById(toAccountId);
            if (toAccount == null) {
                throw new IllegalArgumentException(ACCOUNT_NOT_FOUND);
            }
            if (fromAccount.getBalance().compareTo(amount) < 0) {
                throw new IllegalStateException(INSUFFICIENT_FUNDS);
            }
            if (fromAccount.getBank().getBic().equals(toAccount.getBank().getBic())) {
                System.out.println("Transfer between accounts of the same bank.");
            } else {
                System.out.println("Transfer between accounts of different banks.");
            }
            fromAccount.setBalance(fromAccount.getBalance().subtract(amount));
            accountDAO.updateAccount(fromAccount);
            toAccount.setBalance(toAccount.getBalance().add(amount));
            accountDAO.updateAccount(toAccount);
            Transaction transaction = new Transaction(null, fromAccount, toAccount, amount, LocalDateTime.now());
            transactionDAO.addTransaction(transaction);
            printReceipt(transaction);
            connection.commit();
        } catch (SQLException | ClassNotFoundException e) {
            if (connection != null) {
                connection.rollback();
            }
            throw e;
        } finally {
            if (connection != null) {
                connection.setAutoCommit(true);
                connection.close();
            }
        }
    }


    // метод для начисления процентов по всем счетам в конце месяца
    @SneakyThrows
    public void accrueInterest() {
        Connection connection = null;
        try {
            connection = ConnectorDB.getConnection();
            connection.setAutoCommit(false);
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
            connection.commit();
        } catch (SQLException | ClassNotFoundException e) {
            if (connection != null) {
                try {
                    connection.rollback();
                } catch (SQLException ex) {
                    throw new RuntimeException(ex);
                }
            }
            e.printStackTrace();
        } finally {
            if (connection != null) {
                connection.setAutoCommit(true);
                connection.close();
            }
        }
    }

    // метод для распечатки чека об операции в формате PDF
    public void printReceipt(Transaction transaction) {
        try {
            Document document = new Document();
            String fileName = "check" + File.separator + transaction.getId() + ".pdf";
            File file = new File(fileName);
            FileOutputStream fos = new FileOutputStream(file);
            PdfWriter.getInstance(document, fos);
            document.open();
            document.add(new Paragraph("Clever-Bank"));
            document.add(new Paragraph("Date: " + transaction.getDate().format(DateTimeFormatter.ofPattern(DATE_TIME_FORMAT))));
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
