package org.example.utils;

public class Config {
    static public  final  String DB_URL = "jdbc:mysql://localhost:3306/food_manager";
    static public final String DB_USERNAME = "root";
    static public final String DB_PASSWORD = "";

    static public final int ROLE_ADMIN = 1;
    static public final int ROLE_USER = 0;

    // Screen path
    static public final String LOGIN_PATH = "/login.fxml";

    static public final String HOME_PATH = "/home.fxml";

    static public final String LAYOUT_PATH = "/layout.fxml";

    static public final String ADMIN_PATH = "/admin.fxml";

    static public final String COOK_PATH = "/cook.fxml";

    static public final String RECIPE_PATH = "/recipe.fxml";

    static public final String GROUP_PATH = "/group.fxml";
    static public final String ADD_USER_PATH = "/addUser.fxml";
}
