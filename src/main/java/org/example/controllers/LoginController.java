package org.example.controllers;

import org.example.entity.User;

public class LoginController extends BaseController {
    public Boolean login(String username, String password) {
        return User.login(username, password);
    }
}
