package edu.ucsb.cs.cs190i.aviato.seefood;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.List;

/*
 * Created by sal on 6/4/17.
 */

public class RecipeAdapter extends ArrayAdapter<Recipe> {
    private Context context;
    private List<Recipe> recipes;

    public RecipeAdapter(Context context, List<Recipe> recipes) {
        super(context, 0, recipes);
        this.context = context;
        this.recipes = recipes;
    }


    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Recipe recipe = getItem(position);
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.food_item, parent, false);
        }

        TextView textView = (TextView) convertView.findViewById(R.id.food_name);
        ImageView imageView = (ImageView) convertView.findViewById(R.id.food_imageview);


        textView.setText(recipe.name);
        Picasso.with(context)
                .load(recipe.imageUrl)
                .resize(150, 150)
                .centerCrop().into(imageView);

        return convertView;
    }

    public Recipe getItem(int position){
        return recipes.get(position);
    }


}
