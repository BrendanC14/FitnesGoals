package com.cutlerdevelopment.fitnessgoals.DIalogFragments;

import android.animation.ValueAnimator;
import android.app.AlertDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.DialogFragment;

import com.cutlerdevelopment.fitnessgoals.Constants.Colours;
import com.cutlerdevelopment.fitnessgoals.Constants.Numbers;
import com.cutlerdevelopment.fitnessgoals.Constants.StepModes;
import com.cutlerdevelopment.fitnessgoals.Constants.TacticOptions;
import com.cutlerdevelopment.fitnessgoals.Models.Fixture;
import com.cutlerdevelopment.fitnessgoals.Models.MatchEngine;
import com.cutlerdevelopment.fitnessgoals.Models.Team;
import com.cutlerdevelopment.fitnessgoals.R;
import com.cutlerdevelopment.fitnessgoals.SavedData.AppDBHandler;
import com.cutlerdevelopment.fitnessgoals.SavedData.GameDBHandler;
import com.cutlerdevelopment.fitnessgoals.Data.AppData;
import com.cutlerdevelopment.fitnessgoals.Data.GameData;
import com.cutlerdevelopment.fitnessgoals.Data.UserActivity;
import com.cutlerdevelopment.fitnessgoals.Utils.DateHelper;
import com.cutlerdevelopment.fitnessgoals.Utils.JamesStep;
import com.cutlerdevelopment.fitnessgoals.Utils.StringHelper;
import com.cutlerdevelopment.fitnessgoals.ViewAdapters.MatchSetupStepAdapter;
import com.cutlerdevelopment.fitnessgoals.ViewItems.MatchSetupItem;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;

public class MatchSetup extends DialogFragment implements MatchSetupStepAdapter.MatchSetupInterface {

    private LinearLayout cardView;

    private ImageView phoneButton;
    private TextView titleText;
    private TextView defendingHeader;
    private TextView attackingHeader;
    private TextView yourStepsText;
    private TextView opponentDefendingHeader;
    private TextView opponentAttackingHeader;

    private GridView defendingStepsList;
    private GridView attackingStepsList;
    private GridView usersStepsGridViews;

    private TextView defendingTotalText;
    private TextView attackingTotalText;
    private TextView oppDefendingText;
    private TextView oppAttackingText;

    private Button defendingTactic;
    private Button attackingTactic;
    private Button playMatch;


    MatchSetupStepAdapter defensiveAdapter;
    MatchSetupStepAdapter attackingAdapter;
    MatchSetupStepAdapter userAdapter;

    List<Integer> stepList;

    int daysBetween;
    int opponentDefence;
    int opponentAttack;
    int opponentDefAve;
    int opponentAttAve;
    int userDefence;
    int userAttack;

    Fixture f;

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = requireActivity().getLayoutInflater();
        View matchSetupView = inflater.inflate(R.layout.match_setup, null);

        phoneButton = matchSetupView.findViewById(R.id.matchSetupPhone);
        titleText = matchSetupView.findViewById(R.id.matchSetupTitle);
        defendingHeader = matchSetupView.findViewById(R.id.matchSetupDefendingHeader);
        attackingHeader = matchSetupView.findViewById(R.id.matchSetupAttackingHeader);
        yourStepsText = matchSetupView.findViewById(R.id.matchSetupUserStepsHeader);
        cardView = matchSetupView.findViewById(R.id.matchSetupLayout);
        opponentDefendingHeader = matchSetupView.findViewById(R.id.matchSetupOpponentDefendingHeader);
        opponentAttackingHeader = matchSetupView.findViewById(R.id.matchSetupOpponentAttackingHeader);
        defendingStepsList = matchSetupView.findViewById(R.id.matchSetupDefendingList);
        attackingStepsList = matchSetupView.findViewById(R.id.matchSetupAttackingList);
        usersStepsGridViews = matchSetupView.findViewById(R.id.matchSetupUserStepsGrid);
        defendingTotalText = matchSetupView.findViewById(R.id.matchSetupDefendingAverage);
        attackingTotalText = matchSetupView.findViewById(R.id.matchSetupAttackingAverage);
        oppDefendingText = matchSetupView.findViewById(R.id.matchSetupOpponentDefending);
        oppAttackingText = matchSetupView.findViewById(R.id.matchSetupOpponentAttacking);
        defendingTactic = matchSetupView.findViewById(R.id.matchSetupDefendButton);
        attackingTactic = matchSetupView.findViewById(R.id.matchSetupAttackButton);
        playMatch = matchSetupView.findViewById(R.id.matchSetupPlayMatchButton);
        Button closeButton = matchSetupView.findViewById(R.id.matchSetupCloseButton);

