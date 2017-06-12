package edu.ucsb.cs.cs190i.aviato.seefood;

import android.content.Context;
import android.support.annotation.NonNull;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Pattern;

import edu.ucsb.cs.cs190i.aviato.seefood.json.Recipe;

/*
 * Created by sal on 6/4/17.
 */

public class RecipeAdapter extends ArrayAdapter<RecipeItem> implements Filterable {
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

    @Override
    public int getCount() {
        return recipeItems != null ? recipeItems.size() : 0;
    }

    @NonNull
    @Override
    public Filter getFilter() {
        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence constraint) {
                FilterResults results = new FilterResults();
                ArrayList<RecipeItem> filteredItems = new ArrayList<>();

                String[] filtersArray = constraint.toString().split(Pattern.quote("|"));
                List<String> filters = Arrays.asList(filtersArray);

                for(RecipeItem recipeItem : recipeItems) {
                    if(recipeItem.healthLabels!= null) {
                        boolean isSubset = recipeItem.healthLabels.containsAll(filters);
                        if (isSubset) {
                            filteredItems.add(recipeItem);
                        }
                    }
                    else
                        Log.d("uh oh", "1");
                }

                results.count = filteredItems.size();
                results.values = filteredItems;
                return results;
            }

            @SuppressWarnings("unchecked")
            @Override
            protected void publishResults(CharSequence constraint, FilterResults results) {
                recipeItems = (List<RecipeItem>) results.values;
                notifyDataSetChanged();
            }
        };
    }

    public void setRecipes(List<RecipeItem> recipeItems) {
        this.recipeItems = recipeItems;
    }
}
