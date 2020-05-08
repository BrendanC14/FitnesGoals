package com.cutlerdevelopment.fitnessgoals.DIalogFragments;

import android.app.AlertDialog;
import android.app.Dialog;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.graphics.drawable.ShapeDrawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.constraintlayout.widget.ConstraintSet;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.DialogFragment;

import com.cutlerdevelopment.fitnessgoals.Constants.Colours;
import com.cutlerdevelopment.fitnessgoals.Constants.Numbers;
import com.cutlerdevelopment.fitnessgoals.Data.GameData;
import com.cutlerdevelopment.fitnessgoals.R;
import com.cutlerdevelopment.fitnessgoals.Utils.JamesStep;

import java.util.List;

public class JamesTutorial extends DialogFragment {

    private static ImageView jamesImage;
    private static ConstraintLayout speechBubbleLayout;
    private static TextView speechBubbleText;
    private static Button nextButton;
    private static Button previousButton;
    private static Button doneButton;

    private static Animation slideInRight;
    private static Animation slideInTop;
    private static Animation slideOutLeft;

    private static int currentStep;
    private static List<JamesStep> steps;

    public JamesTutorial(List<JamesStep> jamesSteps) {
        steps = jamesSteps;
        currentStep = 0;
    }


    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = requireActivity().getLayoutInflater();
        View jamesView = inflater.inflate(R.layout.james_tutorial, null);
        ConstraintLayout background = jamesView.findViewById(R.id.jamesTutorialLayout);
        jamesImage = jamesView.findViewById(R.id.jamesTutorialImage);
        speechBubbleLayout = jamesView.findViewById(R.id.jamesTutorialSpeechBubble);
        speechBubbleText = jamesView.findViewById(R.id.jamesTutorialSpeechText);
        nextButton = jamesView.findViewById(R.id.jamesTutorialNext);
        previousButton = jamesView.findViewById(R.id.jamesTutorialPrevious);
        doneButton = jamesView.findViewById(R.id.jamesTutorialDone);

        slideInRight = AnimationUtils.loadAnimation(getActivity(),
                R.anim.slide_in_right);
        slideInRight.setDuration(Numbers.TM_MAIN_MENU_CARD_INTRO_DURATION);
        slideOutLeft = AnimationUtils.loadAnimation(getActivity(),
                R.anim.slide_out_left);
        slideOutLeft.setDuration(Numbers.TM_MAIN_MENU_CARD_INTRO_DURATION);
        slideInTop = AnimationUtils.loadAnimation(getActivity(),
                R.anim.slide_in_top);
        slideInTop.setDuration(Numbers.TM_MAIN_MENU_CARD_INTRO_DURATION);

        previousButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                currentStep--;
                displayJamesStep();
            }
        });
        doneButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dismiss();
            }
        });
        nextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                currentStep++;
                displayJamesStep();
            }
        });

        int primaryColour = Colours.getUsersPrimaryColour();
        int secondaryColour = Colours.getUsersSecondaryColour();
        background.setBackgroundColor(primaryColour);
        speechBubbleLayout.setBackgroundColor(primaryColour);
        speechBubbleText.setTextColor(primaryColour);
        nextButton.setBackgroundColor(secondaryColour);
        nextButton.setTextColor(primaryColour);
        doneButton.setBackgroundColor(secondaryColour);
        doneButton.setTextColor(primaryColour);
        previousButton.setBackgroundColor(secondaryColour);
        previousButton.setTextColor(primaryColour);

        speechBubbleText.getBackground().setColorFilter(secondaryColour, PorterDuff.Mode.SRC_ATOP);

/*
        Drawable draw = speechBubbleText.getBackground();
        if (draw instanceof ShapeDrawable) {
            ((ShapeDrawable)draw).getPaint().setColor(primaryColour);
        } else if (draw instanceof GradientDrawable) {
            ((GradientDrawable)draw).setColor(primaryColour);
        } else if (draw instanceof ColorDrawable) {
            ((ColorDrawable)draw).setColor(primaryColour);
        }
        speechBubbleText.setBackground(draw);
*/
        if (getDialog() != null && getDialog().getWindow() != null) {
            getDialog().getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            getDialog().getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        }
        jamesImage.startAnimation(slideInRight);
        speechBubbleLayout.startAnimation(slideInTop);
        displayJamesStep();
        builder.setView(jamesView);
        return builder.create();
    }

    private static void displayJamesStep() {

        if (steps.size() <= currentStep) { return; }

        JamesStep thisStep = steps.get(currentStep);
        speechBubbleText.setText(thisStep.getSpeechBubbleText());

        if (thisStep.isPreviousButtonAvailable() && currentStep != 0) { previousButton.setVisibility(View.VISIBLE); }
        else { previousButton.setVisibility(View.INVISIBLE); }
        if (thisStep.isDoneButtonAvailable()) { doneButton.setVisibility(View.VISIBLE); }
        else { doneButton.setVisibility(View.INVISIBLE); }
        if (thisStep.isNextButtonAvailable() && currentStep != steps.size() - 1) { nextButton.setVisibility(View.VISIBLE); }
        else { nextButton.setVisibility(View.INVISIBLE); }


    }


    public static void addMoreSteps(List<JamesStep> stepsToAdd) {
        steps.addAll(stepsToAdd);
    }
    public static void forceNextStep() {
        currentStep++;
        displayJamesStep();
    }
}
