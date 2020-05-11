package com.example.android.backingapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;

import com.example.android.backingapp.api.model.Ingredient;
import com.example.android.backingapp.databinding.ActivityStepDetailsBinding;
import com.example.android.backingapp.fragment.StepDetailsFragment;

import java.util.List;

import timber.log.Timber;

public class StepDetailsActivity extends AppCompatActivity {

    public static final String STEP_VIDEO_BUNDLE_KEY = "StepVideoBundleKey";
    public static final String STEP_INSTRUCTIONS_BUNDLE_KEY = "StepInstructionsBundleKey";

    public static final String SERVINGS_BUNDLE_KEY = "ServingsBundleKey";

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

        if (savedInstanceState == null) {
            Timber.d(">>Load new");
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
        } else {
            Timber.d(">>Load saved");
            videoURL = savedInstanceState.getString(STEP_VIDEO_BUNDLE_KEY);
            instructions = savedInstanceState.getString(STEP_INSTRUCTIONS_BUNDLE_KEY);
            servings = savedInstanceState.getInt(SERVINGS_BUNDLE_KEY);
        }

        showStepDetails();
    }

    @Override
    protected void onSaveInstanceState(@NonNull Bundle bundle) {
        super.onSaveInstanceState(bundle);
        bundle.putString(STEP_VIDEO_BUNDLE_KEY, videoURL);
        bundle.putString(STEP_INSTRUCTIONS_BUNDLE_KEY, instructions);
        bundle.putInt(SERVINGS_BUNDLE_KEY, servings);
    }

    public void showStepDetails() {
        StepDetailsFragment videoFragment = new StepDetailsFragment();
        videoFragment.setStepDetails(videoURL);
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.video_player, videoFragment)
                .commit();

        StepDetailsFragment instructionsFragment = new StepDetailsFragment();
        instructionsFragment.setStepDetails(instructions);
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.recipe_instructions, instructionsFragment)
                .commit();
    }
}
