package com.example.android.backingapp.loader;

import android.content.Context;
import android.view.View;
import android.widget.ProgressBar;

import androidx.annotation.NonNull;
import androidx.loader.content.AsyncTaskLoader;

import com.example.android.backingapp.api.http.RecipesApiClient;
import com.example.android.backingapp.api.model.Recipe;

import java.util.List;

public class RecipesAsyncLoader extends AsyncTaskLoader<List<Recipe>> {

    private ProgressBar loadingIndicator;

    public RecipesAsyncLoader(@NonNull Context context, ProgressBar loadingIndicator) {
        super(context);
        this.loadingIndicator = loadingIndicator;
    }

    @Override
    protected void onStartLoading() {
        if (loadingIndicator == null) {
            return;
        }
//        loadingIndicator.setVisibility(View.VISIBLE);
    }

    @Override
    public List<Recipe> loadInBackground() {
        try {
            return new RecipesApiClient().getRecipes();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}
