package com.example.android.backingapp.fragment;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.os.Bundle;
import android.support.v4.media.session.PlaybackStateCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.android.backingapp.R;

public class StepDetailsFragment extends Fragment {

    private static final String STEP_DETAIL_BUNDLE_KEY = "StepDetailBundleKey";
    private String stepDetail;

    public StepDetailsFragment() {
    }

    public void setStepDetails(String stepDetail) {
        this.stepDetail = stepDetail;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        if (savedInstanceState != null){
            setStepDetails(savedInstanceState.getParcelable(STEP_DETAIL_BUNDLE_KEY));
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_step_details, container, false);
        TextView textView = rootView.findViewById(R.id.step_detail_item);
        if (stepDetail != null) {
            textView.setText(stepDetail);
        } else {
            textView.setVisibility(View.GONE);
        }
        return rootView;
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putString(STEP_DETAIL_BUNDLE_KEY, stepDetail);
    }
}