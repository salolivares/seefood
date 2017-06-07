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

public class RecipeAdapter extends ArrayAdapter<RecipeItem> {
    private Context context;
    private List<RecipeItem> recipeItems;

    public RecipeAdapter(Context context, List<RecipeItem> recipeItems) {
        super(context, 0, recipeItems);
        this.context = context;
        this.recipeItems = recipeItems;
    }


    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        RecipeItem recipeItem = getItem(position);
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.food_item, parent, false);
        }

        TextView textView = (TextView) convertView.findViewById(R.id.food_name);
        ImageView imageView = (ImageView) convertView.findViewById(R.id.food_imageview);


        textView.setText(recipeItem.name);
        Picasso.with(context)
                .load(recipeItem.imageUrl)
                .resize(150, 150)
                .centerCrop().into(imageView);

        return convertView;
    }

    public RecipeItem getItem(int position){
        return recipeItems.get(position);
    }


}
