package com.example.android.backingapp.fragment;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.android.backingapp.R;

public class StepDetailsFragment extends Fragment {

    private String stepDetail;

    public StepDetailsFragment() {
    }

    public void setStepDetails(String stepDetail) {
        this.stepDetail = stepDetail;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_step_details, container, false);

        final TextView textView = rootView.findViewById(R.id.step_detail_item);

        textView.setText(stepDetail);

        return rootView;
    }
}
