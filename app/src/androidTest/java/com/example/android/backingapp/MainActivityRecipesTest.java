package com.example.android.backingapp;

import androidx.test.espresso.contrib.RecyclerViewActions;
import androidx.test.espresso.matcher.ViewMatchers;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.rule.ActivityTestRule;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.matcher.ViewMatchers.hasDescendant;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withSubstring;
import static androidx.test.espresso.matcher.ViewMatchers.withText;


@RunWith(AndroidJUnit4.class)
public class MainActivityRecipesTest {

    @Rule public ActivityTestRule<MainActivity> mainActivityTestRule =
            new ActivityTestRule<>(MainActivity.class);

    @Test
    public void verifyMainActivityTitle() {
        onView(withText(R.string.app_name)).check(matches(isDisplayed()));
    }

    @Test
    public void openRecipe_checkIngredientsText() {
        onView(ViewMatchers.withId(R.id.recipes_recycler_view))
                .perform(RecyclerViewActions.actionOnItemAtPosition(0,
                        click()));

        onView(withText(R.string.ingredients_label)).check(matches(isDisplayed()));
    }

    @Test
    public void openRecipe_checkRecipeTitle() {
        String recipeName = "Brownies";
        onView(withId(R.id.recipes_recycler_view))
                .perform(RecyclerViewActions.actionOnItem(
                        hasDescendant(withText(recipeName)),
                        click()));

        onView(withText(recipeName)).check(matches(isDisplayed()));
        onView(withText(R.string.ingredients_label)).check(matches(isDisplayed()));
    }

    @Test
    public void openIngredients_checkServings() {
        String recipeName = "Brownies";
        onView(withId(R.id.recipes_recycler_view))
                .perform(RecyclerViewActions.actionOnItem(
                        hasDescendant(withText(recipeName)),
                        click()));

        onView(withText(R.string.ingredients_label)).perform(click());
        onView(withSubstring("Servings")).check(matches(isDisplayed()));
    }
}
