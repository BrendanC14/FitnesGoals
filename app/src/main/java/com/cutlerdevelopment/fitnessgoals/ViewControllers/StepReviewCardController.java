package com.cutlerdevelopment.fitnessgoals.ViewControllers;

import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.PorterDuff;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.constraintlayout.widget.ConstraintLayout;

import com.cutlerdevelopment.fitnessgoals.Constants.Colours;
import com.cutlerdevelopment.fitnessgoals.Constants.Numbers;
import com.cutlerdevelopment.fitnessgoals.Integrations.IntegrationConnectionHandler;
import com.cutlerdevelopment.fitnessgoals.R;
import com.cutlerdevelopment.fitnessgoals.SavedData.AppDBHandler;
import com.cutlerdevelopment.fitnessgoals.Data.AppData;
import com.cutlerdevelopment.fitnessgoals.Data.UserActivity;
import com.cutlerdevelopment.fitnessgoals.Utils.DateHelper;
import com.cutlerdevelopment.fitnessgoals.Utils.StringHelper;
import com.google.android.material.card.MaterialCardView;

import java.util.Date;

import static android.view.View.GONE;
import static android.view.View.VISIBLE;

public class StepReviewCardController implements IntegrationConnectionHandler.TMListener {

    private Context c;

    private MaterialCardView cardView;
    private  Button leftDateButton;
    private  TextView dateText;
    private  Button rightDateButton;
    private  ImageView goldenFootball;
    private ConstraintLayout progressBarHolder;
    private  ProgressBar progressBar;
    private  TextView stepsText;
    private  TextView notEnoughDataText;

    private boolean animatingGoldenFootball = false;
    private Date dateChosen;
    private Date yesterday;

    int secondaryColour;
    ValueAnimator animateProgressCircle;


