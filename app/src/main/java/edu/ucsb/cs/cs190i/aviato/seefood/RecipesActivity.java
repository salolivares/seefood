package edu.ucsb.cs.cs190i.aviato.seefood;

import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.webkit.WebView;
import android.widget.AdapterView;
import android.widget.ListView;

public class RecipesActivity extends AppCompatActivity {
    private ListView listView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipes);

        Intent intent = getIntent();
        FoodItem fooditem = (FoodItem) intent.getSerializableExtra("food object");

        this.setTitle(fooditem.foodName + " Recipes");

        final RecipeAdapter recipeAdapter = new RecipeAdapter(this, fooditem.recipes);
        listView = (ListView) findViewById(R.id.recipe_lv);
        listView.setAdapter(recipeAdapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener(){
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Recipe item = recipeAdapter.getItem(position);

                Intent intent = new Intent(RecipesActivity.this, RecipeWebViewActivity.class);
                intent.putExtra("recipe url", item.url);
                startActivity(intent);

            }
        });
    }


}
