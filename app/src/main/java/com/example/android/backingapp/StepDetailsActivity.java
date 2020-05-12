package com.example.android.backingapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.example.android.backingapp.api.model.Ingredient;
import com.example.android.backingapp.api.model.Step;
import com.example.android.backingapp.databinding.ActivityStepDetailsBinding;
import com.example.android.backingapp.display.TextFormatter;
import com.example.android.backingapp.fragment.StepDetailsFragment;

import java.util.ArrayList;
import java.util.List;

import timber.log.Timber;

public class StepDetailsActivity extends AppCompatActivity {

    public static final String STEP_BUNDLE_SAVE_KEY = "StepBundleSaveKey";
    public static final String STEP_VIDEO_BUNDLE_SAVE_KEY = "StepVideoBundleSaveKey";
    public static final String STEP_INSTRUCTIONS_BUNDLE_SAVE_KEY = "StepInstructionsBundleSaveKey";
    public static final String INGREDIENTS_BUNDLE_SAVE_KEY = "IngredientsBundleSaveKey";

    public static final String SERVINGS_BUNDLE_SAVE_KEY = "ServingsBundleSaveKey";

    private Step step;
    private String videoURL;
    private String description;
    private ArrayList<Ingredient> ingredients;
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

                if (intent.hasExtra(StepsActivity.SERVINGS_BUNDLE_KEY)) {
                    servings = intent.getIntExtra(StepsActivity.SERVINGS_BUNDLE_KEY, 0);
                }

            }
        } else {
            Timber.d(">>Load saved");
            step = savedInstanceState.getParcelable(STEP_BUNDLE_SAVE_KEY);
            videoURL = savedInstanceState.getString(STEP_VIDEO_BUNDLE_SAVE_KEY);
            description = savedInstanceState.getString(STEP_INSTRUCTIONS_BUNDLE_SAVE_KEY);
            ingredients = savedInstanceState.getParcelableArrayList(INGREDIENTS_BUNDLE_SAVE_KEY);
            servings = savedInstanceState.getInt(SERVINGS_BUNDLE_SAVE_KEY);
        }

        if (step != null) {
            showStepDetails(step);
        }
        if (ingredients != null && !ingredients.isEmpty()) {
            showIngredients(ingredients);
        }
    }

    @Override
    protected void onSaveInstanceState(@NonNull Bundle bundle) {
        super.onSaveInstanceState(bundle);
        bundle.putParcelable(STEP_BUNDLE_SAVE_KEY, step);
        bundle.putString(STEP_VIDEO_BUNDLE_SAVE_KEY, videoURL);
        bundle.putString(STEP_INSTRUCTIONS_BUNDLE_SAVE_KEY, description);
        bundle.putParcelableArrayList(INGREDIENTS_BUNDLE_SAVE_KEY, ingredients);
        bundle.putInt(SERVINGS_BUNDLE_SAVE_KEY, servings);
    }

    private void showIngredients(List<Ingredient> ingredients) {
        findViewById(R.id.video_player).setVisibility(View.GONE);
        findViewById(R.id.recipe_instructions).setVisibility(View.GONE);
        findViewById(R.id.recipe_ingredients).setVisibility(View.VISIBLE);

        StepDetailsFragment ingredientsFragment = new StepDetailsFragment();
        ingredientsFragment.setStepDetails(TextFormatter.formatIngredients(ingredients, servings));
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.recipe_ingredients, ingredientsFragment)
                .commit();
    }

    public void showStepDetails(Step step) {
        findViewById(R.id.recipe_ingredients).setVisibility(View.GONE);

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
