package com.cutlerdevelopment.fitnessgoals.Activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.animation.Animator;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;

import com.cutlerdevelopment.fitnessgoals.Constants.FitnessApps;
import com.cutlerdevelopment.fitnessgoals.Constants.GameModes;
import com.cutlerdevelopment.fitnessgoals.Constants.Numbers;
import com.cutlerdevelopment.fitnessgoals.Constants.StepModes;
import com.cutlerdevelopment.fitnessgoals.Constants.TutorialSteps;
import com.cutlerdevelopment.fitnessgoals.Constants.Words;
import com.cutlerdevelopment.fitnessgoals.Integrations.FitbitIntegrations.FitbitAPI;
import com.cutlerdevelopment.fitnessgoals.Integrations.FitbitIntegrations.FitbitStringsSavedData;
import com.cutlerdevelopment.fitnessgoals.Integrations.GoogleIntegrations.GoogleFitAPI;
import com.cutlerdevelopment.fitnessgoals.Integrations.IntegrationConnectionHandler;
import com.cutlerdevelopment.fitnessgoals.Models.Team;
import com.cutlerdevelopment.fitnessgoals.R;
import com.cutlerdevelopment.fitnessgoals.SavedData.CareerSavedData;
import com.cutlerdevelopment.fitnessgoals.SavedData.AppSettingsSavedData;
import com.cutlerdevelopment.fitnessgoals.Settings.AppSettings;
import com.cutlerdevelopment.fitnessgoals.Settings.CareerSettings;
import com.cutlerdevelopment.fitnessgoals.Utils.DateHelper;
import com.cutlerdevelopment.fitnessgoals.ViewAdapters.ColourDisplaySmallCardAdapter;
import com.cutlerdevelopment.fitnessgoals.ViewAdapters.FitnessAppSmallCardAdapter;
import com.cutlerdevelopment.fitnessgoals.ViewItems.ColourDisplaySmallCard;
import com.cutlerdevelopment.fitnessgoals.ViewItems.FitnessAppSmallCard;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.auth.api.signin.GoogleSignInStatusCodes;
import com.google.android.material.textfield.TextInputLayout;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Random;

/**
 * FirstMenu Activity is the first Activity that loads when the User first opens the app. It's a setup menu.
 * The class is organised into the different steps, overridden activity results then some animation methods.
 */
public class FirstMenu extends AppCompatActivity implements IntegrationConnectionHandler.IntegrationListener {

    Boolean readyToProgress = false;
    Boolean readyToStart = false;
    ConstraintLayout backgroundLayout;
    ImageView jamesImage;
    ConstraintLayout speechBubbleLayout;
    TextView speechBubbleText;

    ConstraintLayout fitnessAppsLayout;
    ListView fitnessAppsList;

    ConstraintLayout playerModeLayout;
    ImageView teamModeBackgroundColour;
    ImageView playerModeBackgroundColour;

    ConstraintLayout teamPlayerNameLayout;
    TextInputLayout teamPlayerNameText;

    ConstraintLayout teamColourLayout;
    GridView teamColourGrid;

    ConstraintLayout favouriteTeamLayout;
    Spinner favouriteTeamSpinner;

    ConstraintLayout targetModeLayout;
    ImageView targetModeBackgroundColour;
    ImageView scaledModeBackgroundColour;

    ConstraintLayout stepTargetLayout;
    Spinner stepTargetSpinner;

    ConstraintLayout daysBetweenLayout;
    Spinner daysBetweenSpinner;

    Button readyButton;

