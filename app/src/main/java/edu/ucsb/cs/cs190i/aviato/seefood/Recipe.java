package edu.ucsb.cs.cs190i.aviato.seefood;

import com.google.firebase.database.IgnoreExtraProperties;

/*
 * Created by sal on 6/3/17.
 */

@IgnoreExtraProperties
class Recipe {
    public String name;
    public String url;

    public Recipe() {
    }

    public Recipe(String name, String url) {
        this.name = name;
        this.url = url;
    }
}
