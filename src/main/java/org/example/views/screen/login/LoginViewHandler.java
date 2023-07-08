package org.example.views.screen.login;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import org.example.controllers.LoginController;
import org.example.utils.Config;
import org.example.views.screen.BaseView;
import org.example.views.screen.layout.LayoutViewHandler;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.ResourceBundle;

public class LoginViewHandler extends BaseView implements Initializable {
    @FXML
    private TextField username;

    @FXML
    private PasswordField password;

    @FXML
    private Button btn_login;
    public LoginViewHandler(Stage stage, String screenPath) throws IOException {
        super(stage, screenPath);
    }

    @Override
    public void initialize(URL arg0, ResourceBundle arg1) {
        setBController(new LoginController());

        // set login action
        btn_login.setOnMouseClicked((e) -> {
            String un = username.getText();
            String pw = password.getText();

            if(un.isEmpty() || pw.isEmpty()){

            }else {
                LoginController loginCon = (LoginController) getBController();
                Boolean success = false;
                try {
                     success = loginCon.login(un, pw);
                } catch (SQLException ex) {
                    throw new RuntimeException(ex);
                }
                if(success == true) {
                    // Alert success
                    Alert alert = new Alert(AlertType.INFORMATION);
                    alert.setTitle("Đăng nhập thành công");
                    alert.setHeaderText(null);
                    alert.setContentText("Bạn đã đăng nhập thành công");
                    alert.showAndWait();

                    // Go to page
                    try {
                        LayoutViewHandler layoutHandler = new LayoutViewHandler(stage, Config.LAYOUT_PATH);
                        layoutHandler.show();
                    } catch (IOException ex) {
                        throw new RuntimeException(ex);
                    }

                }

                if(success == false) {
                    // Load home page
                    Alert alert = new Alert(AlertType.INFORMATION);
                    alert.setTitle("Đăng nhập thất bại");
                    alert.setHeaderText(null);
                    alert.setContentText("Tải khoản hoặc mật khẩu không chính xác");
                    alert.showAndWait();
                }
            }
        });
    }
}
