package ru.clevertec.controller;

import com.google.gson.Gson;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import ru.clevertec.data.bank.request.RequestBankDto;
import ru.clevertec.data.bank.response.ResponseBankDto;
import ru.clevertec.service.api.BankService;
import ru.clevertec.util.ControllerUtil;

import java.io.IOException;
import java.util.List;
import java.util.Objects;

@WebServlet("/banks/*")
@RequiredArgsConstructor
public class BankController extends HttpServlet {

    private final BankService bankService;
    private final Gson gson = new Gson();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        String pathInfo = req.getPathInfo();
        if (Objects.isNull(pathInfo)) {
            List<ResponseBankDto> banks;
            banks = bankService.getAllBanks();
            String json = gson.toJson(banks);
            sendJsonResponse(json, resp);
        } else if (ControllerUtil.isId(pathInfo)) {
            String id = pathInfo.substring(1);
            ResponseBankDto bank;
            bank = bankService.getBankById(Long.parseLong(id));
            String json = gson.toJson(bank);
            sendJsonResponse(json, resp);
        } else {
            resp.sendError(404, String.format("The requested resource [%s] is not available", req.getRequestURI()));
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        RequestBankDto bankDto = gson.fromJson(req.getReader(), RequestBankDto.class);
        bankService.addBank(bankDto);
        String json = gson.toJson(bankDto);
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
        RequestBankDto bank = gson.fromJson(req.getReader(), RequestBankDto.class);
        bankService.updateBank(Long.parseLong(id), bank);
        String json = gson.toJson(bank);
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
        bankService.deleteBank(Long.parseLong(id));
        resp.setStatus(204);
    }

    private void sendJsonResponse(String json, HttpServletResponse response) throws IOException {
        response.setContentType("application/json");
        response.setStatus(200);
        response.getWriter().println(json);
    }
}