    public StepReviewCardController(View card, Context context) {
        c = context;
        cardView = card.findViewById(R.id.stepReviewCard);
        leftDateButton = card.findViewById(R.id.stepReviewLeft);
        dateText = card.findViewById(R.id.stepReviewDate);
        rightDateButton = card.findViewById(R.id.stepReviewRight);
        goldenFootball = card.findViewById(R.id.stepReviewGoldFootball);
        progressBarHolder = card.findViewById(R.id.stepReviewProgressBarHolder);
        progressBar = card.findViewById(R.id.stepReviewProgressBar);
        stepsText = card.findViewById(R.id.stepReviewSteps);
        notEnoughDataText = card.findViewById(R.id.stepReviewNotEnoughData);
        dateText.setText(R.string.step_review_yesterday);

        dateChosen = DateHelper.addDays(new Date(), - 1);
        yesterday = dateChosen;
        checkDayButtonValidity();
        refreshStepCircle();

        leftDateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                backADay();
            }
        });
        rightDateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                forwardADay();
            }
        });

        int primaryColour = Colours.getUsersPrimaryColour();
        secondaryColour = Colours.getUsersSecondaryColour();
        cardView.setBackgroundColor(primaryColour);
        leftDateButton.setTextColor(secondaryColour);
        dateText.setTextColor(secondaryColour);
        rightDateButton.setTextColor(secondaryColour);
        notEnoughDataText.setTextColor(secondaryColour);
        stepsText.setTextColor(secondaryColour);
        progressBar.getProgressDrawable().setColorFilter(
                secondaryColour, PorterDuff.Mode.SRC_IN
        );
    }

    private void refreshStepCircle() {
        AppDBHandler savedData = AppDBHandler.getInstance();
        if (savedData.getActivityOnDate(dateChosen) == null) {
            displayNotEnoughDataMessage();
            leftDateButton.setVisibility(View.INVISIBLE);
            rightDateButton.setVisibility(View.INVISIBLE);
            return;
        }
        progressBar.setVisibility(VISIBLE);
        notEnoughDataText.setVisibility(GONE);

        stepsText.setText(String.valueOf(1));

        progressBar.setProgress(1);

        int steps = savedData.getActivityOnDate(dateChosen).getSteps();
        progressBar.setMax(AppData.getInstance().getStepTarget());
        updateProgressCircleAndSteps(steps);

    }

    private void backADay() {
        dateChosen = DateHelper.addDays(dateChosen, -1);
        dateText.setText(DateHelper.formatDateToString(dateChosen));
        animateProgressCircle.cancel();

        Animation slideOutRight = AnimationUtils.loadAnimation(c,
                R.anim.slide_out_right);
        Animation slideInLeft = AnimationUtils.loadAnimation(c,
                R.anim.slide_in_left);

        ViewGroup.LayoutParams progressParams = progressBar.getLayoutParams();

        progressBar.startAnimation(slideOutRight);
        progressBarHolder.removeView(progressBar);
        ProgressBar newProgressBar = new ProgressBar(c, null, android.R.attr.progressBarStyleHorizontal);
        newProgressBar.setId(View.generateViewId());
        newProgressBar.setProgressDrawable(c.getResources().getDrawable(R.drawable.circular_progress_bar));

        newProgressBar.setLayoutParams(progressParams);
        progressBarHolder.addView(newProgressBar);
        newProgressBar.getProgressDrawable().setColorFilter(
                secondaryColour, PorterDuff.Mode.SRC_IN
        );
        this.progressBar = newProgressBar;
        newProgressBar.startAnimation(slideInLeft);

        refreshStepCircle();
        takeFootballOut();
        animatingGoldenFootball = false;
        checkDayButtonValidity();
    }

    private void forwardADay() {
        dateChosen = DateHelper.addDays(dateChosen, 1);
        if (dateChosen.equals(yesterday)) { dateText.setText(R.string.step_review_yesterday); }
        else { dateText.setText(DateHelper.formatDateToString(dateChosen)); }
        animateProgressCircle.cancel();

        Animation slideOutLeft = AnimationUtils.loadAnimation(c,
                R.anim.slide_out_left);
        Animation slideInRight = AnimationUtils.loadAnimation(c,
                R.anim.slide_in_right);

        ViewGroup.LayoutParams progressParams = progressBar.getLayoutParams();

        progressBar.startAnimation(slideOutLeft);
        progressBarHolder.removeView(progressBar);
        ProgressBar newProgressBar = new ProgressBar(c, null, android.R.attr.progressBarStyleHorizontal);
        newProgressBar.setId(View.generateViewId());
        newProgressBar.setProgressDrawable(c.getResources().getDrawable(R.drawable.circular_progress_bar));

        newProgressBar.setLayoutParams(progressParams);
        progressBarHolder.addView(newProgressBar);
        newProgressBar.getProgressDrawable().setColorFilter(
                secondaryColour, PorterDuff.Mode.SRC_IN
        );
        this.progressBar = newProgressBar;
        newProgressBar.startAnimation(slideInRight);


        refreshStepCircle();
        takeFootballOut();
        animatingGoldenFootball = false;
        checkDayButtonValidity();

    }

    private void checkDayButtonValidity() {

        UserActivity firstActivity = AppDBHandler.getInstance().getFirstAddedActivity();
        if (firstActivity == null) { return; }

        Date firstDate = firstActivity.getDate();
        Date yesterday = DateHelper.addDays(new Date(), - 1);
        if (dateChosen.equals(firstDate)) { leftDateButton.setVisibility(View.INVISIBLE); }
        else { leftDateButton.setVisibility(VISIBLE); }

        if (dateChosen.equals(yesterday)) { rightDateButton.setVisibility(View.INVISIBLE); }
        else { rightDateButton.setVisibility(VISIBLE); }

    }

    private  void displayNotEnoughDataMessage() {
        progressBar.setVisibility(GONE);
        stepsText.setVisibility(View.INVISIBLE);
        notEnoughDataText.setVisibility(VISIBLE);
        takeFootballOut();
    }

    private void updateProgressCircleAndSteps(int steps) {
         animateProgressCircle = ValueAnimator.ofInt(
                1,
                steps);
        animateProgressCircle.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator valueAnimator) {
                int val = (Integer) valueAnimator.getAnimatedValue();
                progressBar.setProgress(val);
                stepsText.setText(StringHelper.getNumberWithCommas(val));
                if (progressBar.getMax() > 0 && val > progressBar.getMax()) {
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


    private void enlargeFootball() {
        ValueAnimator enlargeFootballAnim = ValueAnimator.ofInt(
                goldenFootball.getWidth(),
                (int) c.getResources().getDimension(R.dimen.golden_football_height));
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

        enlargeFootballAnim.start();

    }
    private void takeFootballOut() {
        ViewGroup.LayoutParams layoutParams = goldenFootball.getLayoutParams();
        layoutParams.height = 1;
        layoutParams.width = 1;
        goldenFootball.setLayoutParams(layoutParams);

    }

    @Override
    public void gotStepMap() {
        refreshStepCircle();
    }

}
