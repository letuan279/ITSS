package org.example.views.screen.login;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import org.example.views.screen.BaseView;

import java.io.IOException;

public class LoginViewHandler extends BaseView {
    @FXML
    private TextField username;

    @FXML
    private PasswordField password;

    @FXML
    private Button btn_login;
    public LoginViewHandler(Stage stage, String screenPath) throws IOException {
        super(stage, screenPath);
    }

    public void login() {

    }
}
