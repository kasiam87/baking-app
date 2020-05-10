package com.example.android.backingapp.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.android.backingapp.R;
import com.example.android.backingapp.api.model.Recipe;

import java.util.ArrayList;
import java.util.List;

public class RecipesAdapter extends RecyclerView.Adapter<RecipesAdapter.RecipeViewHolder> {

    private List<Recipe> recipes;

    @NonNull
    @Override
    public RecipeViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        Context context = viewGroup.getContext();

        View view = LayoutInflater.from(context)
                .inflate(R.layout.recipe_grid_item, viewGroup, false);
        return new RecipeViewHolder(view);
    }

    @Override
    public void onBindViewHolder(RecipeViewHolder viewHolder, int position) {
        Recipe recipe = recipes.get(position);
        viewHolder.recipeCard.setText(recipe.getName());
    }

    @Override
    public int getItemCount() {
        return (recipes == null) ? 0 : recipes.size();
    }

    class RecipeViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        TextView recipeCard;

        RecipeViewHolder(View view){
            super(view);
            recipeCard = view.findViewById(R.id.recipe);
            view.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            int adapterPosition = getAdapterPosition();
            Recipe recipe = recipes.get(adapterPosition);
            recipeClickHandler.onClick(recipe);
        }
    }

    private final RecipeAdapterOnClickHandler recipeClickHandler;

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
