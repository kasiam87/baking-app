package com.example.android.backingapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.v4.media.session.MediaSessionCompat;
import android.support.v4.media.session.PlaybackStateCompat;
import android.view.View;

import com.example.android.backingapp.api.model.Ingredient;
import com.example.android.backingapp.api.model.Recipe;
import com.example.android.backingapp.api.model.Step;
import com.example.android.backingapp.display.TextFormatter;
import com.example.android.backingapp.fragment.MasterListFragment;
import com.example.android.backingapp.fragment.OnRecipeStepClickListener;
import com.example.android.backingapp.fragment.StepDetailsFragment;
import com.google.android.exoplayer2.DefaultLoadControl;
import com.google.android.exoplayer2.ExoPlaybackException;
import com.google.android.exoplayer2.ExoPlayer;
import com.google.android.exoplayer2.ExoPlayerFactory;
import com.google.android.exoplayer2.LoadControl;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.Timeline;
import com.google.android.exoplayer2.extractor.DefaultExtractorsFactory;
import com.google.android.exoplayer2.source.ExtractorMediaSource;
import com.google.android.exoplayer2.source.MediaSource;
import com.google.android.exoplayer2.source.TrackGroupArray;
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector;
import com.google.android.exoplayer2.trackselection.TrackSelectionArray;
import com.google.android.exoplayer2.trackselection.TrackSelector;
import com.google.android.exoplayer2.ui.SimpleExoPlayerView;
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory;
import com.google.android.exoplayer2.util.Util;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;

public class StepsActivity extends AppCompatActivity implements OnRecipeStepClickListener, ExoPlayer.EventListener {

    public static final String RECIPE_BUNDLE_SAVED_KEY = "RecipeBundleSavedKey";
    public static final String STEP_BUNDLE_SAVED_KEY = "StepBundleSavedKey";
    public static final String INGREDIENTS_BUNDLE_SAVED_KEY = "IngredientsBundleSavedKey";
    public static final String SHOW_INGREDIENTS_BUNDLE_SAVED_KEY = "ShowIngredientsBundleSavedKey";

    public static final String STEP_BUNDLE_KEY = "StepBundleKey";
    public static final String RECIPE_NAME_BUNDLE_KEY = "RecipeNameBundleKey";
    public static final String POSITION_BUNDLE_KEY = "PositionBundleKey";
    public static final String RECIPE_STEPS_BUNDLE_KEY = "RecipeStepsBundleKey";

    public static final String INGREDIENTS_BUNDLE_KEY = "IngredientsBundleKey";
    public static final String SERVINGS_BUNDLE_KEY = "ServingsBundleKey";

    private boolean tabletDisplay;

    private Recipe recipe;
    private Step currentStep;
    private ArrayList<Ingredient> currentIngredients;
    private boolean showIngredients;

