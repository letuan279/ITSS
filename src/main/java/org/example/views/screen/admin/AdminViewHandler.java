package org.example.views.screen.admin;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import org.example.entity.Database;
import org.example.entity.User;
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

public class AdminViewHandler extends BaseView implements Initializable {

    @FXML
    private Button btnAdd;

    @FXML
    private Button btnDelete;

    @FXML
    private Button btnUpdate;

    @FXML
    private TableColumn<User, String> idCol;

    @FXML
    private TableColumn<User, String> nameCol;

    @FXML
    private TableView<User> tableUser;

    @FXML
    private TextField txtId;

    @FXML
    private TextField txtName;

    @FXML
    private TextField txtPassword;

    @FXML
    private TextField txtRole;


    PreparedStatement pst;
    int myIndex;
    int id;

    @FXML
    void Add(ActionEvent event) {
        String id,name,password, role;
        name = txtName.getText();
        password = txtPassword.getText();
        role  = txtId.getText();
        try
        {
            pst = Database.getConnection().prepareStatement("insert into user(username,password, role)values(?,?,?)");
            pst.setString(1, name);
            pst.setString(2, password);
            pst.setString(3, password);
            pst.executeUpdate();

            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("User Registation");
            alert.setHeaderText("User Registation");
            alert.setContentText("User Addedddd!");
            alert.showAndWait();
            tableUser();

            txtName.setText("");
            txtPassword.setText("");
            txtName.requestFocus();
        }
        catch (SQLException ex)
        {
            Logger.getLogger(AdminViewHandler.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    public void tableUser()
    {
        Connection connect = Database.getConnection();
        ObservableList<User> users = FXCollections.observableArrayList();
        try
        {
            pst = Database.getConnection().prepareStatement("select id,username,password from user");
            ResultSet rs = pst.executeQuery();
            {
                while (rs.next())
                {
                    User st = new User();
//                    st.setId(rs.getString("id"));
                    st.setUsername(rs.getString("username"));
                    st.setPassword(rs.getString("password"));
                    users.add(st);
                }
            }
            tableUser.setItems(users);
            idCol.setCellValueFactory(new PropertyValueFactory<>("id"));
            nameCol.setCellValueFactory(new PropertyValueFactory<>("username"));
//            .setCellValueFactory(f -> f.getValue().mobileProperty());
//            CourseColmn.setCellValueFactory(f -> f.getValue().courseProperty());


        }

        catch (SQLException ex)
        {
            Logger.getLogger(AdminViewHandler.class.getName()).log(Level.SEVERE, null, ex);
        }
        tableUser.setRowFactory( tv -> {
            TableRow<User> myRow = new TableRow<>();
            myRow.setOnMouseClicked (event ->
            {
                if (event.getClickCount() == 1 && (!myRow.isEmpty()))
                {
                    myIndex =  tableUser.getSelectionModel().getSelectedIndex();

                    id = Integer.parseInt(String.valueOf(tableUser.getItems().get(myIndex).getId()));
                    txtName.setText(tableUser.getItems().get(myIndex).getUsername());
//                    txtMobile.setText(table.getItems().get(myIndex).getMobile());
//                    txtCourse.setText(table.getItems().get(myIndex).getCourse());



                }
            });
            return myRow;
        });


    }

    @FXML
    void Delete(ActionEvent event) {

    }

    @FXML
    void Update(ActionEvent event) {

    }

    public AdminViewHandler(Stage stage, String screenPath) throws IOException {
        super(stage, screenPath);
    }

    public void initialize(URL arg0, ResourceBundle arg1) {
        Database.getConnection();
        tableUser();

    }
}
