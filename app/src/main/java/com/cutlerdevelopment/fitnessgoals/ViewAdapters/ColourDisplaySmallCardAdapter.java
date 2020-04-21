package com.cutlerdevelopment.fitnessgoals.ViewAdapters;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.cutlerdevelopment.fitnessgoals.R;
import com.cutlerdevelopment.fitnessgoals.ViewItems.ColourDisplaySmallCard;

import java.util.ArrayList;

public class ColourDisplaySmallCardAdapter extends BaseAdapter {


    private ArrayList<ColourDisplaySmallCard> singleRow;
    private LayoutInflater thisInflater;
    private Context context;


    public ColourDisplaySmallCardAdapter(Context context, ArrayList<ColourDisplaySmallCard> aRow) {
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
            convertView = thisInflater.inflate(R.layout.colour_display_small_card, parent, false);
        }

        ImageView colourImage = convertView.findViewById(R.id.colourDIsplayImage);

        final ColourDisplaySmallCard currentItem = (ColourDisplaySmallCard) getItem(i);

        colourImage.setBackgroundColor(Color.parseColor(currentItem.getTeamColour()));

        return convertView;

    }
}
