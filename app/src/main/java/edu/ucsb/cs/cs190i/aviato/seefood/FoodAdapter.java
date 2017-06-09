package edu.ucsb.cs.cs190i.aviato.seefood;

import android.content.Context;
import android.graphics.Color;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import edu.ucsb.cs.cs190i.aviato.seefood.json.P;
import edu.ucsb.cs.cs190i.aviato.seefood.recycleViewHelpers.ItemTouchHelperAdapter;
import edu.ucsb.cs.cs190i.aviato.seefood.recycleViewHelpers.ItemTouchHelperViewHolder;

import static edu.ucsb.cs.cs190i.aviato.seefood.MainActivity.FIREBASE_DB_KEY;

/**
 * Created by sal on 6/4/17.
 */

public class FoodAdapter extends FirebaseRecyclerAdapter<FoodAdapter.ViewHolder, FoodItem> implements ItemTouchHelperAdapter{


    public static RecyclerViewClickListener mItemListener;



    public static class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener, ItemTouchHelperViewHolder {

        TextView foodName;
        ImageView foodImage;

        public ViewHolder(View view) {
            super(view);

            foodName = (TextView) view.findViewById(R.id.food_name);
            foodImage = (ImageView) view.findViewById(R.id.food_imageview);

            view.setOnClickListener(this);

        }

        @Override
        public void onClick(View v)
        {
            Log.d("ImageAdapter","Position hit" + this.getLayoutPosition());
            mItemListener.recyclerViewListClicked(v, this.getLayoutPosition(), 0);
        }

        @Override
        public void onItemSelected() {
            itemView.setBackgroundColor(Color.LTGRAY);
        }

        @Override
        public void onItemClear() {
            itemView.setBackgroundColor(0);
        }
    }

    private Context context;


    public FoodAdapter(Context context, Query query, @Nullable ArrayList<FoodItem> items, @Nullable ArrayList<String> keys, RecyclerViewClickListener itemListener) {
        super(query, items, keys);
        this.context = context;
        mItemListener = itemListener;
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
        Picasso.with(context)
                .load(item.photoUrl)
                .resize(150, 150)
                .centerCrop().into(holder.foodImage);

    }

    @Override
    public void onItemSwipedLeft(int position) {
        FoodItem foodItem = getItem(position);
        deleteFoodItem(foodItem.key);
    }

    @Override
    public void onItemSwipedRight(int position) {
        FoodItem foodItem = getItem(position);
        if(foodItem.isFavorite) {
            favoriteFoodItem(foodItem.key, false);
        } else {
            favoriteFoodItem(foodItem.key, true);
        }
    }

    private void favoriteFoodItem(String key, boolean b) {
        DatabaseReference database = FirebaseDatabase.getInstance().getReference();
        DatabaseReference foodRef = database.child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child(FIREBASE_DB_KEY).child(key).child("isFavorite");
        foodRef.setValue(b);
    }

    private void deleteFoodItem(String key) {
        DatabaseReference database = FirebaseDatabase.getInstance().getReference();
        DatabaseReference foodRef = database.child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child(FIREBASE_DB_KEY).child(key);
        foodRef.removeValue();
    }

    @Override protected void itemAdded(FoodItem item, String key, int position) {
        Log.d("FoodAdapter", "Added a new item to the adapter.");
    }

    @Override protected void itemChanged(FoodItem oldItem, FoodItem newItem, String key, int position) {
        Log.d("FoodAdapter", "Changed an item.");
    }

    @Override protected void itemRemoved(FoodItem item, String key, int position) {
        Log.d("FoodAdapter", "Removed an item from the adapter.");
    }

    @Override protected void itemMoved(FoodItem item, String key, int oldPosition, int newPosition) {
        Log.d("FoodAdapter", "Moved an item.");
    }
}
