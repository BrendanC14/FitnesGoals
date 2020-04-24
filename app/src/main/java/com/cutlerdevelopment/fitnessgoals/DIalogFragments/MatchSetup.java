package com.cutlerdevelopment.fitnessgoals.DIalogFragments;

import android.animation.Animator;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.app.AlertDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.Button;
import android.widget.GridView;
import android.widget.TextView;

import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.DialogFragment;

import com.cutlerdevelopment.fitnessgoals.Constants.Numbers;
import com.cutlerdevelopment.fitnessgoals.Constants.TacticOptions;
import com.cutlerdevelopment.fitnessgoals.Models.Fixture;
import com.cutlerdevelopment.fitnessgoals.R;
import com.cutlerdevelopment.fitnessgoals.SavedData.AppSavedData;
import com.cutlerdevelopment.fitnessgoals.SavedData.CareerSavedData;
import com.cutlerdevelopment.fitnessgoals.Settings.AppSettings;
import com.cutlerdevelopment.fitnessgoals.Settings.CareerSettings;
import com.cutlerdevelopment.fitnessgoals.Settings.UserActivity;
import com.cutlerdevelopment.fitnessgoals.Utils.DateHelper;
import com.cutlerdevelopment.fitnessgoals.Utils.StringHelper;
import com.cutlerdevelopment.fitnessgoals.ViewAdapters.MatchSetupStepDropAdapter;
import com.cutlerdevelopment.fitnessgoals.ViewItems.MatchSetupDropItem;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;

public class MatchSetup extends DialogFragment implements MatchSetupStepDropAdapter.MatchSetupInterface {

    private GridView defendingStepsList;
    private GridView attackingStepsList;
    private GridView usersStepsGridViews;

    private TextView defendingAverageText;
    private TextView attackingAverageText;
    private TextView oppDefendingText;
    private TextView oppAttackingText;
    private Button defendingTactic;
    private Button attackingTactic;
    private Button randomTactic;
    private Button evenTactic;
    private Button playMatch;


    MatchSetupStepDropAdapter defensiveAdapter;
    MatchSetupStepDropAdapter attackingAdapter;
    MatchSetupStepDropAdapter userAdapter;

    List<Integer> stepList;

    int daysBetween;

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = requireActivity().getLayoutInflater();
        View matchSetupView = inflater.inflate(R.layout.match_setup, null);

        defendingStepsList = matchSetupView.findViewById(R.id.matchSetupDefendingList);
        attackingStepsList = matchSetupView.findViewById(R.id.matchSetupAttackingList);
        usersStepsGridViews = matchSetupView.findViewById(R.id.matchSetupUserStepsGrid);
        defendingAverageText = matchSetupView.findViewById(R.id.matchSetupDefendingAverage);
        attackingAverageText = matchSetupView.findViewById(R.id.matchSetupAttackingAverage);
        oppDefendingText = matchSetupView.findViewById(R.id.matchSetupOpponentDefending);
        oppAttackingText = matchSetupView.findViewById(R.id.matchSetupOpponentAttacking);
        defendingTactic = matchSetupView.findViewById(R.id.matchSetupDefendButton);
        attackingTactic = matchSetupView.findViewById(R.id.matchSetupAttackButton);
        randomTactic = matchSetupView.findViewById(R.id.matchSetupRandomButton);
        evenTactic = matchSetupView.findViewById(R.id.matchSetupEvenButton);
        playMatch = matchSetupView.findViewById(R.id.matchSetupPlayMatchButton);

        stepList = new ArrayList<>();
        daysBetween = CareerSettings.getInstance().getDaysBetween();
        final ArrayList<MatchSetupDropItem> myDefensiveItems = new ArrayList<>();
        for (int i = 0; i < daysBetween / 2; i++) {
            MatchSetupDropItem item = new MatchSetupDropItem();
            item.setSteps("");
            item.setDraggable(false);
            myDefensiveItems.add(item);
        }
        final ArrayList<MatchSetupDropItem> myAttackingItems = new ArrayList<>();
        for (int i = 0; i < daysBetween / 2; i++) {
            MatchSetupDropItem item = new MatchSetupDropItem();
            item.setSteps("");
            item.setDraggable(false);
            myAttackingItems.add(item);
        }
        Fixture f = CareerSavedData.getInstance().getNextFixtureForTeam(CareerSettings.getInstance().getTeamID());
        Date startDate = DateHelper.addDays(f.getDate(), daysBetween * -1);
        Date endDate = DateHelper.addDays(f.getDate(), -1);

