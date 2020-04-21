package com.cutlerdevelopment.fitnessgoals.Constants;

public class Leagues {

    public static final int PREMIER_LEAGUE = 1;
    public static final int CHAMPIONSHIP = 2;
    public static final int LEAGUE1 = 3;
    public static final int LEAGUE2 = 4;

    public static final int TOP_LEAGUE = 1;
    public static final int BOTTOM_LEAGUE = 4;

    public static String getLeagueName(int league) {
        switch (league) {
            case PREMIER_LEAGUE: return "Premier League";
            case CHAMPIONSHIP: return "Championship";
            case LEAGUE1: return "League One";
            default: return "League Two";
        }
    }
}
