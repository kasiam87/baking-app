package com.example.android.backingapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.example.android.backingapp.api.model.Ingredient;
import com.example.android.backingapp.api.model.Recipe;
import com.example.android.backingapp.api.model.Step;
import com.example.android.backingapp.display.TextFormatter;
import com.example.android.backingapp.fragment.MasterListFragment;
import com.example.android.backingapp.fragment.OnRecipeStepClickListener;
import com.example.android.backingapp.fragment.StepDetailsFragment;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;

import timber.log.Timber;

public class StepsActivity extends AppCompatActivity implements OnRecipeStepClickListener {

    public static final String RECIPE_BUNDLE_KEY = "RecipeBundleKey";
    public static final String RECIPE_BUNDLE_SAVED_KEY = "RecipeBundleSavedKey";
    public static final String STEP_BUNDLE_SAVED_KEY = "StepBundleSavedKey";
    public static final String INGREDIENTS_BUNDLE_SAVED_KEY = "IngredientsBundleSavedKey";
    public static final String SHOW_INGREDIENTS_BUNDLE_SAVED_KEY = "ShowIngredientsBundleSavedKey";

    public static final String STEP_BUNDLE_KEY = "StepBundleKey";

    public static final String INGREDIENTS_BUNDLE_KEY = "IngredientsBundleKey";
    public static final String SERVINGS_BUNDLE_KEY = "ServingsBundleKey";

    private boolean tabletDisplay;

    private Recipe recipe;
    private Step currentStep;
    private ArrayList<Ingredient> currentIngredients;
    private boolean showIngredients;

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
            currentIngredients = savedInstanceState.getParcelableArrayList(INGREDIENTS_BUNDLE_SAVED_KEY);
            showIngredients = savedInstanceState.getBoolean(SHOW_INGREDIENTS_BUNDLE_SAVED_KEY);
            if (tabletDisplay && currentStep != null) {
                showStepDetails(currentStep);
            }
            if (tabletDisplay && showIngredients && currentIngredients != null) {
                showIngredients(currentIngredients, recipe.getServings());
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
        bundle.putParcelableArrayList(INGREDIENTS_BUNDLE_SAVED_KEY, currentIngredients);
        bundle.putBoolean(SHOW_INGREDIENTS_BUNDLE_SAVED_KEY, showIngredients);
    }

    @Override
    public void onRecipeStepSelected(Step step) {
        currentStep = step;
        showIngredients = false;
        if (tabletDisplay) {
            showStepDetails(step);
        } else {
            Bundle bundle = new Bundle();
            bundle.putParcelable(STEP_BUNDLE_KEY, step);

            startDetailsActivity(bundle);
        }
    }

    @Override
    public void onRecipeIngredientsSelected(ArrayList<Ingredient> ingredients) {
        currentIngredients = ingredients;
        showIngredients = true;
        if (tabletDisplay) {
            showIngredients(ingredients, recipe.getServings());
        } else {
            Bundle bundle = new Bundle();
            bundle.putParcelableArrayList(INGREDIENTS_BUNDLE_KEY, ingredients);
            bundle.putInt(SERVINGS_BUNDLE_KEY, recipe.getServings());

            startDetailsActivity(bundle);
        }
    }

    private void startDetailsActivity(Bundle bundle) {
        final Intent intent = new Intent(this, StepDetailsActivity.class);
        intent.putExtras(bundle);

        startActivity(intent);
    }

    private void showStepDetails(Step step) {
        findViewById(R.id.recipe_ingredients).setVisibility(View.GONE);
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

    private void showIngredients(List<Ingredient> ingredients, int servings) {
        findViewById(R.id.video_player).setVisibility(View.GONE);
        findViewById(R.id.recipe_instructions).setVisibility(View.GONE);
        findViewById(R.id.recipe_ingredients).setVisibility(View.VISIBLE);

        StepDetailsFragment ingredientsFragment = new StepDetailsFragment();
        ingredientsFragment.setStepDetails(TextFormatter.formatIngredients(ingredients, servings));
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.recipe_ingredients, ingredientsFragment)
                .commit();
    }
}
