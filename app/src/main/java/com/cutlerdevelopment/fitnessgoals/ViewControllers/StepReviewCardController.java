package com.cutlerdevelopment.fitnessgoals.ViewControllers;

import android.animation.AnimatorSet;
import android.animation.ValueAnimator;
import android.content.ClipData;
import android.content.ClipDescription;
import android.content.Context;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.cutlerdevelopment.fitnessgoals.Constants.Numbers;
import com.cutlerdevelopment.fitnessgoals.Integrations.IntegrationConnectionHandler;
import com.cutlerdevelopment.fitnessgoals.R;
import com.cutlerdevelopment.fitnessgoals.SavedData.AppSavedData;
import com.cutlerdevelopment.fitnessgoals.SavedData.CareerSavedData;
import com.cutlerdevelopment.fitnessgoals.Settings.AppSettings;
import com.cutlerdevelopment.fitnessgoals.Settings.CareerSettings;
import com.cutlerdevelopment.fitnessgoals.Utils.DateHelper;
import com.cutlerdevelopment.fitnessgoals.Utils.StringHelper;

import java.util.Date;

import static android.view.View.GONE;
import static android.view.View.VISIBLE;

public class StepReviewCardController implements IntegrationConnectionHandler.TMListener {

    private Context c;

    private  Button leftDateButton;
    private  TextView dateText;
    private  Button rightDateButton;
    private  ImageView goldenFootball;
    private  ProgressBar progressBar;
    private  TextView stepsText;
    private  TextView notEnoughDataText;

    private boolean animatingGoldenFootball = false;
    private Date dateChosen;
    private Date yesterday;

    ValueAnimator animateProgressCircle;


    public StepReviewCardController(View card, Context context) {
        c = context;
        leftDateButton = card.findViewById(R.id.stepReviewLeft);
        dateText = card.findViewById(R.id.stepReviewDate);
        rightDateButton = card.findViewById(R.id.stepReviewRight);
        goldenFootball = card.findViewById(R.id.stepReviewGoldFootball);
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
    }

    private void refreshStepCircle() {
        AppSavedData savedData = AppSavedData.getInstance();
        if (savedData.getActivityOnDate(dateChosen) == null) {
            displayNotEnoughDataMessage();
            return;
        }
        progressBar.setVisibility(VISIBLE);
        notEnoughDataText.setVisibility(GONE);

        stepsText.setText(String.valueOf(1));

        progressBar.setProgress(0);

        int steps = savedData.getActivityOnDate(dateChosen).getSteps();
        progressBar.setMax(AppSettings.getInstance().getStepTarget());
        updateProgressCircleAndSteps(steps);

    }

    private void backADay() {
        dateChosen = DateHelper.addDays(dateChosen, -1);
        dateText.setText(DateHelper.formatDateToString(dateChosen));
        stepsText.setTextColor(c.getResources().getColor(R.color.colorWhite));
        animateProgressCircle.cancel();
        refreshStepCircle();
        takeFootballOut();
        animatingGoldenFootball = false;
        checkDayButtonValidity();
    }

    private void forwardADay() {
        dateChosen = DateHelper.addDays(dateChosen, 1);
        if (dateChosen.equals(yesterday)) { dateText.setText(R.string.step_review_yesterday); }
        else { dateText.setText(DateHelper.formatDateToString(dateChosen)); }
        stepsText.setTextColor(c.getResources().getColor(R.color.colorWhite));
        animateProgressCircle.cancel();
        refreshStepCircle();
        takeFootballOut();
        animatingGoldenFootball = false;
        checkDayButtonValidity();

    }

    private void checkDayButtonValidity() {

        Date firstDate = DateHelper.cleanDate(AppSavedData.getInstance().getFirstAddedActivity().getDate());
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
                    stepsText.setTextColor(c.getResources().getColor(R.color.colorAccent));
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
