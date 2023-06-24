package org.example.controllers;

import org.example.entity.User;

import java.sql.SQLException;

public class LoginController extends BaseController {
    public Boolean login(String username, String password) throws SQLException {
        Boolean result = User.login(username, password);
//        System.out.println(result);
        return result;
    }

    public Boolean logout() {
        User.setCurrentUser(null);
        return true;
    }

//    public static void main(String[] args) throws SQLException {
//        login("Quynh", "12345");
//    }
}
