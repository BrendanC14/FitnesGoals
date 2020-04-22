package com.cutlerdevelopment.fitnessgoals.Constants;

import com.cutlerdevelopment.fitnessgoals.Models.Team;
import com.cutlerdevelopment.fitnessgoals.SavedData.CareerSavedData;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

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


    public static List<Team> getLeagueList(int leagueID) {
        List<Team> allTeams = CareerSavedData.getInstance().getAllTeamsInLeague(leagueID);

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
    public static int getNumTeamsInLeague(int league) {
        return CareerSavedData.getInstance().getAllTeamsInLeague(league).size();
    }


    public static List<Team> getLeagueNeighbours(Team team, int numOfNeighbours) {

        List<Team> neighbouringTeams = new ArrayList<>();
        List<Team> allTeams = getLeagueList(team.getLeague());
        int homePosition = getPositionInLeague(team.getID(), team.getLeague());

        int startingPosition = (homePosition - (numOfNeighbours / 2)) - 1;
        int endPosition = (homePosition + (numOfNeighbours / 2)) - 1;

        if (startingPosition < 0) {
            startingPosition = 0;
            endPosition = startingPosition + numOfNeighbours;
        }
        else if (endPosition > allTeams.size() - 1) {
            endPosition = allTeams.size() - 1;
            startingPosition = endPosition - numOfNeighbours;
        }

        for (int i = startingPosition; i <= endPosition; i++) {
            neighbouringTeams.add(allTeams.get(i));
        }


        return neighbouringTeams;
    }
}
