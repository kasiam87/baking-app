package com.example.android.backingapp.display;

import com.example.android.backingapp.api.model.Ingredient;

import java.text.DecimalFormat;
import java.util.List;

public final class TextFormatter {

    public static String formatIngredients(List<Ingredient> ingredients, int servings){
        StringBuilder ingredientsBuilder = new StringBuilder();
        ingredientsBuilder.append("Servings: ")
                .append(servings)
                .append("\n");
        for (Ingredient ingredient : ingredients){
            ingredientsBuilder.append(new DecimalFormat("0.#").format(ingredient.getQuantity()))
                    .append(" ")
                    .append(ingredient.getMeasure())
                    .append(" of ")
                    .append(ingredient.getIngredient())
                    .append("\n");
        }
        return ingredientsBuilder.toString();
    }
}
