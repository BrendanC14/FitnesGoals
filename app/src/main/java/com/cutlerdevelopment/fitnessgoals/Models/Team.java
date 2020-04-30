package com.cutlerdevelopment.fitnessgoals.Models;

import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

import com.cutlerdevelopment.fitnessgoals.Constants.MatchResult;
import com.cutlerdevelopment.fitnessgoals.Constants.Numbers;
import com.cutlerdevelopment.fitnessgoals.SavedData.CareerSavedData;
import com.cutlerdevelopment.fitnessgoals.Settings.AppSettings;
import com.cutlerdevelopment.fitnessgoals.Settings.CareerSettings;

@Entity
public class Team {

    @Ignore
    public Team(String name, String primaryColour, String secondaryColour, int league, int position) {
        this.ID = CareerSavedData.getInstance().getNumRowsFromTeamTable() + 1;
        this.name = name;
        this.primaryColour = primaryColour;
        this.secondaryColour = secondaryColour;
        this.league = league;

        this.maxNumberOfSteps = 13000 - (60 * position);

        this.minNumberOfSteps = maxNumberOfSteps - Numbers.DIFFERENCE_BETWEEN_TEAM_MIN_MAX;

        CareerSavedData.getInstance().saveObject(this);
    }

    /**
     * Used when loading a player for the solo career. Doesn't take any variables as takes them from RoomDB
     */
    public Team() {

    }

    @PrimaryKey
    private int ID;
    public int getID() { return ID; }
    public void setID(int id) { this.ID = id;}


    private String name;
    public String getName() {return  name; }
    public void setName(String newName) { name = newName; }
    public void changeName(String newName) {
        name = newName;
        CareerSavedData.getInstance().updateObject(this);
    }

    private String primaryColour;
    public String getPrimaryColour() { return primaryColour;}
    public void setPrimaryColour(String newColour) { primaryColour = newColour; }
    public void changePrimaryColour(String newColour) {
        primaryColour = newColour;
        CareerSavedData.getInstance().updateObject(this);
    }

    private String secondaryColour;
    public String getSecondaryColour() { return secondaryColour; }
    public void setSecondaryColour(String secondaryColour) { this.secondaryColour = secondaryColour; }
    public void changeSecondaryColour(String secondaryColour) {
        this.secondaryColour = secondaryColour;
        CareerSavedData.getInstance().updateObject(this);
    }

    private int league;
    public int getLeague() { return league; }
    public void setLeague(int newLeague) { this.league = newLeague; }
    public void changeLeague(int newLeague) {
        this.league = newLeague;
        CareerSavedData.getInstance().updateObject(league);
    }

    private int totalSteps;
    public int getTotalSteps() { return totalSteps; }
    public void setTotalSteps(int totalSteps) { this.totalSteps = totalSteps; }
    public void changeTotalSteps(int totalSteps) {
        this.totalSteps = totalSteps;
        CareerSavedData.getInstance().saveObject(this);
    }

    public int getAverageSteps() {
        return totalSteps / (getGamesPlayed() * CareerSettings.getInstance().getDaysBetween());
    }

    private int totalAttackingSteps;
    public int getTotalAttackingSteps() { return totalAttackingSteps; }
    public void setTotalAttackingSteps(int totalAttackingSteps) { this.totalAttackingSteps = totalAttackingSteps; }
    public void changeAttackingSteps(int averageAttackingSteps) {
        this.totalAttackingSteps = averageAttackingSteps;
        CareerSavedData.getInstance().updateObject(this);
    }
    public int getAverageAttackingSteps() {
        int games = getGamesPlayed();
        if (games > 0) { return totalAttackingSteps / (games * (CareerSettings.getInstance().getDaysBetween() / 2)); }
        return (maxNumberOfSteps + minNumberOfSteps) / 2;
    }

