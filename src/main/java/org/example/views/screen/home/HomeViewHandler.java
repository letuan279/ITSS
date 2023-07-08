package org.example.views.screen.home;

import javafx.beans.Observable;
import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.util.Callback;
import org.example.entity.Group;
import org.example.entity.MarketItem;
import org.example.views.screen.BaseView;

import javax.sound.midi.ShortMessage;
import javax.swing.*;
import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.util.List;
import java.util.ResourceBundle;

public class HomeViewHandler extends BaseView implements Initializable {

    @FXML
    Text homeTitle;
    @FXML
    private TableView<MarketItem> boughtfoodTable;
    @FXML
    private TableColumn<MarketItem, String> sttCol1;
    @FXML
    private TableColumn<MarketItem, String> foodnameCol1;
    @FXML
    private TableColumn<MarketItem, String> quantityCol1;
    @FXML
    private TableColumn<MarketItem, String> timebuyCol1;
    @FXML
    private TableView<MarketItem> expirationSoonTable;
    @FXML
    private TableColumn<MarketItem, String> sttCol2;
    @FXML
    private TableColumn<MarketItem, String> foodnameCol2;
    @FXML
    private TableColumn<MarketItem, String> quantityCol2;
    @FXML
    private TableColumn<MarketItem, String> timebuyCol2;

    // For the table express whether the food had been bought or not
    ObservableList<MarketItem> BoughtFoodList = FXCollections.observableArrayList();
    List<MarketItem> boughtFoodList;

    // for the table express whether the food would get it expiration soon
    ObservableList<MarketItem> ExpirationSoonList = FXCollections.observableArrayList();
    List<MarketItem> expirationSoonList;

    String query = null;
    Connection connection = null;


    public HomeViewHandler(Stage stage, String screenPath) throws IOException {
        super(stage, screenPath);
    }

    public void initialize(URL arg0, ResourceBundle arg1) {
        homeTitle.setText("Trang chủ");


//        sttCol1.setCellValueFactory(new PropertyValueFactory<MarketItem, String>("id"));
        foodnameCol1.setCellValueFactory(new PropertyValueFactory<MarketItem, String>("name"));
        quantityCol1.setCellValueFactory(new PropertyValueFactory<MarketItem, String>("quantity"));
        timebuyCol1.setCellValueFactory(new PropertyValueFactory<MarketItem, String>("dayToBuy"));
        boughtfoodTable.setItems(BoughtFoodList);
    }
}
