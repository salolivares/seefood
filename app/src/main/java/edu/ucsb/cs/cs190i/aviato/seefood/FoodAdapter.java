package edu.ucsb.cs.cs190i.aviato.seefood;

import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.firebase.database.Query;

import java.util.ArrayList;

/**
 * Created by sal on 6/4/17.
 */

public class FoodAdapter extends FirebaseRecyclerAdapter<FoodAdapter.ViewHolder, FoodItem>{

    public static class ViewHolder extends RecyclerView.ViewHolder {

        TextView foodName;
        ImageView foodImage;

        public ViewHolder(View view) {
            super(view);

            foodName = (TextView) view.findViewById(R.id.food_name);
            foodImage = (ImageView) view.findViewById(R.id.food_imageview);
        }
    }

    public FoodAdapter(Query query, @Nullable ArrayList<FoodItem> items, @Nullable ArrayList<String> keys) {
        super(query, items, keys);
    }

    @Override
    public FoodAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.food_item, parent, false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(FoodAdapter.ViewHolder holder, int position) {
        FoodItem item = getItem(position);
        holder.foodName.setText(item.foodName);
        // TODO: Set image here.

    }

    @Override protected void itemAdded(FoodItem item, String key, int position) {
        Log.d("MyAdapter", "Added a new item to the adapter.");
    }

    @Override protected void itemChanged(FoodItem oldItem, FoodItem newItem, String key, int position) {
        Log.d("MyAdapter", "Changed an item.");
    }

    @Override protected void itemRemoved(FoodItem item, String key, int position) {
        Log.d("MyAdapter", "Removed an item from the adapter.");
    }

    @Override protected void itemMoved(FoodItem item, String key, int oldPosition, int newPosition) {
        Log.d("MyAdapter", "Moved an item.");
    }
}