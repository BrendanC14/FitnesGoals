package com.cutlerdevelopment.fitnessgoals.Activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.animation.Animator;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.cutlerdevelopment.fitnessgoals.Constants.Numbers;
import com.cutlerdevelopment.fitnessgoals.R;

public class FirstMenu extends AppCompatActivity {

    Boolean jamesIntroduced = false;
    ConstraintLayout backgroundLayout;
    ImageView jamesImage;
    ConstraintLayout speechBubbleLayout;
    TextView speechBubbleText;
    int tutorialStep = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.first_menu);

        backgroundLayout = findViewById(R.id.firstMenuBackground);
        jamesImage = findViewById(R.id.firstMenuJamesImage);
        speechBubbleLayout = findViewById(R.id.firstMenuSpeechBubble);
        speechBubbleText = findViewById(R.id.firstMenuSpeechText);

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
                        jamesIntroduced = true;
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

        if (jamesIntroduced) {
            switch (tutorialStep) {
                case 1:
                    speechBubbleText.setText(getString(R.string.first_menu_intro_text_2));
                    break;
                case 2:
                    speechBubbleText.setText(getString(R.string.first_menu_intro_text_3));
                    showFitnessApps();
                    break;
            }

            tutorialStep++;
        }
    }

    void showFitnessApps() {

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

        AnimatorSet moveSpeechBubbleSet = new AnimatorSet();

        moveSpeechBubbleSet.playTogether(resizeSpeechBubbleAnimator,
                ObjectAnimator.ofFloat( //Animating the speech bubble moving over
                        speechBubbleLayout,
                        "x",
                        speechBubbleStart)
                        .setDuration(Numbers.FIRST_MENU_FITNESS_ANIM_DURATION)

        );
        moveSpeechBubbleSet.start();
    }
}
