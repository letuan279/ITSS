package org.example.controllers;

import org.example.entity.Recipe;

import java.util.List;

public class RecipeController extends BaseController {

    public List<Recipe> getRecipesOfMember(int idMember){
        return Recipe.getListRecipes(idMember);
    }
    public Recipe createRecipe(String name, String desc, int idUser){
        return Recipe.addRecipe(name, desc, idUser);
    }

    public void updateRecipe(Recipe recipe){
        Recipe.updateRecipe(recipe);
    }

    public void deleteRecipe(int id){
        Recipe.deleteRecipe(id);
    }
}
