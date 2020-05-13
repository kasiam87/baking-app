package com.example.android.backingapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.view.View;

import com.example.android.backingapp.api.model.Ingredient;
import com.example.android.backingapp.api.model.Step;
import com.example.android.backingapp.databinding.ActivityStepDetailsBinding;
import com.example.android.backingapp.display.TextFormatter;
import com.example.android.backingapp.fragment.StepDetailsFragment;

import java.util.ArrayList;
import java.util.List;

public class StepDetailsActivity extends AppCompatActivity {

    public static final String STEP_BUNDLE_SAVE_KEY = "StepBundleSaveKey";
    public static final String STEP_VIDEO_BUNDLE_SAVE_KEY = "StepVideoBundleSaveKey";
    public static final String STEP_INSTRUCTIONS_BUNDLE_SAVE_KEY = "StepInstructionsBundleSaveKey";
    public static final String INGREDIENTS_BUNDLE_SAVE_KEY = "IngredientsBundleSaveKey";

    public static final String SERVINGS_BUNDLE_SAVE_KEY = "ServingsBundleSaveKey";
    public static final String RECIPE_NAME_BUNDLE_SAVE_KEY = "RecipeNameBundleSaveKey";
    public static final String POSITION_BUNDLE_SAVE_KEY = "PositionBundleSaveKey";
    public static final String RECIPE_STEPS_BUNDLE_SAVE_KEY = "RecipeStepsBundleSaveKey";

    private Step step;
    private String videoURL;
    private String description;
    private ArrayList<Ingredient> ingredients;
    private int servings;
    private String recipeName;

    private int position;
    private List<Step> recipeSteps;

    ActivityStepDetailsBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityStepDetailsBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        if (savedInstanceState == null) {
            Intent intent = getIntent();
            if (intent != null) {

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

                if (intent.hasExtra(StepsActivity.RECIPE_NAME_BUNDLE_KEY)) {
                    recipeName = intent.getStringExtra(StepsActivity.RECIPE_NAME_BUNDLE_KEY);
                }

                if (intent.hasExtra(StepsActivity.POSITION_BUNDLE_KEY)) {
                    position = intent.getIntExtra(StepsActivity.POSITION_BUNDLE_KEY, 0);
                }

                if (intent.hasExtra(StepsActivity.RECIPE_STEPS_BUNDLE_KEY)) {
                    recipeSteps = intent.getParcelableArrayListExtra(StepsActivity.RECIPE_STEPS_BUNDLE_KEY);
                }

            }
        } else {
            step = savedInstanceState.getParcelable(STEP_BUNDLE_SAVE_KEY);
            videoURL = savedInstanceState.getString(STEP_VIDEO_BUNDLE_SAVE_KEY);
            description = savedInstanceState.getString(STEP_INSTRUCTIONS_BUNDLE_SAVE_KEY);
            ingredients = savedInstanceState.getParcelableArrayList(INGREDIENTS_BUNDLE_SAVE_KEY);
            servings = savedInstanceState.getInt(SERVINGS_BUNDLE_SAVE_KEY);
            recipeName = savedInstanceState.getString(RECIPE_NAME_BUNDLE_SAVE_KEY);
            position = savedInstanceState.getInt(POSITION_BUNDLE_SAVE_KEY);
            recipeSteps = savedInstanceState.getParcelableArrayList(RECIPE_STEPS_BUNDLE_SAVE_KEY);
        }

        setTitle(recipeName);

        if (position != 0) {
            showStepDetails(step);
        } else if (ingredients != null && !ingredients.isEmpty()) {
            showIngredients(ingredients);
        }

        findViewById(R.id.button_next).setOnClickListener(getOnClickNextListener());
        findViewById(R.id.button_prev).setOnClickListener(getOnClickPrevListener());
    }

    @NonNull
    private View.OnClickListener getOnClickNextListener() {
        return view -> {
            if (position == 0 || position < recipeSteps.size()){
                position++;
                step = recipeSteps.get(position - 1);
                showStepDetails(step);
            }  else {
                position = 0;
                showIngredients(ingredients);
            }
        };
    }

    @NonNull
    private View.OnClickListener getOnClickPrevListener() {
        return view -> {
            if (position == 1){
                position--;
                showIngredients(ingredients);
            } else if (position > 1) {
                position--;
                step = recipeSteps.get(position - 1);
                showStepDetails(step);
            } else {
                position = recipeSteps.size();
                step = recipeSteps.get(position -1);
                showStepDetails(step);
            }
        };
    }

    @Override
    protected void onSaveInstanceState(@NonNull Bundle bundle) {
        super.onSaveInstanceState(bundle);
        bundle.putParcelable(STEP_BUNDLE_SAVE_KEY, step);
        bundle.putString(STEP_VIDEO_BUNDLE_SAVE_KEY, videoURL);
        bundle.putString(STEP_INSTRUCTIONS_BUNDLE_SAVE_KEY, description);
        bundle.putParcelableArrayList(INGREDIENTS_BUNDLE_SAVE_KEY, ingredients);
        bundle.putInt(SERVINGS_BUNDLE_SAVE_KEY, servings);
        bundle.putString(RECIPE_NAME_BUNDLE_SAVE_KEY, recipeName);
        bundle.putInt(POSITION_BUNDLE_SAVE_KEY, position);
        bundle.putParcelableArrayList(RECIPE_STEPS_BUNDLE_SAVE_KEY, (ArrayList<? extends Parcelable>) recipeSteps);
    }

    private void showIngredients(List<Ingredient> ingredients) {
        findViewById(R.id.video_player).setVisibility(View.GONE);
        findViewById(R.id.recipe_instructions).setVisibility(View.VISIBLE);

        StepDetailsFragment ingredientsFragment = new StepDetailsFragment();
        ingredientsFragment.setStepDetails(TextFormatter.formatIngredients(ingredients, servings));
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.recipe_instructions, ingredientsFragment)
                .commit();
    }

    public void showStepDetails(Step step) {
        StepDetailsFragment videoFragment = new StepDetailsFragment();
        if (step.getVideoURL() != null && !step.getVideoURL().isEmpty()) {
            findViewById(R.id.video_player).setVisibility(View.VISIBLE);
            videoFragment.setStepDetails(step.getVideoURL());
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
