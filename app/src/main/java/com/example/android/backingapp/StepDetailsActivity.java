package com.example.android.backingapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;

import android.content.Intent;
import android.os.Bundle;

import com.example.android.backingapp.api.model.Ingredient;
import com.example.android.backingapp.databinding.ActivityStepDetailsBinding;
import com.example.android.backingapp.fragment.StepDetailsFragment;

import java.util.List;

import timber.log.Timber;

public class StepDetailsActivity extends AppCompatActivity {

    // TODO save and restore state

    private List<Ingredient> ingredients;
    private int servings;

    private String videoURL;
    private String instructions;

    ActivityStepDetailsBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityStepDetailsBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        Intent intent = getIntent();
        if (intent != null) {
            Timber.d("Intent not null");

            if (intent.hasExtra(StepsActivity.STEP_VIDEO_BUNDLE_KEY)) {
                videoURL = intent.getStringExtra(StepsActivity.STEP_VIDEO_BUNDLE_KEY);
            }

            if (intent.hasExtra(StepsActivity.STEP_INSTRUCTIONS_BUNDLE_KEY)) {
                instructions = intent.getStringExtra(StepsActivity.STEP_INSTRUCTIONS_BUNDLE_KEY);
            }

            if (intent.hasExtra(StepsActivity.INGREDIENTS_BUNDLE_KEY)) {
                ingredients = intent.getParcelableArrayListExtra(StepsActivity.INGREDIENTS_BUNDLE_KEY);
            }

            if (intent.hasExtra(StepsActivity.INGREDIENTS_BUNDLE_KEY)) {
                servings = intent.getIntExtra(StepsActivity.SERVINGS_BUNDLE_KEY, 0);
            }

        }

        FragmentManager fragmentManager = getSupportFragmentManager();

        StepDetailsFragment videoFragment = new StepDetailsFragment();
        videoFragment.setStepDetails(videoURL);
        fragmentManager.beginTransaction()
                .replace(R.id.video_player, videoFragment)
                .commit();

        StepDetailsFragment instructionsFragment = new StepDetailsFragment();
        instructionsFragment.setStepDetails(instructions);
        fragmentManager.beginTransaction()
                .replace(R.id.recipe_instructions, instructionsFragment)
                .commit();

    }
}
