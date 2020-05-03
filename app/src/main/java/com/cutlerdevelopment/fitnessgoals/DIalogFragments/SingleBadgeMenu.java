package com.cutlerdevelopment.fitnessgoals.DIalogFragments;

import android.app.AlertDialog;
import android.app.Dialog;
import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.DialogFragment;

import com.cutlerdevelopment.fitnessgoals.Constants.Colours;
import com.cutlerdevelopment.fitnessgoals.R;

public class SingleBadgeMenu extends DialogFragment {


    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = requireActivity().getLayoutInflater();
        View singleBadgeView = inflater.inflate(R.layout.single_badge_menu, null);
        String badgeName = getArguments().getString("badgeName");
        String badgeDesc = getArguments().getString("badgeDesc");
        boolean singleBadge = getArguments().getBoolean("newBadge");
        ConstraintLayout layout = singleBadgeView.findViewById(R.id.singleBadgeLayout);
        TextView badgeNameText = singleBadgeView.findViewById(R.id.singleBadgeName);
        TextView badgeDescText = singleBadgeView.findViewById(R.id.singleBadgeDescription);
        Button closeButton = singleBadgeView.findViewById(R.id.singleBadgeCloseButton);

        layout.setBackgroundColor(Colours.getUsersPrimaryColour());
        int secondaryColour = Colours.getUsersSecondaryColour();
        badgeNameText.setTextColor(secondaryColour);
        badgeDescText.setTextColor(secondaryColour);
        closeButton.setTextColor(secondaryColour);
        closeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dismiss();
            }
        });

        badgeNameText.setText(badgeName);
        badgeDescText.setText(badgeDesc);
        if (singleBadge)  {
            TextView title = singleBadgeView.findViewById(R.id.singleBadgeTitle);
            title.setTextColor(secondaryColour);
            ImageView firework = singleBadgeView.findViewById(R.id.singleBadgeFirework);
            ImageView football = singleBadgeView.findViewById(R.id.singleBadgeFootball);
            title.setVisibility(View.VISIBLE);
            firework.setVisibility(View.VISIBLE);
            football.setVisibility(View.VISIBLE);
            firework.setImageResource(R.drawable.firework_explosion);
            AnimationDrawable explosionAnimation = (AnimationDrawable)firework.getDrawable();
            explosionAnimation.start();
        }

        builder.setView(singleBadgeView);
        return builder.create();
    }
}
