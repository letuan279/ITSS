package org.example.views.screen.admin;

import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.util.Callback;
import org.example.controllers.AdminController;
import org.example.entity.Category;
import org.example.entity.Cook;
import org.example.entity.User;
import org.example.views.screen.BaseView;

import javax.swing.*;
import java.io.IOException;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.sql.Date;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;

public class AdminViewHandler extends BaseView implements Initializable {

    @FXML
    private TableView<User> tableUser;

    @FXML
    private TableColumn<User, String> sttUser;

    @FXML
    private TableColumn<User, String> username;
    @FXML
    private TableColumn<User, String> password;

    @FXML
    private TableColumn<User, String> role;

    @FXML
    private TableColumn<User, String> actUser;

    @FXML
    private Button add_btnUser;

    // ----------------------------------------Category--------------------------------------
    @FXML
    private TableView<Category> tableCategory;

    @FXML
    private TableColumn<Category, String> sttCategory;

    @FXML
    private TableColumn<Category, String> name;

    @FXML
    private TableColumn<Category, String> actCategory;

    @FXML
    private Button add_btnCategory;

    int myIndex;
    int id;

    @FXML

    ObservableList<User> UserList = FXCollections.observableArrayList();
    List<User> userList;

    ObservableList<Category> CategoryList = FXCollections.observableArrayList();
    List<Category> categoryList;

    public void initialize(URL arg0, ResourceBundle arg1) {
        setBController(new AdminController());
        userList = ((AdminController) getBController()).getAllUser();
        UserList = FXCollections.observableArrayList(userList);

        username.setCellValueFactory(new PropertyValueFactory<>("username"));
        password.setCellValueFactory(new PropertyValueFactory<>("password"));
        role.setCellValueFactory(new PropertyValueFactory<>("role"));

        sttUser.setCellValueFactory(
                new Callback<TableColumn.CellDataFeatures<User, String>, ObservableValue<String>>() {
                    @Override
                    public ObservableValue<String> call(TableColumn.CellDataFeatures<User, String> param) {
                        return new ReadOnlyObjectWrapper<>(tableUser.getItems().indexOf(param.getValue()) + 1 + "");
                    }
                });
        Callback<TableColumn<User, String>, TableCell<User, String>> cellFactory = (
                TableColumn<User, String> param) -> {
            final TableCell<User, String> cell = new TableCell<User, String>() {
                @Override
                public void updateItem(String item, boolean empty) {
                    super.updateItem(item, empty);
                    if (empty) {
                        setGraphic(null);
                        setText(null);
                    } else {
                        Button deleteIcon = new Button("Xóa");
                        Button editIcon = new Button("Sửa");
                        deleteIcon.setOnMouseClicked((MouseEvent event) -> {
                            handleDeleteUser(getTableView().getItems().get(getIndex()).getId());
                        });
                        editIcon.setOnMouseClicked((MouseEvent event) -> {
                            handleEditUser(getTableView().getItems().get(getIndex()));
                        });

                        HBox managebtn = new HBox(editIcon, deleteIcon);
                        managebtn.setStyle("-fx-alignment:center");
                        HBox.setMargin(deleteIcon, new Insets(2, 2, 0, 3));
                        HBox.setMargin(editIcon, new Insets(2, 3, 0, 2));
                        setGraphic(managebtn);
                        setText(null);
                    }
                }

            };
            return cell;
        };
        actUser.setCellFactory(cellFactory);
        tableUser.setItems(UserList);

        add_btnUser.setOnMouseClicked(e -> {
            System.out.println("ngoc");
            handleAddUser();
        });

        // ------------------------------------------Category-----------------------------------------------

        categoryList = ((AdminController) getBController()).getAllCategory();
        CategoryList = FXCollections.observableArrayList(categoryList);

        name.setCellValueFactory(new PropertyValueFactory<>("name"));

        sttCategory.setCellValueFactory(
                new Callback<TableColumn.CellDataFeatures<Category, String>, ObservableValue<String>>() {
                    @Override
                    public ObservableValue<String> call(TableColumn.CellDataFeatures<Category, String> param) {
                        return new ReadOnlyObjectWrapper<>(tableCategory.getItems().indexOf(param.getValue()) + 1 + "");
                    }
                });
        Callback<TableColumn<Category, String>, TableCell<Category, String>> cellFactoryCategory = (
                TableColumn<Category, String> param) -> {
            final TableCell<Category, String> cell = new TableCell<Category, String>() {
                @Override
                public void updateItem(String item, boolean empty) {
                    super.updateItem(item, empty);
                    if (empty) {
                        setGraphic(null);
                        setText(null);
                    } else {
                        Button deleteIcon = new Button("Xóa");
                        Button editIcon = new Button("Sửa");
                        deleteIcon.setOnMouseClicked((MouseEvent event) -> {
                            handleDeleteCategory(getTableView().getItems().get(getIndex()).getId());
                        });
                        editIcon.setOnMouseClicked((MouseEvent event) -> {
                            handleEditCategory(getTableView().getItems().get(getIndex()));
                        });

                        HBox managebtn = new HBox(editIcon, deleteIcon);
                        managebtn.setStyle("-fx-alignment:center");
                        HBox.setMargin(deleteIcon, new Insets(2, 2, 0, 3));
                        HBox.setMargin(editIcon, new Insets(2, 3, 0, 2));
                        setGraphic(managebtn);
                        setText(null);
                    }
                }

            };
            return cell;
        };
        actCategory.setCellFactory(cellFactoryCategory);
        tableCategory.setItems(CategoryList);

        add_btnCategory.setOnMouseClicked(e -> {
            System.out.println("ngoc");
            handleAddCategory();
        });

    }

