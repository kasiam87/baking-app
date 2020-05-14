package com.example.android.backingapp;

import android.app.IntentService;
import android.appwidget.AppWidgetManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;

import androidx.annotation.Nullable;

public class IngredientsWidgetIntentService extends IntentService {

    public static final String ACTION_UPDATE_INGREDIENTS_WIDGETS = "com.example.android.backingapp.action.update_ingredients_widgets";
    public static final String INGREDIENTS_BUNDLE = "Ingredients";

    public IngredientsWidgetIntentService() {
        super(IngredientsWidgetIntentService.class.getName());
    }

    public static void startBakingService(Context context, String ingredients) {
        Intent intent = new Intent(context, IngredientsWidgetIntentService.class);
        intent.putExtra(INGREDIENTS_BUNDLE, ingredients);
        intent.setAction(ACTION_UPDATE_INGREDIENTS_WIDGETS);
        context.startService(intent);
    }

    @Override
    protected void onHandleIntent(@Nullable Intent intent) {
        if (intent != null) {
            final String action = intent.getAction();
            String ingredients = intent.getStringExtra(INGREDIENTS_BUNDLE);
            if (ACTION_UPDATE_INGREDIENTS_WIDGETS.equals(action)) {
                handleActionUpdateIngredientsWidgets(ingredients);
            }
        }
    }

    private void handleActionUpdateIngredientsWidgets(String ingredients) {
        AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(this);
        int[] appWidgetIds = appWidgetManager.getAppWidgetIds(new ComponentName(this, IngredientsWidgetProvider.class));
        IngredientsWidgetProvider.updateIngredientsWidgets(this, appWidgetManager, ingredients, appWidgetIds);
    }
}
