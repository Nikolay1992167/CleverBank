package ru.clevertec.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import lombok.RequiredArgsConstructor;
import org.apache.commons.configuration2.Configuration;
import org.apache.commons.configuration2.YAMLConfiguration;
import org.apache.commons.configuration2.ex.ConfigurationException;
import org.postgresql.ds.PGSimpleDataSource;
import ru.clevertec.dao.AccountDAOImpl;
import ru.clevertec.dao.BankDAOImpl;
import ru.clevertec.dao.TransactionDAOImpl;
import ru.clevertec.dao.UserDAOImpl;
import ru.clevertec.dao.api.AccountDAO;
import ru.clevertec.dao.api.BankDAO;
import ru.clevertec.dao.api.TransactionDAO;
import ru.clevertec.dao.api.UserDAO;
import ru.clevertec.service.*;
import ru.clevertec.service.api.*;

import javax.sql.DataSource;
import java.io.InputStream;
import java.util.TimeZone;

public class ApplicationConfig {

    private static final String APPLICATION_YML = "/application.yml";
    private static final String DATASOURCE_URL = "datasource.url";
    private static final String DATASOURCE_USERNAME = "datasource.username";
    private static final String DATASOURCE_PASSWORD = "datasource.password";

    private static final DataSource dataSource;
    private static final Configuration configuration;
    private static final ObjectMapper objectMapper;
    private static final AccountDAO accountDAO;
    private static final AccountService accountService;
    private static final BankDAO bankDAO;
    private static final BankService bankService;
    private static final Service service;
    private static final TransactionDAO transactionDAO;
    private static final TransactionService transactionService;
    private static final UserDAO userDAO;
    private static final UserService userService;

    static {
        TimeZone.setDefault(TimeZone.getTimeZone("UTC"));
    }

    static {
        final InputStream resourceAsStream = ApplicationConfig.class.getResourceAsStream(APPLICATION_YML);
        final YAMLConfiguration yamlConfiguration = new YAMLConfiguration();
        try {
            yamlConfiguration.read(resourceAsStream);
        } catch (ConfigurationException e) {
            throw new RuntimeException(e);
        }
        configuration = yamlConfiguration;
    }

    static {
        PGSimpleDataSource pgSimpleDataSource = new PGSimpleDataSource();
        pgSimpleDataSource.setURL(configuration.getString(DATASOURCE_URL));
        pgSimpleDataSource.setUser(configuration.getString(DATASOURCE_USERNAME));
        pgSimpleDataSource.setPassword(configuration.getString(DATASOURCE_PASSWORD));
        dataSource = pgSimpleDataSource;
    }

    static {
        bankDAO = new BankDAOImpl(dataSource);
        userDAO = new UserDAOImpl(dataSource);
        accountDAO = new AccountDAOImpl(dataSource, bankDAO, userDAO);
        transactionDAO = new TransactionDAOImpl(dataSource, accountDAO);
        accountService = new AccountServiceImpl(accountDAO);
        bankService = new BankServiceImpl(bankDAO);
        service = new ServiceImpl(dataSource, accountDAO, transactionDAO);
        transactionService = new TransactionServiceImpl(transactionDAO);
        userService = new UserServiceImpl(userDAO);
        objectMapper = new ObjectMapper().setPropertyNamingStrategy(PropertyNamingStrategies.SNAKE_CASE);
    }

    private ApplicationConfig() {
        throw new UnsupportedOperationException("Config class");
    }

    public static ObjectMapper getObjectMapper() {
        return objectMapper;
    }

    public static Configuration getConfiguration() {
        return configuration;
    }

    public static DataSource getDataSource() {
        return dataSource;
    }

    public static AccountService getAccountService() {
        return accountService;
    }

    public static BankService getBankService() {
        return bankService;
    }

    public static Service getService() {
        return service;
    }

    public static TransactionService getTransactionService() {
        return transactionService;
    }

    public static UserService getUserService() {
        return userService;
    }
}
