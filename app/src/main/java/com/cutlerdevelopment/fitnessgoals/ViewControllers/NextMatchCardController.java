package com.cutlerdevelopment.fitnessgoals.ViewControllers;

import android.content.Context;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.cutlerdevelopment.fitnessgoals.Constants.Leagues;
import com.cutlerdevelopment.fitnessgoals.Models.Fixture;
import com.cutlerdevelopment.fitnessgoals.R;
import com.cutlerdevelopment.fitnessgoals.SavedData.CareerSavedData;
import com.cutlerdevelopment.fitnessgoals.Settings.CareerSettings;
import com.cutlerdevelopment.fitnessgoals.Utils.DateHelper;
import com.cutlerdevelopment.fitnessgoals.Utils.StringHelper;

import java.util.Date;

import static android.view.View.GONE;
import static android.view.View.VISIBLE;

public class NextMatchCardController {

    private Context c;

    private TextView nextFixtureDateText;
    private TextView nextFixtureOpponentText;
    private Button playMatchButton;

    public NextMatchCardController(View card, Context context) {
        c = context;


        nextFixtureDateText = card.findViewById(R.id.nextMatchCardDate);
        nextFixtureOpponentText = card.findViewById(R.id.nextMatchCardOpponent);
        playMatchButton = card.findViewById(R.id.nextMatchCardPlayMatchButton);

        addNextMatch();
    }


    public void addNextMatch() {

        int teamID = CareerSettings.getInstance().getTeamID();
        Fixture f = CareerSavedData.getInstance().getNextFixtureForTeam(teamID);

        if (f == null) {
            playMatchButton.setVisibility(GONE);
            return;
        }

        nextFixtureDateText.setText(DateHelper.formatDateToString(f.getDate()));
        int opponentID;
        if (f.getHomeTeamID() == teamID) {
            opponentID = f.getAwayTeamID();
        } else {
            opponentID = f.getHomeTeamID();
        }
        nextFixtureOpponentText.setText(c.getString(R.string.tm_main_menu_opponent, f.getOpponent(teamID).getName(), StringHelper.getNumberWithDateSuffix(Leagues.getPositionInLeague(opponentID, f.getLeague()))));
        if (f.getDate().before(new Date())) {
            playMatchButton.setVisibility(VISIBLE);
        } else {
            playMatchButton.setVisibility(GONE);
        }

    }
}
