package com.example.android.backingapp.fragment;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.List;

public class MasterListAdapter extends BaseAdapter {

    private Context context;
    private List<String> steps;

    public MasterListAdapter(Context context, List<String> steps) {
        this.context = context;
        this.steps = steps;
    }

    @Override
    public int getCount() {
        return steps.size();
    }

    @Override
    public Object getItem(int i) {
        return null;
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    /**
     * Creates a new ImageView for each item referenced by the adapter
     */
    public View getView(final int position, View convertView, ViewGroup parent) {
        TextView textView;
        if (convertView == null) {
            // If the view is not recycled, this creates a new ImageView to hold an image
            textView = new TextView(context);
            // Define the layout parameters
            textView.setPadding(8, 8, 8, 8);
        } else {
            textView = (TextView) convertView;
        }

        // Set the image resource and return the newly created ImageView
        textView.setText(steps.get(position));
        return textView;
    }

}
