package com.example.android.backingapp.fragment;

import com.example.android.backingapp.api.model.Step;

public interface OnRecipeStepClickListener {
    void onRecipeStepSelected(Step step);
}