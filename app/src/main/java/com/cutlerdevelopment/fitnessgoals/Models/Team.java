package com.cutlerdevelopment.fitnessgoals.Models;

import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

import com.cutlerdevelopment.fitnessgoals.Constants.Colours;
import com.cutlerdevelopment.fitnessgoals.Constants.MatchResult;
import com.cutlerdevelopment.fitnessgoals.SavedData.CareerSavedData;
import com.cutlerdevelopment.fitnessgoals.Settings.CareerSettings;

@Entity
public class Team {

    @Ignore
    public Team(String name, String colour, int league) {
        this.ID = CareerSavedData.getInstance().getNumRowsFromTeamTable() + 1;
        this.name = name;
        this.colour = colour;
        this.league = league;

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

    private String colour;
    public String getColour() { return colour;}
    public void setColour(String newColour) { colour = newColour; }
    public void changeColour(String newColour) {
        colour = newColour;
        CareerSavedData.getInstance().updateObject(this);

    }

    private int league;
    public int getLeague() { return league; }
    public void setLeague(int newLeague) { this.league = newLeague; }
    public void changeLeague(int newLeague) {
        this.league = newLeague;
        CareerSavedData.getInstance().updateObject(league);
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

    public void replaceTeamWithUsersTeam(String name, String colour) {
        this.name = name;
        this.colour = colour;

        CareerSavedData.getInstance().updateObject(this);
    }

    public void playMatch(int matchResult, int scored, int conceded) {
        addGoals(scored);
        concedeGoals(conceded);
        if (matchResult == MatchResult.DRAW) { addDraw(); }
        else if (matchResult == MatchResult.WIN) { addWin(); }
        else { addLoss(); }

        CareerSavedData.getInstance().updateObject(this);


    }
}
