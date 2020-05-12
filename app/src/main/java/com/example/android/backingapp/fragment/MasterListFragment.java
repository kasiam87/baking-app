package com.example.android.backingapp.fragment;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.android.backingapp.R;
import com.example.android.backingapp.adapter.StepAdapterOnClickHandler;
import com.example.android.backingapp.adapter.StepsAdapter;
import com.example.android.backingapp.api.model.Ingredient;
import com.example.android.backingapp.api.model.Recipe;
import com.example.android.backingapp.api.model.Step;

import java.util.ArrayList;
import java.util.List;

public class MasterListFragment extends Fragment implements StepAdapterOnClickHandler {

    private static final String STEPS_BUNDLE_KEY = "StepsBundleKey";
    private static final String INGREDIENTS_BUNDLE_KEY = "IngredientsBundleKey";

    private ArrayList<Ingredient> ingredients = new ArrayList<>();
    private ArrayList<Step> steps = new ArrayList<>();
    private Recipe recipe;

    private OnRecipeStepClickListener callback;

    private StepsAdapter stepsAdapter;

    public MasterListFragment() {
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);

        try {
            callback = (OnRecipeStepClickListener) context;
        } catch (ClassCastException e) {
            throw new ClassCastException(context.toString() + " should implement OnRecipeStepClickListener");
        }
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        if (savedInstanceState != null) {
            steps = savedInstanceState.getParcelableArrayList(STEPS_BUNDLE_KEY);
            ingredients = savedInstanceState.getParcelableArrayList(INGREDIENTS_BUNDLE_KEY);
        } else {
            if (recipe != null) {
                steps.addAll(recipe.getSteps());
                ingredients.addAll(recipe.getIngredients());
            }
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.activity_steps, container, false);

        RecyclerView stepsRecyclerView = rootView.findViewById(R.id.master_list_recycler_view);
        stepsRecyclerView.setHasFixedSize(true);
        stepsRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        stepsAdapter = new StepsAdapter(this);
        stepsAdapter.setSteps(steps);
        stepsAdapter.setIngredients(ingredients);
        stepsRecyclerView.setAdapter(stepsAdapter);
        return rootView;
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putParcelableArrayList(STEPS_BUNDLE_KEY, stepsAdapter.getSteps());
        outState.putParcelableArrayList(INGREDIENTS_BUNDLE_KEY, stepsAdapter.getIngredients());
    }

    @Override
    public void onStepSelected(Step step, int position, List<View> itemViewList) {
        callback.onRecipeStepSelected(step, position, itemViewList);
    }

    @Override
    public void onIngredientsSelected(ArrayList<Ingredient> ingredients, int position, List<View> itemViewList) {
        callback.onRecipeIngredientsSelected(ingredients, position, itemViewList);
    }

    public void setRecipe(Recipe recipe){
        this.recipe = recipe;
    }
}
