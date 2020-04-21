package com.cutlerdevelopment.fitnessgoals.Constants;

import android.widget.ListView;

import com.cutlerdevelopment.fitnessgoals.Models.Team;
import com.cutlerdevelopment.fitnessgoals.SavedData.CareerSavedData;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Colours {



    public static final int WIN_BGROUND_COLOUR = android.R.color.holo_green_light;
    public static final int DRAW_BGROUND_COLOUR = android.R.color.holo_orange_light;
    public static final int LOSE_BGROUND_COLOUR = android.R.color.holo_red_light;

    public static List<String> allTeamColours;

    public static List<String> getAllTeamColours() {

        if (allTeamColours == null) {
            allTeamColours = new ArrayList<>();
            List<Team> allTeams = CareerSavedData.getInstance().getAllTeams();
            for (Team t : allTeams) {
                if (!allTeamColours.contains(t.getColour())) {
                    allTeamColours.add(t.getColour());
                }
            }
            Collections.sort(allTeamColours);
        }
        return allTeamColours;
    }
}
