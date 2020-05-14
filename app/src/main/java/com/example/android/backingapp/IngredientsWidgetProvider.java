package com.example.android.backingapp;

import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.Intent;
import android.widget.RemoteViews;

import timber.log.Timber;

public class IngredientsWidgetProvider extends AppWidgetProvider {

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {

    }

    @Override
    public void onEnabled(Context context) {

    }

    @Override
    public void onDisabled(Context context) {

    }

    public static void updateIngredientsWidgets(Context context, AppWidgetManager appWidgetManager,
                                                String ingredients, int[] appWidgetIds) {
        for (int appWidgetId : appWidgetIds) {
            updateAppWidget(context, appWidgetManager, ingredients, appWidgetId);
        }
    }

    static void updateAppWidget(Context context, AppWidgetManager appWidgetManager,
                                String ingredients, int appWidgetId) {

        Timber.d("Update widget to '%s'", ingredients);
        RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.recipe_ingredients_widget);

        views.setTextViewText(R.id.recipe_ingredients_widget, ingredients);

        Intent ingredientsIntent = new Intent(context, IngredientsWidgetIntentService.class);
        ingredientsIntent.setAction(IngredientsWidgetIntentService.ACTION_UPDATE_INGREDIENTS_WIDGETS);
        appWidgetManager.updateAppWidget(appWidgetId, views);
    }
}