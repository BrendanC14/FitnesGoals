package com.cutlerdevelopment.fitnessgoals.ViewControllers;

import android.animation.Animator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
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

public class LeagueTableCardController {

    private Context c;

    private TextView leagueNameText;
    private ListView leagueTableHolder;
    private Button leagueExpandCollapseButton;
    private Button leagueUpLeagueButton;
    private Button leagueDownLeagueButton;
    private FullTableRowAdapter adapter;

    private int leagueToDisplay;
    private int teamsPosition;
    private int usersLeague;
    private boolean leagueCollapsed = false;

    private Team usersTeam;

    public LeagueTableCardController(View card, Context context) {
        c = context;

        leagueTableHolder = card.findViewById(R.id.leagueTableCardList);
        leagueExpandCollapseButton = card.findViewById(R.id.leagueTableCardExpandCollapseButton);
        leagueNameText = card.findViewById(R.id.leagueTableCardHeader);
        leagueUpLeagueButton = card.findViewById(R.id.leagueTableCardUp);
        leagueDownLeagueButton = card.findViewById(R.id.leagueTableCardDown);

        leagueNameText.setText(Leagues.getLeagueName(leagueToDisplay));

        usersTeam = CareerSavedData.getInstance().getTeamFromID(CareerSettings.getInstance().getTeamID());
        usersLeague = usersTeam.getLeague();
        leagueToDisplay = usersLeague;
        teamsPosition = Leagues.getPositionInLeague(usersTeam.getID(), leagueToDisplay);

        leagueExpandCollapseButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                animateLeagueTable();
            }
        });

        leagueUpLeagueButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                upALeagueTable();
            }
        });
        leagueDownLeagueButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                downALeagueTable();
            }
        });

        fillLeagueTableDisplay();
        checkLeagueButtonValidity();
        animateLeagueTable();
    }


    private void fillLeagueTableDisplay() {

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

        adapter = new FullTableRowAdapter(c, leagueTableItems);
        leagueTableHolder.setAdapter(adapter);
    }

    private  void animateLeagueTable() {

        int newTableHeight = 0;
        int rowHeight = (int) c.getResources().getDimension(R.dimen.full_table_row_height) + leagueTableHolder.getDividerHeight();
        if (leagueCollapsed) {
            newTableHeight = rowHeight * Numbers.TM_MAIN_MENU_BIG_TABLE_ROW_NUMBER;
            leagueExpandCollapseButton.setText(R.string.collapse_league_table);
        }
        else {
            newTableHeight = rowHeight * Numbers.TM_MAIN_MENU_SMALL_TABLE_ROW_NUMBER;
            leagueExpandCollapseButton.setText(R.string.expand_league_table);
        }

        updateLeagueTable(newTableHeight);
    }

    private void updateLeagueTable(int newTableHeight) {

        ValueAnimator expandLeagueTable = ValueAnimator.ofInt(
                leagueTableHolder.getMeasuredHeight(),
                newTableHeight);
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
            public void onAnimationStart(Animator animator) { }
            @Override
            public void onAnimationEnd(Animator animator) {
                if (leagueCollapsed && leagueToDisplay == usersLeague) {
                    leagueTableHolder.smoothScrollToPosition(teamsPosition + 1);
                }
            }
            @Override
            public void onAnimationCancel(Animator animator) { }
            @Override
            public void onAnimationRepeat(Animator animator) { }
        });
        leagueCollapsed = !leagueCollapsed;
        expandLeagueTable.setDuration(Numbers.TM_MAIN_MENU_EXPAND_TABLE_ANIM_DURATION);
        expandLeagueTable.start();
    }


    private  void upALeagueTable() {
        leagueToDisplay--;
        leagueNameText.setText(Leagues.getLeagueName(leagueToDisplay));
        fillLeagueTableDisplay();
        checkLeagueButtonValidity();

    }
    private  void downALeagueTable() {
        leagueToDisplay++;
        leagueNameText.setText(Leagues.getLeagueName(leagueToDisplay));
        fillLeagueTableDisplay();
        checkLeagueButtonValidity();
    }

    private  void checkLeagueButtonValidity() {
        if (leagueToDisplay == Leagues.TOP_LEAGUE) {
            leagueUpLeagueButton.setVisibility(View.INVISIBLE);
        }
        else {
            leagueUpLeagueButton.setVisibility(View.VISIBLE);
        }

        if (leagueToDisplay == Leagues.BOTTOM_LEAGUE) {
            leagueDownLeagueButton.setVisibility(View.INVISIBLE);
        }
        else {
            leagueDownLeagueButton.setVisibility(View.VISIBLE);
        }
    }

    public void playMatch() {


        fillLeagueTableDisplay();
        teamsPosition = Leagues.getPositionInLeague(usersTeam.getID(), leagueToDisplay);
        leagueTableHolder.smoothScrollToPosition(teamsPosition + 1);


    }

}
