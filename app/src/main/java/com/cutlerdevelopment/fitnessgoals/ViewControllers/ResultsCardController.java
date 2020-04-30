package com.cutlerdevelopment.fitnessgoals.ViewControllers;

import android.animation.ValueAnimator;
import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.cutlerdevelopment.fitnessgoals.Constants.Colours;
import com.cutlerdevelopment.fitnessgoals.Constants.Leagues;
import com.cutlerdevelopment.fitnessgoals.Constants.Numbers;
import com.cutlerdevelopment.fitnessgoals.Models.Fixture;
import com.cutlerdevelopment.fitnessgoals.Models.Team;
import com.cutlerdevelopment.fitnessgoals.R;
import com.cutlerdevelopment.fitnessgoals.SavedData.CareerSavedData;
import com.cutlerdevelopment.fitnessgoals.Settings.CareerSettings;
import com.cutlerdevelopment.fitnessgoals.Utils.DateHelper;
import com.cutlerdevelopment.fitnessgoals.Utils.StringHelper;
import com.cutlerdevelopment.fitnessgoals.ViewAdapters.ResultItemRowAdapter;
import com.cutlerdevelopment.fitnessgoals.ViewItems.ResultItem;
import com.google.android.material.card.MaterialCardView;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static android.view.View.GONE;
import static android.view.View.VISIBLE;

public class ResultsCardController {

    private Context c;
    MaterialCardView cardView;
    private Button viewModeButton;
    private  TextView leagueNameText;
    private ListView resultTableHolder;
    private Button resultExpandCollapseButton;
    private Button upLeagueButton;
    private Button downLeagueButton;
    private ResultItemRowAdapter resultAdapter;


    private boolean myResultMode;
    private boolean resultCollapsed;
    private int leagueToDisplay;
    private Team usersTeam;

