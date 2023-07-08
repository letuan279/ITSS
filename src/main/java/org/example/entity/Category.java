package org.example.entity;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class Category extends BaseEntity {
    String name;

    public Category(int id, String name) {
        super(id);
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public static List<Category> getListCategories() {
        PreparedStatement stmt = null;
        ResultSet rs = null;
        List<Category> categories = new ArrayList<>();
        try {
            Connection conn = Database.getConnection();
            stmt = conn.prepareStatement("SELECT * FROM category");
            rs = stmt.executeQuery();
            while (rs.next()) {
                int id = rs.getInt("id");
                String name = rs.getString("name");
                categories.add(new Category(id, name));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return categories;
    }

    public static Category createCategory(String name){
        PreparedStatement stmt = null;
        ResultSet rs = null;
        Category category = null;
        try {
            Connection conn = Database.getConnection();
            conn.setAutoCommit(false);
            String sql = "INSERT INTO category (name) VALUES (?)";
            stmt = conn.prepareStatement(sql);
            stmt.setString(1, name);
            stmt.executeUpdate();
            rs = stmt.executeQuery("SELECT LAST_INSERT_ID()");
            int idCategory = 0;
            if (rs.next()) {
                idCategory = rs.getInt(1);
            }

            conn.commit();
            conn.setAutoCommit(true);

            category = new Category(idCategory,name);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return category;
    }

    public  static void deleteCategory(int id){
        PreparedStatement stmt = null;
        try{
            Connection conn = Database.getConnection();
            String sql = "DELETE FROM category WHERE id = ?";
            stmt = conn.prepareStatement(sql);
            stmt.setInt(1, id);
            stmt.executeUpdate();
        }
        catch (SQLException e){
            e.printStackTrace();
        }
    }

    public static void updateCategory(Category category) {
        PreparedStatement stmt = null;
        try {
            Connection conn = Database.getConnection();
            stmt = conn.prepareStatement("UPDATE `category` SET name = ? WHERE id = ?");
            stmt.setString(1, category.getName());
            stmt.setInt(2, category.getId());
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

}
