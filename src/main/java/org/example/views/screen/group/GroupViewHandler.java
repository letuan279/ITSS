package org.example.views.screen.group;

import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;
import javafx.util.Callback;
import org.example.controllers.GroupController;
import org.example.entity.Group;
import org.example.entity.User;
import org.example.views.screen.BaseView;

import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;

public class GroupViewHandler extends BaseView implements Initializable {
    @FXML
    private TableView<Group> table;
    @FXML
    private TableColumn<Group, String> stt;
    @FXML
    private TableColumn<Group, String> name;
    @FXML
    private TableColumn<Group, String> desc;
    @FXML
    private TableColumn<Group, String> act;

    @FXML
    private Button add_btn;

    ObservableList<Group> GroupList = FXCollections.observableArrayList();
    List<Group> groupList;

    public GroupViewHandler(Stage stage, String screenPath) throws IOException {
        super(stage, screenPath);
    }

    public void initialize(URL arg0, ResourceBundle arg1) {
        setBController(new GroupController());
        groupList = ((GroupController) getBController()).getGroupsOfMember(User.getCurrentUser().getId());
        GroupList = FXCollections.observableArrayList(groupList);

        name.setCellValueFactory(new PropertyValueFactory<>("name"));
        desc.setCellValueFactory(new PropertyValueFactory<>("desc"));

        // Stt
        stt.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<Group, String>, ObservableValue<String>>() {
            @Override
            public ObservableValue<String> call(TableColumn.CellDataFeatures<Group, String> param) {
                return new ReadOnlyObjectWrapper<>(table.getItems().indexOf(param.getValue()) + 1 + "");
            }
        });

        // Thao tác
        Callback<TableColumn<Group, String>, TableCell<Group, String>> cellFactory = (TableColumn<Group, String> param) -> {
            final TableCell<Group, String> cell = new TableCell<Group, String>() {
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
                            System.out.println("delete");
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
        // Thêm data vào bảng
        table.setItems(GroupList);

        // Add hành động cho nút Thêm
        add_btn.setOnMouseClicked(e -> {
            handleAdd();
        });
    }

    public void handleEdit(Group group) {
        Dialog<Group> dialog = new Dialog<>();
        dialog.setTitle("Sửa thông tin nhóm");

        // Tạo các trường nhập liệu
        TextField nameField = new TextField(group.getName());
        TextArea descArea = new TextArea(group.getDesc());

        // Tạo khung modal
        GridPane grid = new GridPane();
        grid.add(new Label("Tên nhóm:"), 0, 0);
        grid.add(nameField, 1, 0);
        grid.add(new Label("Mô tả nhóm:"), 0, 1);
        grid.add(descArea, 1, 1);
        dialog.getDialogPane().setContent(grid);

        // Nút thao tác
        ButtonType saveButtonType = new ButtonType("Lưu", ButtonBar.ButtonData.OK_DONE);
        dialog.getDialogPane().getButtonTypes().addAll(saveButtonType, ButtonType.CANCEL);

        // Xử lý sự kiện khi người dùng bấm nút Lưu
        dialog.setResultConverter(dialogButton -> {
            if (dialogButton == saveButtonType) {
                String name = nameField.getText();
                String desc = descArea.getText();
                return new Group(group.getId(), name, desc);
            }
            return null;
        });

        // Hiển thị hộp thoại và chờ đợi phản hồi từ người dùng
        Optional<Group> result = dialog.showAndWait();

        // Nếu người dùng đã bấm nút Lưu, thì cập nhật thông tin của Group
        result.ifPresent(newGroup -> {
            ((GroupController) getBController()).updateGroup(newGroup);
            // Cập nhật thông tin của Group trong danh sách hiển thị
            for (int i = 0; i < groupList.size(); i++) {
                if (groupList.get(i).getId() == newGroup.getId()) {
                    groupList.set(i, newGroup);
                    break;
                }
            }
            // Cập nhật giao diện của TableView
            table.setItems(FXCollections.observableArrayList(groupList));
        });
    }

    public void handleAdd() {
        // Tạo hộp thoại
        Dialog<Group> dialog = new Dialog<>();
        dialog.setTitle("Thêm nhóm");

        // Thiết lập nút "Thêm" và "Hủy"
        ButtonType addButton = new ButtonType("Thêm", ButtonBar.ButtonData.OK_DONE);
        dialog.getDialogPane().getButtonTypes().addAll(addButton, ButtonType.CANCEL);

        // Tạo các trường nhập liệu
        TextField nameField = new TextField();
        nameField.setPromptText("Tên nhóm");
        TextArea descriptionField = new TextArea();
        descriptionField.setPromptText("Mô tả");
        ObservableList<User> users = FXCollections.observableArrayList();
        users.add(new User(1, "Thành viên 1"));
        users.add(new User(2, "Thành viên 2"));
        users.add(new User(3, "Thành viên 3"));
        users.add(new User(3, "Thành viên 3"));
        users.add(new User(3, "Thành viên 3"));
        users.add(new User(3, "Thành viên 3"));
        users.add(new User(3, "Thành viên 3"));
        users.add(new User(3, "Thành viên 3"));
        users.add(new User(3, "Thành viên 3"));
        ListView<User> memberField = new ListView<>(users);
        memberField.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
        // Thiết lập StringConverter để hiển thị tên của thành viên
        memberField.setCellFactory(new Callback<ListView<User>, ListCell<User>>() {
            @Override
            public ListCell<User> call(ListView<User> param) {
                ListCell<User> cell = new ListCell<User>() {
                    @Override
                    protected void updateItem(User user, boolean empty) {
                        super.updateItem(user, empty);
                        if (user != null) {
                            setText(user.getUsername());
                        } else {
                            setText(null);
                        }
                    }
                };
                return cell;
            }
        });
        memberField.setPrefHeight(100);
        ScrollPane scrollPane = new ScrollPane(memberField);
        scrollPane.setFitToWidth(true);

        // Tạo layout và thêm các trường nhập liệu
        GridPane grid = new GridPane();
        grid.add(new Label("Tên nhóm:"), 0, 0);
        grid.add(nameField, 1, 0);
        grid.add(new Label("Mô tả:"), 0, 1);
        grid.add(descriptionField, 1, 1);
        grid.add(new Label("Thành viên:"), 0, 2);
        grid.add(scrollPane, 1, 2);
        dialog.getDialogPane().setContent(grid);


        // Hiển thị hộp thoại
        Optional<Group> result = dialog.showAndWait();
        if (result.isPresent()) {
            System.out.println(nameField.getText());
            System.out.println(descriptionField.getText());
            ObservableList<User> selectedUsers = memberField.getSelectionModel().getSelectedItems();
            for(User user : selectedUsers) {
                System.out.println(user.getId());
            }
        }
    }
}
