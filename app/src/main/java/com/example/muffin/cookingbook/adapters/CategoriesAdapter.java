package com.example.muffin.cookingbook.adapters;


import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.muffin.cookingbook.R;
import com.example.muffin.cookingbook.activities.RecipeListActivity;
import com.example.muffin.cookingbook.models.CategoryItem;
import com.example.muffin.cookingbook.models.Recipe;

import java.util.List;


public class CategoriesAdapter extends RecyclerView.Adapter<CategoriesAdapter.CategoryItemHolder>{

    List<CategoryItem> mItems;

    public CategoriesAdapter(List<CategoryItem> items) {
        mItems = items;
    }

    @Override
    public CategoryItemHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v =  LayoutInflater.from(parent.getContext())
                                  .inflate(R.layout.categories_item,parent,false);
        return new CategoryItemHolder(v,parent.getContext());
    }

    @Override
    public void onBindViewHolder(CategoryItemHolder holder, int position) {
        holder.bindHolder(mItems.get(position));
    }

    @Override
    public int getItemCount() {
        return mItems.size();
    }


    class CategoryItemHolder extends RecyclerView.ViewHolder{
        private ImageView categoryImage;
        private String mCategory;
        private TextView categoryTitle;

        CategoryItemHolder(View itemView, Context context) {
            super(itemView);
            categoryImage = (ImageView) itemView.findViewById(R.id.category_imageView);
            categoryTitle = (TextView) itemView.findViewById(R.id.category_title);
            itemView.setOnClickListener(v -> context
                            .startActivity(RecipeListActivity.newIntent(context,mCategory)));
        }

        void bindHolder(CategoryItem item){
            categoryImage.setImageResource(item.getImageResId());
            categoryTitle.setText(item.getTitle());
            mCategory = item.getTitle();
        }
    }
}