    public ResultsCardController(View card, Context context, int usersLeague) {
        c = context;

        cardView = card.findViewById(R.id.resultsCardResultsCard);
        viewModeButton = card.findViewById(R.id.resultsCardSwitchButton);
        leagueNameText = card.findViewById(R.id.resultsCardHeader);
        resultTableHolder = card.findViewById(R.id.resultsCardList);
        resultExpandCollapseButton = card.findViewById(R.id.resultsCardExpandCollapseButton);
        upLeagueButton = card.findViewById(R.id.resultsCardLeagueLeft);
        downLeagueButton = card.findViewById(R.id.resultsCardLeagueRight);


        resultExpandCollapseButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                animateResults();
            }
        });
        viewModeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                myResultMode = !myResultMode;
                if (myResultMode) {
                    viewModeButton.setText(R.string.tm_main_menu_switch_to_league_results);
                    upLeagueButton.setVisibility(View.INVISIBLE);
                    downLeagueButton.setVisibility(View.INVISIBLE);
                }
                else {
                    viewModeButton.setText(R.string.tm_main_menu_switch_to_my_results);
                    checkResultLeagueButtonValidity();
                }
                fillResultDisplay();
            }
        });
        upLeagueButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                upAResultLeague();
            }
        });
        downLeagueButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                downAResultLeague();
            }
        });
        usersTeam = CareerSavedData.getInstance().getTeamFromID(CareerSettings.getInstance().getTeamID());
        leagueToDisplay = usersLeague;

        int primaryColour = Colours.getUsersPrimaryColour();
        int secondaryColour = Colours.getUsersSecondaryColour();
        cardView.setBackgroundColor(primaryColour);
        viewModeButton.setTextColor(secondaryColour);
        leagueNameText.setTextColor(secondaryColour);
        resultExpandCollapseButton.setTextColor(secondaryColour);
        upLeagueButton.setTextColor(secondaryColour);
        downLeagueButton.setTextColor(secondaryColour);

        fillResultDisplay();
        animateResults();
        checkResultLeagueButtonValidity();
    }

    private void fillResultDisplay() {
        final ArrayList<ResultItem> resultItems = new ArrayList<>();

        List<Fixture> results;

        if (myResultMode) {
            results = CareerSavedData.getInstance().getAllResultsForTeam(usersTeam.getID());
            leagueNameText.setText(R.string.my_results_mode);
            Collections.reverse(results);
        }
        else {
            leagueNameText.setText(c.getString(R.string.league_results_mode, Leagues.getLeagueName(leagueToDisplay)));
            Fixture lastResult = CareerSavedData.getInstance().getLastResultForTeam(usersTeam.getID());
            if (lastResult == null) {
                return;
            }
            results = CareerSavedData.getInstance().getWeeksResultsFromLeague(lastResult.getDate(), leagueToDisplay);
            Collections.sort(results);
        }

        if (results.size() >= Numbers.TM_MAIN_MENU_SMALL_FIXTURES_TABLE_ROW_NUMBER) { resultExpandCollapseButton.setVisibility(VISIBLE); }
        else { resultExpandCollapseButton.setVisibility(GONE); }
        for (Fixture f : results) {
            ResultItem item = new ResultItem();
            item.setDate(DateHelper.formatDateToString(f.getDate()));
            int homePos = Leagues.getPositionInLeague(f.getHomeTeamID(), f.getLeague());
            int awayPos = Leagues.getPositionInLeague(f.getAwayTeamID(), f.getLeague());
            item.setHomePosition(StringHelper.getNumberWithDateSuffix(homePos));
            item.setAwayPosition(StringHelper.getNumberWithDateSuffix(awayPos));
            item.setHomeTeam(CareerSavedData.getInstance().getTeamFromID(f.getHomeTeamID()).getName());
            item.setAwayTeam(CareerSavedData.getInstance().getTeamFromID(f.getAwayTeamID()).getName());
            item.setHomeScore(String.valueOf(f.getHomeScore()));
            item.setAwayScore(String.valueOf(f.getAwayScore()));
            if (myResultMode) {
                item.setResult(f.getMatchResultForTeam(usersTeam.getID()));
            }
            resultItems.add(item);
        }
        resultAdapter = new ResultItemRowAdapter(c, resultItems);
        resultTableHolder.setAdapter(resultAdapter);
    }

    private void animateResults() {
        int newTableHeight = 0;
        int rowHeight = (int) c.getResources().getDimension(R.dimen.full_table_row_height) + resultTableHolder.getDividerHeight();

        if (resultCollapsed) {
            newTableHeight = rowHeight * Numbers.TM_MAIN_MENU_BIG_FIXTURES_TABLE_ROW_NUMBER;
            resultExpandCollapseButton.setText(R.string.collapse_league_table);
        }
        else {
            newTableHeight = rowHeight * Numbers.TM_MAIN_MENU_SMALL_FIXTURES_TABLE_ROW_NUMBER;
            resultExpandCollapseButton.setText(R.string.expand_league_table);
        }
        ValueAnimator expandLeagueTable = ValueAnimator.ofInt(
                resultTableHolder.getMeasuredHeight(),
                newTableHeight);
        expandLeagueTable.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator valueAnimator) {
                int val = (Integer) valueAnimator.getAnimatedValue();
                ViewGroup.LayoutParams layoutParams = resultTableHolder.getLayoutParams();
                layoutParams.height = val;
                resultTableHolder.setLayoutParams(layoutParams);
            }
        });
        resultCollapsed = !resultCollapsed;
        expandLeagueTable.setDuration(Numbers.TM_MAIN_MENU_EXPAND_TABLE_ANIM_DURATION);
        expandLeagueTable.start();
    }

    public void upAResultLeague() {
        leagueToDisplay--;
        fillResultDisplay();
        checkResultLeagueButtonValidity();

    }
    public void downAResultLeague() {
        leagueToDisplay++;
        fillResultDisplay();
        checkResultLeagueButtonValidity();

    }

    private void checkResultLeagueButtonValidity() {
        if (leagueToDisplay == Leagues.TOP_LEAGUE) {
            upLeagueButton.setVisibility(View.INVISIBLE);
        }
        else {
            upLeagueButton.setVisibility(VISIBLE);
        }

        if (leagueToDisplay == Leagues.BOTTOM_LEAGUE) {
            downLeagueButton.setVisibility(View.INVISIBLE);
        }
        else {
            downLeagueButton.setVisibility(VISIBLE);
        }

    }

    public void playMatch() {

        fillResultDisplay();
    }
}
