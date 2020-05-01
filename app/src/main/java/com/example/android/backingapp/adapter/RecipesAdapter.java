package com.example.android.backingapp.adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.example.android.backingapp.R;
import com.example.android.backingapp.api.model.Recipe;

import java.util.ArrayList;
import java.util.List;

public class RecipesAdapter extends RecyclerView.Adapter<RecipesAdapter.RecipeViewHolder> {

    private List<Recipe> recipes;

    @Override
    public RecipeViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        Context context = viewGroup.getContext();
        int layoutIdForListItem = R.layout.recipe_grid_item;
        LayoutInflater inflater = LayoutInflater.from(context);
        boolean shouldAttachToParentImmediately = false;

        View view = inflater.inflate(layoutIdForListItem, viewGroup, shouldAttachToParentImmediately);
        return new RecipeViewHolder(view);
    }

    @Override
    public void onBindViewHolder(RecipeViewHolder viewHolder, int position) {
        Recipe recipe = recipes.get(position);

        viewHolder.recipeCard.setText(recipe.getName());
    }

    @Override
    public int getItemCount() {
        if (recipes == null) {
            return 0;
        }
        return recipes.size();
    }

    class RecipeViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        public final TextView recipeCard;

        public RecipeViewHolder(View view){
            super(view);
            recipeCard = view.findViewById(R.id.recipe);
            view.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            int adapterPosition = getAdapterPosition();
            Recipe recipe = recipes.get(adapterPosition);
            recipeClickHandler.onClick(recipe);
        }
    }

    private final RecipeAdapterOnClickHandler recipeClickHandler;

    public interface RecipeAdapterOnClickHandler {
        void onClick(Recipe movie);
    }

    public RecipesAdapter(RecipeAdapterOnClickHandler clickHandler) {
        recipeClickHandler = clickHandler;
    }

    public void setRecipes(List<Recipe> recipes) {
        if (recipes == null) {
            this.recipes = new ArrayList<>();
        } else {
            this.recipes = recipes;
        }

        notifyDataSetChanged();
    }

    public ArrayList<Recipe> getRecipes() {
        if (recipes == null){
            return new ArrayList<>();
        }
        return new ArrayList<>(recipes);
    }
}
