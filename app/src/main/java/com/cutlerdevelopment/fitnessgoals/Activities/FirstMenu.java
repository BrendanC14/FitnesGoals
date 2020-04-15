package com.cutlerdevelopment.fitnessgoals.Activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.animation.Animator;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.cutlerdevelopment.fitnessgoals.Constants.FitnessApps;
import com.cutlerdevelopment.fitnessgoals.Constants.Numbers;
import com.cutlerdevelopment.fitnessgoals.Integrations.GoogleFitAPI;
import com.cutlerdevelopment.fitnessgoals.R;
import com.cutlerdevelopment.fitnessgoals.Settings.AppSettings;
import com.cutlerdevelopment.fitnessgoals.ViewAdapters.FitnessAppSmallCardAdapter;
import com.cutlerdevelopment.fitnessgoals.ViewItems.FitnessAppSmallCard;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.auth.api.signin.GoogleSignInStatusCodes;

import java.util.ArrayList;

public class FirstMenu extends AppCompatActivity {

    Boolean readyToProgress = false;
    ConstraintLayout backgroundLayout;
    ImageView jamesImage;
    ConstraintLayout speechBubbleLayout;
    TextView speechBubbleText;
    ConstraintLayout fitnessAppsLayout;
    ListView fitnessAppsList;
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

        speechBubbleText.setText(getText(R.string.first_menu_intro_text_1));
        backgroundLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                nextStep();
            }
        });
        animateJamesIntroduction();
    }

    /**
     * In this method James slides onto the screen from the right, the speech bubble drops down too far then bounces back up, and
     * the Next button slides in from the left. It does all of this after a delay.
     * Delays and speed are controlled by FIRST_MENU variables in Numbers.
     */
    void animateJamesIntroduction() {

        final Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {

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
        }, Numbers.FIRST_MENU_JAMES_APPEAR_WAIT);

    }

    void nextStep() {

        if (readyToProgress) {
            switch (tutorialStep) {
                case 1:
                    speechBubbleText.setText(getString(R.string.first_menu_intro_text_2_1));
                    break;
                case 2:
                    speechBubbleText.setText(getString(R.string.first_menu_intro_text_2_2));
                    readyToProgress = false;
                    populateFitnessAppList();
                    showMenu(fitnessAppsLayout);
                    break;
                case 3:
                    speechBubbleText.setText(getString(R.string.first_menu_intro_text_3));

            }

            tutorialStep++;
        }
    }

    void showMenu(ConstraintLayout layoutToShow) {

        float speechBubbleStart = backgroundLayout.getWidth() / Numbers.FIRST_MENU_FITNESS_APP_DIVIDER;

        ValueAnimator resizeSpeechBubbleAnimator = ValueAnimator.ofInt(
                speechBubbleLayout.getMeasuredWidth(),
                (speechBubbleLayout.getMeasuredWidth() / 2));
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

        AnimatorSet moveSpeechBubbleAndShowFitnessAppsSet = new AnimatorSet();

        moveSpeechBubbleAndShowFitnessAppsSet.playTogether(resizeSpeechBubbleAnimator,
                ObjectAnimator.ofFloat( //Animating the speech bubble moving over
                        speechBubbleLayout,
                        "x",
                        speechBubbleStart)
                        .setDuration(Numbers.FIRST_MENU_FITNESS_ANIM_DURATION),
                ObjectAnimator.ofFloat(
                        layoutToShow,
                        "x",
                        Numbers.FIRST_MENU_FITNESS_MARGIN_PADDING)
                .setDuration(Numbers.FIRST_MENU_FITNESS_ANIM_DURATION)
                );
        moveSpeechBubbleAndShowFitnessAppsSet.start();
    }
    void hideMenu(ConstraintLayout layoutToHide) {

        float speechBubbleXPos = (backgroundLayout.getWidth() / Numbers.FIRST_MENU_FITNESS_APP_DIVIDER)
                - (speechBubbleLayout.getWidth() * 2 / Numbers.FIRST_MENU_FITNESS_APP_DIVIDER);

        ValueAnimator resizeSpeechBubbleAnimator = ValueAnimator.ofInt(
                speechBubbleLayout.getMeasuredWidth(),
                (speechBubbleLayout.getMeasuredWidth() * 2));
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

        AnimatorSet moveSpeechBubbleAndHideFitnessAppsSet = new AnimatorSet();

        moveSpeechBubbleAndHideFitnessAppsSet.playTogether(resizeSpeechBubbleAnimator,
                ObjectAnimator.ofFloat( //Animating the speech bubble moving over
                        speechBubbleLayout,
                        "x",
                        speechBubbleXPos)
                        .setDuration(Numbers.FIRST_MENU_FITNESS_ANIM_DURATION),
                ObjectAnimator.ofFloat(
                        layoutToHide,
                        "x",
                        0 - fitnessAppsLayout.getWidth())
                        .setDuration(Numbers.FIRST_MENU_FITNESS_ANIM_DURATION)
        );
        moveSpeechBubbleAndHideFitnessAppsSet.start();
    }

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

    void selectFitnessApp(int app) {
        switch (app) {
            case FitnessApps.MOCKED:
                speechBubbleText.setText(R.string.first_menu_app_connected);
                hideMenu(fitnessAppsLayout);
                break;
            case FitnessApps.GOOGLE_FIT:
                speechBubbleText.setText(R.string.first_menu_calling_app);
                hideMenu(fitnessAppsLayout);
                GoogleFitAPI.createGoogleFitAPIInstance(this);
                if (GoogleFitAPI.getInstance().hasPermissions(this)) {
                    confirmAppConnected();
                }
                break;
            default:
                speechBubbleText.setText(R.string.first_menu_app_unavailable);
        }
        AppSettings.getInstance().changeFitnessApp(app);

    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == 1234) {
            GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(data);
            int statusCode = result.getStatus().getStatusCode();
            if (statusCode == GoogleSignInStatusCodes.SUCCESS) {
                confirmAppConnected();
            }
            else {
                confirmCantConnect();
            }
        }
    }

    void confirmAppConnected() {
        speechBubbleText.setText(getString(R.string.first_menu_app_connected));
        AppSettings.getInstance().changeFitnessApp(FitnessApps.GOOGLE_FIT);
        readyToProgress = true;
    }
    void confirmCantConnect() {
        speechBubbleText.setText(getString(R.string.first_menu_app_cant_connect));
        showMenu(fitnessAppsLayout);
    }
}
