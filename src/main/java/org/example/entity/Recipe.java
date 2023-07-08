package org.example.entity;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class Recipe extends BaseEntity {
    String name;
    String desc;
    int idUser;

    public int getIdUser() {
        return idUser;
    }

    public void setIdUser(int idUser) {
        this.idUser = idUser;
    }

    public Recipe(int id, String name, String des, int idUser) {
        super(id);
        this.name = name;
        this.desc = des;
        this.idUser = idUser;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String des) {
        this.desc = des;
    }

    public static List<Recipe> getListRecipes(int idUser) {
        PreparedStatement stmt = null;
        ResultSet rs = null;
        List<Recipe> recipes = new ArrayList<>();
        try {
            Connection conn = Database.getConnection();
            stmt = conn.prepareStatement("SELECT * FROM recipe WHERE idUser = ?");
            stmt.setInt(1, idUser);
            rs = stmt.executeQuery();
            while (rs.next()) {
                int id = rs.getInt("id");
                String name = rs.getString("name");
                String desc = rs.getString("desc");
                recipes.add(new Recipe(id, name, desc, idUser));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return recipes;
    }

    public static void updateRecipe(Recipe recipe) {
        PreparedStatement stmt = null;
        try {
            Connection conn = Database.getConnection();
            stmt = conn.prepareStatement("UPDATE `recipe` SET name = ?, desc = ?, WHERE id = ?");
            stmt.setString(1, recipe.getName());
            stmt.setString(2, recipe.getDesc());
            stmt.setInt(3, recipe.getId());
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void deleteRecipe(int id) {
        PreparedStatement stmt = null;
        try {
            Connection conn = Database.getConnection();
            String sql = "DELETE FROM recipe WHERE id = ?";
            stmt = conn.prepareStatement(sql);
            stmt.setInt(1, id);
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static Recipe addRecipe(String name, String desc, int idUser) {
        PreparedStatement stmt = null;
        ResultSet rs = null;
        Recipe recipe = null;
        try {
            Connection conn = Database.getConnection();
            conn.setAutoCommit(false);
            String sql = "INSERT INTO recipe (name, desc,idUser) VALUES (?, ?, ?)";
            stmt = conn.prepareStatement(sql);
            stmt.setString(1, name);
            stmt.setString(2, desc);
            stmt.setInt(3, idUser);
            stmt.executeUpdate();
            rs = stmt.executeQuery("SELECT LAST_INSERT_ID()");
            int idRecipe = 0;
            if (rs.next()) {
                idRecipe = rs.getInt(1);
            }

            conn.commit();
            conn.setAutoCommit(true);

            recipe = new Recipe(idRecipe, name, desc, idUser);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return recipe;
    }

}
