package com.example.muffin.cookingbook.activities;

import android.content.Context;
import android.content.Intent;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.muffin.cookingbook.R;
import com.example.muffin.cookingbook.models.RealmString;
import com.example.muffin.cookingbook.models.Recipe;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import io.realm.Realm;
import io.realm.RealmList;

public class AddRecipeActivity extends AppCompatActivity {
    public static final String TAG = "AddRecipeActivity";
    private static final int PICK_IMAGE_REQUEST = 0;
    private static final String EXTRA_RECIPE_ID = "com.example.muffin.cookingbook.activities.extra_rec_id";

    public static Intent newIntent(Context context) {
        return new Intent(context, AddRecipeActivity.class);
    }

    public static Intent newIntent(Context context,int recipeId){
        Intent intent = newIntent(context);
        intent.putExtra(EXTRA_RECIPE_ID,recipeId);
        return intent;
    }

    private List<EditText> mIngredientsList = new ArrayList<>();
    private List<EditText> mStepsList = new ArrayList<>();

    private LinearLayout mIngredientsLayout;
    private EditText mTitleEdit;
    private EditText mCookingTimeEdit;
    private LinearLayout mStepsLayout;
    private Spinner mSpinner;
    private Button mAddRecipeButton;
    private ImageView mRecipeImage;
    private Realm mRealm;

    private String mImagePath = "";
    private int mRecipeId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_recipe);

        mTitleEdit = (EditText) findViewById(R.id.title_EditText);
        mCookingTimeEdit = (EditText) findViewById(R.id.cooking_time_editText);

        mIngredientsLayout = (LinearLayout) findViewById(R.id.ingredients_layout);
        addEditText(mIngredientsLayout);

        mStepsLayout = (LinearLayout) findViewById(R.id.steps_layout);
        addEditText(mStepsLayout);

        mSpinner = (Spinner) findViewById(R.id.spinner);

        FloatingActionButton addIngredientFab = (FloatingActionButton) findViewById(R.id.add_ingredient_fab);
        addIngredientFab.setOnClickListener(v -> addEditText(mIngredientsLayout));

        FloatingActionButton addStepFab = (FloatingActionButton) findViewById(R.id.add_step_fab);
        addStepFab.setOnClickListener(v -> addEditText(mStepsLayout));

        mAddRecipeButton = (Button) findViewById(R.id.create_recipe_button);
        mAddRecipeButton.setOnClickListener(v -> saveRecipe());

        mRecipeImage = (ImageView) findViewById(R.id.add_recipe_image);
        mRecipeImage.setOnClickListener(v -> downloadPhoto());

        mRealm = Realm.getDefaultInstance();
        bindWithRecipe();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_IMAGE_REQUEST) {
            if (data == null) return;
            Picasso.with(this).load(data.getData()).into(mRecipeImage);
            mImagePath = data.getDataString();
        }
    }

    private void bindWithRecipe(){
        mRecipeId = getIntent().getIntExtra(EXTRA_RECIPE_ID,-1);
        if(mRecipeId == -1) return;
        Recipe recipe = mRealm.where(Recipe.class)
                              .equalTo("mId",mRecipeId)
                              .findFirst();
        mTitleEdit.setText(recipe.getTitle());
        mCookingTimeEdit.setText(String.valueOf(recipe.getCookingTime()));
        if(!recipe.getImageFilePath().isEmpty()) {
            mImagePath = recipe.getImageFilePath();
            Picasso.with(this).load(recipe.getImageFilePath())
                    .into(mRecipeImage);
        }

        int i = 0;
        for(RealmString str : recipe.getIngredients()){
            mIngredientsList.get(i).setText(str.getVal());
            addEditText(mIngredientsLayout);
            i++;
        }

        i = 0;
        for(RealmString str : recipe.getSteps()){
            mStepsList.get(i).setText(str.getVal());
            addEditText(mStepsLayout);
            i++;
        }


        mAddRecipeButton.setText(R.string.update_recipe);
    }

    /*
    private void savePhoto(String fileName, Bitmap bm) {
        OutputStream out = null;
        try {
            out = new FileOutputStream(fileName);
            bm.compress(Bitmap.CompressFormat.PNG, 100, out);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } finally {
            try {
                if (out != null) {
                    out.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }*/

    private void downloadPhoto() {
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType("image/*");
        startActivityForResult(intent, PICK_IMAGE_REQUEST);
    }

    /**Read data for creating recipe from UI and save it in database**/
    private void saveRecipe() {
        if (mTitleEdit.getText().toString().isEmpty()) {
            Toast.makeText(this, R.string.input_title, Toast.LENGTH_SHORT).show();
            return;
        }
        if (mCookingTimeEdit.getText().toString().isEmpty()) {
            Toast.makeText(this, R.string.input_cooking_time, Toast.LENGTH_SHORT).show();
            return;
        }

        String title = mTitleEdit.getText().toString();
        int time = Integer.parseInt(mCookingTimeEdit.getText().toString());
        RealmList<RealmString> ingredients = new RealmList<>();
        RealmList<RealmString> steps = new RealmList<>();

        mRealm.beginTransaction();

        for (EditText edit : mIngredientsList) {
            if (edit.getText().toString().isEmpty()) continue;
            RealmString str = new RealmString(edit.getText().toString());
            if(str.isManaged()) {
                ingredients.add(str);
            }else{
                ingredients.add(mRealm.copyToRealm(str));
            }
        }

        for (EditText edit : mStepsList) {
            if (edit.getText().toString().isEmpty()) continue;
            RealmString str = new RealmString(edit.getText().toString());
            if(str.isManaged()) {
                steps.add(str);
            }else{
                steps.add(mRealm.copyToRealm(str));
            }
        }


        try {
            int id = 0;
            Recipe recipe;
            if (mRecipeId == -1) {
                if (!mRealm.where(Recipe.class).findAll().isEmpty()) {
                    id = mRealm.where(Recipe.class).max("mId").intValue() + 1;
                }
                recipe = mRealm.createObject(Recipe.class);
                recipe.setId(id);
            } else {
                recipe = mRealm.where(Recipe.class)
                        .equalTo("mId", mRecipeId)
                        .findFirst();
            }


            //Recipe recipe = new Recipe(title, time, false, ingredients, steps);
            recipe.setTitle(title);
            recipe.setCookingTime(time);
            recipe.setFavorite(recipe.isFavorite());
            recipe.setIngredients(ingredients);
            recipe.setSteps(steps);
            recipe.setType(mSpinner.getSelectedItem().toString());
            recipe.setImageFilePath(mImagePath);
            mRealm.commitTransaction();
            //mRealm.executeTransaction(realm1 -> realm1.copyToRealm(recipe));
            Log.d(TAG, "saved");
        }catch (Exception e){
            Log.d(TAG,Log.getStackTraceString(e));
        }
    }

    private void addEditText(LinearLayout layout) {
        EditText editText = new EditText(this);

        LinearLayout.LayoutParams lParams = new LinearLayout
                .LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        lParams.topMargin = 10;
        editText.setLayoutParams(lParams);
        if (layout.getId() == R.id.ingredients_layout) {
            editText.setHint(R.string.ingridients_hint);
            mIngredientsList.add(editText);
        } else {
            editText.setHint(getString(R.string.steps_hint, mStepsList.size() + 1));
            mStepsList.add(editText);
        }
        layout.addView(editText);
    }

}
