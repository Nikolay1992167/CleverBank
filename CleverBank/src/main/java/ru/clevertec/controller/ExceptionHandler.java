package ru.clevertec.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import ru.clevertec.config.ApplicationConfig;
import ru.clevertec.exception.*;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.Map;
import java.util.Objects;

@WebServlet("/exceptions")
public class ExceptionHandler extends HttpServlet {

    private final ObjectMapper mapper;

    public ExceptionHandler() {
        this.mapper = ApplicationConfig.getObjectMapper();
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        final Class<?> clazz = (Class<?>) req.getAttribute("jakarta.servlet.error.exception_type");
        final Object message = req.getAttribute("jakarta.servlet.error.message");
        if (Objects.isNull(clazz) || Objects.isNull(message)) {
            resp.sendError(404, String.format("The requested resource [%s] is not available", req.getRequestURI()));
            return;
        }

        resp.setContentType("application/json");
        final PrintWriter writer = resp.getWriter();
        final Map<String, Object> error = Map.of("error", message);

        String valueAsString = mapper.writeValueAsString(error);
        if (Objects.equals(clazz, BadRequestException.class)) {
            resp.setStatus(400);
        } else if (Objects.equals(clazz, AccountNotFoundException.class)
                || Objects.equals(clazz, BankNotFoundException.class)
                || Objects.equals(clazz, TransactionNotFoundException.class)
                || Objects.equals(clazz, UserNotFoundException.class)) {
            resp.setStatus(404);
        } else if (Objects.equals(clazz, ResourceSqlException.class) || Objects.equals(clazz, PdfServiceException.class)) {
            resp.setStatus(500);
        } else {
            resp.setStatus(500);
        }
        writer.println(valueAsString);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        this.doGet(req, resp);
    }

    @Override
    protected void doPut(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        this.doGet(req, resp);
    }

    @Override
    protected void doDelete(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        this.doGet(req, resp);
    }
}
