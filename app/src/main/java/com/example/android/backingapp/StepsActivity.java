package com.example.android.backingapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.view.View;

import com.example.android.backingapp.api.model.Recipe;
import com.example.android.backingapp.api.model.Step;
import com.example.android.backingapp.fragment.MasterListFragment;
import com.example.android.backingapp.fragment.OnRecipeStepClickListener;
import com.example.android.backingapp.fragment.StepDetailsFragment;
import com.google.gson.Gson;

import java.util.ArrayList;

import timber.log.Timber;

public class StepsActivity extends AppCompatActivity implements OnRecipeStepClickListener {

    public static final String RECIPE_BUNDLE_KEY = "RecipeBundleKey";
    public static final String RECIPE_BUNDLE_SAVED_KEY = "RecipeBundleSavedKey";
    public static final String STEP_BUNDLE_SAVED_KEY = "StepBundleSavedKey";

    public static final String STEP_BUNDLE_KEY = "StepBundleKey";
    public static final String STEP_VIDEO_BUNDLE_KEY = "StepVideoBundleKey";
    public static final String STEP_INSTRUCTIONS_BUNDLE_KEY = "StepInstructionsBundleKey";

    public static final String INGREDIENTS_BUNDLE_KEY = "IngredientsBundleKey";
    public static final String SERVINGS_BUNDLE_KEY = "ServingsBundleKey";

    boolean tabletDisplay;

    private Recipe recipe;
    private Step currentStep;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_steps);

        tabletDisplay = findViewById(R.id.step_details_linear_layout) != null;

        if (savedInstanceState == null) {
            Timber.d(">>Load new");

            Intent intent = getIntent();
            if (intent != null) {
                if (intent.hasExtra(MainActivity.RECIPE_JSON)) {
                    String recipeJson = intent.getStringExtra(MainActivity.RECIPE_JSON);
                    recipe = new Gson().fromJson(recipeJson, Recipe.class);
                }
            }

        } else {
            Timber.d(">>Load saved");
            recipe = savedInstanceState.getParcelable(RECIPE_BUNDLE_SAVED_KEY);
            currentStep = savedInstanceState.getParcelable(STEP_BUNDLE_SAVED_KEY);
            if (tabletDisplay && currentStep != null) {
                showStepDetails(currentStep);
            }
        }

        MasterListFragment masterListFragment = new MasterListFragment();

        getSupportFragmentManager().beginTransaction()
                .replace(android.R.id.content, masterListFragment).commit();

        Bundle bundle = new Bundle();

        bundle.putParcelable(RECIPE_BUNDLE_KEY, recipe);
        masterListFragment.setArguments(bundle);

    }

    @Override
    protected void onSaveInstanceState(@NonNull Bundle bundle) {
        super.onSaveInstanceState(bundle);
        bundle.putParcelable(RECIPE_BUNDLE_SAVED_KEY, recipe);
        bundle.putParcelable(STEP_BUNDLE_SAVED_KEY, currentStep);
    }

    @Override
    public void onRecipeStepSelected(Step step) {
        currentStep = step;
        if (tabletDisplay) {
            showStepDetails(step);
        } else {
            Bundle bundle = new Bundle();
            bundle.putParcelable(STEP_BUNDLE_KEY, step);
            bundle.putString(STEP_VIDEO_BUNDLE_KEY, step.getVideoURL());
            bundle.putString(STEP_INSTRUCTIONS_BUNDLE_KEY, step.getDescription());
            bundle.putParcelableArrayList(INGREDIENTS_BUNDLE_KEY, (ArrayList<? extends Parcelable>) recipe.getIngredients());
            bundle.putInt(SERVINGS_BUNDLE_KEY, recipe.getServings());

            final Intent intent = new Intent(this, StepDetailsActivity.class);
            intent.putExtras(bundle);

            startActivity(intent);
        }
    }

    private void showStepDetails(Step step) {
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
