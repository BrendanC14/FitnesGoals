package com.cutlerdevelopment.fitnessgoals.DIalogFragments;

import android.app.AlertDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.GridView;
import android.widget.TextView;

import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.DialogFragment;

import com.cutlerdevelopment.fitnessgoals.Constants.Colours;
import com.cutlerdevelopment.fitnessgoals.Data.GameData;
import com.cutlerdevelopment.fitnessgoals.Models.Badge;
import com.cutlerdevelopment.fitnessgoals.R;
import com.cutlerdevelopment.fitnessgoals.SavedData.AppDBHandler;
import com.cutlerdevelopment.fitnessgoals.SavedData.GameDBHandler;
import com.cutlerdevelopment.fitnessgoals.ViewAdapters.BadgeSmallItemAdapter;
import com.cutlerdevelopment.fitnessgoals.ViewItems.BadgeSmallItem;

import java.util.ArrayList;
import java.util.List;

public class AllBadgesMenu extends DialogFragment {


    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = requireActivity().getLayoutInflater();
        View allBadgesView = inflater.inflate(R.layout.all_badges_menu, null);
        GridView badgesGrid = allBadgesView.findViewById(R.id.allBadgesGridView);
        TextView badgesAchievedText = allBadgesView.findViewById(R.id.allBadgesAchievedText);
        ConstraintLayout allBadgesLayout = allBadgesView.findViewById(R.id.allBadgesLayout);
        TextView allBadgesTitle = allBadgesView.findViewById(R.id.allBadgesTitle);
        Button closeButton = allBadgesView.findViewById(R.id.allBadgesCloseButton);

        int unlocked = 0;
        int total = 0;

        final ArrayList<BadgeSmallItem> badgeItems = new ArrayList<>();
        for (Badge b : AppDBHandler.getInstance().getAllBadges()) {
            BadgeSmallItem item = new BadgeSmallItem();
            item.setBadgeName(b.getName());
            item.setBadgeDescription(b.getDescription());
            item.setUnlocked(b.isUnlocked());
            badgeItems.add(item);
            if (b.isUnlocked()) { unlocked++; }
            total++;
        }

        badgesAchievedText.setText(getResources().getString(R.string.all_badges_achieved, String.valueOf(unlocked), String.valueOf(total)));
        final BadgeSmallItemAdapter adapter = new BadgeSmallItemAdapter(getActivity().getApplicationContext(), badgeItems);
        badgesGrid.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                BadgeSmallItem item = (BadgeSmallItem) adapterView.getItemAtPosition(i);
                if (item.isUnlocked()) {
                    openSingleBadgeMenu(item.getBadgeName(), item.getBadgeDescription());
                }
            }
        });
        badgesGrid.setAdapter(adapter);
        badgesGrid.setNumColumns(2);

        int teamPrimaryColour = Colours.getUsersPrimaryColour();
        int teamSecondaryColour = Colours.getUsersSecondaryColour();
        allBadgesLayout.setBackgroundColor(teamPrimaryColour);
        allBadgesTitle.setTextColor(teamSecondaryColour);
        badgesAchievedText.setTextColor(teamSecondaryColour);
        closeButton.setTextColor(teamSecondaryColour);
        closeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dismiss();
            }
        });

        builder.setView(allBadgesView);
        return builder.create();
    }

    void openSingleBadgeMenu(String badgeName, String badgeDescription) {

        DialogFragment badgeDialog = new SingleBadgeMenu();
        Bundle args = new Bundle();
        args.putString("badgeName", badgeName);
        args.putString("badgeDesc", badgeDescription);
        args.putBoolean("newBadge", false);
        badgeDialog.setArguments(args);
        badgeDialog.setTargetFragment(this, 0);
        badgeDialog.show(this.getFragmentManager(), "SingleBadgeMenu");
    }
}
