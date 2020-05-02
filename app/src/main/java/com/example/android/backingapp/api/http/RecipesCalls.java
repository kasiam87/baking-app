package com.example.android.backingapp.api.http;

import com.example.android.backingapp.api.model.Recipe;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;

public interface RecipesCalls {

    @GET("{path}")
    Call<List<Recipe>> getRecipes(@Path("path") String path);

}
