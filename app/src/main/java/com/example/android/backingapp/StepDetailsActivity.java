package com.example.android.backingapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import com.example.android.backingapp.api.model.Ingredient;
import com.example.android.backingapp.api.model.Recipe;
import com.example.android.backingapp.api.model.Step;
import com.example.android.backingapp.databinding.ActivityMainBinding;
import com.example.android.backingapp.databinding.ActivityStepDetailsBinding;
import com.example.android.backingapp.fragment.StepDetailsFragment;
import com.google.gson.Gson;

import java.util.List;

public class StepDetailsActivity extends AppCompatActivity {

    private Step step;
    private List<Ingredient> ingredients;
    private int servings;

    ActivityStepDetailsBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityStepDetailsBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        Intent intent = getIntent();
        if (intent != null) {
            Log.d("StepDetailsActivity", "Intent not null");
            if (intent.hasExtra(StepsActivity.STEP_BUNDLE_KEY)) {
                step = intent.getParcelableExtra(StepsActivity.STEP_BUNDLE_KEY);
            }

            if (intent.hasExtra(StepsActivity.INGREDIENTS_BUNDLE_KEY)) {
                ingredients = intent.getParcelableArrayListExtra(StepsActivity.INGREDIENTS_BUNDLE_KEY);
            }

            if (intent.hasExtra(StepsActivity.INGREDIENTS_BUNDLE_KEY)) {
                servings = intent.getIntExtra(StepsActivity.SERVINGS_BUNDLE_KEY, 0);
            }
        }

        String videoUrl = step.getVideoURL() == null ? "Empty" : step.getVideoURL();
        String instructions = step.getDescription() == null || step.getDescription().isEmpty()? "Empty" : step.getDescription();
        Log.d("Details::", instructions);
        binding.videoPlayer.setText(videoUrl);
        binding.recipeInstructions.setText(instructions);

//        FragmentManager fragmentManager = getSupportFragmentManager();
//
//        StepDetailsFragment videoFragment = new StepDetailsFragment();
//
//        fragmentManager.beginTransaction()
//                .add(R.id.video_player, videoFragment)
//                .commit();

//        StepDetailsFragment instructionsFragment = new StepDetailsFragment();
//
//        fragmentManager.beginTransaction()
//                .add(R.id.recipe_instructions, instructionsFragment)
//                .commit();

    }
}