        final  ArrayList<MatchSetupDropItem> userItems = new ArrayList<>();
        List<UserActivity> allActivity = AppSavedData.getInstance().getActivityOnAllDates(startDate, endDate);
        for (UserActivity activity : allActivity) {
            MatchSetupDropItem item = new MatchSetupDropItem();
            item.setSteps(StringHelper.getNumberWithCommas(activity.getSteps()));
            userItems.add(item);
            item.setDraggable(true);
            stepList.add(activity.getSteps());
        }

        defensiveAdapter = new MatchSetupStepDropAdapter(getActivity().getApplicationContext(), myDefensiveItems, this);
        defendingStepsList.setAdapter(defensiveAdapter);
        defendingStepsList.setNumColumns(2);

        attackingAdapter = new MatchSetupStepDropAdapter(getActivity().getApplicationContext(), myAttackingItems, this);
        attackingStepsList.setAdapter(attackingAdapter);
        attackingStepsList.setNumColumns(2);

        userAdapter = new MatchSetupStepDropAdapter(getActivity().getApplicationContext(), userItems, this);
        usersStepsGridViews.setAdapter(userAdapter);
        usersStepsGridViews.setNumColumns(4);

        defendingAverageText.setText(String.valueOf(0));
        attackingAverageText.setText(String.valueOf(0));
        defendingAverageText.setTextColor(getResources().getColor(R.color.colorRed));
        attackingAverageText.setTextColor(getResources().getColor(R.color.colorRed));
        oppDefendingText.setText(StringHelper.getNumberWithCommas(AppSettings.getInstance().getStepTarget()));
        oppAttackingText.setText(StringHelper.getNumberWithCommas(AppSettings.getInstance().getStepTarget()));

