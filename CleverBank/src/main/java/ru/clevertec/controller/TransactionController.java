package ru.clevertec.controller;

import com.google.gson.Gson;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import ru.clevertec.data.bank.request.RequestBankDto;
import ru.clevertec.data.bank.response.ResponseBankDto;
import ru.clevertec.data.transaction.request.RequestTransactionDto;
import ru.clevertec.data.transaction.response.ResponseTransactionDto;
import ru.clevertec.service.api.BankService;
import ru.clevertec.service.api.TransactionService;
import ru.clevertec.util.ControllerUtil;

import java.io.IOException;
import java.util.List;
import java.util.Objects;

@WebServlet("/transactions/*")
@RequiredArgsConstructor
public class TransactionController extends HttpServlet {

    private final TransactionService transactionService;
    private final Gson gson = new Gson();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        String pathInfo = req.getPathInfo();
        if (Objects.isNull(pathInfo)) {
            List<ResponseTransactionDto> transactions;
            transactions = transactionService.getAllTransactions();
            String json = gson.toJson(transactions);
            sendJsonResponse(json, resp);
        } else if (ControllerUtil.isId(pathInfo)) {
            String id = pathInfo.substring(1);
            ResponseTransactionDto transaction;
            transaction = transactionService.getTransactionById(Long.parseLong(id));
            String json = gson.toJson(transaction);
            sendJsonResponse(json, resp);
        } else {
            resp.sendError(404, String.format("The requested resource [%s] is not available", req.getRequestURI()));
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        RequestTransactionDto transactionDto = gson.fromJson(req.getReader(), RequestTransactionDto.class);
        transactionService.addTransaction(transactionDto);
        String json = gson.toJson(transactionDto);
        sendJsonResponse(json, resp);
    }

    @Override
    protected void doPut(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        String pathInfo = req.getPathInfo();
        if (ControllerUtil.isId(pathInfo)) {
            resp.sendError(400, "Id must be set");
            return;
        }
        String id = pathInfo.substring(1);
        RequestTransactionDto transactionDto = gson.fromJson(req.getReader(), RequestTransactionDto.class);
        transactionService.updateTransaction(Long.parseLong(id), transactionDto);
        String json = gson.toJson(transactionDto);
        sendJsonResponse(json, resp);
    }

    @Override
    protected void doDelete(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        String pathInfo = req.getPathInfo();
        if (!ControllerUtil.isId(pathInfo)) {
            resp.sendError(400, "Id must be set");
            return;
        }
        String id = pathInfo.substring(1);
        transactionService.deleteTransaction(Long.parseLong(id));
        resp.setStatus(204);
    }

    private void sendJsonResponse(String json, HttpServletResponse response) throws IOException {
        response.setContentType("application/json");
        response.setStatus(200);
        response.getWriter().println(json);
    }
}
