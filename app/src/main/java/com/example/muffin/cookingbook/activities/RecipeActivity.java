package com.example.muffin.cookingbook.activities;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.muffin.cookingbook.R;
import com.example.muffin.cookingbook.fragments.RecipeFragment;
import com.example.muffin.cookingbook.models.Recipe;

import io.realm.Realm;

public class RecipeActivity extends AppCompatActivity {

    private final static String EXTRA_RECIPE_ID = "com.example.muffin.cookingbook.extra_recipe_id";

    public static Intent newIntent(Context context, int recipeId){
        Intent intent = new Intent(context,RecipeActivity.class);
        intent.putExtra(EXTRA_RECIPE_ID,recipeId);
        return intent;
    }

    private int mRecipeId;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipe);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        mRecipeId = getIntent().getIntExtra(EXTRA_RECIPE_ID,0);

        RecipeFragment fragment = (RecipeFragment) getSupportFragmentManager()
                                                    .findFragmentById(R.id.fragment_recipe);

        fragment.setRecipeId(mRecipeId);
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(view -> startActivity(AddRecipeActivity
                                                    .newIntent(RecipeActivity.this,mRecipeId)));
    }


}
