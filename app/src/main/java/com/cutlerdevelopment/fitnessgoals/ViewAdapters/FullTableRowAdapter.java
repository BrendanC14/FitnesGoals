package com.cutlerdevelopment.fitnessgoals.ViewAdapters;

import android.content.Context;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.cutlerdevelopment.fitnessgoals.Constants.Colours;
import com.cutlerdevelopment.fitnessgoals.Models.Team;
import com.cutlerdevelopment.fitnessgoals.R;
import com.cutlerdevelopment.fitnessgoals.ViewItems.FullTableRow;
import com.google.type.Color;

import java.util.ArrayList;

import static androidx.core.content.ContextCompat.getColor;

public class FullTableRowAdapter extends BaseAdapter {


    private ArrayList<FullTableRow> singleRow;
    private LayoutInflater thisInflater;
    private Context context;


    public FullTableRowAdapter(Context context, ArrayList<FullTableRow> aRow) {
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
            convertView = thisInflater.inflate(R.layout.full_table_row, parent, false);
        }

        LinearLayout background = convertView.findViewById(R.id.fullTableBackground);
        TextView teamPosField = convertView.findViewById(R.id.fullTableRowPosition);
        TextView teamNameField = convertView.findViewById(R.id.fullTableRowTeam);
        TextView gamesPlayedField = convertView.findViewById(R.id.fullTableRowGamesPlayed);
        TextView gamesWonField = convertView.findViewById(R.id.fullTableRowWins);
        TextView gamesDrawnField = convertView.findViewById(R.id.fullTableRowDraws);
        TextView gamesLostField = convertView.findViewById(R.id.fullTableRowLosses);
        TextView goalsScoredField = convertView.findViewById(R.id.fullTableRowScored);
        TextView goalsConcededField = convertView.findViewById(R.id.fullTableRowConceded);
        TextView goalDifferenceField = convertView.findViewById(R.id.fullTableRowGoalDifference);
        TextView pointsField = convertView.findViewById(R.id.fullTableRowPoints);

        final FullTableRow currentItem = (FullTableRow) getItem(i);

        teamPosField.setText(currentItem.getPosition());
        teamNameField.setText(currentItem.getTeamName());
        gamesPlayedField.setText(currentItem.getGamesPlayed());
        gamesWonField.setText(currentItem.getWins());
        gamesDrawnField.setText(currentItem.getDraws());
        gamesLostField.setText(currentItem.getLosses());
        goalsScoredField.setText(currentItem.getScored());
        goalsConcededField.setText(currentItem.getConceded());
        goalDifferenceField.setText(currentItem.getGoalDifference());
        pointsField.setText(currentItem.getPoints());

        int primaryColour = Colours.getUsersPrimaryColour();
        int secondaryColour = Colours.getUsersSecondaryColour();
        background.setBackgroundColor(secondaryColour);
        if(currentItem.isPromotionPlace()) {
            background.setBackgroundColor(getColor(context, R.color.colorAccent));
            teamPosField.setTextColor(getColor(context, R.color.colorWhite));
            teamNameField.setTextColor(getColor(context, R.color.colorWhite));
            gamesPlayedField.setTextColor(getColor(context, R.color.colorWhite));
            gamesWonField.setTextColor(getColor(context, R.color.colorWhite));
            gamesDrawnField.setTextColor(getColor(context, R.color.colorWhite));
            gamesLostField.setTextColor(getColor(context, R.color.colorWhite));
            goalsScoredField.setTextColor(getColor(context, R.color.colorWhite));
            goalsConcededField.setTextColor(getColor(context, R.color.colorWhite));
            goalDifferenceField.setTextColor(getColor(context, R.color.colorWhite));
            pointsField.setTextColor(getColor(context, R.color.colorWhite));
        }
        else if(currentItem.isRelegationPlace()) {
            background.setBackgroundColor(getColor(context, R.color.colorRed));
            teamPosField.setTextColor(getColor(context, R.color.colorWhite));
            teamNameField.setTextColor(getColor(context, R.color.colorWhite));
            gamesPlayedField.setTextColor(getColor(context, R.color.colorWhite));
            gamesWonField.setTextColor(getColor(context, R.color.colorWhite));
            gamesDrawnField.setTextColor(getColor(context, R.color.colorWhite));
            gamesLostField.setTextColor(getColor(context, R.color.colorWhite));
            goalsScoredField.setTextColor(getColor(context, R.color.colorWhite));
            goalsConcededField.setTextColor(getColor(context, R.color.colorWhite));
            goalDifferenceField.setTextColor(getColor(context, R.color.colorWhite));
            pointsField.setTextColor(getColor(context, R.color.colorWhite));
        }
        else {
            background.setBackgroundColor(primaryColour);
            teamPosField.setTextColor(secondaryColour);
            teamNameField.setTextColor(secondaryColour);
            gamesPlayedField.setTextColor(secondaryColour);
            gamesWonField.setTextColor(secondaryColour);
            gamesDrawnField.setTextColor(secondaryColour);
            gamesLostField.setTextColor(secondaryColour);
            goalsScoredField.setTextColor(secondaryColour);
            goalsConcededField.setTextColor(secondaryColour);
            goalDifferenceField.setTextColor(secondaryColour);
            pointsField.setTextColor(secondaryColour);
        }

        return convertView;

    }

}
