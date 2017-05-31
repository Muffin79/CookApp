package com.example.muffin.cookingbook.fragments;

import android.nfc.Tag;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.muffin.cookingbook.R;
import com.example.muffin.cookingbook.adapters.RecipeListAdapter;
import com.example.muffin.cookingbook.models.Recipe;

import java.util.ArrayList;
import java.util.List;

import io.realm.Realm;
import io.realm.RealmQuery;
import io.realm.RealmResults;

/**
 * A placeholder fragment containing a simple view.
 */
public class RecipeListFragment extends Fragment {

    private final String TAG = "RecipeListFragment";

    public RecipeListFragment() {
    }

    private RecyclerView mRecipesRecycler;
    private RecipeListAdapter mAdapter;
    private List<Recipe> mRecipes = new ArrayList<>();
    private String mCategory;
    private Realm mRealm;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_recipe_list, container, false);
        mRecipesRecycler = (RecyclerView) v.findViewById(R.id.recipes_recycler);
        mRealm = Realm.getDefaultInstance();

        mAdapter = new RecipeListAdapter(mRecipes);

        mRecipesRecycler.setLayoutManager(new LinearLayoutManager(getContext()));
        mRecipesRecycler.setAdapter(mAdapter);
        return v;
    }

    public void setCategory(String category){
        RealmResults<Recipe> results = mRealm.where(Recipe.class).equalTo("mType",category).findAll();
        Log.d(TAG,"Results count : " + results.size());
        mRecipes.addAll(mRealm.copyFromRealm(results));
        mAdapter.notifyDataSetChanged();
    }

    public void findByTitle(String title){
        if(title == null || title.isEmpty()) return;
        RealmResults<Recipe> results = mRealm.where(Recipe.class).contains("mTitle",title).findAll();
        mRecipes.clear();
        mRecipes.addAll(mRealm.copyFromRealm(results));
        mAdapter.notifyDataSetChanged();
    }

    public void findByTime(int time){
        RealmResults<Recipe> results = mRealm.where(Recipe.class).equalTo("mCookingTime",time).findAll();
        mRecipes.clear();
        mRecipes.addAll(mRealm.copyFromRealm(results));
        mAdapter.notifyDataSetChanged();
    }

    public void findFavorite() {
        RealmResults<Recipe> results = mRealm.where(Recipe.class).equalTo("isFavorite",true).findAll();
        mRecipes.clear();
        mRecipes.addAll(mRealm.copyFromRealm(results));
        mAdapter.notifyDataSetChanged();
    }
}
