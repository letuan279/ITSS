package org.example.entity;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class User extends BaseEntity{
    private String username;
    private String password;
    private int role;

    private static User currentUser;

    public static User getCurrentUser() {
        return currentUser;
    }

    public static void setCurrentUser(User currentUser) {
        User.currentUser = currentUser;
    }

    public int getRole() {
        return role;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setRole(int role) {
        this.role = role;
    }

    public User(int id, String username, String password, int role) {
        super(id);
        this.username = username;
        this.password = password;
        this.role = role;
    }

    public static Boolean login(String username, String password) throws SQLException {
        // Check user in DB
        String query = "SELECT id, role FROM User WHERE username = ? AND password = ?";
        PreparedStatement stmt = Database.getConnection().prepareStatement(query);
        stmt.setString(1, username);
        stmt.setString(2, password);

        ResultSet rs = stmt.executeQuery();
        if (rs.next()) {
            // User is found in the database
            int userId = rs.getInt("id");
            int role = rs.getInt("role");

            // Set the current user
            currentUser = new User(userId, username, password, role);
            setCurrentUser(currentUser);
            return true;
        }
        return false;
    }
}
