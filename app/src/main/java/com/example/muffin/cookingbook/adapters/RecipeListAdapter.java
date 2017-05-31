package com.example.muffin.cookingbook.adapters;


import android.content.Context;
import android.graphics.Region;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.muffin.cookingbook.R;
import com.example.muffin.cookingbook.activities.AddRecipeActivity;
import com.example.muffin.cookingbook.activities.RecipeActivity;
import com.example.muffin.cookingbook.models.Recipe;
import com.squareup.picasso.Picasso;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.List;

import io.realm.Realm;

public class RecipeListAdapter extends RecyclerView.Adapter<RecipeListAdapter.RecipeHolder> {

    private List<Recipe> mRecipes = new ArrayList<>();

    public RecipeListAdapter(List<Recipe> recipes) {
        mRecipes = recipes;
    }

    @Override
    public RecipeHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.recipe_list_item, parent, false);
        return new RecipeHolder(v, parent.getContext());
    }

    @Override
    public void onBindViewHolder(RecipeHolder holder, int position) {
        holder.bindViewHolder(mRecipes.get(position), position);
    }

    @Override
    public int getItemCount() {
        return mRecipes.size();
    }


    class RecipeHolder extends RecyclerView.ViewHolder {

        private ImageView mDeleteImg;
        private ImageView mRecipeImage;
        private TextView mRecipeTitle;
        private TextView mCookTimeTv;
        private CheckBox mFavoriteChkBx;
        private Context mContext;
        private int mRecipeId;

        RecipeHolder(View itemView, Context context) {
            super(itemView);
            itemView.setOnClickListener(v -> context.startActivity(RecipeActivity.
                    newIntent(context, mRecipeId)));

            mRecipeImage = (ImageView) itemView.findViewById(R.id.recipe_imageView);
            mRecipeTitle = (TextView) itemView.findViewById(R.id.recipe_title);
            mCookTimeTv = (TextView) itemView.findViewById(R.id.cooking_time_textView);
            mContext = context;
            mFavoriteChkBx = (CheckBox) itemView.findViewById(R.id.favorite_chkBx);
            mDeleteImg = (ImageView) itemView.findViewById(R.id.delete_image_view);

        }

        private void bindViewHolder(Recipe recipe, int position) {
            mRecipeId = recipe.getId();
            mDeleteImg.setOnClickListener(v -> {
                Realm realm = Realm.getDefaultInstance();
                realm.beginTransaction();
                realm.where(Recipe.class)
                        .equalTo("mId", recipe.getId())
                        .findAll()
                        .deleteAllFromRealm();
                realm.commitTransaction();
                mRecipes.remove(recipe);
                RecipeListAdapter.this.notifyItemRemoved(position);
            });
            mFavoriteChkBx.setOnCheckedChangeListener((buttonView, isChecked) -> {
                Realm realm = Realm.getDefaultInstance();
                Recipe rec = realm.where(Recipe.class)
                        .equalTo("mId", recipe.getId())
                        .findFirst();
                realm.beginTransaction();
                rec.setFavorite(isChecked);
                realm.commitTransaction();
            });
            mRecipeTitle.setText(recipe.getTitle());
            mCookTimeTv.setText(mContext.getString(R.string.cooking_time, recipe.getCookingTime()));
            mFavoriteChkBx.setChecked(recipe.isFavorite());
            if (!recipe.getImageFilePath().isEmpty()) {
                Log.d("AddRecipeActivity", recipe.getImageFilePath());
                Picasso.with(mContext)
                        .load(recipe.getImageFilePath())
                        .placeholder(R.drawable.ic_photo_camera_black)
                        .into(mRecipeImage);
            }


        }
    }
}
