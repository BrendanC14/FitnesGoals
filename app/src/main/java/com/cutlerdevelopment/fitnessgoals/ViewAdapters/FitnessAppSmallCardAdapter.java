package com.cutlerdevelopment.fitnessgoals.ViewAdapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.constraintlayout.widget.ConstraintLayout;

import com.cutlerdevelopment.fitnessgoals.R;
import com.cutlerdevelopment.fitnessgoals.ViewItems.FitnessAppSmallCard;

import java.util.ArrayList;

import static androidx.core.content.ContextCompat.getColor;

public class FitnessAppSmallCardAdapter extends BaseAdapter {


    private ArrayList<FitnessAppSmallCard> singleRow;
    private LayoutInflater thisInflater;
    private Context context;


    public FitnessAppSmallCardAdapter(Context context, ArrayList<FitnessAppSmallCard> aRow) {
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
            convertView = thisInflater.inflate(R.layout.fitness_app_small_card, parent, false);
        }

        TextView appName = convertView.findViewById(R.id.fitnessAppSmallCardAppName);
        ImageView appImage = convertView.findViewById(R.id.fitnessAppSmallCardAppImage);
        LinearLayout background = convertView.findViewById(R.id.fitnessAppSmallCardBackground);

        final FitnessAppSmallCard currentItem = (FitnessAppSmallCard) getItem(i);

        appName.setText(currentItem.getAppName());
        appImage.setBackgroundResource(currentItem.getAppImage());
        background.setBackgroundColor(getColor(context, currentItem.getBackgroundColour()));

        return convertView;

    }
}