    private SimpleExoPlayer exoPlayer;
    private SimpleExoPlayerView playerView;
    private MediaSessionCompat mediaSession;
    private PlaybackStateCompat.Builder stateBuilder;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_steps);

        tabletDisplay = findViewById(R.id.step_details_linear_layout) != null;

        playerView = findViewById(R.id.video_player);

        initializeMediaSession();

        if (savedInstanceState == null) {
            Intent intent = getIntent();
            if (intent != null) {
                if (intent.hasExtra(MainActivity.RECIPE_JSON)) {
                    String recipeJson = intent.getStringExtra(MainActivity.RECIPE_JSON);
                    recipe = new Gson().fromJson(recipeJson, Recipe.class);
                }
            }

        } else {
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

        setTitle(recipe.getName());
        MasterListFragment masterListFragment = new MasterListFragment();

        getSupportFragmentManager().beginTransaction()
                .replace(android.R.id.content, masterListFragment).commit();

        masterListFragment.setRecipe(recipe);
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
    public void onRecipeStepSelected(Step step, int position, List<View> itemViewList) {
        currentStep = step;
        showIngredients = false;
        if (tabletDisplay) {
            releasePlayer();
            highlightSelectedStep(position, itemViewList);
            showStepDetails(step);
        } else {
            Bundle bundle = new Bundle();
            bundle.putParcelable(STEP_BUNDLE_KEY, step);
            bundle.putParcelableArrayList(INGREDIENTS_BUNDLE_KEY, (ArrayList<? extends Parcelable>) recipe.getIngredients());
            bundle.putInt(SERVINGS_BUNDLE_KEY, recipe.getServings());
            bundle.putInt(POSITION_BUNDLE_KEY, position);
            bundle.putParcelableArrayList(RECIPE_STEPS_BUNDLE_KEY, (ArrayList<? extends Parcelable>) recipe.getSteps());

            startDetailsActivity(bundle);
        }
    }

    @Override
    public void onRecipeIngredientsSelected(ArrayList<Ingredient> ingredients, int position, List<View> itemViewList) {
        currentIngredients = ingredients;
        showIngredients = true;
        if (tabletDisplay) {
            releasePlayer();
            highlightSelectedStep(position, itemViewList);
            showIngredients(ingredients, recipe.getServings());
        } else {
            Bundle bundle = new Bundle();
            bundle.putParcelableArrayList(INGREDIENTS_BUNDLE_KEY, ingredients);
            bundle.putInt(SERVINGS_BUNDLE_KEY, recipe.getServings());
            bundle.putInt(POSITION_BUNDLE_KEY, position);
            bundle.putParcelableArrayList(RECIPE_STEPS_BUNDLE_KEY, (ArrayList<? extends Parcelable>) recipe.getSteps());

            startDetailsActivity(bundle);
        }
    }

    private void startDetailsActivity(Bundle bundle) {
        final Intent intent = new Intent(this, StepDetailsActivity.class);
        bundle.putString(RECIPE_NAME_BUNDLE_KEY, recipe.getName());
        intent.putExtras(bundle);

        startActivity(intent);
    }

    private void showStepDetails(Step step) {
        StepDetailsFragment videoFragment = new StepDetailsFragment();
        if (step.getVideoURL() != null && !step.getVideoURL().isEmpty()) {
            findViewById(R.id.video_player).setVisibility(View.VISIBLE);
            initializePlayer(Uri.parse(step.getVideoURL()));
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.video_player, videoFragment)
                    .commit();
        } else {
            findViewById(R.id.video_player).setVisibility(View.GONE);
        }

        if (!step.getDescription().equals(step.getShortDescription())) {
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
        findViewById(R.id.recipe_instructions).setVisibility(View.VISIBLE);

        StepDetailsFragment ingredientsFragment = new StepDetailsFragment();
        ingredientsFragment.setStepDetails(TextFormatter.formatIngredients(ingredients, servings));
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.recipe_instructions, ingredientsFragment)
                .commit();
    }

    private void highlightSelectedStep(int position, List<View> itemViewList) {
        for (View view : itemViewList) {
            if (itemViewList.get(position) == view) {
                view.findViewById(R.id.steps_card_view).setBackgroundResource(R.color.colorStepSelected);
            } else {
                view.findViewById(R.id.steps_card_view).setBackgroundResource(R.color.colorStepDefault);
            }
        }
    }

    private void initializePlayer(Uri uri) {
        if (exoPlayer == null) {
            TrackSelector trackSelector = new DefaultTrackSelector();
            LoadControl loadControl = new DefaultLoadControl();
            exoPlayer = ExoPlayerFactory.newSimpleInstance(this, trackSelector, loadControl);
            playerView.setPlayer(exoPlayer);

            exoPlayer.addListener(this);
            String userAgent = Util.getUserAgent(this, getResources().getString(R.string.app_name));
            MediaSource mediaSource = new ExtractorMediaSource(uri, new DefaultDataSourceFactory(
                    this, userAgent), new DefaultExtractorsFactory(), null, null);
            exoPlayer.prepare(mediaSource);
            exoPlayer.setPlayWhenReady(true);
        }
    }

    private void releasePlayer() {
        if (exoPlayer != null) {
            exoPlayer.stop();
            exoPlayer.release();
            exoPlayer = null;
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        releasePlayer();
        mediaSession.setActive(false);
    }

    @Override
    protected void onPause() {
        super.onPause();
        releasePlayer();
    }

    @Override
    protected void onStop() {
        super.onStop();
        releasePlayer();
    }

    @Override
    public void onTimelineChanged(Timeline timeline, Object manifest) {

    }

    @Override
    public void onTracksChanged(TrackGroupArray trackGroups, TrackSelectionArray trackSelections) {

    }

    @Override
    public void onLoadingChanged(boolean isLoading) {

    }

    @Override
    public void onPlayerStateChanged(boolean playWhenReady, int playbackState) {
        if ((playbackState == ExoPlayer.STATE_READY) && playWhenReady) {
            stateBuilder.setState(PlaybackStateCompat.STATE_PLAYING,
                    exoPlayer.getCurrentPosition(), 1f);
        } else if ((playbackState == ExoPlayer.STATE_READY)) {
            stateBuilder.setState(PlaybackStateCompat.STATE_PAUSED,
                    exoPlayer.getCurrentPosition(), 1f);
        }
        mediaSession.setPlaybackState(stateBuilder.build());
    }

    @Override
    public void onPlayerError(ExoPlaybackException error) {

    }

    @Override
    public void onPositionDiscontinuity() {

    }

    private void initializeMediaSession() {
        mediaSession = new MediaSessionCompat(this, StepsActivity.class.getSimpleName());
        mediaSession.setMediaButtonReceiver(null);

        stateBuilder = new PlaybackStateCompat.Builder()
                .setActions(
                        PlaybackStateCompat.ACTION_PLAY |
                                PlaybackStateCompat.ACTION_PAUSE |
                                PlaybackStateCompat.ACTION_SKIP_TO_PREVIOUS |
                                PlaybackStateCompat.ACTION_PLAY_PAUSE);

        mediaSession.setPlaybackState(stateBuilder.build());
        mediaSession.setCallback(new MediaSessionCallback());
        mediaSession.setActive(true);
    }

    private class MediaSessionCallback extends MediaSessionCompat.Callback {
        @Override
        public void onPlay() {
            exoPlayer.setPlayWhenReady(true);
        }

        @Override
        public void onPause() {
            exoPlayer.setPlayWhenReady(false);
        }

        @Override
        public void onSkipToPrevious() {
            exoPlayer.seekTo(0);
        }
    }
}