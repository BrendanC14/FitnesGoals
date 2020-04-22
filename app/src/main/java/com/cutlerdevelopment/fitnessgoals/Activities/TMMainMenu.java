package com.cutlerdevelopment.fitnessgoals.Activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.animation.Animator;
import android.animation.ValueAnimator;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;

import com.cutlerdevelopment.fitnessgoals.Constants.Leagues;
import com.cutlerdevelopment.fitnessgoals.Constants.Numbers;
import com.cutlerdevelopment.fitnessgoals.Models.Team;
import com.cutlerdevelopment.fitnessgoals.R;
import com.cutlerdevelopment.fitnessgoals.SavedData.CareerSavedData;
import com.cutlerdevelopment.fitnessgoals.Settings.CareerSettings;
import com.cutlerdevelopment.fitnessgoals.ViewAdapters.FullTableRowAdapter;
import com.cutlerdevelopment.fitnessgoals.ViewItems.FullTableRow;

import java.util.ArrayList;
import java.util.List;

public class TMMainMenu extends AppCompatActivity {

    ConstraintLayout teamHeaderBackground;
    TextView teamNameText;
    TextView leagueNameText;
    ListView leagueTableHolder;
    Button expandCollapseButton;
    ImageButton upLeagueButton;
    ImageButton downLeagueButton;

    FullTableRowAdapter adapter;

    Team usersTeam;
    int teamsPosition;
    int usersLeague;
    int leagueToDisplay;

    Boolean leagueCollapsed;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.tm_main_menu);
        teamHeaderBackground = findViewById(R.id.tmMainMenuBackgroundColour);
        teamNameText = findViewById(R.id.tmMainMenuTeamName);
        leagueTableHolder = findViewById(R.id.tmMainMenuTableItemList);
        expandCollapseButton = findViewById(R.id.tmMainMenuExpandCollapseButton);
        leagueNameText = findViewById(R.id.tmMainMenuLeagueHeader);
        upLeagueButton = findViewById(R.id.tmMainMenuLeagueUp);
        downLeagueButton = findViewById(R.id.tmMainMenuLeagueDown);

        usersTeam = CareerSavedData.getInstance().getTeamFromID(CareerSettings.getInstance().getTeamID());
        usersLeague = usersTeam.getLeague();
        leagueCollapsed = false;

        teamNameText.setText(usersTeam.getName());
        leagueNameText.setText(Leagues.getLeagueName(leagueToDisplay));
        teamHeaderBackground.setBackgroundColor(Color.parseColor(usersTeam.getColour()));
        leagueToDisplay = usersTeam.getLeague();
        teamsPosition = Leagues.getPositionInLeague(usersTeam.getID(), leagueToDisplay);

        expandCollapseButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                animateLeagueTable();
            }
        });

        fillLeagueTableDisplay();
        checkLeagueButtonValidity();
        animateLeagueTable();
    }

    void fillLeagueTableDisplay() {

        final ArrayList<FullTableRow> leagueTableItems = new ArrayList<>();


        List<Team> allTeamsInLeague = Leagues.getLeagueList(leagueToDisplay);
        for (Team team : allTeamsInLeague) {
            FullTableRow item = new FullTableRow();
            item.setPosition(String.valueOf(Leagues.getPositionInLeague(team.getID(), team.getLeague())));
            item.setTeamName(team.getName());
            item.setGamesPlayed(String.valueOf(team.getGamesPlayed()));
            item.setWins(String.valueOf(team.getWins()));
            item.setDraws(String.valueOf(team.getDraws()));
            item.setLosses(String.valueOf(team.getLosses()));
            item.setScored(String.valueOf(team.getScored()));
            item.setConceded(String.valueOf(team.getConceded()));
            item.setGoalDifference(String.valueOf(team.getGoalDifference()));
            item.setPoints(String.valueOf(team.getPoints()));

            item.setPromotionPlace(Leagues.isPromotionPlace(team.getID(), team.getLeague()));
            item.setRelegationPlace(Leagues.isRelegationPlace(team.getID(), team.getLeague()));
            if (team.getID() == usersTeam.getID()) { item.setBoldTeam(true); }
            else { item.setBoldTeam(false); }

            leagueTableItems.add(item);
        }

        adapter = new FullTableRowAdapter(this, leagueTableItems);
        leagueTableHolder.setAdapter(adapter);
    }

    public void animateLeagueTable() {

        int newHeight = 0;
        //Gets the height of one
        int rowHeight = (int) getResources().getDimension(R.dimen.full_table_row_height) + leagueTableHolder.getDividerHeight();
        if (leagueCollapsed) {
            newHeight = rowHeight * Numbers.TM_MAIN_MENU_BIG_TABLE_ROW_NUMBER;
            expandCollapseButton.setText(getString(R.string.collapse_league_table));
        }
        else {
            newHeight = rowHeight * Numbers.TM_MAIN_MENU_SMALL_TABLE_ROW_NUMBER;
            expandCollapseButton.setText(getString(R.string.expand_league_table));
        }

        ValueAnimator expandLeagueTable = ValueAnimator.ofInt(
                leagueTableHolder.getMeasuredHeight(),
                newHeight);
        expandLeagueTable.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator valueAnimator) {
                int val = (Integer) valueAnimator.getAnimatedValue();
                ViewGroup.LayoutParams layoutParams = leagueTableHolder.getLayoutParams();
                layoutParams.height = val;
                leagueTableHolder.setLayoutParams(layoutParams);
            }
        });
        expandLeagueTable.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animator) {
            }

            @Override
            public void onAnimationEnd(Animator animator) {
                if (leagueCollapsed && leagueToDisplay == usersLeague) {
                    leagueTableHolder.smoothScrollToPosition(teamsPosition + 1);
                }

            }

            @Override
            public void onAnimationCancel(Animator animator) {

            }

            @Override
            public void onAnimationRepeat(Animator animator) {

            }
        });


        expandLeagueTable.setDuration(Numbers.TM_MAIN_MENU_EXPAND_TABLE_ANIM_DURATION);
        expandLeagueTable.start();
        leagueCollapsed = !leagueCollapsed;
    }

    public void upALeague(View view) {
        leagueToDisplay--;
        leagueNameText.setText(Leagues.getLeagueName(leagueToDisplay));
        fillLeagueTableDisplay();
        checkLeagueButtonValidity();

    }
    public void downALeague(View view) {
        leagueToDisplay++;
        leagueNameText.setText(Leagues.getLeagueName(leagueToDisplay));
        fillLeagueTableDisplay();
        checkLeagueButtonValidity();
    }

    public void checkLeagueButtonValidity() {
        if (leagueToDisplay == Leagues.TOP_LEAGUE) {
            upLeagueButton.setEnabled(false);
        }
        else {
            upLeagueButton.setEnabled(true);
        }

        if (leagueToDisplay == Leagues.BOTTOM_LEAGUE) {
            downLeagueButton.setEnabled(false);
        }
        else {
            downLeagueButton.setEnabled(true);
        }
    }

}
