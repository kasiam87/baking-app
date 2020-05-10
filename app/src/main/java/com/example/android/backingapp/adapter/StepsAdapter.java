package com.example.android.backingapp.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.android.backingapp.R;
import com.example.android.backingapp.api.model.Step;

import java.util.ArrayList;
import java.util.List;

public class StepsAdapter extends RecyclerView.Adapter<StepsAdapter.StepViewHolder> {

    private List<Step> steps;
    private StepAdapterOnClickHandler stepClickHandler;

    public StepsAdapter(StepAdapterOnClickHandler clickHandler) {
        this.stepClickHandler = clickHandler;
    }

    @NonNull
    @Override
    public StepViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        Context context = viewGroup.getContext();

        View view = LayoutInflater.from(context)
                .inflate(R.layout.fragment_master_list, viewGroup, false);
        return new StepViewHolder(view);
    }

    @Override
    public void onBindViewHolder(StepViewHolder viewHolder, int position) {
        Step step = steps.get(position);
        viewHolder.stepCard.setText(step.getShortDescription());
    }

    @Override
    public int getItemCount() {
        return (steps == null) ? 0 : steps.size();
    }

    class StepViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        TextView stepCard;

        StepViewHolder(View view){
            super(view);
            itemView.setOnClickListener(this);
            stepCard = view.findViewById(R.id.step);
        }

        @Override
        public void onClick(View view) {
            int adapterPosition = getAdapterPosition();
            Step step = steps.get(adapterPosition);
            stepClickHandler.onStepSelected(step);
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
}