        stepList = new ArrayList<>();
        daysBetween = GameData.getInstance().getDaysBetween();
        final ArrayList<MatchSetupItem> myDefensiveItems = new ArrayList<>();
        for (int i = 0; i < daysBetween / 2; i++) {
            MatchSetupItem item = new MatchSetupItem();
            item.setSteps("");
            item.setDraggable(false);
            myDefensiveItems.add(item);
        }
        final ArrayList<MatchSetupItem> myAttackingItems = new ArrayList<>();
        for (int i = 0; i < daysBetween / 2; i++) {
            MatchSetupItem item = new MatchSetupItem();
            item.setSteps("");
            item.setDraggable(false);
            myAttackingItems.add(item);
        }
        f = GameDBHandler.getInstance().getNextFixtureForTeam(GameData.getInstance().getTeamID());
        Date startDate = DateHelper.addDays(f.getDate(), daysBetween * -1);
        Date endDate = DateHelper.addDays(f.getDate(), -1);

        final  ArrayList<MatchSetupItem> userItems = new ArrayList<>();
        List<UserActivity> allActivity = AppDBHandler.getInstance().getActivityOnAllDates(startDate, endDate);
        for (UserActivity activity : allActivity) {
            MatchSetupItem item = new MatchSetupItem();
            item.setSteps(StringHelper.getNumberWithCommas(activity.getSteps()));
            userItems.add(item);
            item.setDraggable(true);
            stepList.add(activity.getSteps());
        }

        defensiveAdapter = new MatchSetupStepAdapter(getActivity().getApplicationContext(), myDefensiveItems, this);
        defendingStepsList.setAdapter(defensiveAdapter);
        defendingStepsList.setNumColumns(2);

        attackingAdapter = new MatchSetupStepAdapter(getActivity().getApplicationContext(), myAttackingItems, this);
        attackingStepsList.setAdapter(attackingAdapter);
        attackingStepsList.setNumColumns(2);

        userAdapter = new MatchSetupStepAdapter(getActivity().getApplicationContext(), userItems, this);
        usersStepsGridViews.setAdapter(userAdapter);
        usersStepsGridViews.setNumColumns(4);

