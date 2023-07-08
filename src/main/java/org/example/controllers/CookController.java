package org.example.controllers;

import org.example.entity.Cook;

import java.sql.Date;
import java.util.List;

public class CookController extends BaseController{
    public List<Cook> getCooksOfMember(int idMember) {
        return Cook.getListCooks(idMember);
    }

    public void updateCook(Cook newCook){
        Cook.updateCook(newCook);
    }

    public Cook addCook(String foodName, int quantity, Date date, String timeToCook,int idUser){
        return Cook.addCook(foodName,quantity,date,timeToCook,idUser);
    }

    public void deleteCook(int id){
        Cook.deleteCook(id);
    }
}
