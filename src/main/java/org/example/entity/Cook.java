package org.example.entity;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.sql.Date;
import java.util.List;


public class Cook extends BaseEntity{
    private int quantity;
    private String foodName;
    private Date date;
    private String timeToCook;

    private int idUser;

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public String getFoodName() {
        return foodName;
    }

    public void setFoodName(String foodName) {
        this.foodName = foodName;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public String getTimeToCook() {
        return timeToCook;
    }

    public void setTimeToCook(String timeToCook) {
        this.timeToCook = timeToCook;
    }


    public Cook(int id, int quantity, String foodName, Date date, String timeToCook,int idUser){
        super(id);
        this.quantity = quantity;
        this.foodName = foodName;
        this.date = date;
        this.timeToCook = timeToCook;
        this.idUser = idUser;
    }

    public static List<Cook> getListCooks(int idUser){
        PreparedStatement stmt = null;
        ResultSet rs = null;
        List<Cook> cooks = new ArrayList<>();
        try{
            Connection conn = Database.getConnection();
            stmt = conn.prepareStatement("SELECT * FROM cook WHERE idUser = ?");
            stmt.setInt(1, idUser);
            rs = stmt.executeQuery();
            while (rs.next()){
                int id = rs.getInt("id");
                int quantity = rs.getInt("quantity");
                String foodName = rs.getString("foodName");
                Date date = rs.getDate("date");
                String timeToCook= rs.getString("timeToCook");
                cooks.add(new Cook(id,quantity,foodName,date,timeToCook,idUser));
            }
        }catch (SQLException e){
            e.printStackTrace();
        }
        return cooks;
    }

    public static void updateCook(Cook cook) {
        PreparedStatement stmt = null;
        try {
            Connection conn = Database.getConnection();
            stmt = conn.prepareStatement("UPDATE `cook` SET foodName = ?, `quantity` = ?, `date` = ?, `timeToCook` = ? WHERE id = ?");
            stmt.setString(1, cook.getFoodName());
            stmt.setInt(2, cook.getQuantity());
            stmt.setDate(3,cook.getDate());
            stmt.setString(4, cook.getTimeToCook());
            stmt.setInt(5,cook.getId());
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public  static void deleteCook(int id){
        PreparedStatement stmt = null;
        try{
            Connection conn = Database.getConnection();
            String sql = "DELETE FROM cook WHERE id = ?";
            stmt = conn.prepareStatement(sql);
            stmt.setInt(1, id);
            stmt.executeUpdate();
        }
        catch (SQLException e){
            e.printStackTrace();
        }
    }
    public static Cook addCook(String foodName,int quantity,Date date,String timeToCook,int idUser){
        PreparedStatement stmt = null;
        ResultSet rs = null;
        Cook cook = null;
        try {
            Connection conn = Database.getConnection();
            conn.setAutoCommit(false);
            String sql = "INSERT INTO cook (foodName, quantity,date,timeToCook,idUser) VALUES (?, ?, ?, ?, ?)";
            stmt = conn.prepareStatement(sql);
            stmt.setString(1, foodName);
            stmt.setInt(2, quantity);
            stmt.setDate(3, date);
            stmt.setString(4, timeToCook);
            stmt.setInt(5,idUser);
            stmt.executeUpdate();
            rs = stmt.executeQuery("SELECT LAST_INSERT_ID()");
            int idCook = 0;
            if (rs.next()) {
                idCook = rs.getInt(1);
            }

            conn.commit();
            conn.setAutoCommit(true);

            cook = new Cook(idCook,quantity,foodName,date,timeToCook,idUser);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return cook;
    }

    public static void main(String[] args) {
        int idUser = 2; // Thay đổi idUser để kiểm tra với các giá trị khác nhau
//        Cook cook =  Cook.addCook("Bundau",2,new Date(2020), "Sang",2);
//        System.out.println(cook.getFoodName());
        Cook.deleteCook(10);
    }
}