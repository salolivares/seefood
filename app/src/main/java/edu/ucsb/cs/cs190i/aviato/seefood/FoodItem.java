package edu.ucsb.cs.cs190i.aviato.seefood;

import com.google.firebase.database.IgnoreExtraProperties;

import java.io.Serializable;
import java.util.List;

/**
 * Created by sal on 6/3/17.
 */

@IgnoreExtraProperties
public class FoodItem implements Serializable {
    public String foodName;
    public String photoUrl;
    public List<RecipeItem> recipeItems;

    public FoodItem() {
    }

    public FoodItem(String foodName, String photoUrl, List<RecipeItem> recipeItems) {
        this.foodName = foodName;
        this.photoUrl = photoUrl;
        this.recipeItems = recipeItems;
    }

    public String getFoodName() {
        return foodName;
    }

    public String getPhotoUrl() {
        return photoUrl;
    }

    public List<RecipeItem> getRecipeItems() {
        return recipeItems;
    }
}
