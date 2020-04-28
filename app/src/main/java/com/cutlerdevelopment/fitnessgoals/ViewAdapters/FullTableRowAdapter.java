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

        /*if (currentItem.isBoldTeam()) {
            teamPosField.setTypeface(teamPosField.getTypeface(), Typeface.BOLD);
            teamNameField.setTypeface(teamNameField.getTypeface(), Typeface.BOLD);
            gamesPlayedField.setTypeface(gamesPlayedField.getTypeface(), Typeface.BOLD);
            gamesWonField.setTypeface(gamesWonField.getTypeface(), Typeface.BOLD);
            gamesDrawnField.setTypeface(gamesDrawnField.getTypeface(), Typeface.BOLD);
            gamesLostField.setTypeface(gamesLostField.getTypeface(), Typeface.BOLD);
            goalsScoredField.setTypeface(goalsScoredField.getTypeface(), Typeface.BOLD);
            goalsConcededField.setTypeface(goalsConcededField.getTypeface(), Typeface.BOLD);
            goalDifferenceField.setTypeface(goalDifferenceField.getTypeface(), Typeface.BOLD);
            pointsField.setTypeface(pointsField.getTypeface(), Typeface.BOLD);
        } else {
            teamPosField.setTypeface(teamPosField.getTypeface(), Typeface.NORMAL);
            teamNameField.setTypeface(teamNameField.getTypeface(), Typeface.NORMAL);
            gamesPlayedField.setTypeface(gamesPlayedField.getTypeface(), Typeface.NORMAL);
            gamesWonField.setTypeface(gamesWonField.getTypeface(), Typeface.NORMAL);
            gamesDrawnField.setTypeface(gamesDrawnField.getTypeface(), Typeface.NORMAL);
            gamesLostField.setTypeface(gamesLostField.getTypeface(), Typeface.NORMAL);
            goalsScoredField.setTypeface(goalsScoredField.getTypeface(), Typeface.NORMAL);
            goalsConcededField.setTypeface(goalsConcededField.getTypeface(), Typeface.NORMAL);
            goalDifferenceField.setTypeface(goalDifferenceField.getTypeface(), Typeface.NORMAL);
            pointsField.setTypeface(pointsField.getTypeface(), Typeface.NORMAL);
        }*/

        if(currentItem.isPromotionPlace()) { background.setBackgroundColor(getColor(context, R.color.colorAccent)); }
        else if(currentItem.isRelegationPlace()) { background.setBackgroundColor(getColor(context, R.color.colorRed)); }
        else {background.setBackgroundColor(getColor(context, R.color.colorPrimaryDark)); }

        return convertView;

    }
}
