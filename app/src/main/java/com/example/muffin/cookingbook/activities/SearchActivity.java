package com.example.muffin.cookingbook.activities;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.IdRes;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.example.muffin.cookingbook.R;
import com.example.muffin.cookingbook.fragments.RecipeListFragment;

public class SearchActivity extends AppCompatActivity {

    public static Intent newIntent(Context context){
        return new Intent(context,SearchActivity.class);
    }

    private EditText mSearchEdit;
    private RadioGroup mRadioGroup;
    private RecipeListFragment mRecipeListFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        mRadioGroup = (RadioGroup) findViewById(R.id.radio_group);
        mRadioGroup.setOnCheckedChangeListener((group, checkedId) -> {
            switch (checkedId){
                case R.id.radioBtn_time:
                    mSearchEdit.setInputType(InputType.TYPE_CLASS_NUMBER);
                    break;
                case R.id.radioBtn_title:
                    mSearchEdit.setInputType(InputType.TYPE_CLASS_TEXT);
                    break;
            }
        });

        mRecipeListFragment = (RecipeListFragment)getSupportFragmentManager()
                                                    .findFragmentById(R.id.fragment_search_list);

        mSearchEdit = (EditText) findViewById(R.id.search_edit_text);
        mSearchEdit.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                switch (mRadioGroup.getCheckedRadioButtonId()){
                    case R.id.radioBtn_title:
                        mRecipeListFragment.findByTitle(s.toString());
                        break;
                    case R.id.radioBtn_time:
                        try {
                            int time = Integer.parseInt(s.toString());
                            mRecipeListFragment.findByTime(time);
                        }catch(Exception e){
                            Toast.makeText(SearchActivity.this, R.string.must_input_numbers,
                                    Toast.LENGTH_SHORT).show();
                        }

                        break;
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });


    }
}
