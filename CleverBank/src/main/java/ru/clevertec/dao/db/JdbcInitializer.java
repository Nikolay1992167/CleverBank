package ru.clevertec.dao.db;

import lombok.RequiredArgsConstructor;
import ru.clevertec.exception.InitializationException;

import javax.sql.DataSource;
import java.io.IOException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Objects;

@RequiredArgsConstructor
public class JdbcInitializer {

    private static final Path DROP;
    private static final Path CREATE;
    private static final Path INSERT;

    static {
        URL ddlDrop = JdbcInitializer.class.getResource("/sql/ddl/drop.sql");
        URL ddlCreate = JdbcInitializer.class.getResource("/sql/ddl/create.sql");
        URL dmlInsert = JdbcInitializer.class.getResource("/sql/dml/insert.sql");
        if (Objects.isNull(ddlDrop)) {
            throw new InitializationException("resources/sql/ddl/drop.sql");
        } else if (Objects.isNull(ddlCreate)) {
            throw new InitializationException("resources/sql/ddl/create.sql");
        } else if (Objects.isNull(dmlInsert)) {
            throw new InitializationException("resources/sql/ddl/insert.sql");
        }
        DROP = Paths.get(ddlDrop.getPath());
        CREATE = Paths.get(ddlCreate.getPath());
        INSERT = Paths.get(dmlInsert.getPath());
    }

    private final DataSource dataSource;

    public void create() {
        executeFile(CREATE);
    }
    public void dropCreate() {
        executeFile(DROP);
        executeFile(CREATE);
    }

    public void createInsert() {
        executeFile(DROP);
        executeFile(CREATE);
        executeFile(INSERT);
    }

    private void executeFile(Path path) {
        try(Connection connection = dataSource.getConnection();
            Statement statement = connection.createStatement()) {
            statement.execute(Files.readString(path));
        }catch (SQLException | IOException e) {
            throw new InitializationException("Could not execute drop");
        }
    }
}
