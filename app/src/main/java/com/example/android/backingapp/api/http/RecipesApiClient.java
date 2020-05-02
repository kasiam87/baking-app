package com.example.android.backingapp.api.http;

import android.util.Log;

import com.example.android.backingapp.api.model.Recipe;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.IOException;
import java.util.List;

import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RecipesApiClient {

    private static final String TAG = RecipesApiClient.class.getSimpleName();

    private static final String RECIPES_BASE_URI = "https://d17h27t6h515a5.cloudfront.net/";
    private static final String RECIPES_PATH = "topher/2017/May/59121517_baking/baking.json";

    public List<Recipe> getRecipes() throws IOException {
        RecipesCalls recipesCalls = getRetrofitClient(RECIPES_BASE_URI)
                .create(RecipesCalls.class);

        Call<List<Recipe>> call = recipesCalls.getRecipes(RECIPES_PATH);
        Log.d(TAG, "Performing api call " + call.request().toString());

        return call.execute().body();
    }

    private Retrofit getRetrofitClient(String uri) {
        Gson gson = new GsonBuilder()
                .setLenient()
                .create();

        return new Retrofit.Builder()
                .baseUrl(uri)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build();
    }
}
