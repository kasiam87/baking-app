package com.example.android.backingapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.example.android.backingapp.api.model.Ingredient;
import com.example.android.backingapp.api.model.Step;
import com.example.android.backingapp.databinding.ActivityStepDetailsBinding;
import com.example.android.backingapp.fragment.StepDetailsFragment;

import java.util.List;

import timber.log.Timber;

public class StepDetailsActivity extends AppCompatActivity {

    public static final String STEP_VIDEO_BUNDLE_KEY = "StepVideoBundleKey";
    public static final String STEP_INSTRUCTIONS_BUNDLE_KEY = "StepInstructionsBundleKey";

    public static final String SERVINGS_BUNDLE_KEY = "ServingsBundleKey";

    private Step step;
    private String videoURL;
    private String description;
    private List<Ingredient> ingredients;
    private int servings;

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

                if (intent.hasExtra(StepsActivity.STEP_BUNDLE_KEY)){
                    step = intent.getParcelableExtra(StepsActivity.STEP_BUNDLE_KEY);
                    if (step != null) {
                        videoURL = step.getVideoURL();
                        description = step.getDescription();
                    }
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
            description = savedInstanceState.getString(STEP_INSTRUCTIONS_BUNDLE_KEY);
            servings = savedInstanceState.getInt(SERVINGS_BUNDLE_KEY);
        }

        showStepDetails(step);
    }

    @Override
    protected void onSaveInstanceState(@NonNull Bundle bundle) {
        super.onSaveInstanceState(bundle);
        bundle.putString(STEP_VIDEO_BUNDLE_KEY, videoURL);
        bundle.putString(STEP_INSTRUCTIONS_BUNDLE_KEY, description);
        bundle.putInt(SERVINGS_BUNDLE_KEY, servings);
    }

    public void showStepDetails(Step step) {
        StepDetailsFragment videoFragment = new StepDetailsFragment();
        if (step.getVideoURL() != null && !step.getVideoURL().isEmpty()) {
            findViewById(R.id.video_player).setVisibility(View.VISIBLE);
            videoFragment.setStepDetails(videoURL);
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.video_player, videoFragment)
                    .commit();
        } else {
            findViewById(R.id.video_player).setVisibility(View.GONE);
        }

        if (!step.getDescription().equals(step.getShortDescription())){
            findViewById(R.id.recipe_instructions).setVisibility(View.VISIBLE);
            StepDetailsFragment instructionsFragment = new StepDetailsFragment();
            instructionsFragment.setStepDetails(step.getDescription());
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.recipe_instructions, instructionsFragment)
                    .commit();
        } else {
            findViewById(R.id.recipe_instructions).setVisibility(View.GONE);
        }

    }
}
