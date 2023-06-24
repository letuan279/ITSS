package org.example.views.screen.layout;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import org.example.controllers.LoginController;
import org.example.utils.Config;
import org.example.views.screen.BaseView;
import org.example.views.screen.admin.AdminViewHandler;
import org.example.views.screen.cook.CookViewHandler;
import org.example.views.screen.group.GroupViewHandler;
import org.example.views.screen.home.HomeViewHandler;
import org.example.views.screen.login.LoginViewHandler;
import org.example.views.screen.recipe.RecipeViewHandler;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class LayoutViewHandler extends BaseView implements Initializable {
    @FXML
    Button homepageBtn;
    @FXML
    Button cookBtn;
    @FXML
    Button recipeBtn;
    @FXML
    Button groupBtn;
    @FXML
    Button adminBtn;
    @FXML
    Button logoutBtn;

    @FXML
    AnchorPane contentPage;

    public LayoutViewHandler(Stage stage, String screenPath) throws IOException {
        super(stage, screenPath);
    }

    public void initialize(URL arg0, ResourceBundle arg1) {
        try {
            // Default: Homepage
            HomeViewHandler homeHandler = new HomeViewHandler(stage, Config.HOME_PATH);
            AnchorPane homepageContent = homeHandler.content;
            contentPage.getChildren().setAll(homepageContent);

            // Set action for sidebar
            homepageBtn.setOnAction(e -> {
                contentPage.getChildren().setAll(homepageContent);
            });

            cookBtn.setOnAction(e -> {
                try {
                    CookViewHandler cookHandler = new CookViewHandler(stage, Config.COOK_PATH);
                    AnchorPane cookContent = cookHandler.content;
                    contentPage.getChildren().setAll(cookContent);
                } catch (IOException ex) {
                    throw new RuntimeException(ex);
                }
            });

            groupBtn.setOnAction(e -> {
                try {
                    GroupViewHandler groupHandler = new GroupViewHandler(stage, Config.GROUP_PATH);
                    AnchorPane groupContent = groupHandler.content;
                    contentPage.getChildren().setAll(groupContent);
                } catch (IOException ex) {
                    throw new RuntimeException(ex);
                }
            });

            recipeBtn.setOnAction(e -> {
                try {
                    RecipeViewHandler recipeHandler = new RecipeViewHandler(stage, Config.RECIPE_PATH);
                    AnchorPane recipeContent = recipeHandler.content;
                    contentPage.getChildren().setAll(recipeContent);
                } catch (IOException ex) {
                    throw new RuntimeException(ex);
                }
            });

            adminBtn.setOnAction(e -> {
                try {
                    AdminViewHandler adminHandler = new AdminViewHandler(stage, Config.ADMIN_PATH);
                    AnchorPane adminContent = adminHandler.content;
                    contentPage.getChildren().setAll(adminContent);
                } catch (IOException ex) {
                    throw new RuntimeException(ex);
                }
            });
            // Set action for logout button
            logoutBtn.setOnMouseClicked(e -> {
                (new LoginController()).logout();
                LoginViewHandler LoginHandler = null;
                try {
                    LoginHandler = new LoginViewHandler(stage, Config.LOGIN_PATH);
                } catch (IOException ex) {
                    throw new RuntimeException(ex);
                }
                LoginHandler.show();
            });

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
