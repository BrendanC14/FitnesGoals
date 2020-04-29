package com.cutlerdevelopment.fitnessgoals.ViewControllers;

import android.animation.ValueAnimator;
import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.cutlerdevelopment.fitnessgoals.Constants.Leagues;
import com.cutlerdevelopment.fitnessgoals.Constants.MatchResult;
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

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static android.view.View.GONE;
import static android.view.View.VISIBLE;

public class FixturesCardController {

    private Context c;

    private Button viewModeButton;
    private TextView leagueNameText;
    private ListView fixtureTableHolder;
    private Button fixtureExpandCollapseButton;
    private Button upLeagueButton;
    private Button downLeagueButton;
    private ResultItemRowAdapter fixtureAdapter;

    private int leagueToDisplay;
    private boolean fixtureCollapsed;
    private boolean myFixtureMode;
    private Team usersTeam;

    public FixturesCardController(View card, Context context, int usersLeague) {
        c = context;
        viewModeButton = card.findViewById(R.id.fixturesCardSwitchButton);
        leagueNameText = card.findViewById(R.id.fixturesCardHeader);
        fixtureTableHolder = card.findViewById(R.id.fixturesCardList);
        fixtureExpandCollapseButton = card.findViewById(R.id.fixturesCardExpandCollapseButton);
        upLeagueButton = card.findViewById(R.id.fixturesCardLeagueLeft);
        downLeagueButton = card.findViewById(R.id.fixturesCardLeagueRight);

        usersTeam = CareerSavedData.getInstance().getTeamFromID(CareerSettings.getInstance().getTeamID());

        leagueToDisplay = usersLeague;
        fixtureExpandCollapseButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                animateFixtures();
            }
        });
        viewModeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                myFixtureMode = !myFixtureMode;
                if (myFixtureMode) {
                    viewModeButton.setText(R.string.tm_main_menu_switch_to_league);
                    upLeagueButton.setVisibility(View.INVISIBLE);
                    downLeagueButton.setVisibility(View.INVISIBLE);
                }
                else {
                    viewModeButton.setText(R.string.tm_main_menu_switch_to_my_fixtures);
                    checkFixtureLeagueButtonValidity();
                }
                fillFixtureDisplay();
            }
        });
        upLeagueButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                upAFixtureLeague();
            }
        });
        downLeagueButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                downAFixtureLeague();
            }
        });

        fillFixtureDisplay();
        checkFixtureLeagueButtonValidity();
        animateFixtures();
    }



    private void fillFixtureDisplay() {
        final ArrayList<ResultItem> fixtureItems = new ArrayList<>();

        List<Fixture> fixtures;

        if (myFixtureMode) {
            fixtures = CareerSavedData.getInstance().getAllUpcomingFixturesForTeam(usersTeam.getID());
            leagueNameText.setText(R.string.my_fixtures_mode);
        }
        else {
            Fixture nextFixture = CareerSavedData.getInstance().getNextFixtureForTeam(usersTeam.getID());
            fixtures = CareerSavedData.getInstance().getWeeksFixtureFromLeague(nextFixture.getDate(), leagueToDisplay);
            leagueNameText.setText(c.getString(R.string.league_fixtures_mode, Leagues.getLeagueName(leagueToDisplay)));
        }

        if (fixtures.size() >= Numbers.TM_MAIN_MENU_SMALL_FIXTURES_TABLE_ROW_NUMBER) { fixtureExpandCollapseButton.setVisibility(VISIBLE); }
        else { fixtureExpandCollapseButton.setVisibility(GONE); }
        Collections.sort(fixtures);
        for (Fixture f : fixtures) {
            ResultItem item = new ResultItem();
            item.setDate(DateHelper.formatDateToString(f.getDate()));
            int homePos = Leagues.getPositionInLeague(f.getHomeTeamID(), f.getLeague());
            int awayPos = Leagues.getPositionInLeague(f.getAwayTeamID(), f.getLeague());
            item.setHomePosition(StringHelper.getNumberWithDateSuffix(homePos));
            item.setAwayPosition(StringHelper.getNumberWithDateSuffix(awayPos));
            item.setHomeTeam(CareerSavedData.getInstance().getTeamFromID(f.getHomeTeamID()).getName());
            item.setAwayTeam(CareerSavedData.getInstance().getTeamFromID(f.getAwayTeamID()).getName());
            item.setHomeScore("");
            item.setAwayScore("");
            if (!myFixtureMode && (f.getHomeTeamID() == usersTeam.getID() || f.getAwayTeamID() == usersTeam.getID())) {
                //Bit hacky, but sets the background to a brighter green so easier to see.
                item.setResult(MatchResult.WIN);
            }
            fixtureItems.add(item);
        }
        fixtureAdapter = new ResultItemRowAdapter(c, fixtureItems);
        fixtureTableHolder.setAdapter(fixtureAdapter);
    }


    private void animateFixtures() {
        int newTableHeight = 0;
        int rowHeight = (int) c.getResources().getDimension(R.dimen.full_table_row_height) + fixtureTableHolder.getDividerHeight();

        if (fixtureCollapsed) {
            newTableHeight = rowHeight * Numbers.TM_MAIN_MENU_BIG_FIXTURES_TABLE_ROW_NUMBER;
            fixtureExpandCollapseButton.setText(R.string.collapse_league_table);
        }
        else {
            newTableHeight = rowHeight * Numbers.TM_MAIN_MENU_SMALL_FIXTURES_TABLE_ROW_NUMBER;
            fixtureExpandCollapseButton.setText(R.string.expand_league_table);
        }
        ValueAnimator expandLeagueTable = ValueAnimator.ofInt(
                fixtureTableHolder.getMeasuredHeight(),
                newTableHeight);
        expandLeagueTable.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator valueAnimator) {
                int val = (Integer) valueAnimator.getAnimatedValue();
                ViewGroup.LayoutParams layoutParams = fixtureTableHolder.getLayoutParams();
                layoutParams.height = val;
                fixtureTableHolder.setLayoutParams(layoutParams);
            }
        });
        fixtureCollapsed = !fixtureCollapsed;
        expandLeagueTable.setDuration(Numbers.TM_MAIN_MENU_EXPAND_TABLE_ANIM_DURATION);
        expandLeagueTable.start();
    }


    private  void upAFixtureLeague() {
        leagueToDisplay--;
        fillFixtureDisplay();
        checkFixtureLeagueButtonValidity();

    }
    private  void downAFixtureLeague() {
        leagueToDisplay++;
        fillFixtureDisplay();
        checkFixtureLeagueButtonValidity();

    }

    private void checkFixtureLeagueButtonValidity() {
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

        fillFixtureDisplay();
    }
}
