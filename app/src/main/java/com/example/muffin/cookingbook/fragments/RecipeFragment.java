package com.example.muffin.cookingbook.fragments;

import android.graphics.Bitmap;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.muffin.cookingbook.R;
import com.example.muffin.cookingbook.models.RealmString;
import com.example.muffin.cookingbook.models.Recipe;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Transformation;

import io.realm.Realm;
import io.realm.RealmList;

/**
 * A placeholder fragment containing a simple view.
 */
public class RecipeFragment extends Fragment {

    private Recipe mRecipe;

    private ImageView mRecipeImage;
    private TextView mTitleText;
    private TextView mCookTimeText;
    private TextView mStepsTextView;
    private TextView mIngredientsView;
    private CheckBox mFavoriteChkBox;
    private Realm mRealm;

    public RecipeFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_recipe, container, false);
        mTitleText = (TextView) v.findViewById(R.id.title_textView);

        mRecipeImage = (ImageView) v.findViewById(R.id.recipe_activity_image);

        mStepsTextView = (TextView) v.findViewById(R.id.steps_textView);

        mIngredientsView = (TextView) v.findViewById(R.id.ingredients_textView);

        mCookTimeText = (TextView) v.findViewById(R.id.recipe_cooking_time);

        mFavoriteChkBox = (CheckBox) v.findViewById(R.id.recipe_favorite_chkBx);
        mFavoriteChkBox.setOnCheckedChangeListener((buttonView, isChecked) -> {
            mRealm.beginTransaction();
            mRecipe.setFavorite(isChecked);
            mRealm.commitTransaction();
        });
        return v;
    }

    public void setRecipeId(int recipeId) {
        mRealm = Realm.getDefaultInstance();
        mRecipe = mRealm.where(Recipe.class)
                .equalTo("mId",recipeId)
                .findFirst();

        if(mRecipe == null) return;

        if(getActivity().getActionBar() != null) getActivity().getActionBar()
                                                              .setTitle(mRecipe.getTitle());

        mTitleText.setText(mRecipe.getTitle());
        int width = getResources().getConfiguration().screenWidthDp;
        int height = getResources().getConfiguration().screenHeightDp / 3;

        Transformation transformation = new Transformation() {

            @Override
            public Bitmap transform(Bitmap source) {
                int targetWidth = getResources().getConfiguration().screenWidthDp;

                double aspectRatio = (double) source.getHeight() / (double) source.getWidth();
                int targetHeight = (int) (targetWidth * aspectRatio);
                Bitmap result = Bitmap.createScaledBitmap(source, targetWidth, targetHeight, false);
                if (result != source) {
                    // Same bitmap is returned if sizes are the same
                    source.recycle();
                }
                return result;
            }

            @Override
            public String key() {
                return "transformation" + " desiredWidth";
            }
        };


        if(!mRecipe.getImageFilePath().isEmpty()) Picasso.with(getContext())
                                                        .load(mRecipe.getImageFilePath())
                                                        .resize(width,height)
                                                        .centerInside()
                                                        .into(mRecipeImage);

        mCookTimeText.setText(String.valueOf(mRecipe.getCookingTime()));
        mFavoriteChkBox.setChecked(mRecipe.isFavorite());
        mIngredientsView.setText(getIngredientsString(mRecipe.getIngredients()));
        mStepsTextView.setText(getStepsString(mRecipe.getSteps()));
    }

    private String getStepsString(RealmList<RealmString> steps) {
        StringBuilder builder = new StringBuilder("");
        int i = 1;
        for(RealmString str : steps){
            builder.append(i).append(".").append(str.getVal()).append("\n");
            i++;
        }

        return builder.toString();
    }

    private String getIngredientsString(RealmList<RealmString> ingredients) {
        StringBuilder builder = new StringBuilder("");
        for(RealmString str : ingredients) {
            builder.append(str.getVal()).append("\n");
        }
        return builder.toString();
    }

}