    private int totalDefendingSteps;
    public int getTotalDefendingSteps() { return totalDefendingSteps; }
    public void setTotalDefendingSteps(int totalDefendingSteps) { this.totalDefendingSteps = totalDefendingSteps; }
    public void changeDefendingSteps(int averageDefendingSteps) {
        this.totalDefendingSteps = averageDefendingSteps;
        CareerSavedData.getInstance().updateObject(this);
    }
    public int getAverageDefendingSteps() {
        int games = getGamesPlayed();
        if (games > 0) { return totalDefendingSteps / (games * (CareerSettings.getInstance().getDaysBetween() / 2)); }
        return (maxNumberOfSteps + minNumberOfSteps) / 2;
    }

    private int minNumberOfSteps;
    public int getMinNumberOfSteps() { return minNumberOfSteps; }
    public void setMinNumberOfSteps(int minNumberOfSteps) { this.minNumberOfSteps = minNumberOfSteps; }
    public void changeMinNumberOfSteps (int minNumberOfSteps) {
        this.minNumberOfSteps = minNumberOfSteps;
        CareerSavedData.getInstance().saveObject(this);
    }

    private int maxNumberOfSteps;
    public int getMaxNumberOfSteps() { return maxNumberOfSteps; }
    public void setMaxNumberOfSteps(int maxNumberOfSteps) { this.maxNumberOfSteps = maxNumberOfSteps; }
    public void changeMaxNumberOfSteps(int maxNumberOfSteps) {
        this.maxNumberOfSteps = maxNumberOfSteps;
        CareerSavedData.getInstance().saveObject(this);
    }

    private int points;
    public int getPoints() { return points;}
    public void setPoints(int pts) { this.points = pts; }
    public void addPoints(int pts) { points += pts; }

    private int wins;
    public int getWins() { return wins; }
    public void setWins(int w) { this.wins = w; }
    public void addWin() {
        wins++;
        addPoints(CareerSettings.getInstance().getPointsForWin());
    }

    private int draws;
    public int getDraws() { return draws; }
    public void setDraws(int d) { this.draws = d; }
    public void addDraw() {
        draws++;
        addPoints(CareerSettings.getInstance().getPointsForDraw());
    }

    private int losses;
    public int getLosses() { return losses; }
    public void setLosses(int l) { this.losses = l; }
    public void addLoss() {
        losses++;
        addPoints(CareerSettings.getInstance().getPointsForLoss());
    }

    private int scored;
    public int getScored() { return this.scored; }
    public void setScored(int scored) {this.scored = scored; }
    public void addGoals(int goals) {
        this.scored += goals;

    }

    private int conceded;
    public int getConceded() { return this.conceded; }
    public void setConceded(int conceded) { this.conceded = conceded; }
    public void concedeGoals( int conceded) { this.conceded += conceded; }

    public int getGoalDifference() { return scored - conceded; }
    public int getGamesPlayed() {return wins + draws + losses;}


    public void playMatch(int matchResult, int scored, int conceded, int attackingSteps, int defendingSteps) {
        addGoals(scored);
        concedeGoals(conceded);
        if (matchResult == MatchResult.DRAW) { addDraw(); }
        else if (matchResult == MatchResult.WIN) { addWin(); }
        else { addLoss(); }

        int totalStepsInMatch = attackingSteps + defendingSteps;

        this.totalAttackingSteps += attackingSteps;
        this.totalDefendingSteps += defendingSteps;
        this.totalSteps += totalStepsInMatch;

        int dailySteps = totalStepsInMatch / CareerSettings.getInstance().getDaysBetween();
        int currentAve = (minNumberOfSteps + maxNumberOfSteps) / 2;
        minNumberOfSteps += (dailySteps - currentAve) / 25;
        maxNumberOfSteps += (dailySteps - currentAve) / 25;

        //this.minNumberOfSteps = getAverageSteps() - (Numbers.DIFFERENCE_BETWEEN_TEAM_MIN_MAX / 2);
        //this.maxNumberOfSteps = getAverageSteps() + (Numbers.DIFFERENCE_BETWEEN_TEAM_MIN_MAX / 2);

        CareerSavedData.getInstance().updateObject(this);


    }
}
