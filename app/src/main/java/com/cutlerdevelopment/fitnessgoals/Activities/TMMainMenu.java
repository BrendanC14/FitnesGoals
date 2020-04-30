package com.cutlerdevelopment.fitnessgoals.Activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.DialogFragment;

import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.content.Context;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.cutlerdevelopment.fitnessgoals.Constants.Numbers;
import com.cutlerdevelopment.fitnessgoals.DIalogFragments.MatchResult;
import com.cutlerdevelopment.fitnessgoals.DIalogFragments.MatchSetup;
import com.cutlerdevelopment.fitnessgoals.Models.MatchEngine;
import com.cutlerdevelopment.fitnessgoals.Models.Team;
import com.cutlerdevelopment.fitnessgoals.R;
import com.cutlerdevelopment.fitnessgoals.SavedData.CareerSavedData;
import com.cutlerdevelopment.fitnessgoals.Settings.CareerSettings;
import com.cutlerdevelopment.fitnessgoals.ViewControllers.FixturesCardController;
import com.cutlerdevelopment.fitnessgoals.ViewControllers.LeagueTableCardController;
import com.cutlerdevelopment.fitnessgoals.ViewControllers.NextMatchCardController;
import com.cutlerdevelopment.fitnessgoals.ViewControllers.ResultsCardController;
import com.cutlerdevelopment.fitnessgoals.ViewControllers.StepReviewCardController;
import com.google.android.material.card.MaterialCardView;

import static android.view.View.GONE;
import static android.view.View.VISIBLE;

public class TMMainMenu extends AppCompatActivity implements MatchEngine.MatchEngineListener {

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


    ImageView jamesImage;
    ConstraintLayout speechBubbleLayout;
    TextView speechBubbleText;
    Button speechBubbleButton;
    Button jamesNextButton;
    Button jamesPreviousButton;
    ConstraintLayout backgroundLayout;

    MaterialCardView header;
    TextView teamNameText;
    CardView footer;
    ImageButton phoneButton;

    Context c;

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
        header = findViewById(R.id.tmMainMenuheader);
        teamNameText = findViewById(R.id.tmMainMenuTeamName);
        footer = findViewById(R.id.tmMainMenuFooter);
        phoneButton = findViewById(R.id.tmMainMenuPhoneButton);

        layoutParent = findViewById(R.id.tmMainMenuLinearLayoutScroll);

        usersTeam = CareerSavedData.getInstance().getTeamFromID(CareerSettings.getInstance().getTeamID());
        usersLeague = usersTeam.getLeague();

        c = this;
        teamNameText.setText(usersTeam.getName());
        teamNameText.setTextColor(Color.parseColor(usersTeam.getSecondaryColour()));
        header.setBackgroundColor(Color.parseColor(usersTeam.getPrimaryColour()));
        footer.setBackgroundColor(Color.parseColor(usersTeam.getPrimaryColour()));
        layoutParent.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                    layoutParent.getViewTreeObserver().removeOnGlobalLayoutListener(this);
                } else {
                    layoutParent.getViewTreeObserver().removeGlobalOnLayoutListener(this);
                }

                AnimatorSet fullSet = new AnimatorSet();

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

                stepReviewCard.setX(backgroundLayout.getWidth());
                nextMatchCard.setX(backgroundLayout.getWidth());
                leagueTableCard.setX(backgroundLayout.getWidth());
                fixturesCard.setX(backgroundLayout.getWidth());
                resultsCard.setX(backgroundLayout.getWidth());

                fullSet.playSequentially(
                        animateCard(stepReviewCard, teamNameText),
                        animateCard(nextMatchCard, stepReviewCard),
                        animateCard(leagueTableCard, nextMatchCard),
                        animateCard(fixturesCard, leagueTableCard),
                        animateCard(resultsCard, fixturesCard));

                fullSet.start();

            }
        });

    }

    AnimatorSet animateCard(View cardToMove, View beneathLayout) {
        float xPosition = 8.0f;
        float yPosition = beneathLayout.getY() + 8.0f;

        AnimatorSet animatorSet = new AnimatorSet();
        animatorSet.playTogether(
                ObjectAnimator.ofFloat(
                        cardToMove,
                        "x",
                        xPosition)
                        .setDuration(1000),
                ObjectAnimator.ofFloat(
                        cardToMove,
                        "x",
                        yPosition)
                        .setDuration(1000)
        );
        return animatorSet;

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