        defendingTactic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                applyTactic(TacticOptions.DEFENDING);
            }
        });
        attackingTactic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                applyTactic(TacticOptions.ATTACKING);
            }
        });
        randomTactic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                applyTactic(TacticOptions.RANDOM);
            }
        });
        evenTactic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                applyTactic(TacticOptions.EVEN);
            }
        });

        builder.setView(matchSetupView);
        return builder.create();
    }

    @Override
    public void itemDropped() {
        int defensiveNumber = 0;
        int defensiveSteps = 0;
        for (int i = 0; i < defendingStepsList.getChildCount(); i++) {
            ConstraintLayout layout = (ConstraintLayout) defendingStepsList.getChildAt(i);
            Button view = (Button) layout.getChildAt(0);
            if (!view.getText().equals("")) {
                defensiveNumber++;
                defensiveSteps += Integer.parseInt(StringHelper.removeCommaFromString(view.getText().toString()));
            }
        }
        int attackingNumber = 0;
        int attackingSteps = 0;
        for (int i = 0; i < attackingStepsList.getChildCount(); i++) {
            ConstraintLayout layout = (ConstraintLayout)  attackingStepsList.getChildAt(i);
            Button view = (Button) layout.getChildAt(0);
            if (!view.getText().equals("")) {
                attackingNumber++;
                attackingSteps += Integer.parseInt(StringHelper.removeCommaFromString(view.getText().toString()));
            }
        }

        int defensiveAverageNumber = defensiveSteps > 0 ? defensiveSteps / defensiveNumber : 0;
        int attackingAverageNumber = attackingSteps > 0 ? attackingSteps / attackingNumber : 0;
        if (defensiveNumber + attackingNumber == daysBetween) { playMatch.setVisibility(View.VISIBLE); }
        else { playMatch.setVisibility(View.GONE); }
        defendingAverageText.setText(StringHelper.getNumberWithCommas(defensiveAverageNumber));
        if (defensiveAverageNumber >= AppSettings.getInstance().getStepTarget()) { defendingAverageText.setTextColor(getResources().getColor(R.color.colorAccent)); }
        else { defendingAverageText.setTextColor(getResources().getColor(R.color.colorRed)); }

        attackingAverageText.setText(StringHelper.getNumberWithCommas(attackingAverageNumber));
        if (attackingAverageNumber >= AppSettings.getInstance().getStepTarget()) { attackingAverageText.setTextColor(getResources().getColor(R.color.colorAccent)); }
        else { attackingAverageText.setTextColor(getResources().getColor(R.color.colorRed)); }
    }

    void applyTactic(int tactic) {

        List<Integer> attackingNumbers = new ArrayList<>();
        List<Integer> defendingNumbers = new ArrayList<>();

        Collections.sort(stepList);
        int numDays = stepList.size();

        if (tactic == TacticOptions.EVEN) {
            for (int i = 0; i < numDays; i += 2) {
                defendingNumbers.add(stepList.get(i));
            }

            for (int i = 1; i < numDays; i += 2) {
                attackingNumbers.add(stepList.get(i));
            }
        }
        else {
            if (tactic == TacticOptions.RANDOM) { Collections.shuffle(stepList); }
            //Sorted smallest to largest. Go through first half (lower step numbers)
            for (int i = 0; i < numDays / 2; i++) {
                switch (tactic) {
                    case TacticOptions.ATTACKING:
                        defendingNumbers.add(stepList.get(i));
                        break;
                    case TacticOptions.DEFENDING:
                        attackingNumbers.add(stepList.get(i));
                        break;
                    case TacticOptions.RANDOM:
                        defendingNumbers.add(stepList.get(i));
                        break;
                }
            }
            //Go through second half (higher step numbers)
            for (int i = numDays / 2; i < numDays; i++) {
                switch (tactic) {
                    case TacticOptions.ATTACKING:
                        attackingNumbers.add(stepList.get(i));
                        break;
                    case TacticOptions.DEFENDING:
                        defendingNumbers.add(stepList.get(i));
                        break;
                    case TacticOptions.RANDOM:
                        attackingNumbers.add(stepList.get(i));
                        break;
                }
            }
        }

        List<Button> allButtons = new ArrayList<>();
        //Go through each grid view and add create a list of buttons (including the empty ones)
        for (int i = 0; i < usersStepsGridViews.getChildCount(); i++) {
            ConstraintLayout stepLayout = (ConstraintLayout) usersStepsGridViews.getChildAt(i);
            Button button = (Button) stepLayout.getChildAt(0);
            allButtons.add(button);
        }

        for (int i = 0; i < defendingStepsList.getChildCount(); i++) {
            ConstraintLayout stepLayout = (ConstraintLayout) defendingStepsList.getChildAt(i);
            Button button = (Button) stepLayout.getChildAt(0);
            allButtons.add(button);

        }

        for (int i = 0; i < attackingStepsList.getChildCount(); i++) {
            ConstraintLayout stepLayout = (ConstraintLayout) attackingStepsList.getChildAt(i);
            Button button = (Button) stepLayout.getChildAt(0);
            allButtons.add(button);

        }

        //Remove all parents from the current buttons
        for (Button b : allButtons) {
            ConstraintLayout layout = (ConstraintLayout) b.getParent();
            layout.removeView(b);
        }
        int emptyCount = 0;
        //Go back through the buttons and assign a new parent based on whether there's a text value and what list the step number is
        for (Button b2 : allButtons) {
            String text = b2.getText().toString();
            if (!text.equals("")) {
                ConstraintLayout newParent;
                int steps = Integer.parseInt(StringHelper.removeCommaFromString(text));
                if (attackingNumbers.contains(steps)) {
                    newParent = (ConstraintLayout) attackingStepsList.getChildAt(attackingNumbers.indexOf(steps));
                } else {
                    newParent = (ConstraintLayout) defendingStepsList.getChildAt(defendingNumbers.indexOf(steps));
                }
                animateViewsMoving(newParent, b2);
            }
            else {
                animateViewsMoving((ConstraintLayout) usersStepsGridViews.getChildAt(emptyCount), b2);
                emptyCount++;

            }
        }

        itemDropped();
    }


    void animateViewsMoving(final ConstraintLayout newParent, final Button stepButton) {

        moveViews(
                newParent,
                stepButton);
    }


    void moveViews(ConstraintLayout newParent, Button stepItem) {
        newParent.addView(stepItem);
    }
}
