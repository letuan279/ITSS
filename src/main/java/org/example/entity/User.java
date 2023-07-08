package org.example.entity;

import javafx.beans.property.SimpleStringProperty;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class User extends BaseEntity {
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

    public User() {
    }

    public User(int id, String username) {
        super(id);
        this.username = username;
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

    public static List<User> getListUsers() {
        PreparedStatement stmt = null;
        ResultSet rs = null;
        List<User> users = new ArrayList<>();
        try {
            Connection conn = Database.getConnection();
            stmt = conn.prepareStatement("SELECT * FROM user");
            rs = stmt.executeQuery();
            while (rs.next()) {
                int id = rs.getInt("id");
                String username = rs.getString("username");
                String password = rs.getString("password");
                int role = rs.getInt("role");
                users.add(new User(id, username, password, role));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return users;
    }

    public static User addUser(String username, String password, int role) {
        PreparedStatement stmt = null;
        ResultSet rs = null;
        User user = null;
        try {
            Connection conn = Database.getConnection();
            conn.setAutoCommit(false);
            String sql = "INSERT INTO user (username,password,role) VALUES (?, ?, ?)";
            stmt = conn.prepareStatement(sql);
            stmt.setString(1, username);
            stmt.setString(2, password);
            stmt.setInt(3, role);
            stmt.executeUpdate();
            rs = stmt.executeQuery("SELECT LAST_INSERT_ID()");
            int idUser = 0;
            if (rs.next()) {
                idUser = rs.getInt(1);
            }

            conn.commit();
            conn.setAutoCommit(true);

            user = new User(idUser, username, password, role);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return user;
    }

    public static void deleteUser(int id) {
        PreparedStatement stmt = null;
        try {
            Connection conn = Database.getConnection();
            String sql = "DELETE FROM user WHERE id = ?";
            stmt = conn.prepareStatement(sql);
            stmt.setInt(1, id);
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void updateUser(User user) {
        PreparedStatement stmt = null;
        try {
            Connection conn = Database.getConnection();
            stmt = conn.prepareStatement("UPDATE `user` SET username = ?, `password` = ?, `role` = ? WHERE id = ?");
            stmt.setString(1, user.getUsername());
            stmt.setString(2, user.getPassword());
            stmt.setInt(3, user.getRole());
            stmt.setInt(5, user.getId());
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
