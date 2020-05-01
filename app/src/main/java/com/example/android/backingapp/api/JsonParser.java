package com.example.android.backingapp.api;

import com.example.android.backingapp.api.model.Recipe;
import com.google.gson.Gson;

import java.util.Arrays;
import java.util.List;

public class JsonParser {

    public static final String RECIPES_BASE_URI = "https://d17h27t6h515a5.cloudfront.net/";
    public static final String RECIPES_PATH = "topher/2017/May/59121517_baking/baking.json";

    public static List<Recipe> getRecipes(String json){
        return Arrays.asList(new Gson().fromJson(json, Recipe[].class));
    }
}
