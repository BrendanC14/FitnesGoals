package com.cutlerdevelopment.fitnessgoals.ViewControllers;

import android.animation.Animator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.os.Build;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import androidx.constraintlayout.widget.ConstraintLayout;

import com.cutlerdevelopment.fitnessgoals.Constants.Colours;
import com.cutlerdevelopment.fitnessgoals.Constants.Leagues;
import com.cutlerdevelopment.fitnessgoals.Constants.Numbers;
import com.cutlerdevelopment.fitnessgoals.Models.Team;
import com.cutlerdevelopment.fitnessgoals.R;
import com.cutlerdevelopment.fitnessgoals.SavedData.GameDBHandler;
import com.cutlerdevelopment.fitnessgoals.Data.GameData;
import com.cutlerdevelopment.fitnessgoals.ViewAdapters.FullTableRowAdapter;
import com.cutlerdevelopment.fitnessgoals.ViewItems.FullTableRow;
import com.google.android.material.card.MaterialCardView;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.List;

public class LeagueTableCardController {

    private Context c;

    MaterialCardView cardView;
    private LinearLayout leagueTableLayout;
    private TextView leagueNameText;
    private ListView leagueTableHolder;
    private Button leagueExpandCollapseButton;
    private Button leagueUpLeagueButton;
    private Button leagueDownLeagueButton;
    private LinearLayout leagueTableHeader;
    private FullTableRowAdapter adapter;

    private int leagueToDisplay;
    private int teamsPosition;
    private int usersLeague;
    private boolean leagueCollapsed = false;

    private Team usersTeam;

    public LeagueTableCardController(View card, Context context) {
        c = context;

        cardView = card.findViewById(R.id.leagueTableCard);
        leagueTableLayout = card.findViewById(R.id.leagueTableLayout);
        leagueTableHolder = card.findViewById(R.id.leagueTableCardList);
        leagueExpandCollapseButton = card.findViewById(R.id.leagueTableCardExpandCollapseButton);
        leagueNameText = card.findViewById(R.id.leagueTableCardHeader);
        leagueUpLeagueButton = card.findViewById(R.id.leagueTableCardUp);
        leagueDownLeagueButton = card.findViewById(R.id.leagueTableCardDown);
        leagueTableHeader = card.findViewById(R.id.leagueTableHeader);


        usersTeam = GameData.getInstance().getUsersTeam();
        usersLeague = usersTeam.getLeague();
        leagueToDisplay = usersLeague;
        teamsPosition = Leagues.getPositionInLeague(usersTeam.getID(), leagueToDisplay);
        leagueNameText.setText(Leagues.getLeagueName(leagueToDisplay));

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

        int primaryColour = Colours.getUsersPrimaryColour();
        int secondaryColour = Colours.getUsersSecondaryColour();
        cardView.setBackgroundColor(primaryColour);
        leagueDownLeagueButton.setTextColor(secondaryColour);
        leagueExpandCollapseButton.setTextColor(secondaryColour);
        leagueNameText.setTextColor(secondaryColour);
        leagueUpLeagueButton.setTextColor(secondaryColour);
        for (int i = 0; i < leagueTableHeader.getChildCount(); i++) {
            TextView text = (TextView) leagueTableHeader.getChildAt(i);
            text.setTextColor(secondaryColour);
        }

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

        Animation slideOutRight = AnimationUtils.loadAnimation(c,
                R.anim.slide_out_right);
        Animation slideInLeft = AnimationUtils.loadAnimation(c,
                R.anim.slide_in_left);

        ViewGroup.LayoutParams listParams = leagueTableHolder.getLayoutParams();

        leagueTableHolder.startAnimation(slideOutRight);
        leagueTableLayout.removeView(leagueTableHolder);
        ListView newListView = new ListView(c);
        newListView.setId(View.generateViewId());
        newListView.setLayoutParams(listParams);
        leagueTableLayout.addView(newListView);
        this.leagueTableHolder = newListView;
        newListView.startAnimation(slideInLeft);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            newListView.setNestedScrollingEnabled(true);
        }

        leagueNameText.setText(Leagues.getLeagueName(leagueToDisplay));
        fillLeagueTableDisplay();
        checkLeagueButtonValidity();

    }
    private  void downALeagueTable() {
        leagueToDisplay++;

        ViewGroup.LayoutParams layoutParams = leagueTableHolder.getLayoutParams();
        leagueTableHolder.setLayoutParams(layoutParams);

        Animation slideOutLeft = AnimationUtils.loadAnimation(c,
                R.anim.slide_out_left);
        Animation slideInRight = AnimationUtils.loadAnimation(c,
                R.anim.slide_in_right);

        leagueTableHolder.startAnimation(slideOutLeft);
        leagueTableLayout.removeView(leagueTableHolder);
        ListView newListView = new ListView(c);
        newListView.setId(View.generateViewId());
        newListView.setLayoutParams(layoutParams);
        leagueTableLayout.addView(newListView);
        this.leagueTableHolder = newListView;
        newListView.startAnimation(slideInRight);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            newListView.setNestedScrollingEnabled(true);
        }

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
