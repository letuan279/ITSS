package org.example.entity;

import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class MarketItem extends BaseEntity{
    private int quantity;
    private String unit;
    private String name;
    private int type;
    private int idGroup;
    private LocalDate dayToBuy;
    private User buyer;
    private int expirationDate;

    public MarketItem(int id, int quantity, String unit, String name, int type, int idGroup, LocalDate dayToBuy, User buyer, int expirationDate) {
        super(id);
        this.quantity = quantity;
        this.unit = unit;
        this.name = name;
        this.type = type;
        this.idGroup = idGroup;
        this.dayToBuy = dayToBuy;
        this.buyer = buyer;
        this.expirationDate = expirationDate;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public String getUnit() {
        return unit;
    }

    public void setUnit(String unit) {
        this.unit = unit;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public int getIdGroup() {
        return idGroup;
    }

    public void setIdGroup(int idGroup) {
        this.idGroup = idGroup;
    }

    public LocalDate getDayToBuy() {
        return dayToBuy;
    }

    public void setDayToBuy(LocalDate dayToBuy) {
        this.dayToBuy = dayToBuy;
    }

    public User getBuyer() {
        return buyer;
    }

    public void setBuyer(User buyer) {
        this.buyer = buyer;
    }

    public int getExpirationDate() {
        return expirationDate;
    }

    public void setExpirationDate(int expirationDate) {
        this.expirationDate = expirationDate;
    }


    public static List<MarketItem> getListBoughtFood(int idFood){
        PreparedStatement stmt = null;
        ResultSet rs = null;
        List<MarketItem> listBoughtFoods = new ArrayList<>();
        try{
            Connection conn = Database.getConnection();
            stmt = conn.prepareStatement("SELECT * FROM marketitem WHERE idUser IS NOT NULL");
            stmt.setInt(1, idFood);
            rs = stmt.executeQuery();
            while (rs.next()){
                int id = rs.getInt("id");
                int quantity = rs.getInt("quantity");
                String unit = rs.getString("unit");
                String foodName = rs.getString("name");
                int type = rs.getInt("type");
                int idGroup = rs.getInt("idGroup");
                LocalDate dayToBuy1 = rs.getDate("dayToBuy").toLocalDate();
                User buyer = (User) rs.getObject("idUser");
                int expirationDate = rs.getInt("expirationTime");
                listBoughtFoods.add(new MarketItem(id,quantity,unit, foodName, type, idGroup, dayToBuy1, buyer, expirationDate));
            }
        }catch (SQLException e){
            e.printStackTrace();
        }
        return listBoughtFoods;
    }

}
