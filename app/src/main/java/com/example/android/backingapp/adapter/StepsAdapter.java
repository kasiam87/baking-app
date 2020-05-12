package com.example.android.backingapp.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.android.backingapp.R;
import com.example.android.backingapp.api.model.Ingredient;
import com.example.android.backingapp.api.model.Step;

import java.util.ArrayList;
import java.util.List;

public class StepsAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private List<Step> steps;
    private ArrayList<Ingredient> ingredients;
    private StepAdapterOnClickHandler stepClickHandler;

    public List<View> itemViewList = new ArrayList<>();

    public StepsAdapter(StepAdapterOnClickHandler clickHandler) {
        this.stepClickHandler = clickHandler;
    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        Context context = viewGroup.getContext();

        LayoutInflater inflater = LayoutInflater.from(context);

        View stepView = inflater.inflate(R.layout.fragment_master_list, viewGroup, false);
        itemViewList.add(stepView);
        if (viewType == 0) {
            return new IngredientsViewHolder(stepView);
        }

        return new StepViewHolder(stepView);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder viewHolder, int position) {
        if (viewHolder.getItemViewType() == 0) {
            IngredientsViewHolder ingredientsViewHolder = (IngredientsViewHolder) viewHolder;
            ingredientsViewHolder.ingredientsTextView.setText(R.string.ingredients_label);
        } else {
            StepViewHolder stepViewHolder = (StepViewHolder) viewHolder;
            stepViewHolder.stepTextView.setText(steps.get(position - 1).getShortDescription());
        }
    }

    @Override
    public int getItemCount() {
        return (steps == null) ? 0 : steps.size() + 1;
    }

    class StepViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        CardView stepCard;
        TextView stepTextView;

        StepViewHolder(View view){
            super(view);
            itemView.setOnClickListener(this);
            stepCard = view.findViewById(R.id.steps_card_view);
            stepTextView = view.findViewById(R.id.step);
        }

        @Override
        public void onClick(View view) {
            int adapterPosition = getAdapterPosition();
            Step step = steps.get(adapterPosition - 1);

            stepClickHandler.onStepSelected(step, adapterPosition, itemViewList);
        }
    }

    public class IngredientsViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        CardView ingredientsCard;
        TextView ingredientsTextView;

        IngredientsViewHolder(View itemView) {
            super(itemView);
            itemView.setOnClickListener(this);
            ingredientsCard = itemView.findViewById(R.id.steps_card_view);
            ingredientsTextView = itemView.findViewById(R.id.step);
        }

        @Override
        public void onClick(View view) {
            stepClickHandler.onIngredientsSelected(ingredients, getAdapterPosition(), itemViewList);
        }
    }

    public void setSteps(List<Step> steps) {
        if (steps == null) {
            this.steps = new ArrayList<>();
        } else {
            this.steps = steps;
        }

        notifyDataSetChanged();
    }

    public ArrayList<Step> getSteps() {
        if (steps == null){
            return new ArrayList<>();
        }
        return new ArrayList<>(steps);
    }

    public void setIngredients(ArrayList<Ingredient> ingredients) {
        if (ingredients == null) {
            this.ingredients = new ArrayList<>();
        } else {
            this.ingredients = ingredients;
        }

        notifyDataSetChanged();
    }

    public ArrayList<Ingredient> getIngredients() {
        if (ingredients == null){
            return new ArrayList<>();
        }
        return new ArrayList<>(ingredients);
    }
}