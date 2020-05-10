package com.example.android.backingapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;

import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;

import com.example.android.backingapp.api.model.Recipe;
import com.example.android.backingapp.api.model.Step;
import com.example.android.backingapp.fragment.MasterListFragment;
import com.example.android.backingapp.fragment.StepDetailsFragment;
import com.google.gson.Gson;

import java.util.ArrayList;

public class StepsActivity extends AppCompatActivity implements MasterListFragment.OnStepClickListener {

    /// TODO Should be a recycler view
    // TODO save and restore state

    public static final String RECIPE_BUNDLE_KEY = "RecipeBundleKey";

    public static final String STEP_BUNDLE_KEY = "StepBundleKey";
    public static final String STEP_VIDEO_BUNDLE_KEY = "StepVideoBundleKey";
    public static final String STEP_INSTRUCTIONS_BUNDLE_KEY = "StepInstructionsBundleKey";

    public static final String INGREDIENTS_BUNDLE_KEY = "IngredientsBundleKey";
    public static final String SERVINGS_BUNDLE_KEY = "ServingsBundleKey";

    boolean tabletDisplay;

    private Recipe recipe;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_steps);

        tabletDisplay = findViewById(R.id.step_details_linear_layout) != null;

        Intent intent = getIntent();
        if (intent != null) {
            if (intent.hasExtra(MainActivity.RECIPE_JSON)) {
                String recipeJson = intent.getStringExtra(MainActivity.RECIPE_JSON);
                recipe = new Gson().fromJson(recipeJson, Recipe.class);

                MasterListFragment masterListFragment = new MasterListFragment();

                getSupportFragmentManager().beginTransaction()
                        .add(android.R.id.content, masterListFragment).commit();

                Bundle bundle = new Bundle();
                bundle.putParcelable(RECIPE_BUNDLE_KEY, recipe);
                masterListFragment.setArguments(bundle);
            }
        }
    }

    @Override
    public void onStepSelected(Step step) {
        if (tabletDisplay) {
            FragmentManager fragmentManager = getSupportFragmentManager();

            StepDetailsFragment videoFragment = new StepDetailsFragment();
            videoFragment.setStepDetails(step.getVideoURL());
            fragmentManager.beginTransaction()
                    .replace(R.id.video_player, videoFragment)
                    .commit();

            StepDetailsFragment instructionsFragment = new StepDetailsFragment();
            instructionsFragment.setStepDetails(step.getDescription());
            fragmentManager.beginTransaction()
                    .replace(R.id.recipe_instructions, instructionsFragment)
                    .commit();
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
}
