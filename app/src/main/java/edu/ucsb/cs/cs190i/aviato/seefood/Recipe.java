package edu.ucsb.cs.cs190i.aviato.seefood;

import com.google.firebase.database.IgnoreExtraProperties;

import java.io.Serializable;

/*
 * Created by sal on 6/3/17.
 */

@IgnoreExtraProperties
class Recipe implements Serializable {
    public String name;
    public String url;
    public String imageUrl;

    public Recipe() {
    }

    public Recipe(String name, String url, String imageUrl) {
        this.name = name;
        this.url = url;
        this.imageUrl = imageUrl;
    }
}
