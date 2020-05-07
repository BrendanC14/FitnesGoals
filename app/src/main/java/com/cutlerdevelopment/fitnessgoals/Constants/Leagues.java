package com.cutlerdevelopment.fitnessgoals.Constants;

import com.cutlerdevelopment.fitnessgoals.Models.Team;
import com.cutlerdevelopment.fitnessgoals.SavedData.GameDBHandler;
import com.google.android.material.bottomappbar.BottomAppBar;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class Leagues {

    public static final int LEAGUE1 = 1;
    public static final int LEAGUE2 = 2;
    public static final int LEAGUE3 = 3;
    public static final int LEAGUE4 = 4;

    private static final String LEAGUE_1_STRING = "Premier League";
    private static final String LEAGUE_2_STRING = "Championship";
    private static final String LEAGUE_3_STRING = "League 1";
    private static final String LEAGUE_4_STRING = "League 2";

    public static final int TOP_LEAGUE = 1;
    public static final int BOTTOM_LEAGUE = 4;

    public static String getLeagueName(int league) {
        switch (league) {
            case LEAGUE1: return LEAGUE_1_STRING;
            case LEAGUE2: return LEAGUE_2_STRING;
            case LEAGUE3: return LEAGUE_3_STRING;
            default: return LEAGUE_4_STRING;
        }
    }


    public static List<Team> getLeagueList(int leagueID) {
        List<Team> allTeams = GameDBHandler.getInstance().getAllTeamsInLeague(leagueID);

        Collections.sort(allTeams, new Comparator<Team>() {
            @Override
            public int compare(Team Team, Team t1) {
                int value1 = t1.getPoints() - Team.getPoints();
                if (value1 == 0) {
                    int value2 = t1.getGoalDifference() - Team.getGoalDifference();
                    if (value2 == 0) {
                        int value3 = t1.getWins() - Team.getWins();
                        if (value3 == 0) {
                            return Team.getName().compareTo(t1.getName());
                        }
                        return value3;
                    }
                    return value2;
                }
                return value1;
            }
        });

        return allTeams;
    }

    public static int getPositionInLeague(int teamID, int leagueID) {
        List<Team> teams = getLeagueList(leagueID);
        //TODO: This isn't great...
        List<Integer> idList = new ArrayList<>();
        for (Team t : teams) {
            idList.add(t.getID());
        }
        return idList.indexOf(teamID) + 1;
    }

    public static Team getTeamAtPosition(int position, int leagueID) {
        List<Team> teams = getLeagueList(leagueID);
        return teams.get(position - 1);
    }

    public static Boolean isPromotionPlace(int teamID, int league) {
        int pos = getPositionInLeague(teamID, league);
        return league > TOP_LEAGUE && pos <= 3;
    }
    public static Boolean isRelegationPlace(int teamID, int league) {
        int pos = getPositionInLeague(teamID, league);
        return (league == TOP_LEAGUE && pos >= 18) || (league > TOP_LEAGUE && league < BOTTOM_LEAGUE && pos >= 22);
    }

    public static List<Team> getAllTeamsForPromotion() {
        List<Team> teams = new ArrayList<>();
        for (int i = 2; i <= BOTTOM_LEAGUE; i++) {
            teams.add(getTeamAtPosition(1, i));
            teams.add(getTeamAtPosition(2, i));
            teams.add(getTeamAtPosition(3, i));
        }
        return teams;
    }
    public static List<Team> getAllTeamsForRelegation() {
        List<Team> teams = new ArrayList<>();
        teams.add(getTeamAtPosition(18, 1));
        teams.add(getTeamAtPosition(19, 1));
        teams.add(getTeamAtPosition(20, 1));
        for (int i = TOP_LEAGUE + 1; i < BOTTOM_LEAGUE; i++) {
            teams.add(getTeamAtPosition(22, i));
            teams.add(getTeamAtPosition(23, i));
            teams.add(getTeamAtPosition(24, i));
        }
        return teams;
    }

    public static int getAverageStepsInLeague(int league) {
        int leagueTotal = 0;
        int leagueCount = 0;
        for (Team t : GameDBHandler.getInstance().getAllTeamsInLeague(league)) {
            int teamAverage = (t.getMaxNumberOfSteps() + t.getMinNumberOfSteps()) / 2;
            leagueTotal += teamAverage;
            leagueCount++;
        }

        return leagueTotal / leagueCount;
    }
}
