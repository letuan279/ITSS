package org.example.views.screen.home;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import org.example.views.screen.BaseView;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class HomeViewHandler extends BaseView implements Initializable {

    @FXML
    Text homeTitle;

    public HomeViewHandler(Stage stage, String screenPath) throws IOException {
        super(stage, screenPath);
    }

    public void initialize(URL arg0, ResourceBundle arg1) {
        homeTitle.setText("Trang chủ");
    }
}
