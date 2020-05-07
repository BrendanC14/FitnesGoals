package com.cutlerdevelopment.fitnessgoals.Activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.DialogFragment;

import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.content.Context;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.cutlerdevelopment.fitnessgoals.Constants.Colours;
import com.cutlerdevelopment.fitnessgoals.Constants.Numbers;
import com.cutlerdevelopment.fitnessgoals.DIalogFragments.AllBadgesMenu;
import com.cutlerdevelopment.fitnessgoals.DIalogFragments.JamesTutorial;
import com.cutlerdevelopment.fitnessgoals.DIalogFragments.MatchResult;
import com.cutlerdevelopment.fitnessgoals.DIalogFragments.MatchSetup;
import com.cutlerdevelopment.fitnessgoals.DIalogFragments.Settings;
import com.cutlerdevelopment.fitnessgoals.DIalogFragments.SingleBadgeMenu;
import com.cutlerdevelopment.fitnessgoals.Models.Badge;
import com.cutlerdevelopment.fitnessgoals.Models.Fixture;
import com.cutlerdevelopment.fitnessgoals.Models.MatchEngine;
import com.cutlerdevelopment.fitnessgoals.Models.Team;
import com.cutlerdevelopment.fitnessgoals.R;
import com.cutlerdevelopment.fitnessgoals.SavedData.GameDBHandler;
import com.cutlerdevelopment.fitnessgoals.Data.GameData;
import com.cutlerdevelopment.fitnessgoals.Utils.JamesStep;
import com.cutlerdevelopment.fitnessgoals.ViewControllers.FixturesCardController;
import com.cutlerdevelopment.fitnessgoals.ViewControllers.LeagueTableCardController;
import com.cutlerdevelopment.fitnessgoals.ViewControllers.NextMatchCardController;
import com.cutlerdevelopment.fitnessgoals.ViewControllers.ResultsCardController;
import com.cutlerdevelopment.fitnessgoals.ViewControllers.StepReviewCardController;
import com.google.android.material.card.MaterialCardView;

import java.util.ArrayList;
import java.util.List;

import static android.view.View.GONE;
import static android.view.View.VISIBLE;

