package com.cutlerdevelopment.fitnessgoals.Constants;

public class Colours {


    private static final int RED = 1;
    public static final int RED_COLOUR = android.R.color.holo_red_dark;
    public static final int RED_BGROUND_TEXT_COLOUR = android.R.color.white;
    private static final int DARK_BLUE = 2;
    public static final int DARK_BLUE_COLOUR = android.R.color.holo_blue_dark;
    public static final int DARK_BLUE_BGROUND_TEXT_COLOUR = android.R.color.white;
    private static final int WHITE = 3;
    public static final int WHITE_COLOUR = android.R.color.white;
    public static final int WHITE_BGROUND_TEXT_COLOUR = android.R.color.black;
    private static final int ORANGE = 4;
    public static final int ORANGE_COLOUR = android.R.color.holo_orange_dark;
    public static final int ORANGE_BGROUND_TEXT_COLOUR = android.R.color.black;
    private static final int GREEN = 5;
    public static final int GREEN_COLOUR = android.R.color.holo_green_dark;
    public static final int GREEN_BGROUND_TEXT_COLOUR = android.R.color.black;
    private static final int BLACK = 6;
    public static final int BLACK_COLOUR = android.R.color.black;
    public static final int BLACK_BGROUND_TEXT_COLOUR = android.R.color.white;
    private static final int LIGHT_BLUE = 7;
    public static final int LIGHT_BLUE_COLOUR = android.R.color.holo_blue_light;
    public static final int LIGHT_BLUE_BGROUND_TEXT_COLOUR = android.R.color.white;
    private static final int PURPLE = 8;
    public static final int PURPLE_COLOUR = android.R.color.holo_purple;
    public static final int PURPLE_BGROUND_TEXT_COLOUR = android.R.color.white;
    public static final int NUM_TEAM_COLOURS = 8;

    public static final int WIN_BGROUND_COLOUR = android.R.color.holo_green_light;
    public static final int DRAW_BGROUND_COLOUR = android.R.color.holo_orange_light;
    public static final int LOSE_BGROUND_COLOUR = android.R.color.holo_red_light;


    public static String getColourName(int colour) {
        switch (colour) {
            case RED:
                return "Red";
            case DARK_BLUE:
                return "Dark Blue";
            case WHITE:
                return "White";
            case ORANGE:
                return "Orange";
            case GREEN:
                return "Green";
            case BLACK:
                return "Black";
            case LIGHT_BLUE:
                return "Light Blue";
            case PURPLE:
                return "Purple";
        }
        return "Unknown";
    }

    public static int getBackgroundColour(int teamColour) {
        switch (teamColour) {
            case RED:
                return RED_COLOUR;
            case DARK_BLUE:
                return DARK_BLUE_COLOUR;
            case WHITE:
                return WHITE_COLOUR;
            case ORANGE:
                return ORANGE_COLOUR;
            case GREEN:
                return GREEN_COLOUR;
            case BLACK:
                return BLACK_COLOUR;
            case LIGHT_BLUE:
                return LIGHT_BLUE_COLOUR;
            case PURPLE:
                return PURPLE_COLOUR;
        }
        return 0;
    }

    public static int getTextColour(int teamColour) {
        switch (teamColour) {
            case RED:
                return RED_BGROUND_TEXT_COLOUR;
            case DARK_BLUE:
                return DARK_BLUE_BGROUND_TEXT_COLOUR;
            case WHITE:
                return WHITE_BGROUND_TEXT_COLOUR;
            case ORANGE:
                return ORANGE_BGROUND_TEXT_COLOUR;
            case GREEN:
                return GREEN_BGROUND_TEXT_COLOUR;
            case BLACK:
                return BLACK_BGROUND_TEXT_COLOUR;
            case LIGHT_BLUE:
                return LIGHT_BLUE_BGROUND_TEXT_COLOUR;
            case PURPLE:
                return PURPLE_BGROUND_TEXT_COLOUR;
        }
        return 0;
    }

    public static int getDefaultColourForTeam(String teamName) {
        switch (teamName) {
            case "Arsenal":
                return RED;
            case "Liverpool":
                return RED;
            case "Manchester City":
                return LIGHT_BLUE;
            case "Leicester City":
                return DARK_BLUE;
            case "Chelsea":
                return DARK_BLUE;
            case "Manchester United":
                return RED;
            case "Wolves":
                return ORANGE;
            case "Sheffield United":
                return RED;
            case "Tottenham Hotspur":
                return WHITE;
            case "Burnley":
                return PURPLE;
            case "Crystal Palace":
                return PURPLE;
            case "Everton":
                return DARK_BLUE;
            case "Newcastle United":
                return BLACK;
            case "Southampton":
                return RED;
            case "Brighton":
                return LIGHT_BLUE;
            case "West Ham":
                return PURPLE;
            case "Watford":
                return ORANGE;
            case "Bournemouth":
                return RED;
            case "Aston Villa":
                return PURPLE;
            case "Norwich":
                return ORANGE;

        }

        return RED;
    }
}
