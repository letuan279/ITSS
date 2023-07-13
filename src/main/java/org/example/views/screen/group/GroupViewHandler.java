package org.example.views.screen.group;

import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.util.Callback;
import javafx.util.StringConverter;
import org.example.controllers.BaseController;
import org.example.controllers.GroupController;
import org.example.controllers.MarketItemController;
import org.example.entity.Group;
import org.example.entity.MarketItem;
import org.example.entity.User;
import org.example.views.screen.BaseView;

import javax.swing.*;
import java.io.IOException;
import java.net.URL;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;
import java.util.stream.Collectors;

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
                        Button memberIcon = new Button("Thành viên");
                        Button marketIcon = new Button("Đi chợ");

                        deleteIcon.setOnMouseClicked((MouseEvent event) -> {
                            System.out.println("delete");
                        });
                        editIcon.setOnMouseClicked((MouseEvent event) -> {
                            handleEdit(getTableView().getItems().get(getIndex()));
                        });
                        memberIcon.setOnMouseClicked((MouseEvent event) -> {
                            handleMember(getTableView().getItems().get(getIndex()));
                        });
                        marketIcon.setOnMouseClicked((MouseEvent event) -> {
                            handleMarket(getTableView().getItems().get(getIndex()));
                        });

                        HBox managebtn = new HBox(memberIcon, marketIcon, editIcon, deleteIcon);
                        managebtn.setStyle("-fx-alignment:center");
                        HBox.setMargin(deleteIcon, new Insets(2, 2, 2, 2));
                        HBox.setMargin(editIcon, new Insets(2, 2, 2, 2));
                        HBox.setMargin(memberIcon, new Insets(2, 2, 2, 2));
                        HBox.setMargin(marketIcon, new Insets(2, 2, 2, 2));
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
        List<User> listUser = ((GroupController) getBController()).getMembers(User.getCurrentUser().getId());
        ObservableList<User> users = FXCollections.observableArrayList(listUser);
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
        if (result.isPresent() && result.get() != null) {
            ObservableList<User> selectedUsers = memberField.getSelectionModel().getSelectedItems();
            List<Integer> userIds = new ArrayList<>();
            for(User user : selectedUsers) {
                userIds.add(user.getId());
            }
            userIds.add(User.getCurrentUser().getId());
            Group newGroup = ((GroupController) getBController()).addGroup(nameField.getText(), descriptionField.getText(), userIds, User.getCurrentUser().getId());

            // Hiện thông báo tạo thành công và cập nhật lại bảng
            JOptionPane.showMessageDialog(null, "Tạo nhóm mới thành công");
            groupList.add(newGroup);
            table.setItems(FXCollections.observableArrayList(groupList));
        }
    }

    public void handleMember(Group group) {
        // view members
        TableColumn<User, String> stt = new TableColumn<>("STT");

        TableColumn<User, String> nameColumn = new TableColumn<>("Tên thành viên");
        nameColumn.setCellValueFactory(new PropertyValueFactory<>("username"));
        TableColumn<User, String> roleColumn = new TableColumn<>("Vai trò");
        TableColumn<User, String> act = new TableColumn<>("Thao tác");
        roleColumn.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<User, String>, ObservableValue<String>>() {
            @Override
            public ObservableValue<String> call(TableColumn.CellDataFeatures<User, String> cellData) {
                Boolean isLeader = ((GroupController) getBController()).isLeader(cellData.getValue().getId(), group.getId());
                String tag = (isLeader == true) ? "Trưởng nhóm" : "Thành viên";
                return new SimpleStringProperty(tag);
            }
        });
        // Lấy dữ liệu và hiển thị
        List<User> members = ((GroupController) getBController()).getMemberInGroup(group.getId());
        TableView<User> tableView = new TableView<>();
        ObservableList<User> MemberList = FXCollections.observableArrayList(members);
        tableView.setItems(MemberList);
        stt.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<User, String>, ObservableValue<String>>() {
            @Override
            public ObservableValue<String> call(TableColumn.CellDataFeatures<User, String> param) {
                return new ReadOnlyObjectWrapper<>(tableView.getItems().indexOf(param.getValue()) + 1 + "");
            }
        });
        Callback<TableColumn<User, String>, TableCell<User, String>> cellFactory = (TableColumn<User, String> param) -> {
            final TableCell<User, String> cell = new TableCell<User, String>() {
                @Override
                public void updateItem(String item, boolean empty) {
                    super.updateItem(item, empty);
                    if (empty) {
                        setGraphic(null);
                        setText(null);

                    } else {
                        Button deleteIcon = new Button("Xóa");

                        deleteIcon.setOnMouseClicked((MouseEvent event) -> {
                            User selectedUser = getTableView().getItems().get(getIndex());
                            ((GroupController) getBController()).deleteMember(selectedUser.getId(), group.getId());
                            for(User member : members) {
                                if (member.getId() == selectedUser.getId()) {
                                    members.remove(member);
                                    break;
                                }
                            }
                            tableView.setItems(FXCollections.observableArrayList(members));
                        });

                        HBox managebtn = new HBox(deleteIcon);
                        managebtn.setStyle("-fx-alignment:center");
                        HBox.setMargin(deleteIcon, new Insets(2, 2, 0, 3));
                        setGraphic(managebtn);
                        setText(null);
                    }
                }

            };
            return cell;
        };
        act.setCellFactory(cellFactory);
        tableView.getColumns().addAll(stt, nameColumn, roleColumn, act);
        // Tạo nút "Thêm thành viên"
        Button addButton = new Button("Thêm thành viên");
        addButton.setOnAction(event -> {
            List<User> users = ((GroupController) getBController()).getMemberAddToGroup(group.getId());
            ListView<User> listView = new ListView<>();
            listView.getItems().addAll(users);
            listView.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);

            Dialog<User> dialog = new Dialog<>();
            dialog.setTitle("Chọn thành viên để thêm");

            ButtonType saveButtonType = new ButtonType("Lưu", ButtonBar.ButtonData.OK_DONE);
            dialog.getDialogPane().getButtonTypes().addAll(saveButtonType, ButtonType.CANCEL);
            listView.setCellFactory(new Callback<ListView<User>, ListCell<User>>() {
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
            listView.setPrefHeight(200);
            ScrollPane scrollPane = new ScrollPane(listView);
            scrollPane.setFitToWidth(true);

            GridPane grid = new GridPane();
            grid.add(new Label("Thành viên:"), 0, 2);
            grid.add(scrollPane, 1, 2);
            dialog.getDialogPane().setContent(grid);

            // Hiển thị hộp thoại
            Optional<User> result = dialog.showAndWait();
            if (result.isPresent() && result.get() != null) {
                ObservableList<User> selectedUsers = listView.getSelectionModel().getSelectedItems();
                List<Integer> userIds = new ArrayList<>();
                for(User user : selectedUsers) {
                    userIds.add(user.getId());
                }
                ((GroupController) getBController()).addMembers(group.getId(), userIds);
                // Hiện thông báo tạo thành công và cập nhật lại bảng
                for(User user : selectedUsers) {
                    members.add(user);
                }
                tableView.setItems(FXCollections.observableArrayList(members));
                JOptionPane.showMessageDialog(null, "Thêm thành viên thành công");
            }
        });
        HBox hbox = new HBox(addButton);
        hbox.setAlignment(Pos.CENTER_RIGHT);
        hbox.setPadding(new Insets(10));
        VBox vbox = new VBox(hbox, tableView);
        Stage modalStage = new Stage();
        modalStage.initModality(Modality.APPLICATION_MODAL);
        modalStage.setScene(new Scene(vbox));
        modalStage.showAndWait();
    }

    public void handleMarket(Group group) {
        // Hiển thị thông tin
        MarketItemController marketItemController = new MarketItemController();
        List<MarketItem> marketItems = marketItemController.getAllInGroup(group.getId());
        TableView<MarketItem> tableView = new TableView<>();
        ObservableList<MarketItem> marketItemsList = FXCollections.observableArrayList(marketItems);
        tableView.setItems(marketItemsList);

        TableColumn<MarketItem, String> stt = new TableColumn<>("STT");
        TableColumn<MarketItem, String> nameColumn = new TableColumn<>("Tên");
        TableColumn<MarketItem, String> quantityColumn = new TableColumn<>("Số lượng");
        TableColumn<MarketItem, String> unitColumn = new TableColumn<>("Đơn vị");
        TableColumn<MarketItem, String> dayToBuyColumn = new TableColumn<>("Ngày mua");
        TableColumn<MarketItem, String> buyerColumn = new TableColumn<>("Người mua");
        TableColumn<MarketItem, String> stateColumn = new TableColumn<>("Trạng thái");
        TableColumn<MarketItem, String> act = new TableColumn<>("Thao tác");

        nameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
        quantityColumn.setCellValueFactory(new PropertyValueFactory<>("quantity"));
        unitColumn.setCellValueFactory(new PropertyValueFactory<>("unit"));
        dayToBuyColumn.setCellValueFactory(new PropertyValueFactory<>("dayToBuy"));

        stt.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<MarketItem, String>, ObservableValue<String>>() {
            @Override
            public ObservableValue<String> call(TableColumn.CellDataFeatures<MarketItem, String> param) {
                return new ReadOnlyObjectWrapper<>(tableView.getItems().indexOf(param.getValue()) + 1 + "");
            }
        });
        buyerColumn.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<MarketItem, String>, ObservableValue<String>>() {
            @Override
            public ObservableValue<String> call(TableColumn.CellDataFeatures<MarketItem, String> cellData) {
                User buyer = cellData.getValue().getBuyer();
                if(buyer == null) return new SimpleStringProperty("Chưa có");
                return new SimpleStringProperty(cellData.getValue().getBuyer().getUsername());
            }
        });
        stateColumn.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<MarketItem, String>, ObservableValue<String>>() {
            @Override
            public ObservableValue<String> call(TableColumn.CellDataFeatures<MarketItem, String> cellData) {
                int state = cellData.getValue().getState();
                if(state == 0) return new SimpleStringProperty("Chưa mua");
                return new SimpleStringProperty("Đã mua");
            }
        });
        Callback<TableColumn<MarketItem, String>, TableCell<MarketItem, String>> cellFactory = (TableColumn<MarketItem, String> param) -> {
            final TableCell<MarketItem, String> cell = new TableCell<MarketItem, String>() {
                @Override
                public void updateItem(String item, boolean empty) {
                    super.updateItem(item, empty);
                    if (empty) {
                        setGraphic(null);
                        setText(null);

                    } else {
                        Button buyIcon = new Button("Đã mua");

                        buyIcon.setOnMouseClicked((MouseEvent event) -> {
                            handleBuy(getTableView().getItems().get(getIndex()).getId(), marketItemController);
                            // Cập nhật lại bảng

                        });

                        if(getTableView().getItems().get(getIndex()).getBuyer() == null){
                            HBox managebtn = new HBox(buyIcon);
                            managebtn.setStyle("-fx-alignment:center");
                            HBox.setMargin(buyIcon, new Insets(2, 2, 0, 3));
                            setGraphic(managebtn);
                            setText(null);
                        }
                    }
                }

            };
            return cell;
        };
        act.setCellFactory(cellFactory);
        tableView.getColumns().addAll(stt, nameColumn, quantityColumn, unitColumn, dayToBuyColumn, buyerColumn, stateColumn, act);

        DatePicker datePicker = new DatePicker(LocalDate.now());
        List<MarketItem> firstMarketItems = marketItems.stream()
                .filter(marketItem -> marketItem.getDayToBuy().isEqual(LocalDate.now()))
                .collect(Collectors.toList());
        tableView.setItems(FXCollections.observableArrayList(firstMarketItems));
        datePicker.setConverter(new StringConverter<LocalDate>() {
            String pattern = "yyyy/MM/dd";
            DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern(pattern);

            {
                datePicker.setPromptText(pattern.toLowerCase());
            }

            @Override
            public String toString(LocalDate date) {
                if (date != null) {
                    return dateFormatter.format(date);
                } else {
                    return "";
                }
            }

            @Override
            public LocalDate fromString(String string) {
                if (string != null && !string.isEmpty()) {
                    return LocalDate.parse(string, dateFormatter);
                } else {
                    return null;
                }
            }
        });

        ObservableList<MarketItem> filteredMarketItemsList = FXCollections.observableArrayList(marketItems);
        datePicker.setOnAction(event -> {
            LocalDate selectedDate = datePicker.getValue();
            List<MarketItem> filteredMarketItems = marketItems.stream()
                    .filter(marketItem -> marketItem.getDayToBuy().isEqual(selectedDate))
                    .collect(Collectors.toList());
            filteredMarketItemsList.setAll(filteredMarketItems);
            tableView.setItems(filteredMarketItemsList);
        });

        Button addButton = new Button("Thêm món ăn cần mua");
        addButton.setOnAction(event -> {
            MarketItem newMarketItem = handleAddNewBuyItem(marketItemController, group.getId());
            if(newMarketItem != null){
                marketItems.add(newMarketItem);
                List<MarketItem> filteredMarketItems = marketItems.stream()
                        .filter(marketItem -> marketItem.getDayToBuy().isEqual(LocalDate.now()))
                        .collect(Collectors.toList());
                tableView.setItems(FXCollections.observableArrayList(filteredMarketItems));
            }
        });

        HBox hbox = new HBox(datePicker, addButton);
        hbox.setAlignment(Pos.CENTER_RIGHT);
        hbox.setPadding(new Insets(10));
        VBox vbox = new VBox(hbox, tableView);
        Stage modalStage = new Stage();
        modalStage.initModality(Modality.APPLICATION_MODAL);
        modalStage.setScene(new Scene(vbox));
        modalStage.showAndWait();
    }

    public void handleBuy(int idMarketItem, BaseController controller) {
        // Cần nhập: Thời hạn, trạng thái

    }

    public MarketItem handleAddNewBuyItem(BaseController controller, int idGroup) {
        // Hiện hộp thoại thêm các trường: Tên, số lượng, đơn vị, kiểu, ngày mua
        // Tạo hộp thoại
        Dialog<ButtonType> dialog = new Dialog<>();
        dialog.setTitle("Thêm món đồ định mua");

        // Thiết lập nút "Thêm" và "Hủy"
        ButtonType addButton = new ButtonType("Thêm", ButtonBar.ButtonData.OK_DONE);
        dialog.getDialogPane().getButtonTypes().addAll(addButton, ButtonType.CANCEL);

        // Tạo các trường nhập liệu
        TextField nameField = new TextField();
        nameField.setPromptText("Tên món đồ");
        TextField quantityField = new TextField();
        quantityField.setPromptText("Số lượng");
        TextField unitField = new TextField();
        unitField.setPromptText("Đơn vị");
        quantityField.setTextFormatter(new TextFormatter<>(change -> {
            String newText = change.getControlNewText();
            if (newText.matches("\\d*")) {
                return change;
            }
            return null;
        }));
        ObservableList<String> typeOptions = FXCollections.observableArrayList("món ăn", "thực phẩm");
        ChoiceBox<String> typeField = new ChoiceBox<>(typeOptions);
        typeField.setValue("thực phẩm");

        DatePicker dateField = new DatePicker(LocalDate.now());
        dateField.setConverter(new StringConverter<LocalDate>() {
            String pattern = "yyyy/MM/dd";
            DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern(pattern);

            {
                dateField.setPromptText(pattern.toLowerCase());
            }

            @Override
            public String toString(LocalDate date) {
                if (date != null) {
                    return dateFormatter.format(date);
                } else {
                    return "";
                }
            }

            @Override
            public LocalDate fromString(String string) {
                if (string != null && !string.isEmpty()) {
                    return LocalDate.parse(string, dateFormatter);
                } else {
                    return null;
                }
            }
        });
        dateField.setValue(LocalDate.now());
        dateField.setPromptText("Ngày mua");

        // Tạo layout và thêm các trường nhập liệu
        GridPane gridPane = new GridPane();
        gridPane.setHgap(10);
        gridPane.setVgap(10);
        gridPane.setPadding(new Insets(10));

        Label nameLabel = new Label("Tên món đồ:");
        gridPane.add(nameLabel, 0, 0);
        gridPane.add(nameField, 1, 0);
        Label quantityLabel = new Label("Số lượng:");
        gridPane.add(quantityLabel, 0, 1);
        gridPane.add(quantityField, 1, 1);

        Label unitLabel = new Label("Đơn vị:");
        gridPane.add(unitLabel, 0, 2);
        gridPane.add(unitField, 1, 2);

        Label typeLabel = new Label("Loại:");
        gridPane.add(typeLabel, 0, 3);
        gridPane.add(typeField, 1, 3);

        Label dateLabel = new Label("Ngày mua:");
        gridPane.add(dateLabel, 0, 4);
        gridPane.add(dateField, 1, 4);
        dialog.getDialogPane().setContent(gridPane);


        // Hiển thị hộp thoại
        Optional<ButtonType> result = dialog.showAndWait();
        if (result.isPresent() && result.get() == addButton) {
            String name = nameField.getText();
            int quantity = Integer.parseInt(quantityField.getText());
            String type = typeField.getValue();
            LocalDate date = dateField.getValue();
            String unit = unitField.getText();

            MarketItem newMarketItem = ((MarketItemController) controller).addABuyMarketItem(
                    idGroup,
                    name,
                    quantity,
                    type,
                    unit,
                    date
            );
            return newMarketItem;
        }
        return null;
    }
}


