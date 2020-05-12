package com.example.android.backingapp.adapter;

import android.view.View;

import com.example.android.backingapp.api.model.Ingredient;
import com.example.android.backingapp.api.model.Step;

import java.util.ArrayList;
import java.util.List;

public interface StepAdapterOnClickHandler {

    void onStepSelected(Step step, int position, List<View> itemViewList);

    void onIngredientsSelected(ArrayList<Ingredient> ingredients, int position, List<View> itemViewList);
}
