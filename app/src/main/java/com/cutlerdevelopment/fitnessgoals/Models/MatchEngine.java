package com.cutlerdevelopment.fitnessgoals.Models;

import com.cutlerdevelopment.fitnessgoals.Constants.Leagues;
import com.cutlerdevelopment.fitnessgoals.Constants.MatchResult;
import com.cutlerdevelopment.fitnessgoals.Constants.Numbers;
import com.cutlerdevelopment.fitnessgoals.SavedData.GameDBHandler;
import com.cutlerdevelopment.fitnessgoals.Data.GameData;

import java.util.Date;
import java.util.Random;

public class MatchEngine {

    public interface MatchEngineListener {
        public void matchesPlayed();
    }

    static MatchEngineListener listener;
    public static void setListener(MatchEngineListener lis) {
        listener = lis;
    }

    public static void playTeamModeMatch(int userDefence, int userAttack, int oppDefence, int oppAttack, Fixture f) {

        int userGoals = getTeamModeGoals(userAttack, oppDefence);
        int oppGoals = getTeamModeGoals(oppAttack, userDefence);

        int userID = GameData.getInstance().getTeamID();
        if (f.getHomeTeamID() == userID) {
            f.updateScores(userGoals, oppGoals, userAttack, userDefence, oppAttack, oppDefence);
        }
        else {
            f.updateScores(oppGoals, userGoals, oppAttack, oppDefence, userAttack, userDefence);
        }

        int usersResult;
        if (userGoals > oppGoals) { usersResult = MatchResult.WIN; }
        else if (userGoals == oppGoals) { usersResult = MatchResult.DRAW; }
        else { usersResult = MatchResult.LOSE; }

        GameData.getInstance().addGamesPlayed(usersResult, userGoals);
        playOtherGames(f.getDate());

    }

    static int getTeamModeGoals(int attackingTeam, int defendingTeam) {
        int goalThreshold = Numbers.TM_GOAL;
        int divider = GameData.getInstance().getDaysBetween() / 2;
        return Math.max((int) ((goalThreshold + ((attackingTeam - defendingTeam) / divider)) / goalThreshold), 0);

    }

    static void playOtherGames(Date date) {
        GameDBHandler savedData = GameDBHandler.getInstance();

        for (int league = Leagues.TOP_LEAGUE; league <= Leagues.BOTTOM_LEAGUE; league++) {
            for (Fixture f : savedData.getWeeksFixtureFromLeague(date, league)) {
                Team homeTeam = savedData.getTeamFromID(f.getHomeTeamID());
                Team awayTeam = savedData.getTeamFromID(f.getAwayTeamID());


                int homeDefence = getRandomAISteps(homeTeam);
                int homeAttack = getRandomAISteps(homeTeam);
                int awayDefence = getRandomAISteps(awayTeam);
                int awayAttack = getRandomAISteps(awayTeam);

                int homeGoals = getTeamModeGoals(homeAttack, awayDefence);
                int awayGoals = getTeamModeGoals(awayAttack, homeDefence);

                f.updateScores(homeGoals, awayGoals, homeAttack, homeDefence, awayAttack, awayDefence);

            }
        }

        listener.matchesPlayed();

    }

    public static void playAIGame(Fixture f) {

        GameDBHandler savedData = GameDBHandler.getInstance();
        Team homeTeam = savedData.getTeamFromID(f.getHomeTeamID());
        Team awayTeam = savedData.getTeamFromID(f.getAwayTeamID());


        int homeDefence = getRandomAISteps(homeTeam);
        int homeAttack = getRandomAISteps(homeTeam);
        int awayDefence = getRandomAISteps(awayTeam);
        int awayAttack = getRandomAISteps(awayTeam);

        int homeGoals = getTeamModeGoals(homeAttack, awayDefence);
        int awayGoals = getTeamModeGoals(awayAttack, homeDefence);

        f.updateScores(homeGoals, awayGoals, homeAttack, homeDefence, awayAttack, awayDefence);
    }

    static int getRandomAISteps(Team team) {
        int steps = 0;
        for (int i = 0; i < GameData.getInstance().getDaysBetween() / 2; i++) {
            Random r = new Random();
            int max = team.getMaxNumberOfSteps();
            int min = team.getMinNumberOfSteps();
            steps += r.nextInt(max - min) + min;
        }
        return  steps;
    }
}
