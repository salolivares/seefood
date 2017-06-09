package edu.ucsb.cs.cs190i.aviato.seefood;

import com.google.firebase.database.IgnoreExtraProperties;

import java.io.Serializable;
import java.util.List;

/**
 * Created by sal on 6/3/17.
 */

@IgnoreExtraProperties
public class FoodItem implements Serializable {
    public String key;
    public String foodName;
    public String photoUrl;
    public boolean isFavorite;
    public List<RecipeItem> recipeItems;

    public FoodItem() {
    }

    public FoodItem(String key, String foodName, String photoUrl, List<RecipeItem> recipeItems) {
        this.key = key;
        this.foodName = foodName;
        this.photoUrl = photoUrl;
        this.recipeItems = recipeItems;
        isFavorite = false;
    }
}
