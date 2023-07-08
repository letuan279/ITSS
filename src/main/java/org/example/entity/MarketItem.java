package org.example.entity;

import java.time.LocalDate;

public class MarketItem extends BaseEntity{
    private int quantity;
    private String unit;
    private String name;
    private int type;
    private int idGroup;
    private LocalDate dayToBuy;
    private Integer idUser;
    private LocalDate expirationDate;

    public MarketItem(int id, int quantity, String unit, String name, int type, int idGroup, LocalDate dayToBuy, Integer idUser, LocalDate expirationDate) {
        super(id);
        this.quantity = quantity;
        this.unit = unit;
        this.name = name;
        this.type = type;
        this.idGroup = idGroup;
        this.dayToBuy = dayToBuy;
        this.idUser = idUser;
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

    public Integer getIdUser() {
        return idUser;
    }

    public void setIdUser(Integer idUser) {
        this.idUser = idUser;
    }

    public LocalDate getExpirationDate() {
        return expirationDate;
    }

    public void setExpirationDate(LocalDate expirationDate) {
        this.expirationDate = expirationDate;
    }
}
