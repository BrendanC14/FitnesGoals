package com.cutlerdevelopment.fitnessgoals.Constants;

import android.graphics.Color;

import com.cutlerdevelopment.fitnessgoals.SavedData.GameDBHandler;
import com.cutlerdevelopment.fitnessgoals.Data.GameData;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Colours {

    public static final String BLACK_TEAM_COLOUR = "#000000";
    public static final String MID_BLUE_TEAM_COLOUR = "#0000e7";
    public static final String DARK_BLUE_TEAM_COLOUR = "#001489";
    public static final String TURQUOISE_TEAM_COLOUR = "#005045";
    public static final String LIGHT_BLUE_TEAM_COLOUR = "#009ee0";
    public static final String DARK_PURPLE_TEAM_COLOUR = "#040042";
    public static final String MID_PURPLE_TEAM_COLOUR = "#060067";
    public static final String LIGHT_PURPLE_TEAM_COLOUR ="#192552";
    public static final String PINK_TEAM_COLOUR = "#7b003a";
    public static final String RED_TEAM_COLOUR = "#800000";
    public static final String YELLOW_GREEN_TEAM_COLOUR = "#b5dc17";
    public static final String ORANGE_TEAM_COLOUR = "#f68712";
    public static final String YELLOW_TEAM_COLOUR = "#ffee00";
    public static final String WHITE_TEAM_COLOUR = "#ffffff";


    public static List<String> allTeamColours;

    public static List<String> getAllTeamColours() {

        if (allTeamColours == null) {
            allTeamColours = new ArrayList<>();
            allTeamColours.add(BLACK_TEAM_COLOUR);
            allTeamColours.add(MID_BLUE_TEAM_COLOUR);
            allTeamColours.add(DARK_BLUE_TEAM_COLOUR);
            allTeamColours.add(TURQUOISE_TEAM_COLOUR);
            allTeamColours.add(LIGHT_BLUE_TEAM_COLOUR);
            allTeamColours.add(DARK_PURPLE_TEAM_COLOUR);
            allTeamColours.add(MID_PURPLE_TEAM_COLOUR);
            allTeamColours.add(LIGHT_PURPLE_TEAM_COLOUR);
            allTeamColours.add(PINK_TEAM_COLOUR);
            allTeamColours.add(RED_TEAM_COLOUR);
            allTeamColours.add(YELLOW_GREEN_TEAM_COLOUR);
            allTeamColours.add(ORANGE_TEAM_COLOUR);
            allTeamColours.add(YELLOW_TEAM_COLOUR);
            allTeamColours.add(WHITE_TEAM_COLOUR);
            Collections.sort(allTeamColours);
        }
        return allTeamColours;
    }

    public static String getSecondaryColour(String colour) {
        if (colour.equals(WHITE_TEAM_COLOUR)) {
            return BLACK_TEAM_COLOUR;
        }
        else if (colour.equals(YELLOW_TEAM_COLOUR)) {
            return MID_BLUE_TEAM_COLOUR;
        }
        else if (colour.equals(YELLOW_GREEN_TEAM_COLOUR)) {
            return BLACK_TEAM_COLOUR;
        }

        return WHITE_TEAM_COLOUR;
    }

    public static int getUsersPrimaryColour() {

        return Color.parseColor(
                GameData.getInstance().getUsersTeam()
                .getPrimaryColour()
                );
    }
    public static int getUsersSecondaryColour() {

        return Color.parseColor(
                GameData.getInstance().getUsersTeam()
                        .getSecondaryColour()
        );

    }
}
