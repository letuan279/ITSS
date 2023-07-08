package org.example.controllers;

import org.example.entity.MarketItem;
import org.example.entity.Database;

import java.util.List;
import java.sql.Date;

public class HomeController extends BaseController{
    public List<MarketItem> getBoughtFoods(int idFood){
        return MarketItem.getListBoughtFood(idFood);
    }
}
