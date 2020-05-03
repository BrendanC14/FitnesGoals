package com.cutlerdevelopment.fitnessgoals.ViewAdapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.cutlerdevelopment.fitnessgoals.Constants.Colours;
import com.cutlerdevelopment.fitnessgoals.R;
import com.cutlerdevelopment.fitnessgoals.ViewItems.BadgeSmallItem;

import java.util.ArrayList;

public class BadgeSmallItemAdapter extends BaseAdapter {

    private ArrayList<BadgeSmallItem> singleRow;
    private LayoutInflater thisInflater;
    private Context context;


    public BadgeSmallItemAdapter(Context context, ArrayList<BadgeSmallItem> aRow) {
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
            convertView = thisInflater.inflate(R.layout.badge_small_item, parent, false);
        }

        ImageView badgeImage = convertView.findViewById(R.id.badgeSmallItemImage);
        TextView badgeName = convertView.findViewById(R.id.badgeSmallItemName);

        final BadgeSmallItem currentItem = (BadgeSmallItem) getItem(i);

        if (currentItem.isUnlocked()) {
            badgeImage.setBackground(context.getResources().getDrawable(R.drawable.goldfootball));
            badgeName.setText(currentItem.getBadgeName());
        }
        else {
            badgeImage.setBackground(context.getResources().getDrawable(R.drawable.football));
            badgeName.setText(context.getResources().getString(R.string.all_badges_locked_badge_name));
        }

        badgeName.setTextColor(Colours.getUsersSecondaryColour());

        return convertView;

    }
}
