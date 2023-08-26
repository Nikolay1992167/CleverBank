package ru.clevertec.controller;

import com.google.gson.Gson;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import ru.clevertec.entity.User;
import ru.clevertec.service.api.UserService;
import ru.clevertec.util.ControllerUtil;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;
import java.util.Objects;

@WebServlet("/users/*")
@RequiredArgsConstructor
public class UserController extends HttpServlet {

    private final UserService userService;

    private final Gson gson = new Gson();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        String pathInfo = req.getPathInfo();
        if (Objects.isNull(pathInfo)) {
            List<User> users = null;
            try {
                users = userService.getAllUsers();
            } catch (SQLException | ClassNotFoundException e) {
                throw new RuntimeException(e);
            }
            String json = gson.toJson(users);
            sendJsonResponse(json, resp);
        } else if (ControllerUtil.isId(pathInfo)) {
            String id = pathInfo.substring(1);
            User user = null;
            try {
                user = userService.getUserById(Long.parseLong(id));
            } catch (SQLException | ClassNotFoundException e) {
                throw new RuntimeException(e);
            }
            String json = gson.toJson(user);
            sendJsonResponse(json, resp);
        } else {
            resp.sendError(404, String.format("The requested resource [%s] is not available", req.getRequestURI()));
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        User user = gson.fromJson(req.getReader(), User.class);
        try {
            userService.addUser(user);
        } catch (SQLException | ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
        String json = gson.toJson(user);
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
        User user = gson.fromJson(req.getReader(), User.class);
        user.setId(Long.parseLong(id));
        try {
            userService.updateUser(user);
        } catch (SQLException | ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
        String json = gson.toJson(user);
        sendJsonResponse(json, resp);
    }

    // Переопределяем метод doDelete для обработки DELETE-запросов к ресурсу /users
    @Override
    protected void doDelete(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        String pathInfo = req.getPathInfo();
        if (!ControllerUtil.isId(pathInfo)) {
            resp.sendError(400, "Id must be set");
            return;
        }
        String id = pathInfo.substring(1);
        try {
            userService.deleteUser(userService.getUserById(Long.parseLong(id)));
        } catch (SQLException | ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
        resp.setStatus(204);
    }

    private void sendJsonResponse(String json, HttpServletResponse response) throws IOException {
        response.setContentType("application/json");
        response.setStatus(200);
        response.getWriter().println(json);
    }
}
