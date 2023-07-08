package org.example.controllers;

import org.example.entity.Category;
import org.example.entity.User;

import java.util.List;

public class AdminController extends BaseController {
    public List<User> getAllUser(){
        return User.getListUsers();
    }

    public User addUser(String username,String password,int role){
        return User.addUser(username,password,role);
    }

    public void deleteUser(int id){
        User.deleteUser(id);
    }

    public void updateUser(User user){
        User.updateUser(user);
    }

    //--------------Category-------------
    public List<Category> getAllCategory() {
        return Category.getListCategories();
    }

    public Category createCategory(String name){
        return Category.createCategory(name);
    }

    public void deleteCategory(int id){
        Category.deleteCategory(id);
    }

    public void updateCategory(Category category){
        Category.updateCategory(category);
    }
}
