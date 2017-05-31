package com.example.muffin.cookingbook.models;


import android.content.Context;

import com.example.muffin.cookingbook.R;

import java.util.ArrayList;
import java.util.List;

public class CategoryItem {

    private String mTitle;

    private int mImageResId;

    public CategoryItem(String title, int imageResId) {
        mTitle = title;
        mImageResId = imageResId;
    }

    public String getTitle() {
        return mTitle;
    }

    public int getImageResId() {
        return mImageResId;
    }

    public static List<CategoryItem> getCategories(Context context){
        List<CategoryItem> categories = new ArrayList<>();
        String []titles = context.getResources().getStringArray(R.array.categories);

        categories.add(new CategoryItem(titles[0],R.drawable.ic_steak));
        categories.add(new CategoryItem(titles[1],R.drawable.ic_soup));
        categories.add(new CategoryItem(titles[2],R.drawable.ic_restaurant));
        categories.add(new CategoryItem(titles[3],R.drawable.ic_salad));
        categories.add(new CategoryItem(titles[4],R.drawable.ic_fried_egg));
        categories.add(new CategoryItem(titles[5],R.drawable.ic_ketchup));
        categories.add(new CategoryItem(titles[6],R.drawable.ic_ice_cream));
        categories.add(new CategoryItem(titles[7],R.drawable.ic_glass));

        return categories;
    }
}
