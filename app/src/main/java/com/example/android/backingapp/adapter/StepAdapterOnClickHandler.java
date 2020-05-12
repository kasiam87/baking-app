package com.example.android.backingapp.adapter;

import com.example.android.backingapp.api.model.Ingredient;
import com.example.android.backingapp.api.model.Step;

import java.util.ArrayList;
import java.util.List;

public interface StepAdapterOnClickHandler {

    void onStepSelected(Step step);

    void onIngredientsSelected(ArrayList<Ingredient> ingredients);
}
