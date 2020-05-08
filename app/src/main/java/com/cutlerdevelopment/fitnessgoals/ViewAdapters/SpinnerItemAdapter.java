package com.cutlerdevelopment.fitnessgoals.ViewAdapters;

import android.content.Context;
import android.graphics.Typeface;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.cutlerdevelopment.fitnessgoals.Constants.Colours;
import com.cutlerdevelopment.fitnessgoals.R;

import java.util.ArrayList;
import java.util.List;

public class SpinnerItemAdapter extends BaseAdapter {

    private ArrayList<String> singleRow;
    private LayoutInflater thisInflater;
    private Context context;


    public SpinnerItemAdapter(Context context, ArrayList<String> aRow) {
        this.singleRow = aRow;
        this.context = context;
        thisInflater = (thisInflater.from(context));
    }
    @Override
    public int getCount() {
        return singleRow.size();
    }

    @Override
    public Object getItem(int i) {
        return singleRow.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = thisInflater.inflate(R.layout.spinner_item, parent, false);
        }
        TextView text = convertView.findViewById(R.id.spinnerText);

        final String currentItem = (String) getItem(i);
        text.setBackgroundColor(Colours.getUsersPrimaryColour());
        text.setTextColor(Colours.getUsersSecondaryColour());
        text.setText(currentItem);
        text.setGravity(Gravity.BOTTOM);

        return convertView;

    }
}
