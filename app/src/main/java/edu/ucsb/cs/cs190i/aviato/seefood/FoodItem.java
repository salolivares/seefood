package edu.ucsb.cs.cs190i.aviato.seefood;

import com.google.firebase.database.IgnoreExtraProperties;

import java.util.List;

/**
 * Created by sal on 6/3/17.
 */

@IgnoreExtraProperties
public class FoodItem {
    public String foodName;
    public String photoUri;
    public List<Recipe> recipes;

    public FoodItem() {
    }

    public FoodItem(String foodName, String photoUri, List<Recipe> recipes) {
        this.foodName = foodName;
        this.photoUri = photoUri;
        this.recipes = recipes;
    }

    public String getFoodName() {
        return foodName;
    }

    public String getPhotoUri() {
        return photoUri;
    }

    public List<Recipe> getRecipes() {
        return recipes;
    }
}
