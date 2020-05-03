package com.cutlerdevelopment.fitnessgoals.DIalogFragments;

import android.app.AlertDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import androidx.fragment.app.DialogFragment;

import com.cutlerdevelopment.fitnessgoals.Constants.Colours;
import com.cutlerdevelopment.fitnessgoals.Constants.Leagues;
import com.cutlerdevelopment.fitnessgoals.Models.Fixture;
import com.cutlerdevelopment.fitnessgoals.R;
import com.cutlerdevelopment.fitnessgoals.SavedData.GameDBHandler;
import com.cutlerdevelopment.fitnessgoals.Data.GameData;
import com.cutlerdevelopment.fitnessgoals.Utils.StringHelper;
import com.cutlerdevelopment.fitnessgoals.ViewAdapters.ResultItemRowAdapter;
import com.cutlerdevelopment.fitnessgoals.ViewItems.ResultItem;

import java.util.ArrayList;
import java.util.Date;

public class MatchResult extends DialogFragment {

    private LinearLayout layout;
    private TextView header;
    private TextView homeTeamText;
    private TextView homeTeamScore;
    private TextView awayTeamText;
    private TextView awayTeamScore;
    private TextView leagueNameText;

    private Button upALeagueButton;
    private Button downALeagueButton;
    private Button doneButton;

    private ListView otherResultsList;

    int leagueToDisplay;
    Date fixtureDate;

    ResultItemRowAdapter adapter;


    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = requireActivity().getLayoutInflater();
        View resultView = inflater.inflate(R.layout.match_result, null);

        layout = resultView.findViewById(R.id.matchResultLayout);
        header = resultView.findViewById(R.id.matchResultsTitle);
        homeTeamText = resultView.findViewById(R.id.matchResultsHomeTeam);
        homeTeamScore = resultView.findViewById(R.id.matchResultsHomeScore);
        awayTeamText = resultView.findViewById(R.id.matchResultsAwayTeam);
        awayTeamScore = resultView.findViewById(R.id.matchResultsAwayScore);
        otherResultsList = resultView.findViewById(R.id.matchResultsList);
        leagueNameText = resultView.findViewById(R.id.matchResultsLeagueHeader);
        upALeagueButton = resultView.findViewById(R.id.matchResultsLeagueUp);
        downALeagueButton = resultView.findViewById(R.id.matchResultsLeagueDown);
        doneButton = resultView.findViewById(R.id.matchResultDoneButton);
        Button closeButton = resultView.findViewById(R.id.matchResultCloseButton);

        Fixture userResult = GameDBHandler.getInstance().getLastResultForTeam(GameData.getInstance().getTeamID());
        fixtureDate = userResult.getDate();
        homeTeamText.setText(GameDBHandler.getInstance().getTeamFromID(userResult.getHomeTeamID()).getName());
        homeTeamScore.setText(String.valueOf(userResult.getHomeScore()));
        awayTeamScore.setText(String.valueOf(userResult.getAwayScore()));
        awayTeamText.setText(GameDBHandler.getInstance().getTeamFromID(userResult.getAwayTeamID()).getName());

        leagueToDisplay = userResult.getLeague();
        leagueNameText.setText(Leagues.getLeagueName(leagueToDisplay));
        fillLeaguesResults();
        otherResultsList.setAdapter(adapter);

        upALeagueButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                upALeague();
                otherResultsList.setAdapter(adapter);
            }
        });
        downALeagueButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                downALeague();
                otherResultsList.setAdapter(adapter);
            }
        });

        doneButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                donePressed();
            }
        });

        int primaryColour = Colours.getUsersPrimaryColour();
        int secondaryColour = Colours.getUsersSecondaryColour();

        layout.setBackgroundColor(primaryColour);
        header.setTextColor(secondaryColour);
        homeTeamText.setTextColor(secondaryColour);
        homeTeamScore.setTextColor(secondaryColour);
        awayTeamScore.setTextColor(secondaryColour);
        awayTeamText.setTextColor(secondaryColour);
        upALeagueButton.setTextColor(secondaryColour);
        downALeagueButton.setTextColor(secondaryColour);
        leagueNameText.setTextColor(secondaryColour);
        doneButton.setBackgroundColor(secondaryColour);
        doneButton.setTextColor(primaryColour);
        closeButton.setTextColor(secondaryColour);
        closeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dismiss();
            }
        });

        builder.setView(resultView);
        return builder.create();
    }

    void fillLeaguesResults() {

        final ArrayList<ResultItem> resultItems = new ArrayList<>();

        for (Fixture f : GameDBHandler.getInstance().getWeeksResultsFromLeague(fixtureDate, leagueToDisplay)) {
            ResultItem item = new ResultItem();
            item.setDate("");
            item.setHomeTeam(GameDBHandler.getInstance().getTeamFromID(f.getHomeTeamID()).getName());
            item.setHomeScore(String.valueOf(f.getHomeScore()));
            item.setAwayScore(String.valueOf(f.getAwayScore()));
            item.setAwayTeam(GameDBHandler.getInstance().getTeamFromID(f.getAwayTeamID()).getName());

            int homePos = Leagues.getPositionInLeague(f.getHomeTeamID(), f.getLeague());
            int awayPos = Leagues.getPositionInLeague(f.getAwayTeamID(), f.getLeague());
            item.setHomePosition(StringHelper.getNumberWithDateSuffix(homePos));
            item.setAwayPosition(StringHelper.getNumberWithDateSuffix(awayPos));

            resultItems.add(item);
        }

        adapter = new ResultItemRowAdapter(getActivity().getApplicationContext(), resultItems);
        adapter.notifyDataSetChanged();
    }

    public void upALeague() {
        leagueToDisplay--;
        leagueNameText.setText(Leagues.getLeagueName(leagueToDisplay));
        fillLeaguesResults();
        checkLeagueButtonValidity();

    }
    public void downALeague() {
        leagueToDisplay++;
        leagueNameText.setText(Leagues.getLeagueName(leagueToDisplay));
        fillLeaguesResults();
        checkLeagueButtonValidity();
    }

    public void checkLeagueButtonValidity() {
        if (leagueToDisplay == Leagues.TOP_LEAGUE) {
            upALeagueButton.setEnabled(false);
        }
        else {
            upALeagueButton.setEnabled(true);
        }

        if (leagueToDisplay == Leagues.BOTTOM_LEAGUE) {
            downALeagueButton.setEnabled(false);
        }
        else {
            downALeagueButton.setEnabled(true);
        }
    }

    void donePressed() {
        dismiss();
    }
}