    public void handleDeleteUser(int id) {
        System.out.println("ngoc");
        Dialog<User> dialog = new Dialog<>();
        dialog.setTitle("Bạn có chắc chắn muốn xóa");
        ButtonType deleteButton = new ButtonType("Xóa", ButtonBar.ButtonData.OK_DONE);
        dialog.getDialogPane().getButtonTypes().addAll(deleteButton, ButtonType.CANCEL);

        dialog.setResultConverter(dialogButton -> {
            if (dialogButton == deleteButton) {
                ((AdminController) getBController()).deleteUser(id);
                JOptionPane.showMessageDialog(null, "Xóa thành công");
                for (int i = 0; i < userList.size(); i++) {
                    if (userList.get(i).getId() == id) {
                        userList.remove(i);
                        break;
                    }
                }
                tableUser.setItems(FXCollections.observableArrayList(userList));
            }
            return null;
        });

        // Hiển thị hộp thoại
        Optional<User> result = dialog.showAndWait();

    }

    public void handleEditUser(User user) {
        Dialog<User> dialog = new Dialog<>();
        dialog.setTitle("Sửa thông tin user");

        // Tạo các trường nhập liệu
        TextField usernameField = new TextField(user.getUsername());
        usernameField.setPromptText("username");
        TextField passwordField = new TextField(String.valueOf(user.getPassword()));
        passwordField.setPromptText("password");
        ComboBox<Integer> roleField = new ComboBox<>();
        roleField.setPromptText("Thời gian nấu");
        roleField.setItems((FXCollections.observableArrayList(1, 0)));
        roleField.setValue(user.getRole());
        // Tạo khung modal
        GridPane grid = new GridPane();
        grid.add(new Label("Username:"), 0, 0);
        grid.add(usernameField, 1, 0);
        grid.add(new Label("Password:"), 0, 1);
        grid.add(passwordField, 1, 1);
        grid.add(new Label("Role"), 0, 2);
        grid.add(roleField, 1, 2);
        dialog.getDialogPane().setContent(grid);

        // Nút thao tác
        ButtonType saveButtonType = new ButtonType("Lưu", ButtonBar.ButtonData.OK_DONE);
        dialog.getDialogPane().getButtonTypes().addAll(saveButtonType, ButtonType.CANCEL);

        // Xử lý sự kiện khi người dùng bấm nút Lưu
        dialog.setResultConverter(dialogButton -> {
            if (dialogButton == saveButtonType) {
                String username = usernameField.getText();
                String password = passwordField.getText();
                int role = roleField.getValue();
                return new User(user.getId(), username, password, role);
            }
            return null;
        });

        // Hiển thị hộp thoại và chờ đợi phản hồi từ người dùng
        Optional<User> result = dialog.showAndWait();

        // Nếu người dùng đã bấm nút Lưu, thì cập nhật thông tin của User
        result.ifPresent(newUser -> {
            ((AdminController) getBController()).updateUser(newUser);
            // Cập nhật thông tin của User trong danh sách hiển thị
            for (int i = 0; i < userList.size(); i++) {
                if (userList.get(i).getId() == newUser.getId()) {
                    userList.set(i, newUser);
                    break;
                }
            }
            // Cập nhật giao diện của TableView
            tableUser.setItems(FXCollections.observableArrayList(userList));
        });
    }

