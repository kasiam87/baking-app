package com.example.android.backingapp;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.loader.app.LoaderManager;
import androidx.loader.content.AsyncTaskLoader;
import androidx.loader.content.Loader;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.android.backingapp.adapter.RecipesAdapter;
import com.example.android.backingapp.api.JsonParser;
import com.example.android.backingapp.api.NetworkHelper;
import com.example.android.backingapp.api.model.Recipe;
import com.google.gson.Gson;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import static android.content.res.Configuration.ORIENTATION_PORTRAIT;

public class MainActivity extends AppCompatActivity implements RecipesAdapter.RecipeAdapterOnClickHandler,
        LoaderManager.LoaderCallbacks<List<Recipe>> {

    private static final String TAG = MainActivity.class.getSimpleName();
    private static final int COLUMNS_NUMBER_PORTRAIT = 1;
    private static final int COLUMNS_NUMBER_LANDSCAPE = 3;

    private static final int RECIPES_LOADER_ID = 1;
    private static final String LOADER_PATH_KEY = "loader_path";

    public static final String RECIPE_OBJECT = "RecipeObject";

    private RecyclerView recipesRecyclerView;
    private RecipesAdapter recipesAdapter;

    private TextView errorMsg;
    private ProgressBar loadingIndicator;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        recipesRecyclerView = findViewById(R.id.recipes_recycler_view);

        errorMsg = findViewById(R.id.error_msg);
        loadingIndicator = findViewById(R.id.loading_indicator);

        Configuration config = this.getResources().getConfiguration();
        int columns = config.orientation == ORIENTATION_PORTRAIT ? COLUMNS_NUMBER_PORTRAIT : COLUMNS_NUMBER_LANDSCAPE;

        GridLayoutManager layoutManager = new GridLayoutManager(this, columns);
        recipesRecyclerView.setLayoutManager(layoutManager);
        recipesRecyclerView.setHasFixedSize(true);
        recipesAdapter = new RecipesAdapter(this);


        loadRecipes();

//        if (savedInstanceState == null) {
//            loadRecipes();
//        } else {
////            loadingIndicator.setVisibility(View.INVISIBLE);
////            ArrayList<Result> movies = savedInstanceState.getParcelableArrayList(MOVIE_ADAPTER_BUNDLE_KEY);
////            movieAdapter.setMovies(movies);
////            showMoviesOrError(movies, savedInstanceState.getString(ERROR_BUNDLE_KEY));
//        }
        recipesRecyclerView.setAdapter(recipesAdapter);
    }

    @Override
    protected void onSaveInstanceState(Bundle bundle) {
        super.onSaveInstanceState(bundle);
//        bundle.putParcelableArrayList(MOVIE_ADAPTER_BUNDLE_KEY, movieAdapter.getMovies());
//        bundle.putString(ERROR_BUNDLE_KEY, errorMsg.getText().toString());
    }

    private void loadRecipes() {
        Bundle queryBundle = new Bundle();

        LoaderManager loaderManager = getSupportLoaderManager();
        Loader<List<Recipe>> recipesLoader = loaderManager.getLoader(RECIPES_LOADER_ID);
        if (recipesLoader == null) {
            loaderManager.initLoader(RECIPES_LOADER_ID, queryBundle, this).forceLoad();
        } else {
            loaderManager.restartLoader(RECIPES_LOADER_ID, queryBundle, this).forceLoad();
        }
    }

    @Override
    public Loader<List<Recipe>> onCreateLoader(int id, final Bundle args) {
        return new AsyncTaskLoader<List<Recipe>>(this) {

            @Override
            protected void onStartLoading() {
                if (args == null) {
                    return;
                }
                loadingIndicator.setVisibility(View.VISIBLE);
            }

            @Override
            public List<Recipe> loadInBackground() {

                URL recipesUrl = NetworkHelper.buildUrl();

                Log.d(TAG, "Perform api call!\n" + recipesUrl.toString());
                try {
                    String recipesJson = NetworkHelper.getResponseFromHttpUrl(recipesUrl);
                    return JsonParser.getRecipes(recipesJson);
                } catch (Exception e) {
                    e.printStackTrace();
                    return null;
                }
            }
        };
    }

    @Override
    public void onLoadFinished(@NonNull Loader<List<Recipe>> loader, List<Recipe> recipes) {
        loadingIndicator.setVisibility(View.INVISIBLE);
        recipesAdapter.setRecipes(recipes);
        if (recipes != null && !recipes.isEmpty()) {
            recipesRecyclerView.setVisibility(View.VISIBLE);
            errorMsg.setVisibility(View.INVISIBLE);
        } else {
            Log.d(TAG, "No recipe available!");
            recipesRecyclerView.setVisibility(View.INVISIBLE);
            errorMsg.setText("No recipe available");
            errorMsg.setVisibility(View.VISIBLE);
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
