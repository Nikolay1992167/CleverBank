package ru.clevertec.controller;

import com.google.gson.Gson;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import ru.clevertec.data.account.request.RequestAccountDto;
import ru.clevertec.data.account.response.ResponseAccountDto;
import ru.clevertec.service.api.AccountService;
import ru.clevertec.util.ControllerUtil;

import java.io.IOException;
import java.util.List;
import java.util.Objects;

@WebServlet("/accounts/*")
@RequiredArgsConstructor
public class AccountController extends HttpServlet {

    private final AccountService accountService;
    private final Gson gson = new Gson();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        String pathInfo = req.getPathInfo();
        if (Objects.isNull(pathInfo)) {
            List<ResponseAccountDto> accounts;
            accounts = accountService.geAllAccounts();
            String json = gson.toJson(accounts);
            sendJsonResponse(json, resp);
        } else if (ControllerUtil.isId(pathInfo)) {
            String id = pathInfo.substring(1);
            ResponseAccountDto account;
            account = accountService.getAccountById(Long.parseLong(id));
            String json = gson.toJson(account);
            sendJsonResponse(json, resp);
        } else {
            resp.sendError(404, String.format("The requested resource [%s] is not available", req.getRequestURI()));
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        RequestAccountDto accountDto = gson.fromJson(req.getReader(), RequestAccountDto.class);
        accountService.addAccount(accountDto);
        String json = gson.toJson(accountDto);
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
        RequestAccountDto account = gson.fromJson(req.getReader(), RequestAccountDto.class);
        accountService.updateAccount(Long.parseLong(id), account);
        String json = gson.toJson(account);
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
        accountService.deleteAccount(Long.parseLong(id));
        resp.setStatus(204);
    }

    private void sendJsonResponse(String json, HttpServletResponse response) throws IOException {
        response.setContentType("application/json");
        response.setStatus(200);
        response.getWriter().println(json);
    }
}
