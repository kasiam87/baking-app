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
import com.example.android.backingapp.api.model.Step;
import com.example.android.backingapp.databinding.ActivityStepDetailsBinding;
import com.example.android.backingapp.display.TextFormatter;
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

import java.util.ArrayList;
import java.util.List;

public class StepDetailsActivity extends AppCompatActivity implements ExoPlayer.EventListener{

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

    private SimpleExoPlayer mExoPlayer;
    private SimpleExoPlayerView mPlayerView;

    private MediaSessionCompat mMediaSession;
    private PlaybackStateCompat.Builder mStateBuilder;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityStepDetailsBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        initializeMediaSession();

        mPlayerView = findViewById(R.id.video_player);

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
        releasePlayer();
        findViewById(R.id.video_player).setVisibility(View.GONE);
        findViewById(R.id.recipe_instructions).setVisibility(View.VISIBLE);

        StepDetailsFragment ingredientsFragment = new StepDetailsFragment();
        ingredientsFragment.setStepDetails(TextFormatter.formatIngredients(ingredients, servings));
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.recipe_instructions, ingredientsFragment)
                .commit();
    }

    public void showStepDetails(Step step) {
        releasePlayer();
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

    private void initializePlayer(Uri mediaUri) {
        if (mExoPlayer == null) {
            TrackSelector trackSelector = new DefaultTrackSelector();
            LoadControl loadControl = new DefaultLoadControl();
            mExoPlayer = ExoPlayerFactory.newSimpleInstance(this, trackSelector, loadControl);
            mPlayerView.setPlayer(mExoPlayer);

            mExoPlayer.addListener(this);
            String userAgent = Util.getUserAgent(this, getResources().getString(R.string.app_name));
            MediaSource mediaSource = new ExtractorMediaSource(mediaUri, new DefaultDataSourceFactory(
                    this, userAgent), new DefaultExtractorsFactory(), null, null);
            mExoPlayer.prepare(mediaSource);
            mExoPlayer.setPlayWhenReady(true);
        }
    }

    private void releasePlayer() {
        if (mExoPlayer != null) {
            mExoPlayer.stop();
            mExoPlayer.release();
            mExoPlayer = null;
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        releasePlayer();
        mMediaSession.setActive(false);
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
        if((playbackState == ExoPlayer.STATE_READY) && playWhenReady){
            mStateBuilder.setState(PlaybackStateCompat.STATE_PLAYING,
                    mExoPlayer.getCurrentPosition(), 1f);
        } else if((playbackState == ExoPlayer.STATE_READY)){
            mStateBuilder.setState(PlaybackStateCompat.STATE_PAUSED,
                    mExoPlayer.getCurrentPosition(), 1f);
        }
        mMediaSession.setPlaybackState(mStateBuilder.build());
    }

    @Override
    public void onPlayerError(ExoPlaybackException error) {

    }

    @Override
    public void onPositionDiscontinuity() {

    }

    private void initializeMediaSession() {
        mMediaSession = new MediaSessionCompat(this, StepsActivity.class.getSimpleName());
        mMediaSession.setMediaButtonReceiver(null);

        mStateBuilder = new PlaybackStateCompat.Builder()
                .setActions(
                        PlaybackStateCompat.ACTION_PLAY |
                                PlaybackStateCompat.ACTION_PAUSE |
                                PlaybackStateCompat.ACTION_SKIP_TO_PREVIOUS |
                                PlaybackStateCompat.ACTION_PLAY_PAUSE);

        mMediaSession.setPlaybackState(mStateBuilder.build());
        mMediaSession.setCallback(new MediaSessionCallback());
        mMediaSession.setActive(true);
    }

    private class MediaSessionCallback extends MediaSessionCompat.Callback {
        @Override
        public void onPlay() {
            mExoPlayer.setPlayWhenReady(true);
        }

        @Override
        public void onPause() {
            mExoPlayer.setPlayWhenReady(false);
        }

        @Override
        public void onSkipToPrevious() {
            mExoPlayer.seekTo(0);
        }
    }
}
