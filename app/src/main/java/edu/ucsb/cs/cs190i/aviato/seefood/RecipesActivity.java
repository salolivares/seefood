package edu.ucsb.cs.cs190i.aviato.seefood;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.google.firebase.auth.FirebaseAuth;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

public class RecipesActivity extends AppCompatActivity {
    private ListView listView;
    private List<String> filters;
    private RecipeAdapter recipeAdapter;
    private FoodItem fooditem;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipes);

        Intent intent = getIntent();
        fooditem = (FoodItem) intent.getSerializableExtra("food_object");

        this.setTitle(fooditem.foodName + " Recipes");

        recipeAdapter = new RecipeAdapter(this, fooditem.recipeItems);
        listView = (ListView) findViewById(R.id.recipe_lv);
        listView.setAdapter(recipeAdapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener(){
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                RecipeItem item = recipeAdapter.getItem(position);

                Intent intent = new Intent(RecipesActivity.this, RecipeWebViewActivity.class);
                intent.putExtra("recipe_url", item.url);
                startActivity(intent);

            }
        });

        filters = new ArrayList<>();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.recipes_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch(item.getItemId()) {
            case R.id.rm_alcohol:
            case R.id.rm_dairy:
            case R.id.rm_gluten:
            case R.id.rm_peanut:
            case R.id.rm_vegan:
            case R.id.rm_vegetarian:
                if (item.isChecked()) {
                    item.setChecked(false);
                    filters.remove(item.getTitle().toString());
                }
                else {
                    filters.add(item.getTitle().toString());
                    item.setChecked(true);
                }

                String filter="";
                for(String f: filters)
                    filter+=(f+"|");

                recipeAdapter.setRecipes(fooditem.recipeItems);
                recipeAdapter.getFilter().filter(filter);

                return true;
            default:
                return super.onOptionsItemSelected(item);
        }

    }


}
