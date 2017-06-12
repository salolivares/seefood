package edu.ucsb.cs.cs190i.aviato.seefood;

import com.google.firebase.database.IgnoreExtraProperties;

import java.io.Serializable;
import java.util.List;

/*
 * Created by sal on 6/3/17.
 */

@IgnoreExtraProperties
class RecipeItem implements Serializable {
    public String name;
    public String url;
    public String imageUrl;
    public List<String> healthLabels;

    public RecipeItem() {
    }

    public RecipeItem(String name, String url, String imageUrl, List<String> healthLabels) {
        this.name = name;
        this.url = url;
        this.imageUrl = imageUrl;
        this.healthLabels = healthLabels;
    }
}
