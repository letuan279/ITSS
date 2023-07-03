package org.example.entity;

import java.util.Date;

public class Cook extends BaseEntity{
    private int id;
    private int quantity;
    private String foodName;
    private Date date;
    private int timeToCook;
    private int idUser;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

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

    public int getTimeToCook() {
        return timeToCook;
    }

    public void setTimeToCook(int timeToCook) {
        this.timeToCook = timeToCook;
    }

    public int getIdUser() {
        return idUser;
    }

    public void setIdUser(int idUser) {
        this.idUser = idUser;
    }

    public Cook(int id, int quantity, String foodName, Date date, int timeToCook, int idUser){
        this.id = id;
        this.quantity = quantity;
        this.foodName = foodName;
        this.date = date;
        this.timeToCook = timeToCook;
        this.idUser = idUser;

    }
}