    int gameMode;
    int stepMode;
    String teamPlayerName;
    String favouriteTeam;
    String teamColour;
    int stepTarget;
    int daysBetween;
    int tutorialStep = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.first_menu);

        backgroundLayout = findViewById(R.id.firstMenuBackground);
        jamesImage = findViewById(R.id.firstMenuJamesImage);
        speechBubbleLayout = findViewById(R.id.firstMenuSpeechBubble);
        speechBubbleText = findViewById(R.id.firstMenuSpeechText);
        fitnessAppsLayout = findViewById(R.id.firstMenuAppOptions);
        fitnessAppsList = findViewById(R.id.firstMenuAppList);
        playerModeLayout = findViewById(R.id.firstMenuPlayerModeMenu);
        teamModeBackgroundColour = findViewById(R.id.firstMenuTeamModeColour);
        playerModeBackgroundColour = findViewById(R.id.firstMenuPlayerModeColour);
        teamPlayerNameLayout = findViewById(R.id.firstMenuTeamPlayerNameLayout);
        teamPlayerNameText = findViewById(R.id.firstMenuTeamPlayerName);
        teamColourLayout = findViewById(R.id.firstMenuTeamColourLayout);
        teamColourGrid = findViewById(R.id.firstMenuColourGrid);
        favouriteTeamLayout = findViewById(R.id.firstMenuFavouriteTeamLayout);
        favouriteTeamSpinner = findViewById(R.id.firstMenuFavouriteTeamSpinner);
        targetModeLayout = findViewById(R.id.firstMenuTargetModeLayout);
        targetModeBackgroundColour = findViewById(R.id.firstMenuTargetModeColour);
        scaledModeBackgroundColour = findViewById(R.id.firstMenuScaledModeColour);
        stepTargetLayout = findViewById(R.id.firstMenuStepTargetLayout);
        stepTargetSpinner = findViewById(R.id.firstMenuStepTargetSpinner);
        daysBetweenLayout = findViewById(R.id.firstMenuDaysBetweenLayout);
        daysBetweenSpinner = findViewById(R.id.firstMenuDaysBetweenSpinner);
        readyButton = findViewById(R.id.firstMenuReadyButton);

        speechBubbleText.setText(getText(R.string.first_menu_intro_text_1));
        backgroundLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                nextStep();
            }
        });


        AppSettingsSavedData.createAppSettingsSavedData(this);
        CareerSavedData.createCareerSavedData(this);
        if (CareerSavedData.getInstance().loadSettings() != null) {
            loadGame();
        }
        else {
            IntegrationConnectionHandler.getInstance().setListener(this);
            IntegrationConnectionHandler.getInstance().getTeamsForNewGame();
        }

    }

    /**
     * nextStep is called whenever the User presses anywhere on the screen.
     * If the readyToProgress boolean is true a switch statement checks which step the user is on, updates the UI, sets readyToProgress to false and increases tutorialStep.
     */
    void nextStep() {

        if (readyToProgress) {
            switch (tutorialStep) {
                case TutorialSteps.FIRST_MENU_MEDICAL_INTRO:
                    speechBubbleText.setText(getString(R.string.first_menu_intro_text_2_1));
                    break;
                case TutorialSteps.FIRST_MENU_FITNESS_APP_STEP:
                    speechBubbleText.setText(getString(R.string.first_menu_intro_text_2_2));
                    readyToProgress = false;
                    populateFitnessAppList();
                    showMenu(fitnessAppsLayout);
                    pushSpeechBubbleToRight(fitnessAppsLayout);
                    break;
                case TutorialSteps.FIRST_MENU_TEAM_PLAYER_INTRO:
                    speechBubbleText.setText(getString(R.string.first_menu_intro_text_3));
                    break;
                case TutorialSteps.FIRST_MENU_PLAYER_MODE_STEP:
                    speechBubbleText.setText(getString(R.string.first_menu_intro_text_4));
                    readyToProgress = false;
                    showMenu(playerModeLayout);
                    moveSpeechBubbleUp(playerModeLayout);
                    break;
                case TutorialSteps.FIRST_MENU_NAME_COLOUR_STEP:
                    showMenu(teamPlayerNameLayout);
                    readyToProgress = false;
                    if (gameMode == GameModes.TEAM_MODE) {
                        speechBubbleText.setText(getString(R.string.firs_menu_confirm_name, "team"));
                        teamPlayerNameText.setHint("Team Name");
                    }
                    else {
                        speechBubbleText.setText(getString(R.string.firs_menu_confirm_name, "player"));
                        teamPlayerNameText.setHint("Player Name");
                        if (AppSettings.getInstance().getFitnessApp() == FitnessApps.GOOGLE_FIT) {
                            String name = GoogleFitAPI.getInstance().getName();
                            teamPlayerNameText.getEditText().setText(name);
                        }

                    }
                    pushSpeechBubbleToRight(teamPlayerNameLayout);
                    break;
                case TutorialSteps.FIRST_MENU_TARGET_MODE_STEP_1:
                    showMenu(targetModeLayout);
                    readyToProgress = false;
                    speechBubbleText.setText(getString(R.string.first_menu_intro_text_6));
                    moveSpeechBubbleUp(targetModeLayout);
                    break;
                case TutorialSteps.FIRST_MENU_TARGET_MODE_STEP_2:
                    if (stepMode == StepModes.TARGETED_MODE) {
                        populateStepTargetSpinner();
                        showMenu(stepTargetLayout);
                        readyToProgress = false;
                        speechBubbleText.setText(getString(R.string.first_menu_step_target_question));
                        pushSpeechBubbleToRight(stepTargetLayout);
                    }
                    else {

                    }
                    break;
                case TutorialSteps.FIRST_MENU_DAYS_BETWEEN_INTRO:
                    speechBubbleText.setText(getString(R.string.first_menu_days_between_intro));
                    break;
                case TutorialSteps.FIRST__MENU_DAYS_BETWEEN_STEP:
                    populateDaysBetweenSpinner();
                    showMenu(daysBetweenLayout);
                    readyToProgress = false;
                    speechBubbleText.setText(getString(R.string.first_menu_choose_days_between));
                    pushSpeechBubbleToRight(daysBetweenLayout);

                    break;
                case TutorialSteps.FIRST_MENU_COMPLETE_STEP_1:
                    if (readyToStart) {
                        speechBubbleText.setText("WOO");
                    }
                    else {
                        speechBubbleText.setText(getString(R.string.first_menu_still_loading));
                    }

                    break;
            }

            tutorialStep++;
        }
    }

    /**
     * In this method James slides onto the screen from the right, the speech bubble drops down too far then bounces back up, and
     * the Next button slides in from the left. It does all of this after a delay.
     * Delays and speed are controlled by FIRST_MENU variables in Numbers.
     */
    void animateJamesIntroduction() {


        float jamesPositionForBounce = jamesImage.getHeight() / Numbers.FIRST_MENU_SPEECH_BUBBLE_BOUNCE_POSITION;
        float jamesFinalXPosition = backgroundLayout.getX() + (backgroundLayout.getWidth() - jamesImage.getWidth());
        float speechBounceYPosition = (jamesImage.getTop() - speechBubbleLayout.getHeight()) + jamesPositionForBounce + Numbers.SPEECH_BUBBLE_PADDING_OFFSET;
        float speechFinalYPosition = speechBounceYPosition - jamesPositionForBounce;

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
        fullSet.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animator) {
            }

            @Override
            public void onAnimationEnd(Animator animator) {
                readyToProgress = true;
            }

            @Override
            public void onAnimationCancel(Animator animator) {
            }

            @Override
            public void onAnimationRepeat(Animator animator) {
            }
        });
        fullSet.start();


    }

    void pushSpeechBubbleToRight(View layoutToRightOf) {
        int speechBubbleWidth = backgroundLayout.getWidth() - (int) ( + layoutToRightOf.getWidth()
                + Numbers.FIRST_MENU_FITNESS_MARGIN_PADDING * 3);

        float speechBubbleStart = layoutToRightOf.getWidth() + Numbers.FIRST_MENU_FITNESS_MARGIN_PADDING;

        ValueAnimator resizeSpeechBubbleAnimator = ValueAnimator.ofInt(
                speechBubbleLayout.getMeasuredWidth(),
                speechBubbleWidth);
        resizeSpeechBubbleAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator valueAnimator) {
                int val = (Integer) valueAnimator.getAnimatedValue();
                ViewGroup.LayoutParams layoutParams = speechBubbleLayout.getLayoutParams();
                layoutParams.width = val;
                speechBubbleLayout.setLayoutParams(layoutParams);
                speechBubbleLayout.requestLayout();
            }
        });
        resizeSpeechBubbleAnimator.setDuration(Numbers.FIRST_MENU_FITNESS_ANIM_DURATION);

        AnimatorSet resizeAndMoveSpeechBubble = new AnimatorSet();

        resizeAndMoveSpeechBubble.playTogether(resizeSpeechBubbleAnimator,
                ObjectAnimator.ofFloat( //Animating the speech bubble moving over
                        speechBubbleLayout,
                        "x",
                        speechBubbleStart)
                        .setDuration(Numbers.FIRST_MENU_FITNESS_ANIM_DURATION)
        );
        resizeAndMoveSpeechBubble.start();
    }
    void putSpeechBubbleBackIntoMiddle() {
        int speechBubbleWidth = backgroundLayout.getWidth() - (int) (Numbers.FIRST_MENU_FITNESS_MARGIN_PADDING * 2);
        float speechBubbleXPos = Numbers.FIRST_MENU_FITNESS_MARGIN_PADDING;

        ValueAnimator resizeSpeechBubbleAnimator = ValueAnimator.ofInt(
                speechBubbleLayout.getMeasuredWidth(),
                speechBubbleWidth);
        resizeSpeechBubbleAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator valueAnimator) {
                int val = (Integer) valueAnimator.getAnimatedValue();
                ViewGroup.LayoutParams layoutParams = speechBubbleLayout.getLayoutParams();
                layoutParams.width = val;
                speechBubbleLayout.setLayoutParams(layoutParams);
                speechBubbleLayout.requestLayout();
            }
        });
        resizeSpeechBubbleAnimator.setDuration(Numbers.FIRST_MENU_FITNESS_ANIM_DURATION);

        AnimatorSet resizeAndMoveSpeechBubble = new AnimatorSet();

        resizeAndMoveSpeechBubble.playTogether(resizeSpeechBubbleAnimator,
                ObjectAnimator.ofFloat( //Animating the speech bubble moving over
                        speechBubbleLayout,
                        "x",
                        speechBubbleXPos)
                        .setDuration(Numbers.FIRST_MENU_FITNESS_ANIM_DURATION));
        resizeAndMoveSpeechBubble.start();
    }
    void moveSpeechBubbleUp(final View aboveView) {

        AnimatorSet moveSpeechBubbleDownSlightly = new AnimatorSet();
        moveSpeechBubbleDownSlightly.play(
                ObjectAnimator.ofFloat(
                        speechBubbleLayout,
                        "y",
                        speechBubbleLayout.getY())
                        .setDuration(100)
        );
        moveSpeechBubbleDownSlightly.addListener(new Animator.AnimatorListener() {
            @Override public void onAnimationStart(Animator animator) { }
            @Override
            public void onAnimationEnd(Animator animator) {
                final AnimatorSet moveSpeechBubble = new AnimatorSet();
                float speechBubbleYPos = aboveView.getY() - speechBubbleLayout.getMeasuredHeight() + Numbers.SPEECH_BUBBLE_PADDING_OFFSET;

                moveSpeechBubble.play(
                        ObjectAnimator.ofFloat(
                                speechBubbleLayout,
                                "y",
                                speechBubbleYPos)
                                .setDuration(Numbers.FIRST_MENU_FITNESS_ANIM_DURATION)
                );
                moveSpeechBubble.start();
            }
            @Override public void onAnimationCancel(Animator animator) { }
            @Override public void onAnimationRepeat(Animator animator) { }
        });

        moveSpeechBubbleDownSlightly.start();

    }
    void showMenu(ConstraintLayout layoutToShow) {

        AnimatorSet showLayoutSet = new AnimatorSet();

        showLayoutSet.play(
                ObjectAnimator.ofFloat(
                        layoutToShow,
                        "x",
                        Numbers.FIRST_MENU_FITNESS_MARGIN_PADDING)
                        .setDuration(Numbers.FIRST_MENU_FITNESS_ANIM_DURATION)
        );
        showLayoutSet.start();
    }
    void hideMenu(ConstraintLayout layoutToHide) {


        AnimatorSet hideLayoutSet = new AnimatorSet();

        hideLayoutSet.playTogether(
                ObjectAnimator.ofFloat(
                        layoutToHide,
                        "x",
                        0 - layoutToHide.getWidth())
                        .setDuration(Numbers.FIRST_MENU_FITNESS_ANIM_DURATION)
        );
        hideLayoutSet.start();
    }
    /**
     * Populates the fitness app list with all available apps as a fitness_app_small_card.
     * FitnessApps.NUM_OF_APPS controls how many items it populates with.
     * Also assigns an onItemClickListener that calls selectFitnessApp
     */
    void populateFitnessAppList() {
        final ArrayList<FitnessAppSmallCard> fitnessApps = new ArrayList<>();
        boolean offsetColour = false;

        for (int i = 0; i < FitnessApps.NUM_OF_APPS; i++) {
            FitnessAppSmallCard item = new FitnessAppSmallCard();
            item.setAppName(FitnessApps.getFitnessAppName(i));
            item.setAppImage(FitnessApps.getFitnessAppImage(i));
            if (offsetColour) {
                item.setBackgroundColour(R.color.colorPrimary);
                offsetColour = false;
            }
            else {
                item.setBackgroundColour(R.color.colorPrimary);
                offsetColour = true;
            }

            fitnessApps.add(item);

        }

        final FitnessAppSmallCardAdapter adapter = new FitnessAppSmallCardAdapter(getApplicationContext(), fitnessApps);
        fitnessAppsList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                FitnessAppSmallCard item = (FitnessAppSmallCard) adapterView.getItemAtPosition(i);
                selectFitnessApp(FitnessApps.getFitnessAppFromName(item.getAppName()));
            }
        });
        fitnessAppsList.setAdapter(adapter);

        ViewGroup.LayoutParams layoutParams = fitnessAppsLayout.getLayoutParams();
        layoutParams.width = (int) (jamesImage.getX() - Numbers.FIRST_MENU_FITNESS_MARGIN_PADDING);
        fitnessAppsLayout.setLayoutParams(layoutParams);
        fitnessAppsLayout.requestLayout();

    }

    void populateTeamColourSpinner() {

        final ArrayList<ColourDisplaySmallCard> colourItems = new ArrayList<>();
        List<String> colours = new ArrayList<>();
        List<Team> allTeams = CareerSavedData.getInstance().getAllTeams();
        for (Team t : allTeams) {
            if(!colours.contains(t.getColour())) {
                colours.add(t.getColour());
            }
        }
        Collections.sort(colours);
        for (String c : colours) {
            ColourDisplaySmallCard item = new ColourDisplaySmallCard();
            item.setTeamColour(c);
            colourItems.add(item);
        }


        final ColourDisplaySmallCardAdapter adapter = new ColourDisplaySmallCardAdapter(this, colourItems);
        teamColourGrid.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                ColourDisplaySmallCard item = (ColourDisplaySmallCard) adapterView.getItemAtPosition(i);
                teamColour = item.getTeamColour();
                confirmTeamColour();
            }
        });
        teamColourGrid.setAdapter(adapter);
        teamColourGrid.setNumColumns(3);

    }

    void populateFavouriteTeamSpinner() {
        List<String> spinnerList = new ArrayList<>();

        spinnerList = Words.teamNames;
        Collections.sort(spinnerList);


        ArrayAdapter<String> adapter = new ArrayAdapter<>(
                this, R.layout.spinner_item, spinnerList);
        favouriteTeamSpinner.setAdapter(adapter);
    }


    /**
     * Populates the stepTarget spinner.
     * Starts at 1 and loops through to Numbers.MAX_NUM_STEPS_TARGET
     */
    void populateStepTargetSpinner() {
        List<String> spinnerList = new ArrayList<>();
        for (int i = 1; i <= Numbers.MAX_NUM_STEPS_TARGET; i++) {
            spinnerList.add(String.valueOf(i));
        }
        ArrayAdapter<String> stepAdapter = new ArrayAdapter<>(
                this, R.layout.spinner_item, spinnerList);
        stepTargetSpinner.setAdapter(stepAdapter);
    }


    /**
     * Populates the daysBetween spinner.
     * Starts at 2 and increments by 2, up to and including Numbers.MAX_NUM_DAYS_BETWEEN
     */
    void populateDaysBetweenSpinner() {
        List<String> spinnerList = new ArrayList<>();
        for (int i = 2; i <= Numbers.MAX_NUM_DAYS_BETWEEN; i+=2) {
            spinnerList.add(String.valueOf(i));
        }
        ArrayAdapter<String> daysAdapter = new ArrayAdapter<>(
                this, R.layout.spinner_item, spinnerList);
        daysBetweenSpinner.setAdapter(daysAdapter);
    }



    /**
     * WHen a fitness app is selected, selectFitnessApp is called.
     * If Mocked is selected, it immediately confirms app connected.
     * If GoogleFit is selected, will create an instance of the GoogleFitAPI and check permissions.
     * If we have permission, will confirm app connected. If not, GoogleFitAPI will request permissions.
     * If Fitbit is selected, will create an instance of the FitbitStringsSavedData and FitbitAPI.
     * Will then request permission from FitbitAPI.
     * @param app the int value of the app selected, will be compared to FitnessApps.
     */
    void selectFitnessApp(int app) {
        switch (app) {
            case FitnessApps.MOCKED:
                hideMenu(fitnessAppsLayout);
                putSpeechBubbleBackIntoMiddle();
                confirmAppConnected();
                break;
            case FitnessApps.GOOGLE_FIT:
                speechBubbleText.setText(R.string.first_menu_calling_app);
                hideMenu(fitnessAppsLayout);
                putSpeechBubbleBackIntoMiddle();
                GoogleFitAPI.createGoogleFitAPIInstance(this);
                if (GoogleFitAPI.getInstance().hasPermissions(this)) {
                    confirmAppConnected();
                }
                break;
            case FitnessApps.FITBIT:
                speechBubbleText.setText(R.string.first_menu_calling_app);
                hideMenu(fitnessAppsLayout);
                putSpeechBubbleBackIntoMiddle();
                FitbitStringsSavedData.createFitbitStringsSavedDataInstance(this);
                FitbitAPI.createFitbitAPIInstance(this);
                FitbitAPI.getInstance().requestFitbitPermission(this);
                break;
            default:
                speechBubbleText.setText(R.string.first_menu_app_connected);
        }
        AppSettings.getInstance().changeFitnessApp(app);

    }

    /**
     * When GoogleFit or FitbitAPI get authorisation we startActivity for result with a defined Request Code from FitnessApps.
     * Depending on result, will either confirmAppConnected or confirmCantConnect
     * @param requestCode
     * @param resultCode
     * @param data
     */
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == FitnessApps.GOOGLE_FIT_INIT_REQUEST_CODE) {
            GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(data);
            int statusCode = result.getStatus().getStatusCode();
            if (statusCode == GoogleSignInStatusCodes.SUCCESS) {
                confirmAppConnected();
            }
            else {
                confirmCantConnect();
            }
        }
        if (requestCode == FitnessApps.FITBIT_INIT_REQUEST_CODE) {
            if (FitbitStringsSavedData.getInstance() != null &&
                    FitbitStringsSavedData.getInstance().getTopData() != null) {
                confirmAppConnected();
            }
            else {
                confirmCantConnect();
            }
        }
    }
    /**
     * Sets speech bubble text to first_menu_app_connected String
     * Sets the app ready to progress
     */
    void confirmAppConnected() {
        speechBubbleText.setText(getString(R.string.first_menu_app_connected));
        readyToProgress = true;
    }

    /**
     * Sets speech bubble text to first_menu_app_cant_connect String
     * Shows fitnessAppsLayout
     * Moves speech bubble to the right
     */
    void confirmCantConnect() {
        speechBubbleText.setText(getString(R.string.first_menu_app_cant_connect));
        showMenu(fitnessAppsLayout);
        pushSpeechBubbleToRight(fitnessAppsLayout);
    }

    /**
     * Sets speech bubble text to team_mode_details String
     * Changes background colour of teamMode to primaryDark
     * Changes background colour of playerMode to black
     */
    public void whatIsTeamMode(View view) {
        speechBubbleText.setText(getString(R.string.team_mode_details));
        teamModeBackgroundColour.setBackgroundColor(getResources().getColor(R.color.colorPrimaryDark));
        playerModeBackgroundColour.setBackgroundColor(getResources().getColor(R.color.colorBlack));
    }

    /**
     * Sets speech bubble text to player_mode_details String
     * Changes background colour of teamMode to black
     * Changes background colour of playerMode to primaryDark
     */
    public void whatIsPlayerMode(View view) {
        speechBubbleText.setText(getString(R.string.player_mode_details));
        teamModeBackgroundColour.setBackgroundColor(getResources().getColor(R.color.colorBlack));
        playerModeBackgroundColour.setBackgroundColor(getResources().getColor(R.color.colorPrimaryDark));
    }

    /**
     * Sets speech bubble text to first_menu_game_mode_selected String and passes the words manager and team
     * Hides playerModeLayout
     * Moves speech bubble above jamesImage
     */
    public void selectTeamMode(View view) {
        gameMode = GameModes.TEAM_MODE;
        hideMenu(playerModeLayout);
        speechBubbleText.setText(getString(R.string.first_menu_game_mode_selected, "manager", "team"));
        moveSpeechBubbleUp(jamesImage);
        readyToProgress = true;

    }

    /**
     * Sets speech bubble text to first_menu_game_mode_selected String and passes the words player and player
     * Hides playerModeLayout
     * Moves speech bubble above jamesImage
     */
    public void selectPlayerMode(View view) {
        gameMode = GameModes.PLAYER_MODE;
        hideMenu(playerModeLayout);
        speechBubbleText.setText(getString(R.string.first_menu_game_mode_selected, "player", "player"));
        moveSpeechBubbleUp(jamesImage);
        readyToProgress = true;
    }

    public void confirmTeamPlayerName(View view) {
        teamPlayerName = teamPlayerNameText.getEditText().getText().toString();
        if (teamPlayerName.equals("")) {
            speechBubbleText.setText(getString(R.string.first_menu_empty_name));
            return;
        }
        hideKeyboard(this);
        if (gameMode == GameModes.TEAM_MODE) {
            speechBubbleText.setText(getString(R.string.first_menu_team_nane_confirmed, teamPlayerName));
        } else {
            speechBubbleText.setText(getString(R.string.first_menu_player_name_confirmed, teamPlayerName));
        }

        hideMenu(teamPlayerNameLayout);
        if (gameMode == GameModes.TEAM_MODE) {
            populateTeamColourSpinner();
            showMenu(teamColourLayout);
        } else {
            populateFavouriteTeamSpinner();
            showMenu(favouriteTeamLayout);
        }
    }

    public void confirmTeamColour() {
        hideMenu(teamColourLayout);
        putSpeechBubbleBackIntoMiddle();
        readyToProgress = true;
        speechBubbleText.setText(getString(R.string.first_menu_team_colour_confirmed, teamPlayerName, Words.getLeagueName(4)));
    }

    public void confirmFavouriteTeam(View view) {
        favouriteTeam = favouriteTeamSpinner.getSelectedItem().toString();
        hideMenu(favouriteTeamLayout);
        putSpeechBubbleBackIntoMiddle();
        readyToProgress = true;
        speechBubbleText.setText(getString(R.string.first_menu_favourite_club_confirmed, favouriteTeam));

    }

    public void whatIsTargetMode(View view) {
        speechBubbleText.setText(getString(R.string.target_mode_details));
        targetModeBackgroundColour.setBackgroundColor(getResources().getColor(R.color.colorPrimaryDark));
        scaledModeBackgroundColour.setBackgroundColor(getResources().getColor(R.color.colorBlack));
    }
    public void whatIsScaledMode(View view) {
        speechBubbleText.setText(getString(R.string.scaled_mode_details));
        targetModeBackgroundColour.setBackgroundColor(getResources().getColor(R.color.colorBlack));
        scaledModeBackgroundColour.setBackgroundColor(getResources().getColor(R.color.colorPrimaryDark));
    }
    public void selectTargetMode(View view) {
        stepMode = StepModes.TARGETED_MODE;
        hideMenu(targetModeLayout);
        speechBubbleText.setText(getString(R.string.first_menu_targeted_mode_selected));
        moveSpeechBubbleUp(jamesImage);
        readyToProgress = true;

    }
    public void selectScaledMode(View view) {
        stepMode = StepModes.TARGETED_MODE;
        hideMenu(targetModeLayout);
        speechBubbleText.setText(getString(R.string.first_menu_scaled_mode_selected));
        moveSpeechBubbleUp(jamesImage);
        readyToProgress = true;
    }

    public void whatIsMyAverage(View view) {
        hideMenu(stepTargetLayout);
        speechBubbleText.setText(getString(R.string.first_menu_getting_average));
        putSpeechBubbleBackIntoMiddle();

        Date yesterday = DateHelper.addDays(new Date(), -1);
        IntegrationConnectionHandler.getInstance().getAverageStepsFrom(DateHelper.addDays(yesterday, -7), yesterday);

    }
    /**
     * Method called by IntegrationConnectionHandler.ConnectionListener. Is called when we've had response from appropriate API class with average
     * @param average the number returned by API. Can be 0 when there are problems.
     */
    @Override
    public void getAverageSteps(int average) {
        if (average > 0) { speechBubbleText.setText(getString(R.string.first_menu_got_average, "7", Words.getNumberWithCommas(average))); }
        else { speechBubbleText.setText(getString(R.string.first_menu_cant_get_average)); }
        showMenu(stepTargetLayout);
        pushSpeechBubbleToRight(stepTargetLayout);

    }

    public void confirmStepTarget(View view) {

        stepTarget = Integer.parseInt(stepTargetSpinner.getSelectedItem().toString()) * 1000;
        hideMenu(stepTargetLayout);
        putSpeechBubbleBackIntoMiddle();
        readyToProgress = true;
        speechBubbleText.setText(getString(R.string.first_menu_got_step_target, String.valueOf(stepTarget)));

    }

    public void confirmDaysBetween(View view) {
        daysBetween = Integer.parseInt(daysBetweenSpinner.getSelectedItem().toString());
        hideMenu(daysBetweenLayout);
        putSpeechBubbleBackIntoMiddle();
        if (gameMode == GameModes.TEAM_MODE && stepMode == StepModes.TARGETED_MODE) {
            speechBubbleText.setText(getString(
                    R.string.first_menu_team_target_final_conf,
                    teamPlayerName,
                    Words.getLeagueName(4),
                    String.valueOf(daysBetween),
                    Words.getNumberWithCommas(stepTarget),
                    FitnessApps.getFitnessAppName(AppSettings.getInstance().getFitnessApp())));
        }

        readyButton.setVisibility(View.VISIBLE);
    }

    public void confirmReady(View view) {

        newGame();

    }

    void newGame() {

        AppSettings.getInstance().setupTeamGame(stepMode, stepTarget);
        CareerSettings.getInstance().createNewCareerSettings(daysBetween);
        CareerSettings.getInstance().startNewSeason();

        if (gameMode == GameModes.TEAM_MODE) {
            replaceRandomTeam();
        }

        Intent intent = new Intent(this, TMMainMenu.class);
        startActivity(intent);

    }

    @Override
    public void teamsRetrieved() {

        animateJamesIntroduction();




    }




    void replaceRandomTeam() {
        List<Team> league4Teams = CareerSavedData.getInstance().getAllTeamsInLeague(4);
        Random r = new Random();
        int randomTeam = r.nextInt(league4Teams.size());
        Team teamToReplace = league4Teams.get(randomTeam);
        teamToReplace.changeName(teamPlayerName);
        teamToReplace.changeColour(teamColour);
        CareerSettings.getInstance().setTeamID(teamToReplace.getID());
    }


    void loadGame() {
        AppSettingsSavedData.getInstance().loadSettings();
        CareerSavedData.getInstance().loadSettings();

        Intent intent = new Intent(this, TMMainMenu.class);
        startActivity(intent);
    }



    public void hideKeyboard(Activity activity) {
        View view = activity.getCurrentFocus();
        if (view != null) {
            InputMethodManager imm = (InputMethodManager) activity.getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }
}