    public void handleAddUser() {
        Dialog<User> dialog = new Dialog<>();
        dialog.setTitle("Thêm Nguoi dung moi");

        // Thiết lập nút "Thêm" và "Hủy"
        ButtonType addButton = new ButtonType("Thêm", ButtonBar.ButtonData.OK_DONE);
        dialog.getDialogPane().getButtonTypes().addAll(addButton, ButtonType.CANCEL);

        // Tạo các trường nhập liệu
        TextField usernameField = new TextField();
        usernameField.setPromptText("Username");
        TextField passwordField = new TextField();
        passwordField.setPromptText("Password");

        ComboBox<Integer> roleField = new ComboBox<>();
        roleField.setPromptText("Thời gian nấu");
        roleField.setItems((FXCollections.observableArrayList(1, 0)));

        // Tạo layout và thêm các trường nhập liệu
        GridPane grid = new GridPane();
        grid.add(new Label("Username:"), 0, 0);
        grid.add(usernameField, 1, 0);
        grid.add(new Label("Password:"), 0, 1);
        grid.add(passwordField, 1, 1);
        grid.add(new Label("Role"), 0, 3);
        // grid.add(roleField, 1, 3);
        grid.add(roleField, 1, 3);
        dialog.getDialogPane().setContent(grid);

        // Xử lý sự kiện khi người dùng bấm nút Lưu
        dialog.setResultConverter(dialogButton -> {
            if (dialogButton == addButton) {
                String username = usernameField.getText();
                String password = passwordField.getText();
                // String timeToUser = roleField.getText();
                int role = roleField.getValue();
                User newUser = ((AdminController) getBController()).addUser(username, password, role);
                System.out.println(newUser.getUsername());
                // Hiện thông báo tạo thành công và cập nhật lại bảng
                JOptionPane.showMessageDialog(null, "Thanh cong");
                userList.add(newUser);
                tableUser.setItems(FXCollections.observableArrayList(userList));
            }
            return null;
        });

        // Hiển thị hộp thoại
        Optional<User> result = dialog.showAndWait();
        if (result.isPresent()) {

        }
    }

    // ---------------------------------------Category-------------------------------------
    // ----------------------------------------------------------------------------------------------------------------
    // ----------------------------------------------------------------------------------------------------------------
    // ----------------------------------------------------------------------------------------------------------------

