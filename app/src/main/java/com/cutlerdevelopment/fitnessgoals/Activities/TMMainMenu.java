package com.cutlerdevelopment.fitnessgoals.Activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.animation.Animator;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.provider.Telephony;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.view.textclassifier.TextClassificationManager;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.cutlerdevelopment.fitnessgoals.Constants.FilterOptions;
import com.cutlerdevelopment.fitnessgoals.Constants.Leagues;
import com.cutlerdevelopment.fitnessgoals.Constants.Numbers;
import com.cutlerdevelopment.fitnessgoals.Constants.Words;
import com.cutlerdevelopment.fitnessgoals.Integrations.IntegrationConnectionHandler;
import com.cutlerdevelopment.fitnessgoals.Models.Fixture;
import com.cutlerdevelopment.fitnessgoals.Models.Team;
import com.cutlerdevelopment.fitnessgoals.R;
import com.cutlerdevelopment.fitnessgoals.SavedData.AppSavedData;
import com.cutlerdevelopment.fitnessgoals.SavedData.CareerSavedData;
import com.cutlerdevelopment.fitnessgoals.Settings.AppSettings;
import com.cutlerdevelopment.fitnessgoals.Settings.CareerSettings;
import com.cutlerdevelopment.fitnessgoals.Settings.UserActivity;
import com.cutlerdevelopment.fitnessgoals.Utils.DateHelper;
import com.cutlerdevelopment.fitnessgoals.ViewAdapters.FullTableRowAdapter;
import com.cutlerdevelopment.fitnessgoals.ViewItems.FullTableRow;
import com.google.android.material.tabs.TabLayout;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static android.view.View.GONE;
import static android.view.View.VISIBLE;

public class TMMainMenu extends AppCompatActivity implements IntegrationConnectionHandler.TMListener {

    ImageView jamesImage;
    ConstraintLayout speechBubbleLayout;
    TextView speechBubbleText;
    Button speechBubbleButton;
    Button jamesNextButton;
    Button jamesPreviousButton;
    ConstraintLayout backgroundLayout;

    CardView teamHeaderBackground;
    TextView teamNameText;
    TextView leagueNameText;
    ListView leagueTableHolder;
    Button expandCollapseButton;
    ImageButton upLeagueButton;
    ImageButton downLeagueButton;
    FullTableRowAdapter adapter;

    CardView progressCard;
    TabLayout tabLayout;
    ProgressBar stepProgress;
    TextView stepProgressAchieved;
    TextView stepProgressTarget;
    TextView daysAchievedText;
    TextView numDaysText;
    TextView stepsSlash;
    TextView numDaysSlash;
    TextView notEnoughDataText;
    ImageView goldenFootball;

    TextView nextFixtureDateText;
    TextView nextFixtureOpponentText;
    Button playMatchButton;
    CardView footer;

    ImageButton phoneButton;

    Team usersTeam;
    int teamsPosition;
    int usersLeague;
    int leagueToDisplay;
    int tutorialStep = 1;

