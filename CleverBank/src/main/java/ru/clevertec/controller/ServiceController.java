package ru.clevertec.controller;

import com.google.gson.Gson;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import ru.clevertec.data.transaction.response.ResponseTransactionDto;
import ru.clevertec.service.api.Service;

import java.io.IOException;
import java.sql.SQLException;
import java.util.Objects;

@WebServlet("/services/*")
@RequiredArgsConstructor
public class ServiceController extends HttpServlet {
    private final Service service;
    private final Gson gson = new Gson();

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        String pathInfo = req.getPathInfo();
        if (Objects.isNull(pathInfo)) {
            resp.sendError(400, "Operation must be specified");
            return;
        }
        switch (pathInfo) {
            case "/deposit" -> deposit(req, resp);
            case "/withdraw" -> withdraw(req, resp);
            case "/transfer" -> transfer(req, resp);
            default ->
                    resp.sendError(404, String.format("The requested resource [%s] is not available", req.getRequestURI()));
        }
    }

    private void deposit(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        ResponseTransactionDto depositDto = gson.fromJson(req.getReader(), ResponseTransactionDto.class);
        service.deposit(depositDto.fromAccount().getId(), depositDto.amount());
        String json = gson.toJson(depositDto);
        sendJsonResponse(json, resp);
    }

    private void withdraw(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        ResponseTransactionDto withdrawDto = gson.fromJson(req.getReader(), ResponseTransactionDto.class);
        service.withdraw(withdrawDto.fromAccount().getId(), withdrawDto.amount());
        String json = gson.toJson(withdrawDto);
        sendJsonResponse(json, resp);
    }

    private void transfer(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        ResponseTransactionDto transferDto = gson.fromJson(req.getReader(), ResponseTransactionDto.class);
        try {
            service.transfer(transferDto.fromAccount().getId(), transferDto.toAccount().getId(), transferDto.amount());
            String json = gson.toJson(transferDto);
            sendJsonResponse(json, resp);
        } catch (SQLException e) {
            resp.sendError(500, e.getMessage());
        }
    }

    private void sendJsonResponse(String json, HttpServletResponse response) throws IOException {
        response.setContentType("application/json");
        response.setStatus(200);
        response.getWriter().println(json);
    }
}

