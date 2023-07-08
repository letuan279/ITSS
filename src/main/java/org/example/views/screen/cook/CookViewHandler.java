package org.example.views.screen.cook;

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
import org.example.controllers.CookController;
import org.example.entity.Cook;
import org.example.entity.User;
import org.example.views.screen.BaseView;

import javax.swing.*;
import java.io.IOException;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.time.LocalDate;
import javafx.scene.control.ComboBox;
import java.time.ZoneId;
import java.sql.Date;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;

public class CookViewHandler extends BaseView implements Initializable {

    @FXML
    private TableView<Cook> tableCook;
    @FXML
    private TableColumn<Cook, String> stt;
    @FXML
    private TableColumn<Cook, String> qtyCol;
    @FXML
    private TableColumn<Cook, String> foodCol;
    @FXML
    private TableColumn<Cook, String> dateCol;
    @FXML
    private TableColumn<Cook, String> timeCol;
    @FXML
    private TableColumn<Cook, String> act;

    @FXML
    private Button add_btn;
    int myIndex;
    int id;

    ObservableList<Cook> CookList = FXCollections.observableArrayList();
    List<Cook> cookList;

    public void initialize(URL arg0, ResourceBundle arg1) {
        setBController(new CookController());
        cookList = ((CookController) getBController()).getCooksOfMember(User.getCurrentUser().getId());
        CookList = FXCollections.observableArrayList(cookList);

        qtyCol.setCellValueFactory(new PropertyValueFactory<>("quantity"));
        foodCol.setCellValueFactory(new PropertyValueFactory<>("foodName"));
        dateCol.setCellValueFactory(new PropertyValueFactory<>("date"));
        timeCol.setCellValueFactory(new PropertyValueFactory<>("timeToCook"));
        stt.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<Cook, String>, ObservableValue<String>>() {
            @Override
            public ObservableValue<String> call(TableColumn.CellDataFeatures<Cook, String> param) {
                return new ReadOnlyObjectWrapper<>(tableCook.getItems().indexOf(param.getValue()) + 1 + "");
            }
        });
        Callback<TableColumn<Cook, String>, TableCell<Cook, String>> cellFactory = (
                TableColumn<Cook, String> param) -> {
            final TableCell<Cook, String> cell = new TableCell<Cook, String>() {
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
                            handleDelete(getTableView().getItems().get(getIndex()).getId());
                        });
                        editIcon.setOnMouseClicked((MouseEvent event) -> {
                            handleEdit(getTableView().getItems().get(getIndex()));
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
        act.setCellFactory(cellFactory);
        tableCook.setItems(CookList);

        add_btn.setOnMouseClicked(e -> {
            handleAdd();
        });
    }

    public void handleEdit(Cook cook) {
        Dialog<Cook> dialog = new Dialog<>();
        dialog.setTitle("Sửa thông tin nhóm");

        // Tạo các trường nhập liệu
        TextField foodNameField = new TextField(cook.getFoodName());
        foodNameField.setPromptText("Tên món ăn");
        TextField quantityField = new TextField(String.valueOf(cook.getQuantity()));
        quantityField.setPromptText("Số lượng");
        DatePicker dateField = new DatePicker(cook.getDate().toLocalDate());
        dateField.setPromptText("Ngày nấu");
        ComboBox<String> timeToCookField = new ComboBox<>();
        timeToCookField.setPromptText("Thời gian nấu");
        timeToCookField.setItems((FXCollections.observableArrayList("Sáng", "Trưa", "Tối")));
        timeToCookField.setValue(cook.getTimeToCook());
        // Tạo khung modal
        GridPane grid = new GridPane();
        grid.add(new Label("Tên món ăn:"), 0, 0);
        grid.add(foodNameField, 1, 0);
        grid.add(new Label("Số lượng:"), 0, 1);
        grid.add(quantityField, 1, 1);
        grid.add(new Label("Ngày nấu"), 0, 2);
        grid.add(dateField, 1, 2);
        grid.add(new Label("Thời gian nấu"), 0, 3);
        grid.add(timeToCookField, 1, 3);
        dialog.getDialogPane().setContent(grid);

        // Nút thao tác
        ButtonType saveButtonType = new ButtonType("Lưu", ButtonBar.ButtonData.OK_DONE);
        dialog.getDialogPane().getButtonTypes().addAll(saveButtonType, ButtonType.CANCEL);

        // Xử lý sự kiện khi người dùng bấm nút Lưu
        dialog.setResultConverter(dialogButton -> {
            if (dialogButton == saveButtonType) {
                String foodName = foodNameField.getText();
                int quantity = Integer.parseInt(quantityField.getText());
                Date date = Date.valueOf(dateField.getValue());
                String timeToCook = timeToCookField.getValue();
                return new Cook(cook.getId(), quantity, foodName, date, timeToCook, User.getCurrentUser().getId());
            }
            return null;
        });

        // Hiển thị hộp thoại và chờ đợi phản hồi từ người dùng
        Optional<Cook> result = dialog.showAndWait();

        // Nếu người dùng đã bấm nút Lưu, thì cập nhật thông tin của Cook
        result.ifPresent(newCook -> {
            ((CookController) getBController()).updateCook(newCook);
            // Cập nhật thông tin của Cook trong danh sách hiển thị
            for (int i = 0; i < cookList.size(); i++) {
                if (cookList.get(i).getId() == newCook.getId()) {
                    cookList.set(i, newCook);
                    break;
                }
            }
            // Cập nhật giao diện của TableView
            tableCook.setItems(FXCollections.observableArrayList(cookList));
        });
    }

    public void handleDelete(int id) {
        System.out.println("Ngoc");
        // Thiết lập nút "Xóa" và "Hủy"
        Dialog<Cook> dialog = new Dialog<>();
        dialog.setTitle("Bạn có chắc chắn muốn xóa");
        ButtonType deleteButton = new ButtonType("Xóa", ButtonBar.ButtonData.OK_DONE);
        dialog.getDialogPane().getButtonTypes().addAll(deleteButton, ButtonType.CANCEL);

        dialog.setResultConverter(dialogButton -> {
            if (dialogButton == deleteButton) {
                ((CookController) getBController()).deleteCook(id);
                JOptionPane.showMessageDialog(null, "Xóa thành công");
                for (int i = 0; i < cookList.size(); i++) {
                    if (cookList.get(i).getId() == id) {
                        cookList.remove(i);
                        break;
                    }
                }
                tableCook.setItems(FXCollections.observableArrayList(cookList));
            }
            return null;
        });

        // Hiển thị hộp thoại
        Optional<Cook> result = dialog.showAndWait();

    }

    public void handleAdd() {
        // Tạo hộp thoại
        Dialog<Cook> dialog = new Dialog<>();
        dialog.setTitle("Thêm món ăn");

        // Thiết lập nút "Thêm" và "Hủy"
        ButtonType addButton = new ButtonType("Thêm", ButtonBar.ButtonData.OK_DONE);
        dialog.getDialogPane().getButtonTypes().addAll(addButton, ButtonType.CANCEL);

        // Tạo các trường nhập liệu
        TextField foodNameField = new TextField();
        foodNameField.setPromptText("Tên món ăn");
        TextField quantityField = new TextField();
        quantityField.setPromptText("Số lượng");
        DatePicker dateField = new DatePicker();
        dateField.setPromptText("Ngày nấu");
//        TextField timeToCookField = new TextField();
        ComboBox<String> timeToCookField = new ComboBox<>();
        timeToCookField.setPromptText("Thời gian nấu");
        timeToCookField.setItems((FXCollections.observableArrayList("Sáng", "Trưa", "Tối")));

        // Tạo layout và thêm các trường nhập liệu
        GridPane grid = new GridPane();
        grid.add(new Label("Tên món ăn:"), 0, 0);
        grid.add(foodNameField, 1, 0);
        grid.add(new Label("Số lượng:"), 0, 1);
        grid.add(quantityField, 1, 1);
        grid.add(new Label("Ngày nấu"), 0, 2);
        grid.add(dateField, 1, 2);
        grid.add(new Label("Thời gian nấu"), 0, 3);
        // grid.add(timeToCookField, 1, 3);
        grid.add(timeToCookField, 1, 3);
        dialog.getDialogPane().setContent(grid);

        // Xử lý sự kiện khi người dùng bấm nút Lưu
        dialog.setResultConverter(dialogButton -> {
            if (dialogButton == addButton) {
                String foodName = foodNameField.getText();
                int quantity = Integer.parseInt(quantityField.getText());
                Date date = Date.valueOf(dateField.getValue());
                // String timeToCook = timeToCookField.getText();
                String timeToCook = timeToCookField.getValue();
                Cook newCook = ((CookController) getBController()).addCook(foodName, quantity, date, timeToCook,
                        User.getCurrentUser().getId());
                System.out.println(newCook.getFoodName());
                // Hiện thông báo tạo thành công và cập nhật lại bảng
                JOptionPane.showMessageDialog(null, "Thanh cong");
                cookList.add(newCook);
                tableCook.setItems(FXCollections.observableArrayList(cookList));
            }
            return null;
        });

        // Hiển thị hộp thoại
        Optional<Cook> result = dialog.showAndWait();

        if (result.isPresent()) {

        }
    }

    public CookViewHandler(Stage stage, String screenPath) throws IOException {
        super(stage, screenPath);
    }

}