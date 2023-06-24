package org.example;

import java.io.IOException;

import javafx.animation.FadeTransition;
import javafx.application.Application;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;
import javafx.util.Duration;
import org.example.utils.Config;
import org.example.views.screen.login.LoginViewHandler;

public class Main extends Application {

    @Override
    public void start(Stage primaryStage) throws IOException {
        LoginViewHandler LoginHandler = new LoginViewHandler(primaryStage, Config.LOGIN_PATH);
        LoginHandler.show();
    }

    public static void main(String[] args) {
        launch(args);
    }

}
