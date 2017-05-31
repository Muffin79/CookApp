package com.example.muffin.cookingbook.activities;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;

import com.example.muffin.cookingbook.R;
import com.example.muffin.cookingbook.adapters.CategoriesAdapter;
import com.example.muffin.cookingbook.models.CategoryItem;
import com.example.muffin.cookingbook.models.Recipe;

public class MainActivity extends AppCompatActivity {


    private RecyclerView mRecyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        mRecyclerView = (RecyclerView) findViewById(R.id.categories_recycler);
        mRecyclerView.setLayoutManager(new GridLayoutManager(this,2));

        CategoriesAdapter adapter = new CategoriesAdapter(CategoryItem.getCategories(this));

        mRecyclerView.setAdapter(adapter);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(view -> startActivity(AddRecipeActivity.newIntent(MainActivity.this)));

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        switch (item.getItemId()){
            case R.id.action_search:
                startActivity(SearchActivity.newIntent(this));
                break;
            case R.id.action_favorite:
                startActivity(RecipeListActivity.newIntent(this,true));
                break;
        }

        return super.onOptionsItemSelected(item);
    }

}
