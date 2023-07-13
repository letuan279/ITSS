package org.example.controllers;

import org.example.entity.MarketItem;

import java.time.LocalDate;
import java.util.List;

public class MarketItemController extends BaseController{
    public List<MarketItem> getAllInGroup(int idGroup) {
        return MarketItem.getAllInGroup(idGroup);
    }

    public void buyAMarketItem(int idUser, int idMarketItem) {
        MarketItem.buyAMarketItem(idUser, idMarketItem);
    }

    public MarketItem addABuyMarketItem(int idGroup, String name, int quantity, String type, String unit, LocalDate date) {
        if(type == "thực phẩm") return MarketItem.add(idGroup, name, quantity, 1, unit, date);
        else MarketItem.add(idGroup, name, quantity, 0, unit, date);
        return null;
    }
}
