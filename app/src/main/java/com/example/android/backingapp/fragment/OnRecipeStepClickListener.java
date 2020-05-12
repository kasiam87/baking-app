package com.example.android.backingapp.fragment;

import com.example.android.backingapp.api.model.Ingredient;
import com.example.android.backingapp.api.model.Step;

import java.util.ArrayList;

public interface OnRecipeStepClickListener {

    void onRecipeStepSelected(Step step);

    void onRecipeIngredientsSelected(ArrayList<Ingredient> ingredients);
}
