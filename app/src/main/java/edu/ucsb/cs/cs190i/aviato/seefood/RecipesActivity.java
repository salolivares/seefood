package edu.ucsb.cs.cs190i.aviato.seefood;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class RecipesActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipes);

        Intent intent = getIntent();
        FoodItem fooditem = (FoodItem) intent.getSerializableExtra("food object");

        System.out.println(fooditem);
    }
}
