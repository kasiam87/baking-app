package com.example.android.backingapp.fragment;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.content.Context;
import android.os.Bundle;
import android.os.Parcelable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.TextView;

import com.example.android.backingapp.MainActivity;
import com.example.android.backingapp.R;
import com.example.android.backingapp.StepsActivity;
import com.example.android.backingapp.api.model.Recipe;
import com.example.android.backingapp.api.model.Step;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.stream.Collectors;

public class MasterListFragment extends Fragment {

    public MasterListFragment() {
    }

    // Define a new interface OnStepClickListener that triggers a callback in the host activity
    OnStepClickListener callback;
    Recipe recipe;
    String ingredients;
    ArrayList<String> stepDescriptions = new ArrayList<>();

    // OnRecipeClickListener interface, calls a method in the host activity named onStepSelected
    public interface OnStepClickListener {
        void onStepSelected(int position);
    }

    // Override onAttach to make sure that the container activity has implemented the callback
    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        // This makes sure that the host activity has implemented the callback interface
        // If not, it throws an exception
        try {
            callback = (OnStepClickListener) context;
        } catch (ClassCastException e) {
            throw new ClassCastException(context.toString()
                    + " must implement OnStepClickListener");
        }
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        Bundle bundle = getArguments();
        if (bundle != null) {
            Log.d("masterListFragemnt", "BUNDLE###");
            recipe = bundle.getParcelable(StepsActivity.RECIPE_BUNDLE_KEY);
            Log.d("masterListFragemnt", "recipe name: " + recipe.getName());

            stepDescriptions.add("Ingredients");
            for (Step step: recipe.getSteps()){
            stepDescriptions.add(step.getShortDescription());
        }
        }
    }

    // Inflates the List of steps
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        final View rootView = inflater.inflate(R.layout.fragment_master_list, container, false);

        // Get a reference to the text view in the fragment_master_list xml layout file
        GridView gridView = (GridView) rootView.findViewById(R.id.step);

        // Create the adapter
        // This adapter takes in the context and an ArrayList of ALL the image resources to display

        MasterListAdapter mAdapter = new MasterListAdapter(getContext(), stepDescriptions);

        // Set the adapter on the GridView
        gridView.setAdapter(mAdapter);

        // Set a click listener on the gridView and trigger the callback onImageSelected when an item is clicked
        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                // Trigger the callback method and pass in the position that was clicked
                callback.onStepSelected(position);
            }
        });

        // Return the root view
        return rootView;
    }
}
