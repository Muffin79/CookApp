package com.example.muffin.cookingbook.fragments;

import android.graphics.Bitmap;
import android.os.CountDownTimer;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.muffin.cookingbook.R;
import com.example.muffin.cookingbook.models.RealmString;
import com.example.muffin.cookingbook.models.Recipe;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Transformation;

import java.util.concurrent.TimeUnit;

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
    private TextView mTimerTextView;
    private CheckBox mFavoriteChkBox;
    private Button mStartTimerButton;
    private Button mStopTimerButton;

    private CountDownTimer mTimer;
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
        mStartTimerButton = (Button) v.findViewById(R.id.timer_start_button);
        mStopTimerButton = (Button) v.findViewById(R.id.timer_stop_button);
        mTimerTextView = (TextView) v.findViewById(R.id.timer_textView);

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

        if(!mRecipe.getImageFilePath().isEmpty()) Picasso.with(getContext())
                                                        .load(mRecipe.getImageFilePath())
                                                        .resize(width,height)
                                                        .centerInside()
                                                        .into(mRecipeImage);

        long cookTimeMillis = TimeUnit.MINUTES.toMillis(mRecipe.getCookingTime());
        mTimerTextView.setText(formatMillisToStr(cookTimeMillis));

        mStartTimerButton.setOnClickListener(v -> {
            if(mTimer != null) mTimer.cancel();
            mTimer = new CountDownTimer(cookTimeMillis,1000) {
                @Override
                public void onTick(long millisUntilFinished) {
                    mTimerTextView.setText(formatMillisToStr(millisUntilFinished));
                }

                @Override
                public void onFinish() {

                }
            };
            mTimer.start();
            mStartTimerButton.setText(R.string.restart);
        });

        mStopTimerButton.setOnClickListener(v -> {
            if(mTimer != null){
                mTimer.cancel();
                mStartTimerButton.setText(R.string.start);
                mTimerTextView.setText(formatMillisToStr(cookTimeMillis));
            }
        });

        mCookTimeText.setText(String.valueOf(mRecipe.getCookingTime()));
        mFavoriteChkBox.setChecked(mRecipe.isFavorite());
        mIngredientsView.setText(getIngredientsString(mRecipe.getIngredients()));
        mStepsTextView.setText(getStepsString(mRecipe.getSteps()));
    }

    private String formatMillisToStr(long millis){
       return String.format("%02d : %02d",
                TimeUnit.MILLISECONDS.toMinutes(millis),
                TimeUnit.MILLISECONDS.toSeconds(millis) -
                        TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(millis))
       );
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