        int stepTarget = AppData.getInstance().getStepTarget();
        defendingTotalText.setText(String.valueOf(0));
        attackingTotalText.setText(String.valueOf(0));
        defendingTotalText.setTextColor(getResources().getColor(R.color.colorRed));
        attackingTotalText.setTextColor(getResources().getColor(R.color.colorRed));
        int numInEach = daysBetween / 2;
        if (AppData.getInstance().getStepMode() == StepModes.TARGETED_MODE) {
            opponentDefence = stepTarget * numInEach;
            opponentAttack = stepTarget * numInEach;
            opponentDefAve = opponentAttAve = stepTarget;
        }
        else {
            Team opponent = f.getOpponent(GameData.getInstance().getTeamID());
            opponentDefAve = opponent.getAverageDefendingSteps();
            opponentAttAve = opponent.getAverageAttackingSteps();
            opponentDefence = opponentDefAve * numInEach;
            opponentAttack = opponentAttAve * numInEach;
        }
        oppDefendingText.setText(StringHelper.getNumberWithCommas(opponentDefAve));
        oppAttackingText.setText(StringHelper.getNumberWithCommas(opponentAttAve));

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
        playMatch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                playMatch();
            }
        });

        int primaryColour = Colours.getUsersPrimaryColour();
        int secondaryColour = Colours.getUsersSecondaryColour();
        cardView.setBackgroundColor(primaryColour);
        titleText.setTextColor(secondaryColour);
        defendingHeader.setTextColor(secondaryColour);
        attackingHeader.setTextColor(secondaryColour);
        oppDefendingText.setTextColor(secondaryColour);
        oppAttackingText.setTextColor(secondaryColour);
        opponentDefendingHeader.setTextColor(secondaryColour);
        opponentAttackingHeader.setTextColor(secondaryColour);
        yourStepsText.setTextColor(secondaryColour);
        defendingTactic.setBackgroundColor(secondaryColour);
        defendingTactic.setTextColor(primaryColour);
        attackingTactic.setBackgroundColor(secondaryColour);
        attackingTactic.setTextColor(primaryColour);
        playMatch.setBackgroundColor(secondaryColour);
        playMatch.setTextColor(primaryColour);
        closeButton.setTextColor(secondaryColour);
        closeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dismiss();
            }
        });
        phoneButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                callJames();
            }
        });

        builder.setView(matchSetupView);
        return builder.create();
    }


    void applyTactic(int tactic) {

        List<Integer> attackingNumbers = new ArrayList<>();
        List<Integer> defendingNumbers = new ArrayList<>();

        Collections.sort(stepList);
        int numDays = stepList.size();
        if (numDays == 0) {
            return;
        }

        //Sorted smallest to largest. Go through first half (lower step numbers)
        for (int i = 0; i < numDays / 2; i++) {
            switch (tactic) {
                case TacticOptions.ATTACKING:
                    defendingNumbers.add(stepList.get(i));
                    break;
                case TacticOptions.DEFENDING:
                    attackingNumbers.add(stepList.get(i));
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
        int attackIndex = 0;
        int defendIndex = 0;
        for (Button b2 : allButtons) {
            String text = b2.getText().toString();
            if (!text.equals("")) {
                ConstraintLayout newParent;
                int steps = Integer.parseInt(StringHelper.removeCommaFromString(text));
                if (attackingNumbers.contains(steps)) {
                    newParent = (ConstraintLayout) attackingStepsList.getChildAt(attackIndex);
                    attackIndex++;
                } else {
                    newParent = (ConstraintLayout) defendingStepsList.getChildAt(defendIndex);
                    defendIndex++;
                }
                animateViewsMoving(newParent, b2, false);
            } else {
                animateViewsMoving((ConstraintLayout) usersStepsGridViews.getChildAt(emptyCount), b2, true);
                emptyCount++;

            }
        }

        itemDropped();
    }

    @Override
    public void itemDropped() {
        userAttack = 0;
        userDefence = 0;
        int defensiveNumber = 0;
        for (int i = 0; i < defendingStepsList.getChildCount(); i++) {
            ConstraintLayout layout = (ConstraintLayout) defendingStepsList.getChildAt(i);
            Button view = (Button) layout.getChildAt(0);
            if (view == null) { return; }
            if (!view.getText().equals("")) {
                defensiveNumber++;
                userDefence += Integer.parseInt(StringHelper.removeCommaFromString(view.getText().toString()));
            }
        }
        int attackingNumber = 0;
        for (int i = 0; i < attackingStepsList.getChildCount(); i++) {
            ConstraintLayout layout = (ConstraintLayout)  attackingStepsList.getChildAt(i);
            Button view = (Button) layout.getChildAt(0);
            if (view == null) { return; }
            if (!view.getText().equals("")) {
                attackingNumber++;
                userAttack += Integer.parseInt(StringHelper.removeCommaFromString(view.getText().toString()));
            }
        }

        int userDefenceAverage = userDefence / (daysBetween / 2);
        int userAttackAverage = userAttack / (daysBetween / 2);

        if (defensiveNumber + attackingNumber == daysBetween) { playMatch.setVisibility(View.VISIBLE); }
        else { playMatch.setVisibility(View.GONE); }

        defendingTotalText.setText(StringHelper.getNumberWithCommas(userDefenceAverage));
        if (userDefenceAverage >= opponentAttAve) { defendingTotalText.setTextColor(getResources().getColor(R.color.colorAccent)); }
        else { defendingTotalText.setTextColor(getResources().getColor(R.color.colorRed)); }

        attackingTotalText.setText(StringHelper.getNumberWithCommas(userAttackAverage));
        if (userAttackAverage >= opponentDefAve) { attackingTotalText.setTextColor(getResources().getColor(R.color.colorAccent)); }
        else { attackingTotalText.setTextColor(getResources().getColor(R.color.colorRed)); }
    }

    void animateViewsMoving(final ConstraintLayout newParent, final Button stepButton, boolean empty) {


        Animation slideInRight = AnimationUtils.loadAnimation(getContext(),
                R.anim.slide_in_right);
        slideInRight.setDuration(500);
        if (empty) { stepButton.startAnimation(slideInRight); }
        else { stepButton.startAnimation(slideInRight); }
        moveViews(
                newParent,
                stepButton);
    }


    void moveViews(ConstraintLayout newParent, Button stepItem) {
        newParent.addView(stepItem);
    }

    void playMatch() {

        MatchEngine.playTeamModeMatch(userDefence, userAttack, opponentDefence, opponentAttack, f);

        dismiss();
    }

    void callJames() {

        String goalSteps = StringHelper.getNumberWithCommas(Numbers.TM_GOAL);
        List<JamesStep> steps = new ArrayList<>();
        steps.add(new JamesStep(getString(R.string.match_setup_james_description1)));
        steps.add(new JamesStep(getString(R.string.match_setup_james_description2)));
        steps.add(new JamesStep(getString(R.string.match_setup_james_description3, goalSteps)));
        steps.add(new JamesStep(getString(R.string.match_setup_james_description4, goalSteps)));
        JamesTutorial james = new JamesTutorial(steps);
        james.show(getFragmentManager(), "james");

    }
}