public class TMMainMenu extends AppCompatActivity implements MatchEngine.MatchEngineListener, GameData.BadgeListener,
        Settings.SettingsListener {

    LinearLayout layoutParent;
    View stepReviewCard;
    StepReviewCardController stepReviewCardController;
    View leagueTableCard;
    LeagueTableCardController leagueTableCardController;
    View nextMatchCard;
    NextMatchCardController nextMatchCardController;
    View fixturesCard;
    FixturesCardController fixturesCardController;
    View resultsCard;
    ResultsCardController resultsCardController;


    ConstraintLayout backgroundLayout;

    MaterialCardView header;
    TextView teamNameText;
    CardView footer;
    ImageButton phoneButton;

    Context c;

    Team usersTeam;
    int usersLeague;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.tm_main_menu);

        backgroundLayout = findViewById(R.id.tmMainMenuBackground);
        header = findViewById(R.id.tmMainMenuheader);
        teamNameText = findViewById(R.id.tmMainMenuTeamName);
        footer = findViewById(R.id.tmMainMenuFooter);
        phoneButton = findViewById(R.id.tmMainMenuPhoneButton);

        layoutParent = findViewById(R.id.tmMainMenuLinearLayoutScroll);

        usersTeam = GameData.getInstance().getUsersTeam();
        usersLeague = usersTeam.getLeague();

        c = this;
        teamNameText.setText(usersTeam.getName());
        teamNameText.setTextColor(Color.parseColor(usersTeam.getSecondaryColour()));

        setupAndSlideCardsIn();

        GameData.getInstance().setListener(this);

        RefreshActivityAsync refreshActivityAsync = new RefreshActivityAsync();
        refreshActivityAsync.execute();
    }

    void setupAndSlideCardsIn() {
        usersTeam = GameData.getInstance().getUsersTeam();
        header.setBackgroundColor(Colours.getUsersPrimaryColour());
        backgroundLayout.setBackgroundColor(Colours.getUsersSecondaryColour());
        teamNameText.setTextColor(Colours.getUsersSecondaryColour());
        teamNameText.setText(usersTeam.getName());
        getLayoutInflater().inflate(R.layout.step_review_card, layoutParent);
        stepReviewCard = layoutParent.getChildAt(0);
        stepReviewCardController = new StepReviewCardController(stepReviewCard, c);

        getLayoutInflater().inflate(R.layout.next_match_card, layoutParent);
        nextMatchCard = layoutParent.getChildAt(1);
        nextMatchCardController = new NextMatchCardController(nextMatchCard, c);

        getLayoutInflater().inflate(R.layout.league_table_card, layoutParent);
        leagueTableCard = layoutParent.getChildAt(2);
        leagueTableCardController = new LeagueTableCardController(leagueTableCard, c);

        getLayoutInflater().inflate(R.layout.fixtures_card, layoutParent);
        fixturesCard = layoutParent.getChildAt(3);
        fixturesCardController = new FixturesCardController(fixturesCard, c, usersLeague);

        getLayoutInflater().inflate(R.layout.results_card, layoutParent);
        resultsCard = layoutParent.getChildAt(4);
        resultsCardController = new ResultsCardController(resultsCard, c, usersLeague);

        Animation slideInRight = AnimationUtils.loadAnimation(c,
                R.anim.slide_in_right);
        slideInRight.setDuration(Numbers.TM_MAIN_MENU_CARD_INTRO_DURATION);
        Animation slideInTop = AnimationUtils.loadAnimation(c,
                R.anim.slide_in_top);
        slideInTop.setDuration(Numbers.TM_MAIN_MENU_CARD_INTRO_DURATION);
        Animation slideInBottom = AnimationUtils.loadAnimation(c,
                R.anim.slide_in_bottom);
        slideInBottom.setDuration(Numbers.TM_MAIN_MENU_CARD_INTRO_DURATION);
        Animation slideInLeft = AnimationUtils.loadAnimation(c,
                R.anim.slide_in_left);
        slideInLeft.setDuration(Numbers.TM_MAIN_MENU_CARD_INTRO_DURATION);


        stepReviewCard.startAnimation(slideInTop);
        nextMatchCard.startAnimation(slideInRight);
        leagueTableCard.startAnimation(slideInBottom);
        fixturesCard.startAnimation(slideInLeft);
        resultsCard.startAnimation(slideInBottom);
    }

    public void callJames(View view) {
        List<JamesStep> steps = new ArrayList<>();
        steps.add(new JamesStep(getString(R.string.tm_main_menu_james_description1)));
        steps.add(new JamesStep(getString(R.string.tm_main_menu_james_description2)));
        steps.add(new JamesStep(getString(R.string.tm_main_menu_james_description3)));
        steps.add(new JamesStep(getString(R.string.tm_main_menu_james_description4)));

        JamesTutorial james = new JamesTutorial(steps);
        james.show(getSupportFragmentManager(), "james");



    }


    public void openMatchSetup(View view) {
        int teamID = GameData.getInstance().getTeamID();
        Fixture f = GameDBHandler.getInstance().getNextFixtureForTeam(teamID);

        if (f == null) {
            GameData.getInstance().startNextSeason();
            layoutParent.removeAllViews();
            setupAndSlideCardsIn();
        } else {
            DialogFragment careerDialog = new MatchSetup();
            careerDialog.show(getSupportFragmentManager(), "MatchSetup");
            MatchEngine.setListener(this);
        }
    }

    @Override
    public void matchesPlayed() {

        leagueTableCardController.playMatch();
        nextMatchCardController.addNextMatch();
        fixturesCardController.playMatch();
        resultsCardController.playMatch();

        DialogFragment careerDialog = new MatchResult();
        careerDialog.show(getSupportFragmentManager(), "MatchResult");


    }

    @Override
    public void badgeUnlocked(Badge badgeUnlocked) {
        DialogFragment badgeDialog = new SingleBadgeMenu();
        Bundle args = new Bundle();
        args.putString("badgeName", badgeUnlocked.getName());
        args.putString("badgeDesc", badgeUnlocked.getDescription());
        args.putBoolean("newBadge", true);
        badgeDialog.setArguments(args);
        badgeDialog.show(getSupportFragmentManager(), "SingleBadgeMenu");
    }

    public void openAllBadges(View view) {
        DialogFragment allBadgesDialog = new AllBadgesMenu();
        allBadgesDialog.show(getSupportFragmentManager(), "AllBadgesMenu");

    }

    public void openSettings(View view) {
        DialogFragment settingsDialog = new Settings();
        settingsDialog.show(getSupportFragmentManager(), "settingsDialog");

    }

    @Override
    public void settingsChanged(boolean refreshMenu) {
        if (refreshMenu) {
            layoutParent.removeAllViews();
            setupAndSlideCardsIn();
        }
    }

    private class RefreshActivityAsync extends AsyncTask<Void, Void, Void> {


        @Override
        protected Void doInBackground(Void... voids) {
            GameData.getInstance().refreshPlayerActivity();
            return null;
        }
    }
}

