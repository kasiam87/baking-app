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
import com.example.android.backingapp.StepsActivity;
import com.example.android.backingapp.adapter.StepAdapterOnClickHandler;
import com.example.android.backingapp.adapter.StepsAdapter;
import com.example.android.backingapp.api.model.Recipe;
import com.example.android.backingapp.api.model.Step;

import java.util.ArrayList;

public class MasterListFragment extends Fragment implements StepAdapterOnClickHandler {

    private static final String STEPS_BUNDLE_KEY = "StepsBundleKey";

    String ingredients;
    private ArrayList<Step> steps = new ArrayList<>();

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
            throw new ClassCastException(context.toString() + " should implement OnStepClickListener");
        }
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        if (savedInstanceState != null){
            steps = savedInstanceState.getParcelableArrayList(STEPS_BUNDLE_KEY);
        } else {
            if (getArguments() != null) {
                Recipe recipe = getArguments().getParcelable(StepsActivity.RECIPE_BUNDLE_KEY);
//            stepDescriptions.add("Ingredients");
                if (recipe != null) {
                    steps.addAll(recipe.getSteps());
                }
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
        stepsRecyclerView.setAdapter(stepsAdapter);
        return rootView;
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putParcelableArrayList(STEPS_BUNDLE_KEY, stepsAdapter.getSteps());
    }

    @Override
    public void onStepSelected(Step step) {
        callback.onStepSelected(step);
    }
}