    boolean leagueCollapsed;
    boolean animatingGoldenFootball;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.tm_main_menu);

        jamesImage = findViewById(R.id.tmMainMenuJamesImage);
        speechBubbleLayout = findViewById(R.id.tmMainMenuSpeechBubble);
        speechBubbleText = findViewById(R.id.tmMainMenuSpeechText);
        backgroundLayout = findViewById(R.id.tmMainMenuBackground);
        jamesNextButton = findViewById(R.id.tmMainMenuSpeechBubbleNext);
        jamesPreviousButton = findViewById(R.id.tmMainMenuSpeechBubblePrevious);

        speechBubbleButton = findViewById(R.id.tmMainMenuSpeechBubbleButton);
        teamHeaderBackground = findViewById(R.id.tmMainMenuheader);
        teamNameText = findViewById(R.id.tmMainMenuTeamName);
        leagueTableHolder = findViewById(R.id.tmMainMenuTableItemList);
        expandCollapseButton = findViewById(R.id.tmMainMenuExpandCollapseButton);
        leagueNameText = findViewById(R.id.tmMainMenuLeagueHeader);
        upLeagueButton = findViewById(R.id.tmMainMenuLeagueUp);
        downLeagueButton = findViewById(R.id.tmMainMenuLeagueDown);
        progressCard = findViewById(R.id.tmMainMenuStepDisplayCard);
        tabLayout = findViewById(R.id.tmMainMenuStepTabs);
        stepProgress = findViewById(R.id.tmMainMenuStepProgress);
        notEnoughDataText = findViewById(R.id.tmMainMenuNotEnoughData);
        stepProgressTarget = findViewById(R.id.tmMainMenuStepTarget);
        stepProgressAchieved = findViewById(R.id.tmMainMenuSteps);
        daysAchievedText = findViewById(R.id.tmMainMenuDaysAchieved);
        numDaysText = findViewById(R.id.tmMainMenuNumDays);
        stepsSlash = findViewById(R.id.tmMainMenuProgressSlash);
        numDaysSlash = findViewById(R.id.tmMainMenuProgressSlash2);
        goldenFootball = findViewById(R.id.tmMainMenuGoldFootball);
        nextFixtureDateText = findViewById(R.id.tmMainMenuNextMatchDate);
        nextFixtureOpponentText = findViewById(R.id.tmMainMenuNextMatchOpponent);
        playMatchButton = findViewById(R.id.tmMainMenuPlayMatchButton);
        footer = findViewById(R.id.tmMainMenuFooter);
        phoneButton = findViewById(R.id.tmMainMenuPhoneButton);

        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                refreshStepCircle(FilterOptions.getFilterOption(tab.getText().toString()));
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
        usersTeam = CareerSavedData.getInstance().getTeamFromID(CareerSettings.getInstance().getTeamID());
        usersLeague = usersTeam.getLeague();
        leagueCollapsed = false;

        teamNameText.setText(usersTeam.getName());
        leagueNameText.setText(Leagues.getLeagueName(leagueToDisplay));
        teamHeaderBackground.setBackgroundColor(Color.parseColor(usersTeam.getColour()));
        footer.setBackgroundColor(Color.parseColor(usersTeam.getColour()));
        leagueToDisplay = usersTeam.getLeague();
        teamsPosition = Leagues.getPositionInLeague(usersTeam.getID(), leagueToDisplay);

        expandCollapseButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                animateLeagueTable();
            }
        });
        List<Integer> choices = CareerSettings.getInstance().getFilterChoices();
       /* for (Integer choice : choices) {
            tabLayout.addTab(tabLayout.newTab().setText(FilterOptions.getTabName(choice)));
        }*/
        for (int i = 1; i < 6; i++) {
            tabLayout.addTab(tabLayout.newTab().setText(FilterOptions.getTabName(i)));

        }
        //refreshStepCircle(choices.get(0));

        progressCard.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                    progressCard.getViewTreeObserver().removeOnGlobalLayoutListener(this);
                } else {
                    progressCard.getViewTreeObserver().removeGlobalOnLayoutListener(this);
                }
                //here the size is already available. create new button2 here with the size of button1

                fillLeagueTableDisplay();
                checkLeagueButtonValidity();
                animateLeagueTable();
                takeFootballOut();
            }
        });

        int teamID = CareerSettings.getInstance().getTeamID();
        Fixture f = CareerSavedData.getInstance().getNextFixtureForTeam(teamID);
        nextFixtureDateText.setText(DateHelper.formatDateToString(f.getDate()));
        nextFixtureOpponentText.setText(getString(R.string.tm_main_menu_opponent, f.getOpponent(teamID).getName()));
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

        int newTableHeight = 0;
        int newProgressCardHeight = (int) getResources().getDimension(R.dimen.progress_card_height);
        int rowHeight = (int) getResources().getDimension(R.dimen.full_table_row_height) + leagueTableHolder.getDividerHeight();

        int tabHeight = (int) getResources().getDimension(R.dimen.progress_tabs_height);
        int newTabHeight = 0;
        if (leagueCollapsed) {
            newTableHeight = rowHeight * Numbers.TM_MAIN_MENU_BIG_TABLE_ROW_NUMBER;
            expandCollapseButton.setText(getString(R.string.collapse_league_table));
            newProgressCardHeight -= tabHeight;
        }
        else {
            newTableHeight = rowHeight * Numbers.TM_MAIN_MENU_SMALL_TABLE_ROW_NUMBER;
            expandCollapseButton.setText(getString(R.string.expand_league_table));
            newTabHeight = tabHeight;
        }

        updateLeagueTable(newTableHeight);
        updateProgressCard(newProgressCardHeight);
        updateProgressTabs(newTabHeight);
    }

    void updateLeagueTable(int newTableHeight) {

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
        expandLeagueTable.setDuration(Numbers.TM_MAIN_MENU_EXPAND_TABLE_ANIM_DURATION);
        expandLeagueTable.start();
    }

    void updateProgressCard(int newProgressCardHeight) {
        ValueAnimator resizeProgressCard = ValueAnimator.ofInt(
                progressCard.getHeight(),
                newProgressCardHeight);
        resizeProgressCard.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator valueAnimator) {
                int val = (Integer) valueAnimator.getAnimatedValue();
                ViewGroup.LayoutParams layoutParams = progressCard.getLayoutParams();
                layoutParams.height = val;
                progressCard.setLayoutParams(layoutParams);
            }
        });

        resizeProgressCard.setDuration(Numbers.TM_MAIN_MENU_EXPAND_TABLE_ANIM_DURATION);
        resizeProgressCard.start();
    }

    void updateProgressTabs(int newTabHeight) {
        ValueAnimator hideProgressTabs = ValueAnimator.ofInt(
                tabLayout.getHeight(),
                newTabHeight);
        hideProgressTabs.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator valueAnimator) {
                int val = (Integer) valueAnimator.getAnimatedValue();
                ViewGroup.LayoutParams layoutParams = tabLayout.getLayoutParams();
                layoutParams.height = val;
                tabLayout.setLayoutParams(layoutParams);
            }
        });

        hideProgressTabs.setDuration(Numbers.TM_MAIN_MENU_EXPAND_TABLE_ANIM_DURATION);
        hideProgressTabs.start();
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

    void refreshStepCircle(int filterOption) {
        stepProgress.setVisibility(VISIBLE);
        notEnoughDataText.setVisibility(GONE);
        stepProgressTarget.setVisibility(VISIBLE);
        stepProgressAchieved.setVisibility(VISIBLE);
        if (animatingGoldenFootball) {
            takeFootballOut();
            animatingGoldenFootball = false;
        }

        stepProgressAchieved.setText(String.valueOf(1));
        stepProgressTarget.setText(String.valueOf(1));
        stepProgressAchieved.setTextColor(getResources().getColor(R.color.colorWhite));
        daysAchievedText.setTextColor(getResources().getColor(R.color.colorWhite));


        AppSavedData savedData = AppSavedData.getInstance();
        AppSettings appSettings = AppSettings.getInstance();
        CareerSettings careerSettings = CareerSettings.getInstance();
        stepProgress.setProgress(0);

        Date today = new Date();
        Date yesterday = DateHelper.addDays(new Date(), -1);
        Date careerStartDate = careerSettings.getStartDate();
        if (savedData.getActivityOnDate(yesterday) == null) {
            displayNotEnoughDataMessage();
            return;
        }

        int max = appSettings.getStepTarget();
        int steps = savedData.getActivityOnDate(yesterday).getSteps();

        int numDays = 0;
        int daysAchieved = 0;

        Date startDate;
        switch (filterOption) {

            case FilterOptions.LAST_7_DAYS:
                startDate = DateHelper.addDays(today, - 7);

                if (startDate.before(careerStartDate)) {
                    displayNotEnoughDataMessage();
                    return;
                }
                max = appSettings.getStepTarget() * 7;
                steps = 0;
                numDays = 7;
                for (UserActivity activity : savedData.getActivityOnAllDates(startDate, yesterday)) {
                    steps += activity.getSteps();
                    if (steps > appSettings.getStepTarget()) {
                        daysAchieved++;
                    }
                }

                break;
            case FilterOptions.LAST_MONTH:
                startDate = DateHelper.addDays(today, - 30);
                if (startDate.before(careerStartDate)) {
                    displayNotEnoughDataMessage();
                    return;
                }

                max = appSettings.getStepTarget() * 30;
                steps = 0;
                numDays = 30;
                for (UserActivity activity : savedData.getActivityOnAllDates(startDate, yesterday)) {
                    steps += activity.getSteps();
                    if (steps > appSettings.getStepTarget()) {
                        daysAchieved++;
                    }
                }
                break;
            case FilterOptions.LAST_MATCH:
                //TODO: Get numbers from match
                int teamID = careerSettings.getTeamID();
                Fixture f = CareerSavedData.getInstance().getLastResultForTeam(teamID);
                if (f == null) {
                    displayNotEnoughDataMessage();
                    return;
                }
                max = f.getStepTarget();
                steps = f.getSteps(teamID);
                numDays = careerSettings.getDaysBetween();
                Date start = DateHelper.addDays(f.getDate(), numDays * -1);
                Date end = DateHelper.addDays(f.getDate(), -1);
                for (UserActivity activity : savedData.getActivityOnAllDates(start, end)) {
                    if (activity.getSteps() > appSettings.getStepTarget()) {
                        daysAchieved++;
                    }
                }
                break;
            case FilterOptions.SEASON:
                if (yesterday.before(careerStartDate)) {
                    displayNotEnoughDataMessage();
                    return;
                }

                int daysBeteenYestAndSeasonStart = DateHelper.getDaysBetween(today, careerStartDate);
                max = appSettings.getStepTarget() * daysBeteenYestAndSeasonStart;
                startDate = DateHelper.addDays(yesterday, daysBeteenYestAndSeasonStart * -1);
                numDays = daysBeteenYestAndSeasonStart - 1; //Days between is inclusive of both dates but we don't take steps on start date
                steps = 0;
                for (UserActivity activity : savedData.getActivityOnAllDates(startDate, yesterday)) {
                    steps += activity.getSteps();
                    if (steps > appSettings.getStepTarget()) {
                        daysAchieved++;
                    }
                }

                break;


        }
        stepProgress.setMax(max);
        stepProgressTarget.setText(Words.getNumberWithCommas(max));
        updateProgressCircleAndSteps(steps);
        if (filterOption != FilterOptions.YESTERDAY) {
            numDaysText.setVisibility(VISIBLE);
            daysAchievedText.setVisibility(VISIBLE);
            numDaysSlash.setVisibility(VISIBLE);
            numDaysText.setText(String.valueOf(numDays));
            updateNumDays(daysAchieved);
        }
        else {
            numDaysText.setVisibility(GONE);
            daysAchievedText.setVisibility(GONE);
            numDaysSlash.setVisibility(GONE);
        }

    }

    void displayNotEnoughDataMessage() {
        stepProgress.setVisibility(GONE);
        notEnoughDataText.setVisibility(VISIBLE);
        stepProgressTarget.setVisibility(GONE);
        stepsSlash.setVisibility(GONE);
        stepProgressAchieved.setVisibility(GONE);
        numDaysText.setVisibility(GONE);
        numDaysSlash.setVisibility(GONE);
        daysAchievedText.setVisibility(GONE);
    }

    void updateProgressCircleAndSteps(int steps) {
        final ValueAnimator animateProgressCircle = ValueAnimator.ofInt(
                1,
                steps);
        animateProgressCircle.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator valueAnimator) {
                int val = (Integer) valueAnimator.getAnimatedValue();
                stepProgress.setProgress(val);
                stepProgressAchieved.setText(Words.getNumberWithCommas(val));
                if (val > stepProgress.getMax()) {
                    stepProgressAchieved.setTextColor(getResources().getColor(R.color.colorPrimaryDark));
                    if (!animatingGoldenFootball) {
                        enlargeFootball();
                        animatingGoldenFootball = true;
                    }
                }
            }
        });
        animateProgressCircle.setDuration(Numbers.TM_MAIN_MENU_STEP_PROGRESS_ANIM_DURATION);
        animateProgressCircle.start();
    }

    void updateNumDays(int daysAchieved) {

        final ValueAnimator animateNumDays = ValueAnimator.ofInt(
                0,
                daysAchieved);
        animateNumDays.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator valueAnimator) {
                int val = (Integer) valueAnimator.getAnimatedValue();
                daysAchievedText.setText(Words.getNumberWithCommas(val));
                if (val >= Integer.parseInt(numDaysText.getText().toString())) {
                    daysAchievedText.setTextColor(getResources().getColor(R.color.colorPrimaryDark));
                }
            }
        });
        animateNumDays.setDuration(Numbers.TM_MAIN_MENU_STEP_PROGRESS_ANIM_DURATION);
        animateNumDays.start();
    }

    void enlargeFootball() {
        ValueAnimator enlargeFootballAnim = ValueAnimator.ofInt(
                goldenFootball.getWidth(),
                (int) getResources().getDimension(R.dimen.golden_football_height));
        enlargeFootballAnim.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator valueAnimator) {
                int val = (Integer) valueAnimator.getAnimatedValue();
                ViewGroup.LayoutParams layoutParams = goldenFootball.getLayoutParams();
                layoutParams.height = val;
                layoutParams.width = val;
                goldenFootball.setLayoutParams(layoutParams);
            }
        });
        enlargeFootballAnim.setDuration(Numbers.TM_MAIN_MENU_GOLDEN_FOOTBALL_ANIM_DURATION);

        AnimatorSet resizeFootballAndMoveText = new AnimatorSet();

        resizeFootballAndMoveText.playTogether(enlargeFootballAnim
        );
        resizeFootballAndMoveText.start();

    }
    void takeFootballOut() {
        ViewGroup.LayoutParams layoutParams = goldenFootball.getLayoutParams();
        layoutParams.height = 1;
        layoutParams.width = 1;
        goldenFootball.setLayoutParams(layoutParams);

    }
    @Override
    public void gotStepMap() {
        refreshStepCircle(0);
    }

    public void callJames(View view) {
        animateJamesIntroduction();
    }
    /**
     * In this method James slides onto the screen from the right, the speech bubble drops down too far then bounces back up, and
     * the Next button slides in from the left. It does all of this after a delay.
     * Delays and speed are controlled by FIRST_MENU variables in Numbers.
     */
    void animateJamesIntroduction() {

        speechBubbleText.setText(getString(R.string.tm_main_menu_james_description1));
        jamesNextButton.setVisibility(VISIBLE);
        jamesPreviousButton.setVisibility(GONE);
        tutorialStep = 1;

        float jamesFinalXPosition = backgroundLayout.getX() + (backgroundLayout.getWidth() - jamesImage.getWidth());
        float speechBounceYPosition = jamesImage.getScaleY() + Numbers.SPEECH_BUBBLE_PADDING_OFFSET;
        float speechFinalYPosition = jamesImage.getScaleY() + Numbers.SPEECH_BUBBLE_PADDING_OFFSET;

        AnimatorSet manAndSpeechIntroAnimSet = new AnimatorSet();
        AnimatorSet speechBounceAndButtonIntroAnimSet = new AnimatorSet();
        AnimatorSet fullSet = new AnimatorSet();

        manAndSpeechIntroAnimSet.playTogether(
                ObjectAnimator.ofFloat( //Animating the man onto the screen
                        jamesImage,
                        "x",
                        jamesFinalXPosition)
                        .setDuration(Numbers.FIRST_MENU_JAMES_ANIM_DURATION),
                ObjectAnimator.ofFloat( //Animating the speech bubble onto the screen
                        speechBubbleLayout,
                        "y",
                        speechBounceYPosition)
                        .setDuration(Numbers.FIRST_MENU_SPEECH_BUBBLE_ANIM_DURATION));

        speechBounceAndButtonIntroAnimSet.play(
                ObjectAnimator.ofFloat( //Animating the speech bubble bouncing up
                        speechBubbleLayout,
                        "y",
                        speechFinalYPosition)
                        .setDuration(Numbers.FIRST_MENU_SPEECH_BUBBLE_BOUNCE_ANIM_DURATION));

        fullSet.playSequentially(manAndSpeechIntroAnimSet, speechBounceAndButtonIntroAnimSet);
        fullSet.start();


    }

    public void nextJames(View view) {
        tutorialStep++;
        jamesPreviousButton.setVisibility(VISIBLE);
        if (tutorialStep == 2) {
            speechBubbleText.setText(getString(R.string.tm_main_menu_james_description2));
        }
        else if (tutorialStep == 3) {
            speechBubbleText.setText(getString(R.string.tm_main_menu_james_description3));
            jamesNextButton.setVisibility(GONE);
        }

    }

    public void previousJames(View view) {
        tutorialStep--;
        jamesNextButton.setVisibility(VISIBLE);
        if (tutorialStep == 1) {
            speechBubbleText.setText(getString(R.string.tm_main_menu_james_description1));
            jamesPreviousButton.setVisibility(GONE);
        }
        else if (tutorialStep == 2) {
            speechBubbleText.setText(getString(R.string.tm_main_menu_james_description2));
        }
    }

    public void doneJames(View view) {
        jamesImage.setX(backgroundLayout.getWidth());
        speechBubbleLayout.setY(0 - speechBubbleLayout.getHeight());
    }

}
