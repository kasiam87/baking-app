package com.example.android.backingapp.loader;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.loader.content.AsyncTaskLoader;

import com.example.android.backingapp.api.http.RecipesApiClient;
import com.example.android.backingapp.api.model.Recipe;

import java.util.List;

public class RecipesAsyncLoader extends AsyncTaskLoader<List<Recipe>> {

    public RecipesAsyncLoader(@NonNull Context context) {
        super(context);
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
