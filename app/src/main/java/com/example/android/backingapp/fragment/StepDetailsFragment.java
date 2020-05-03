package com.example.android.backingapp.fragment;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.android.backingapp.R;

import java.util.ArrayList;
import java.util.List;

public class StepDetailsFragment extends Fragment {

    // Final Strings to store state information about the list of images and list index

    public static final String STEPS_ID_LIST = "steps_ids";
    public static final String LIST_INDEX = "list_index";

    // Tag for logging
    private static final String TAG = "StepDetailsFragment";

    // Variables to store a list of image resources and the index of the image that this fragment displays
    private List<Integer> stepIds;
    private int listIndex;

    /**
     * Mandatory empty constructor for the fragment manager to instantiate the fragment
     */
    public StepDetailsFragment() {
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        // Load the saved state (the list of images and list index) if there is one
        if(savedInstanceState != null) {
            stepIds = savedInstanceState.getIntegerArrayList(STEPS_ID_LIST);
            listIndex = savedInstanceState.getInt(LIST_INDEX);
        }

        // Inflate the Android-Me fragment layout
        View rootView = inflater.inflate(R.layout.fragment_step_details, container, false);

        // Get a reference to the ImageView in the fragment layout
        final TextView textView = (TextView) rootView.findViewById(R.id.step_instructions);

        // If a list of image ids exists, set the image resource to the correct item in that list
        // Otherwise, create a Log statement that indicates that the list was not found
        if(stepIds != null){
            // Set the image resource to the list item at the stored index
            textView.setText(stepIds.get(listIndex));

            // Set a click listener on the image view
            textView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    // Increment position as long as the index remains <= the size of the image ids list
                    if(listIndex < stepIds.size()-1) {
                        listIndex++;
                    } else {
                        // The end of list has been reached, so return to beginning index
                        listIndex = 0;
                    }
                    // Set the image resource to the new list item
                    textView.setText(stepIds.get(listIndex));
                }
            });

        } else {
            Log.v(TAG, "This fragment has a null list of image id's");
        }

        // Return the rootView
        return rootView;
    }

    // Setter methods for keeping track of the list images this fragment can display and which image
    // in the list is currently being displayed

    public void setStepIds(List<Integer> stepIds) {
        this.stepIds = stepIds;
    }

    public void setListIndex(int listIndex) {
        this.listIndex = listIndex;
    }

    /**
     * Save the current state of this fragment
     */
    @Override
    public void onSaveInstanceState(Bundle currentState) {
        currentState.putIntegerArrayList(STEPS_ID_LIST, (ArrayList<Integer>) stepIds);
        currentState.putInt(LIST_INDEX, listIndex);
    }
}
