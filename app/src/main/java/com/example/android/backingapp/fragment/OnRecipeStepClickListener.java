package com.example.android.backingapp.fragment;

import android.view.View;

import com.example.android.backingapp.api.model.Ingredient;
import com.example.android.backingapp.api.model.Step;

import java.util.ArrayList;
import java.util.List;

public interface OnRecipeStepClickListener {

    void onRecipeStepSelected(Step step, int position, List<View> itemViewList);

    void onRecipeIngredientsSelected(ArrayList<Ingredient> ingredients, int position, List<View> itemViewList);
}
