package org.example.views.screen.recipe;

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

import org.example.controllers.RecipeController;
import org.example.entity.Recipe;
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

public class RecipeViewHandler extends BaseView implements Initializable {

    @FXML
    private TableView<Recipe> tableRecipe;
    @FXML
    private TableColumn<Recipe, String> stt;
    @FXML
    private TableColumn<Recipe, String> name;
    @FXML
    private TableColumn<Recipe, String> desc;
    @FXML
    private TableColumn<Recipe, String> act;

    @FXML
    private Button add_btn;
    int myIndex;
    int id;

    ObservableList<Recipe> RecipeList = FXCollections.observableArrayList();
    List<Recipe> recipeList;

    public void initialize(URL arg0, ResourceBundle arg1) {
        setBController(new RecipeController());
        recipeList = ((RecipeController) getBController()).getRecipesOfMember(User.getCurrentUser().getId());
        RecipeList = FXCollections.observableArrayList(recipeList);

        name.setCellValueFactory(new PropertyValueFactory<>("name"));
        desc.setCellValueFactory(new PropertyValueFactory<>("desc"));
        stt.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<Recipe, String>, ObservableValue<String>>() {
            @Override
            public ObservableValue<String> call(TableColumn.CellDataFeatures<Recipe, String> param) {
                return new ReadOnlyObjectWrapper<>(tableRecipe.getItems().indexOf(param.getValue()) + 1 + "");
            }
        });
        Callback<TableColumn<Recipe, String>, TableCell<Recipe, String>> cellFactory = (
                TableColumn<Recipe, String> param) -> {
            final TableCell<Recipe, String> cell = new TableCell<Recipe, String>() {
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
        tableRecipe.setItems(RecipeList);

        add_btn.setOnMouseClicked(e -> {
            handleAdd();
        });

        handleRowClick();
    }

    public void handleEdit(Recipe recipe) {
        Dialog<Recipe> dialog = new Dialog<>();
        dialog.setTitle("Sửa cong thuc");

        // Tạo các trường nhập liệu
        TextField nameField = new TextField(recipe.getName());
        nameField.setPromptText("Tên cong thuc");
        TextArea descField = new TextArea(String.valueOf(recipe.getDesc()));
        descField.setPromptText("Số lượng");
        // Tạo khung modal
        GridPane grid = new GridPane();
        grid.add(new Label("Tên món ăn:"), 0, 0);
        grid.add(nameField, 1, 0);
        grid.add(new Label("Số lượng:"), 0, 1);
        grid.add(descField, 1, 1);

        dialog.getDialogPane().setContent(grid);

        // Nút thao tác
        ButtonType saveButtonType = new ButtonType("Lưu", ButtonBar.ButtonData.OK_DONE);
        dialog.getDialogPane().getButtonTypes().addAll(saveButtonType, ButtonType.CANCEL);

        // Xử lý sự kiện khi người dùng bấm nút Lưu
        dialog.setResultConverter(dialogButton -> {
            if (dialogButton == saveButtonType) {
                String name = nameField.getText();
                String desc = descField.getText();
                return new Recipe(recipe.getId(), name, desc,
                        User.getCurrentUser().getId());
            }
            return null;
        });

        // Hiển thị hộp thoại và chờ đợi phản hồi từ người dùng
        Optional<Recipe> result = dialog.showAndWait();

        // Nếu người dùng đã bấm nút Lưu, thì cập nhật thông tin của Recipe
        result.ifPresent(newRecipe -> {
            ((RecipeController) getBController()).updateRecipe(newRecipe);
            // Cập nhật thông tin của Recipe trong danh sách hiển thị
            for (int i = 0; i < recipeList.size(); i++) {
                if (recipeList.get(i).getId() == newRecipe.getId()) {
                    recipeList.set(i, newRecipe);
                    break;
                }
            }
            // Cập nhật giao diện của TableView
            tableRecipe.setItems(FXCollections.observableArrayList(recipeList));
        });
    }

    public void handleDelete(int id) {
        System.out.println("Ngoc");
        // Thiết lập nút "Xóa" và "Hủy"
        Dialog<Recipe> dialog = new Dialog<>();
        dialog.setTitle("Bạn có chắc chắn muốn xóa");
        ButtonType deleteButton = new ButtonType("Xóa", ButtonBar.ButtonData.OK_DONE);
        dialog.getDialogPane().getButtonTypes().addAll(deleteButton, ButtonType.CANCEL);

        dialog.setResultConverter(dialogButton -> {
            if (dialogButton == deleteButton) {
                ((RecipeController) getBController()).deleteRecipe(id);
                JOptionPane.showMessageDialog(null, "Xóa thành công");
                for (int i = 0; i < recipeList.size(); i++) {
                    if (recipeList.get(i).getId() == id) {
                        recipeList.remove(i);
                        break;
                    }
                }
                tableRecipe.setItems(FXCollections.observableArrayList(recipeList));
            }
            return null;
        });

        // Hiển thị hộp thoại
        Optional<Recipe> result = dialog.showAndWait();

    }

    public void handleAdd() {
        // Tạo hộp thoại
        Dialog<Recipe> dialog = new Dialog<>();
        dialog.setTitle("Thêm cong thuc");

        // Thiết lập nút "Thêm" và "Hủy"
        ButtonType addButton = new ButtonType("Thêm", ButtonBar.ButtonData.OK_DONE);
        dialog.getDialogPane().getButtonTypes().addAll(addButton, ButtonType.CANCEL);

        // Tạo các trường nhập liệu
        TextField nameField = new TextField();
        nameField.setPromptText("Tên cong thuc");
        TextArea descField = new TextArea();
        descField.setPromptText("Chi tiet");

        // Tạo layout và thêm các trường nhập liệu
        GridPane grid = new GridPane();
        grid.add(new Label("Tên cong thuc:"), 0, 0);
        grid.add(nameField, 1, 0);
        grid.add(new Label("Chi tiet:"), 0, 1);
        grid.add(descField, 1, 1);
        dialog.getDialogPane().setContent(grid);
        // Xử lý sự kiện khi người dùng bấm nút Lưu
        dialog.setResultConverter(dialogButton -> {
            if (dialogButton == addButton) {
                String name = nameField.getText();
                String desc = descField.getText();
                Recipe newRecipe = ((RecipeController) getBController()).createRecipe(name, desc,
                        User.getCurrentUser().getId());
                // Hiện thông báo tạo thành công và cập nhật lại bảng
                JOptionPane.showMessageDialog(null, "Thanh cong");
                recipeList.add(newRecipe);
                tableRecipe.setItems(FXCollections.observableArrayList(recipeList));
            }
            return null;
        });

        // Hiển thị hộp thoại
        Optional<Recipe> result = dialog.showAndWait();

        if (result.isPresent()) {

        }
    }

    public void handleRowClick() {
        tableRecipe.setRowFactory(tv -> {
            TableRow<Recipe> row = new TableRow<>();
            row.setOnMouseClicked(event -> {
                if (event.getClickCount() == 2 && !row.isEmpty()) {
                    Recipe selectedRecipe = row.getItem();
                    showRecipeDetails(selectedRecipe);
                }
            });
            return row;
        });
    }

    private void showRecipeDetails(Recipe recipe) {
        // Create a new dialog to display the recipe details
        Dialog<Void> dialog = new Dialog<>();
        dialog.setTitle("Recipe Details");

        // Create a VBox to hold the recipe details
        VBox vbox = new VBox();
        vbox.setSpacing(10);
        vbox.setPadding(new Insets(10));

        // Create labels to display the recipe information
        Label nameLabel = new Label("Name: " + recipe.getName());
        Label descLabel = new Label("Description: " + recipe.getDesc());

        // Add the labels to the VBox
        vbox.getChildren().addAll(nameLabel, descLabel);

        // Set the dialog content
        dialog.getDialogPane().setContent(vbox);

        // Add a close button to the dialog
        ButtonType closeButton = new ButtonType("Close", ButtonBar.ButtonData.CANCEL_CLOSE);
        dialog.getDialogPane().getButtonTypes().add(closeButton);

        // Show the dialog and wait for it to be closed
        dialog.showAndWait();
    }


    public RecipeViewHandler(Stage stage, String screenPath) throws IOException {
        super(stage, screenPath);
    }

}
