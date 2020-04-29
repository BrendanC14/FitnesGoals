package com.cutlerdevelopment.fitnessgoals.Activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.DialogFragment;

import android.animation.Animator;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.cutlerdevelopment.fitnessgoals.Constants.Leagues;
import com.cutlerdevelopment.fitnessgoals.Constants.Numbers;
import com.cutlerdevelopment.fitnessgoals.DIalogFragments.MatchResult;
import com.cutlerdevelopment.fitnessgoals.DIalogFragments.MatchSetup;
import com.cutlerdevelopment.fitnessgoals.Models.Fixture;
import com.cutlerdevelopment.fitnessgoals.Models.MatchEngine;
import com.cutlerdevelopment.fitnessgoals.Models.Team;
import com.cutlerdevelopment.fitnessgoals.R;
import com.cutlerdevelopment.fitnessgoals.SavedData.CareerSavedData;
import com.cutlerdevelopment.fitnessgoals.Settings.CareerSettings;
import com.cutlerdevelopment.fitnessgoals.Utils.DateHelper;
import com.cutlerdevelopment.fitnessgoals.Utils.StringHelper;
import com.cutlerdevelopment.fitnessgoals.ViewAdapters.FullTableRowAdapter;
import com.cutlerdevelopment.fitnessgoals.ViewAdapters.ResultItemRowAdapter;
import com.cutlerdevelopment.fitnessgoals.ViewControllers.FixturesCardController;
import com.cutlerdevelopment.fitnessgoals.ViewControllers.LeagueTableCardController;
import com.cutlerdevelopment.fitnessgoals.ViewControllers.NextMatchCardController;
import com.cutlerdevelopment.fitnessgoals.ViewControllers.ResultsCardController;
import com.cutlerdevelopment.fitnessgoals.ViewItems.FullTableRow;
import com.cutlerdevelopment.fitnessgoals.ViewItems.ResultItem;
import com.cutlerdevelopment.fitnessgoals.ViewControllers.StepReviewCardController;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import static android.view.View.GONE;
import static android.view.View.VISIBLE;

public class TMMainMenu extends AppCompatActivity implements MatchEngine.MatchEngineListener {

    LinearLayout layoutParent;
    StepReviewCardController stepReviewCardController;
    LeagueTableCardController leagueTableCardController;
    NextMatchCardController nextMatchCardController;
    FixturesCardController fixturesCardController;
    ResultsCardController resultsCardController;


    ImageView jamesImage;
    ConstraintLayout speechBubbleLayout;
    TextView speechBubbleText;
    Button speechBubbleButton;
    Button jamesNextButton;
    Button jamesPreviousButton;
    ConstraintLayout backgroundLayout;

    TextView teamNameText;
    CardView footer;
    ImageButton phoneButton;

    Team usersTeam;
    int usersLeague;
    int tutorialStep = 1;




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
        teamNameText = findViewById(R.id.tmMainMenuTeamName);
        footer = findViewById(R.id.tmMainMenuFooter);
        phoneButton = findViewById(R.id.tmMainMenuPhoneButton);

        layoutParent = findViewById(R.id.tmMainMenuLinearLayoutScroll);

        usersTeam = CareerSavedData.getInstance().getTeamFromID(CareerSettings.getInstance().getTeamID());
        usersLeague = usersTeam.getLeague();

        teamNameText.setText(usersTeam.getName());
        backgroundLayout.setBackgroundColor(Color.parseColor(usersTeam.getColour()));

        getLayoutInflater().inflate(R.layout.step_review_card, layoutParent);
        View stepReviewCard = layoutParent.getChildAt(0);
        stepReviewCardController = new StepReviewCardController(stepReviewCard, this);

        View nextMatchCard = getLayoutInflater().inflate(R.layout.next_match_card, layoutParent);
        nextMatchCardController = new NextMatchCardController(nextMatchCard, this);

        View leagueTableCard = getLayoutInflater().inflate(R.layout.league_table_card, layoutParent);
        leagueTableCardController = new LeagueTableCardController(leagueTableCard, this);

        View fixturesCard = getLayoutInflater().inflate(R.layout.fixtures_card, layoutParent);
        fixturesCardController = new FixturesCardController(fixturesCard, this, usersLeague);

        View resultsCard = getLayoutInflater().inflate(R.layout.results_card, layoutParent);
        resultsCardController = new ResultsCardController(resultsCard, this, usersLeague);
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
        }
        else if (tutorialStep == 4) {
            speechBubbleText.setText(getString(R.string.tm_main_menu_james_description4));
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
        else if (tutorialStep == 3) {
            speechBubbleText.setText(getString(R.string.tm_main_menu_james_description3));
        }
    }

    public void doneJames(View view) {
        jamesImage.setX(backgroundLayout.getWidth());
        speechBubbleLayout.setY(0 - speechBubbleLayout.getHeight());
    }

    public void openMatchSetup(View view) {

        DialogFragment careerDialog = new MatchSetup();
        careerDialog.show(getSupportFragmentManager(), "MatchSetup");
        MatchEngine.setListener(this);
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

}
