package com.example.muffin.cookingbook.activities;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;

import com.example.muffin.cookingbook.R;
import com.example.muffin.cookingbook.fragments.RecipeListFragment;

public class RecipeListActivity extends AppCompatActivity {

    public static final String EXTRA_CATEGORY = "com.example.muffin.cookingbook.extra_category";
    public static final String EXTRA_FAVORITE = "com.example.muffin.cookingbook.extra_favorite";

    public static Intent newIntent(Context context){
        return new Intent(context,RecipeListActivity.class);
    }

    public static Intent newIntent(Context context,String category){
        Intent intent = new Intent(context,RecipeListActivity.class);
        intent.putExtra(EXTRA_CATEGORY,category);
        return  intent;
    }

    public static Intent newIntent(Context context,boolean isFavorite){
        Intent intent = new Intent(context,RecipeListActivity.class);
        intent.putExtra(EXTRA_FAVORITE,isFavorite);
        return  intent;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipe_list);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(view -> startActivity(AddRecipeActivity.newIntent(this)));


        RecipeListFragment fragment = (RecipeListFragment) getSupportFragmentManager()
                .findFragmentById(R.id.fragment);

        String category = getIntent().getStringExtra(EXTRA_CATEGORY);
        ActionBar bar = getSupportActionBar();
        if(category != null && !category.isEmpty()) {
            fragment.setCategory(category);
            bar.setTitle(category);
        }

        if(getIntent().getBooleanExtra(EXTRA_FAVORITE,false)){
            fragment.findFavorite();
        }

        bar.setDisplayHomeAsUpEnabled(true);
    }

}
