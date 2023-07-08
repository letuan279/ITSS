package org.example.views.screen.home;

import javafx.beans.Observable;
import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.scene.control.Button;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.util.Callback;
import org.example.controllers.CookController;
import org.example.controllers.HomeController;
import org.example.entity.*;
import org.example.views.screen.BaseView;

import javax.sound.midi.ShortMessage;
import javax.swing.*;
import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;

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

    ObservableList<MarketItem> BoughtFoodList = FXCollections.observableArrayList();
    List<MarketItem> boughtFoodList;

    // for the table express whether the food would get it expiration soon
    ObservableList<MarketItem> ExpirationSoonList = FXCollections.observableArrayList();
    List<MarketItem> expirationSoonList;

    PreparedStatement query = null;
    Connection connection = null;


    public HomeViewHandler(Stage stage, String screenPath) throws IOException {
        super(stage, screenPath);
    }

    public void initialize(URL arg0, ResourceBundle arg1) {
        homeTitle.setText("Trang chủ");

//        connection = Database.getConnection();
//
//        try {
//            query = Database.getConnection().prepareStatement("SELECT * FROM `marketitem` WHERE idUser IS NOT NULL;");
//            ResultSet rs = query.executeQuery();
//
//            while (rs.next()){
//                BoughtFoodList.add(new MarketItem(
//                        rs.getInt("id"),
//                        rs.getInt("quantity"),
//                        rs.getString("unit"),
//                        rs.getString("name"),
//                        rs.getInt("type"),
//                        rs.getInt("idGroup"),
//                        rs.getDate("dayToBuy").toLocalDate(),
//                        (User) rs.getObject("buyer"),
//                        rs.getInt("expirationDate"))
//                );
//            }
//            boughtfoodTable.setItems(BoughtFoodList);
//            sttCol1.setCellValueFactory(new PropertyValueFactory<>("id"));
//            foodnameCol1.setCellValueFactory(new PropertyValueFactory<>("name"));
//            quantityCol1.setCellValueFactory(new PropertyValueFactory<>("quantity"));
//            timebuyCol1.setCellValueFactory(new PropertyValueFactory<>("dayToBuy"));

//        }catch (SQLException ex){
//            System.err.println(ex);
//        }

        setBController(new HomeController());
        boughtFoodList = ((HomeController) getBController()).getBoughtFoods(User.getCurrentUser().getId());
        BoughtFoodList = FXCollections.observableArrayList(boughtFoodList);

//        sttCol1.setCellValueFactory(new PropertyValueFactory<>("id"));
        foodnameCol1.setCellValueFactory(new PropertyValueFactory<>("name"));
        quantityCol1.setCellValueFactory(new PropertyValueFactory<>("quantity"));
        timebuyCol1.setCellValueFactory(new PropertyValueFactory<>("dayToBuy"));

//        sttCol1.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<MarketItem, String>, ObservableValue<String>>() {
//            @Override
//            public ObservableValue<String> call(TableColumn.CellDataFeatures<Cook, String> param) {
//                return new ReadOnlyObjectWrapper<>(boughtfoodTable.getItems().indexOf(param.getValue()) + 1 + "");
//            }
//        });
        Callback<TableColumn<MarketItem, String>, TableCell<MarketItem, String>> cellFactory = (
                TableColumn<MarketItem, String> param) -> {
            final TableCell<MarketItem, String> cell = new TableCell<MarketItem, String>() {
                @Override
                public void updateItem(String item, boolean empty) {
                    super.updateItem(item, empty);
                    if (empty) {
                        setGraphic(null);
                        setText(null);

                    } else {
                        Button deleteIcon = new Button("Xóa");
                        Button editIcon = new Button("Sửa");
//                        deleteIcon.setOnMouseClicked((MouseEvent event) -> {
//                            handleDelete(getTableView().getItems().get(getIndex()).getId());
//                        });
//                        editIcon.setOnMouseClicked((MouseEvent event) -> {
//                            handleEdit(getTableView().getItems().get(getIndex()));
//                        });

                        HBox managebtn = new HBox(editIcon, deleteIcon);
                        managebtn.setStyle("-fx-alignment:center");
                        HBox.setMargin(deleteIcon, new Insets(2, 2, 0, 3));
                        HBox.setMargin(editIcon, new Insets(2, 3, 0, 2));
                        setGraphic(managebtn);
                        setText(null);
                    }
                };

            };
            return cell;
        };

        boughtfoodTable.setItems(BoughtFoodList);
    }
}
