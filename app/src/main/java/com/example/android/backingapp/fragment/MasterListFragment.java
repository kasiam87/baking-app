package com.example.android.backingapp.fragment;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;

import com.example.android.backingapp.R;
import com.example.android.backingapp.StepsActivity;
import com.example.android.backingapp.api.model.Recipe;
import com.example.android.backingapp.api.model.Step;

import java.util.ArrayList;

public class MasterListFragment extends Fragment {

    private static final String TAG = MasterListFragment.class.getSimpleName();

    OnStepClickListener callback;
    Recipe recipe;
    String ingredients;
    ArrayList<String> stepDescriptions = new ArrayList<>();

    public MasterListFragment() {
    }

    public interface OnStepClickListener {
        void onStepSelected(int position);
    }

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
            recipe = bundle.getParcelable(StepsActivity.RECIPE_BUNDLE_KEY);
            Log.d(TAG, "recipe name: " + recipe.getName());

//            stepDescriptions.add("Ingredients");
            for (Step step : recipe.getSteps()) {
                stepDescriptions.add(step.getShortDescription());
            }
        }
    }

    // Inflates the List of steps
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        final View rootView = inflater.inflate(R.layout.fragment_master_list, container, false);

        // Get a reference to the text view in the fragment_master_list xml layout file
        GridView gridView = (GridView) rootView.findViewById(R.id.step);

        // Create the adapter
        MasterListAdapter mAdapter = new MasterListAdapter(getContext(), stepDescriptions);

        gridView.setAdapter(mAdapter);
        gridView.setOnItemClickListener((adapterView, view, position, l) -> callback.onStepSelected(position));

        return rootView;
    }
}