    public void handleDeleteCategory(int id) {
        System.out.println("Ngoc");
        // Thiết lập nút "Xóa" và "Hủy"
        Dialog<Category> dialog = new Dialog<>();
        dialog.setTitle("Bạn có chắc chắn muốn xóa");
        ButtonType deleteButton = new ButtonType("Xóa", ButtonBar.ButtonData.OK_DONE);
        dialog.getDialogPane().getButtonTypes().addAll(deleteButton, ButtonType.CANCEL);

        dialog.setResultConverter(dialogButton -> {
            if (dialogButton == deleteButton) {
                ((AdminController) getBController()).deleteCategory(id);
                JOptionPane.showMessageDialog(null, "Xóa thành công");
                for (int i = 0; i < categoryList.size(); i++) {
                    if (categoryList.get(i).getId() == id) {
                        categoryList.remove(i);
                        break;
                    }
                }
                tableCategory.setItems(FXCollections.observableArrayList(categoryList));
            }
            return null;
        });

        // Hiển thị hộp thoại
        Optional<Category> result = dialog.showAndWait();
    }

    public void handleEditCategory(Category category) {
        Dialog<Category> dialog = new Dialog<>();
        dialog.setTitle("Sửa thông tin nhóm");

        // Tạo các trường nhập liệu
        TextField nameField = new TextField(category.getName());
        nameField.setPromptText("Tên loai moi");

        GridPane grid = new GridPane();
        grid.add(new Label("Ten loai moi:"), 0, 0);
        grid.add(nameField, 1, 0);
        dialog.getDialogPane().setContent(grid);

        // Nút thao tác
        ButtonType saveButtonType = new ButtonType("Lưu", ButtonBar.ButtonData.OK_DONE);
        dialog.getDialogPane().getButtonTypes().addAll(saveButtonType, ButtonType.CANCEL);

        // Xử lý sự kiện khi người dùng bấm nút Lưu
        dialog.setResultConverter(dialogButton -> {
            if (dialogButton == saveButtonType) {
                String name = nameField.getText();
                return new Category(category.getId(), name);
            }
            return null;
        });

        // Hiển thị hộp thoại và chờ đợi phản hồi từ người dùng
        Optional<Category> result = dialog.showAndWait();

        // Nếu người dùng đã bấm nút Lưu, thì cập nhật thông tin của Category
        result.ifPresent(newCategory -> {
            ((AdminController) getBController()).updateCategory(newCategory);
            // Cập nhật thông tin của Category trong danh sách hiển thị
            for (int i = 0; i < categoryList.size(); i++) {
                if (categoryList.get(i).getId() == newCategory.getId()) {
                    categoryList.set(i, newCategory);
                    break;
                }
            }
            // Cập nhật giao diện của TableView
            tableCategory.setItems(FXCollections.observableArrayList(categoryList));
        });

    }

    public void handleAddCategory() {
        Dialog<Category> dialog = new Dialog<>();
        dialog.setTitle("Thêm Phan loai moi");

        // Thiết lập nút "Thêm" và "Hủy"
        ButtonType addButton = new ButtonType("Thêm", ButtonBar.ButtonData.OK_DONE);
        dialog.getDialogPane().getButtonTypes().addAll(addButton, ButtonType.CANCEL);

        // Tạo các trường nhập liệu
        TextField nameField = new TextField();
        nameField.setPromptText("name");

        // Tạo layout và thêm các trường nhập liệu
        GridPane grid = new GridPane();
        grid.add(new Label("Categoryname:"), 0, 0);
        grid.add(nameField, 1, 0);
        dialog.getDialogPane().setContent(grid);

        // Xử lý sự kiện khi người dùng bấm nút Lưu
        dialog.setResultConverter(dialogButton -> {
            if (dialogButton == addButton) {
                String name = nameField.getText();
                Category newCategory = ((AdminController) getBController()).createCategory(name);
                System.out.println(newCategory.getName());
                // Hiện thông báo tạo thành công và cập nhật lại bảng
                JOptionPane.showMessageDialog(null, "Thanh cong");
                categoryList.add(newCategory);
                tableCategory.setItems(FXCollections.observableArrayList(categoryList));
            }
            return null;
        });

        // Hiển thị hộp thoại
        Optional<Category> result = dialog.showAndWait();
        if (result.isPresent()) {

        }
    }

    public AdminViewHandler(Stage stage, String screenPath) throws IOException {
        super(stage, screenPath);
    }

}