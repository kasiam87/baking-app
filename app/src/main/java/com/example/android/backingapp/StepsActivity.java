package com.example.android.backingapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.util.Log;

import com.example.android.backingapp.api.model.Recipe;
import com.example.android.backingapp.fragment.MasterListFragment;
import com.example.android.backingapp.fragment.StepDetailsFragment;
import com.google.gson.Gson;

import java.util.ArrayList;

public class StepsActivity extends AppCompatActivity implements MasterListFragment.OnStepClickListener {

    /// TODO Should be a recycler view

    public static final String STEP_BUNDLE_KEY = "StepBundleKey";
    public static final String INGREDIENTS_BUNDLE_KEY = "IngredientsBundleKey";
    public static final String SERVINGS_BUNDLE_KEY = "ServingsBundleKey";
    public static final String RECIPE_BUNDLE_KEY = "RecipeBundleKey";

    boolean displayTwoPane;

    private Recipe recipe;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_steps);

        displayTwoPane = findViewById(R.id.step_details) != null;

        Intent intent = getIntent();
        if (intent != null) {
            Log.d("StepActivity", "INTENT NOT NULL");
            if (intent.hasExtra(MainActivity.RECIPE_OBJECT)) {
                Log.d("StepActivity", "HAS EXTRA");
                String recipeObject = intent.getStringExtra(MainActivity.RECIPE_OBJECT);
                recipe = new Gson().fromJson(recipeObject, Recipe.class);

                MasterListFragment masterListFragment = new MasterListFragment();

                getSupportFragmentManager().beginTransaction()
                        .add(android.R.id.content, masterListFragment).commit();

                Log.d("StepActivity", "Set arguments");
                Bundle bundle = new Bundle();
                bundle.putParcelable(RECIPE_BUNDLE_KEY, recipe);
                masterListFragment.setArguments(bundle);
            }
        }
    }

    @Override
    public void onStepSelected(int position) {

        if (displayTwoPane){
            StepDetailsFragment stepDetailsFragment = new StepDetailsFragment();
            stepDetailsFragment.setListIndex(position);
        } else {

        }

        Bundle bundle = new Bundle();
        bundle.putParcelable(STEP_BUNDLE_KEY, recipe.getSteps().get(position));
        bundle.putParcelableArrayList(INGREDIENTS_BUNDLE_KEY, (ArrayList<? extends Parcelable>) recipe.getIngredients());
        bundle.putInt(SERVINGS_BUNDLE_KEY, recipe.getServings());

        final Intent intent = new Intent(this, StepDetailsActivity.class);
        intent.putExtras(bundle);

        startActivity(intent);
    }
}
