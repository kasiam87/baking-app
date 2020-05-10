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

import timber.log.Timber;

public class MasterListFragment extends Fragment implements StepAdapterOnClickHandler {

    Recipe recipe;
    String ingredients;
    ArrayList<Step> steps = new ArrayList<>();

    OnStepClickListener callback;

    private RecyclerView stepsRecyclerView;
    private StepsAdapter stepsAdapter;

    public MasterListFragment() {
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);

        try {
            callback = (OnStepClickListener) context;
        } catch (ClassCastException e) {
            throw new ClassCastException(context.toString() + " should implement OnStepClickListener");
        }

    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        Bundle bundle = getArguments();
        if (bundle != null) {
            recipe = bundle.getParcelable(StepsActivity.RECIPE_BUNDLE_KEY);
            Timber.d("recipe name: %s", recipe.getName());

//            stepDescriptions.add("Ingredients");
            steps.addAll(recipe.getSteps());
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.activity_steps, container, false);

        stepsRecyclerView = rootView.findViewById(R.id.master_list_recycler_view);
        stepsRecyclerView.setHasFixedSize(true);
        stepsRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        stepsAdapter = new StepsAdapter(this);
        stepsAdapter.setSteps(steps);
        stepsRecyclerView.setAdapter(stepsAdapter);
//        stepsRecyclerView.setOnClickListener((adapterView, view, position, l) -> callback.onStepSelected(position));
        return rootView;
    }

    @Override
    public void onStepSelected(Step step) {
        callback.onStepSelected(step);
    }

    public interface OnStepClickListener {
        void onStepSelected(Step step);
    }
}
