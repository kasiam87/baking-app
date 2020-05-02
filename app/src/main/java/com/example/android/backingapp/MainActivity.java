package com.example.android.backingapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.loader.app.LoaderManager;
import androidx.loader.content.Loader;
import androidx.recyclerview.widget.GridLayoutManager;

import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.example.android.backingapp.adapter.RecipesAdapter;
import com.example.android.backingapp.api.model.Recipe;
import com.example.android.backingapp.databinding.ActivityMainBinding;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;

import static android.content.res.Configuration.ORIENTATION_PORTRAIT;

public class MainActivity extends AppCompatActivity implements RecipesAdapter.RecipeAdapterOnClickHandler,
        LoaderManager.LoaderCallbacks<List<Recipe>> {

    private static final String TAG = MainActivity.class.getSimpleName();
    private static final int COLUMNS_NUMBER_PORTRAIT = 1;
    private static final int COLUMNS_NUMBER_LANDSCAPE = 3;

    private static final String RECIPE_ADAPTER_BUNDLE_KEY = "RecipeBundleKey";

    private static final int RECIPES_LOADER_ID = 1;

    public static final String RECIPE_OBJECT = "RecipeObject";

    private RecipesAdapter recipesAdapter;

    private ActivityMainBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        Configuration config = this.getResources().getConfiguration();
        int columns = config.orientation == ORIENTATION_PORTRAIT ? COLUMNS_NUMBER_PORTRAIT : COLUMNS_NUMBER_LANDSCAPE;

        GridLayoutManager layoutManager = new GridLayoutManager(this, columns);
        binding.recipesRecyclerView.setLayoutManager(layoutManager);
        binding.recipesRecyclerView.setHasFixedSize(true);
        recipesAdapter = new RecipesAdapter(this);

        if (savedInstanceState == null) {
            loadRecipesView();
        } else {
            restoreRecipesView(savedInstanceState);
        }
        binding.recipesRecyclerView.setAdapter(recipesAdapter);
    }

    private void loadRecipesView() {
        LoaderManager loaderManager = LoaderManager.getInstance(this);

        Loader<List<Recipe>> recipesLoader = loaderManager.getLoader(RECIPES_LOADER_ID);
        if (recipesLoader == null) {
            loaderManager.initLoader(RECIPES_LOADER_ID, null, this).forceLoad();
        } else {
            loaderManager.restartLoader(RECIPES_LOADER_ID, null, this).forceLoad();
        }
    }

    private void restoreRecipesView(Bundle savedInstanceState) {
        binding.loadingIndicator.setVisibility(View.INVISIBLE);

        ArrayList<Recipe> recipes = savedInstanceState.getParcelableArrayList(RECIPE_ADAPTER_BUNDLE_KEY);
        recipesAdapter.setRecipes(recipes);

        if (recipes != null && !recipes.isEmpty()) {
            recipesAdapter.setRecipes(recipes);
            binding.recipesRecyclerView.setVisibility(View.VISIBLE);
            binding.errorMsg.setVisibility(View.INVISIBLE);
        } else {
            binding.errorMsg.setVisibility(View.VISIBLE);
        }
    }

    @Override
    protected void onSaveInstanceState(@NonNull Bundle bundle) {
        super.onSaveInstanceState(bundle);
        bundle.putParcelableArrayList(RECIPE_ADAPTER_BUNDLE_KEY, recipesAdapter.getRecipes());
    }

    @NonNull
    @Override
    public Loader<List<Recipe>> onCreateLoader(int id, final Bundle args) {
        return new RecipesAsyncLoader(this, binding.loadingIndicator);
    }

    @Override
    public void onLoadFinished(@NonNull Loader<List<Recipe>> loader, List<Recipe> recipes) {
        binding.loadingIndicator.setVisibility(View.INVISIBLE);
        recipesAdapter.setRecipes(recipes);
        if (recipes != null && !recipes.isEmpty()) {
            binding.recipesRecyclerView.setVisibility(View.VISIBLE);
            binding.errorMsg.setVisibility(View.INVISIBLE);
        } else {
            Log.d(TAG, "No recipe available!");
            binding.recipesRecyclerView.setVisibility(View.INVISIBLE);
            binding.errorMsg.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void onLoaderReset(@NonNull Loader<List<Recipe>> loader) {

    }

    @Override
    public void onClick(Recipe recipe) {
        Intent recipeIntent = new Intent(this, RecipeDetailsActivity.class);
        recipeIntent.putExtra(RECIPE_OBJECT, new Gson().toJson(recipe));
        startActivity(recipeIntent);
    }
}
