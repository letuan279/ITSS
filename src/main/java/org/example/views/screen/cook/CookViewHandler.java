package org.example.views.screen.cook;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import org.example.entity.Database;
import org.example.entity.Cook;
import org.example.views.screen.BaseView;

import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;


import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;


public class CookViewHandler extends BaseView implements Initializable {

    @FXML
    private TableView<Cook> tableCook;
    @FXML
    private TableColumn<Cook, String> idCol;
    @FXML
    private TableColumn<Cook, String> qtyCol;
    @FXML
    private TableColumn<Cook, String> foodCol;
    @FXML
    private TableColumn<Cook, String> dateCol;
    @FXML
    private TableColumn<Cook, String> timeCol;
    @FXML
    private TableColumn<Cook, String> userCol;

    @FXML
    private Button btnInsert;
    @FXML
    private Button btnUpdate;
    @FXML
    private Button btnDelete;

    @FXML
    private TextField txtId1;
    @FXML
    private TextField txtId;

    @FXML
    private TextField txtQty;

    @FXML
    private TextField txtFood;

    @FXML
    private TextField txtDate;

    @FXML
    private TextField txtTime;

    @FXML
    private TextField txtUser;
    PreparedStatement pst;
    int myIndex;
    int id;


    public void tableCook()
    {
        Connection connect = Database.getConnection();
        ObservableList<Cook> cooks = FXCollections.observableArrayList();
        try
        {
            pst = Database.getConnection().prepareStatement("select * from cook");
            ResultSet rs = pst.executeQuery();

                while (rs.next())
                {
                    cooks.add(new Cook(
                            rs.getInt("id"),
                            rs.getInt("quantity"),
                            rs.getString("foodName"),
                            rs.getDate("date"),
                            rs.getInt("timeToCook"),
                            rs.getInt("idUser")));

                }

            tableCook.setItems(cooks);
            idCol.setCellValueFactory(new PropertyValueFactory<>("id"));
            qtyCol.setCellValueFactory(new PropertyValueFactory<>("quantity"));
            foodCol.setCellValueFactory(new PropertyValueFactory<>("foodName"));
            dateCol.setCellValueFactory(new PropertyValueFactory<>("date"));
            timeCol.setCellValueFactory(new PropertyValueFactory<>("timeToCook"));
            userCol.setCellValueFactory(new PropertyValueFactory<>("idUser"));

        }

        catch (SQLException ex)
        {
            Logger.getLogger(CookViewHandler.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    Connection connect = Database.getConnection();
    ObservableList<Cook> cooks = FXCollections.observableArrayList();
    private void insertRecord() {
        String query = "INSERT INTO cook (quantity, foodName, date, timeToCook, idUser) VALUES (?, ?, ?, ?, ?)";
        try {
            PreparedStatement pst = Database.getConnection().prepareStatement(query);
            pst.setString(1, txtQty.getText());
            pst.setString(2, txtFood.getText());
            pst.setString(3, txtDate.getText());
            pst.setString(4, txtTime.getText());
            pst.setString(5, txtUser.getText());
            pst.executeUpdate();
            pst.close();
            tableCook();
        } catch (SQLException e) {
            System.err.println(e);
        }

    }
    private void updateRecord() {
        String query = "UPDATE cook SET quantity = ?, foodName = ?, date = ?, timeToCook = ?, idUser = ? WHERE id = ?";
        try {
            PreparedStatement pst = Database.getConnection().prepareStatement(query);

            pst.setString(1, txtQty.getText());
            pst.setString(2, txtFood.getText());
            pst.setString(3, txtDate.getText());
            pst.setString(4, txtTime.getText());
            pst.setString(5, txtUser.getText());
            pst.setString(6, txtId1.getText());

            pst.executeUpdate();
            pst.close();
            tableCook();
        } catch (SQLException e) {
            System.err.println(e);
        }

    }
    private void deleteButton() {
        String query = "DELETE FROM cook WHERE id = ?";
        try {
            PreparedStatement pst = Database.getConnection().prepareStatement(query);
            pst.setString(1, txtId.getText());
            pst.executeUpdate();
            pst.close();
            tableCook();
        } catch (SQLException e) {
            System.err.println(e);
        }
    }

    @FXML
    private void handleButtonAction(ActionEvent event) {

        if(event.getSource() == btnInsert){
            insertRecord();
        }else if (event.getSource() == btnUpdate){
            updateRecord();
        }else if(event.getSource() == btnDelete){
            deleteButton();
        }

    }


    public CookViewHandler(Stage stage, String screenPath) throws IOException {
        super(stage, screenPath);
    }

    public void initialize(URL arg0, ResourceBundle arg1) {
        Database.getConnection();
        tableCook();
    }
}
