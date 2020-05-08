package com.cutlerdevelopment.fitnessgoals.ViewAdapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.TextView;

import com.cutlerdevelopment.fitnessgoals.Constants.Colours;
import com.cutlerdevelopment.fitnessgoals.R;
import com.cutlerdevelopment.fitnessgoals.Utils.StringHelper;
import com.cutlerdevelopment.fitnessgoals.ViewItems.LeagueStepsItem;

import java.util.ArrayList;

public class LeagueStepsItemAdapter extends BaseAdapter {
    private ArrayList<LeagueStepsItem> singleRow;
    private LayoutInflater thisInflater;
    private Context context;


    public LeagueStepsItemAdapter(Context context, ArrayList<LeagueStepsItem> aRow) {
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
            convertView = thisInflater.inflate(R.layout.league_steps_item, parent, false);
        }

        TextView leagueName = convertView.findViewById(R.id.leagueStepsLeagueName);
        final TextView leagueSteps = convertView.findViewById(R.id.leagueStepsTeamsSteps);
        final Button decreaseSteps = convertView.findViewById(R.id.leagueStepsDecreaseSteps);
        Button increaseSteps = convertView.findViewById(R.id.leagueStepsIncreaseSteps);

        final LeagueStepsItem currentItem = (LeagueStepsItem) getItem(i);

        leagueName.setText(currentItem.getLeagueName());
        leagueSteps.setText(currentItem.getLeagueSteps());

        decreaseSteps.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                int newSteps = Integer.parseInt(StringHelper.removeCommaFromString(String.valueOf(leagueSteps.getText()))) - 500;
                leagueSteps.setText(StringHelper.getNumberWithCommas(newSteps));
                currentItem.setLeagueSteps(StringHelper.getNumberWithCommas(newSteps));
                checkButtonValidity(newSteps, decreaseSteps);
            }
        });

        increaseSteps.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int newSteps = Integer.parseInt(StringHelper.removeCommaFromString(String.valueOf(leagueSteps.getText()))) + 500;
                currentItem.setLeagueSteps(StringHelper.getNumberWithCommas(newSteps));
                leagueSteps.setText(StringHelper.getNumberWithCommas(newSteps));
            }
        });

        int primaryColour = Colours.getUsersPrimaryColour();
        int secondaryColour = Colours.getUsersSecondaryColour();

        leagueName.setTextColor(secondaryColour);
        leagueSteps.setTextColor(secondaryColour);
        decreaseSteps.setBackgroundColor(secondaryColour);
        decreaseSteps.setTextColor(primaryColour);
        increaseSteps.setBackgroundColor(secondaryColour);
        increaseSteps.setTextColor(primaryColour);

        return convertView;

    }

    void checkButtonValidity(int steps, Button decreaseSteps) {
        if (steps <= 3500) { decreaseSteps.setVisibility(View.INVISIBLE); }
        else { decreaseSteps.setVisibility(View.VISIBLE); }
    }
}
