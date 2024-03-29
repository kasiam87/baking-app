package com.example.android.backingapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.loader.app.LoaderManager;
import androidx.loader.content.Loader;
import androidx.recyclerview.widget.GridLayoutManager;

import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.view.View;

import com.example.android.backingapp.adapter.RecipeAdapterOnClickHandler;
import com.example.android.backingapp.adapter.RecipesAdapter;
import com.example.android.backingapp.api.model.Recipe;
import com.example.android.backingapp.databinding.ActivityMainBinding;
import com.example.android.backingapp.display.TextFormatter;
import com.example.android.backingapp.loader.RecipesAsyncLoader;
import com.example.android.backingapp.widget.IngredientsWidgetIntentService;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;

import timber.log.Timber;

import static android.content.res.Configuration.ORIENTATION_PORTRAIT;

public class MainActivity extends AppCompatActivity implements RecipeAdapterOnClickHandler,
        LoaderManager.LoaderCallbacks<List<Recipe>> {

    private static final int COLUMNS_NUMBER_PORTRAIT = 1;
    private static final int COLUMNS_NUMBER_LANDSCAPE = 3;

    private static final String RECIPES_BUNDLE_KEY = "RecipesBundleKey";
    public static final String RECIPE_JSON = "RecipeJson";

    private static final int RECIPES_LOADER_ID = 1;

    private RecipesAdapter recipesAdapter;

    private ActivityMainBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (BuildConfig.DEBUG) {
            Timber.plant(new Timber.DebugTree());
        }

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
        ArrayList<Recipe> recipes = savedInstanceState.getParcelableArrayList(RECIPES_BUNDLE_KEY);
        showResult(recipes);
    }

    private void showResult(List<Recipe> recipes) {
        if (recipes != null && !recipes.isEmpty()) {
            Timber.d("Show recipes");
            recipesAdapter.setRecipes(recipes);
            binding.recipesRecyclerView.setVisibility(View.VISIBLE);
            binding.errorMsg.setVisibility(View.INVISIBLE);
        } else {
            Timber.d("No recipe available!");
            binding.recipesRecyclerView.setVisibility(View.INVISIBLE);
            binding.errorMsg.setVisibility(View.VISIBLE);
        }
    }

    @Override
    protected void onSaveInstanceState(@NonNull Bundle bundle) {
        super.onSaveInstanceState(bundle);
        bundle.putParcelableArrayList(RECIPES_BUNDLE_KEY, recipesAdapter.getRecipes());
    }

    @NonNull
    @Override
    public Loader<List<Recipe>> onCreateLoader(int id, final Bundle args) {
        return new RecipesAsyncLoader(this);
    }

    @Override
    public void onLoadFinished(@NonNull Loader<List<Recipe>> loader, List<Recipe> recipes) {
        showResult(recipes);
    }

    @Override
    public void onLoaderReset(@NonNull Loader<List<Recipe>> loader) {
    }

    @Override
    public void onClick(Recipe recipe) {
        Intent recipeIntent = new Intent(this, StepsActivity.class);
        recipeIntent.putExtra(RECIPE_JSON, new Gson().toJson(recipe));
        IngredientsWidgetIntentService.startBakingService(this,
                TextFormatter.formatIngredients(recipe.getIngredients(), recipe.getServings()));
        startActivity(recipeIntent);
    }
}