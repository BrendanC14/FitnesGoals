package com.cutlerdevelopment.fitnessgoals.ViewControllers;

import android.content.Context;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.cutlerdevelopment.fitnessgoals.Constants.Colours;
import com.cutlerdevelopment.fitnessgoals.Constants.Leagues;
import com.cutlerdevelopment.fitnessgoals.Models.Fixture;
import com.cutlerdevelopment.fitnessgoals.R;
import com.cutlerdevelopment.fitnessgoals.SavedData.GameDBHandler;
import com.cutlerdevelopment.fitnessgoals.Data.GameData;
import com.cutlerdevelopment.fitnessgoals.Utils.DateHelper;
import com.cutlerdevelopment.fitnessgoals.Utils.StringHelper;
import com.google.android.material.card.MaterialCardView;

import java.util.Date;

import static android.view.View.GONE;
import static android.view.View.VISIBLE;

public class NextMatchCardController {

    private Context c;

    MaterialCardView cardView;
    private TextView titleText;
    private TextView dateText;
    private TextView opponentText;
    private Button playMatchButton;

    public NextMatchCardController(View card, Context context) {
        c = context;

        titleText = card.findViewById(R.id.nextMatchCardHeader);
        cardView = card.findViewById(R.id.nextMatchCard);
        dateText = card.findViewById(R.id.nextMatchCardDate);
        opponentText = card.findViewById(R.id.nextMatchCardOpponent);
        playMatchButton = card.findViewById(R.id.nextMatchCardPlayMatchButton);

        int primaryColour = Colours.getUsersPrimaryColour();
        int secondaryColour = Colours.getUsersSecondaryColour();
        cardView.setBackgroundColor(primaryColour);
        titleText.setTextColor(secondaryColour);
        dateText.setTextColor(secondaryColour);
        opponentText.setTextColor(secondaryColour);
        dateText.setTextColor(secondaryColour);
        playMatchButton.setBackgroundColor(secondaryColour);
        playMatchButton.setTextColor(primaryColour);
        addNextMatch();
    }


    public void addNextMatch() {

        int teamID = GameData.getInstance().getTeamID();
        Fixture f = GameDBHandler.getInstance().getNextFixtureForTeam(teamID);

        if (f == null) {
            playMatchButton.setText(c.getResources().getString(R.string.next_season));
            opponentText.setText(c.getResources().getString(R.string.next_season));
            return;
        }

        dateText.setText(DateHelper.formatDateToString(f.getDate()));
        int opponentID;
        if (f.getHomeTeamID() == teamID) {
            opponentID = f.getAwayTeamID();
        } else {
            opponentID = f.getHomeTeamID();
        }
        opponentText.setText(c.getString(R.string.tm_main_menu_opponent, f.getOpponent(teamID).getName(), StringHelper.getNumberWithDateSuffix(Leagues.getPositionInLeague(opponentID, f.getLeague()))));
        if (f.getDate().before(new Date())) {
            playMatchButton.setVisibility(VISIBLE);
        } else {
            playMatchButton.setVisibility(GONE);
        }

    }

    public void addNextSeason() {

    }

}
