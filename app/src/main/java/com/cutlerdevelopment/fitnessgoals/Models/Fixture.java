package com.cutlerdevelopment.fitnessgoals.Models;

import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

import com.cutlerdevelopment.fitnessgoals.Constants.MatchResult;
import com.cutlerdevelopment.fitnessgoals.SavedData.CareerSavedData;

import java.util.Date;

@Entity
public class Fixture implements Comparable<Fixture> {

    @Ignore
    public Fixture(int id, int homeTeamID, int awayTeamID, int stepTarget, int week, Date date, int league) {
        this.ID = id;
        this.homeTeamID = homeTeamID;
        this.awayTeamID = awayTeamID;
        this.stepTarget = stepTarget;
        this.week = week;
        this.date = date;
        this.league = league;

        this.homeScore = -1;
        this.awayScore = -1;

        CareerSavedData.getInstance().saveObject(this);
    }

    /**
     * Used when loading a player for the solo career. Doesn't take any variables as takes them from RoomDB
     */
    public Fixture() {

    }

    @PrimaryKey
    private int ID;
    public int getID() {return  ID; }
    public void setID(int id) { this.ID = id; }
    public void changeID(int id) {
        this.ID = id;
        CareerSavedData.getInstance().updateObject(this);
    }

    private int homeTeamID;
    public int getHomeTeamID() { return homeTeamID;}
    public void setHomeTeamID(int id) { this.homeTeamID = id; }
    public void changeHomeTeamID(int id) {
        this.homeTeamID = id;
        CareerSavedData.getInstance().updateObject(this);
    }

    private int awayTeamID;
    public int getAwayTeamID() { return awayTeamID; }
    public void setAwayTeamID(int id) { this.awayTeamID = id; }
    public void changeAwayTeamID(int id) {
        this.awayTeamID = id;
        CareerSavedData.getInstance().updateObject(this);
    }

    private int stepTarget;
    public int getStepTarget() { return stepTarget; }
    public void setStepTarget(int target) { this.stepTarget = target; }
    public void changeStepTarget(int target) {
        this.stepTarget = target;
        CareerSavedData.getInstance().updateObject(this);
    }

    private int homeScore;
    public int getHomeScore() { return  homeScore; }
    public void setHomeScore(int score) { this.homeScore = score; }


    private int awayScore;
    public int getAwayScore() { return awayScore; }
    public void setAwayScore(int score) { this.awayScore = score; }

    public void updateScores(int homeScore, int awayScore, int homeAttack, int homeDefence, int awayAttack, int awayDefence) {
        this.homeScore = homeScore;
        this.awayScore = awayScore;

        Team homeTeam = CareerSavedData.getInstance().getTeamFromID(homeTeamID);
        Team awayTeam = CareerSavedData.getInstance().getTeamFromID(awayTeamID);

        homeTeam.playMatch(getMatchResultForTeam(homeTeamID), homeScore, awayScore);
        awayTeam.playMatch(getMatchResultForTeam(awayTeamID), awayScore, homeScore);

        this.homeAttackingSteps = homeAttack;
        this.homeDefendingSteps = homeDefence;
        this.awayAttackingSteps = awayAttack;
        this.awayDefendingSteps = awayDefence;

        CareerSavedData.getInstance().updateObject(this);
    }

    private int homeAttackingSteps;
    public int getHomeAttackingSteps() { return homeAttackingSteps; }
    public void setHomeAttackingSteps(int homeAttackingSteps) { this.homeAttackingSteps = homeAttackingSteps; }
    public void changeHomeAttackingSteps(int attackingSteps) {
        this.homeAttackingSteps = attackingSteps;
        CareerSavedData.getInstance().updateObject(this);
    }

    private int homeDefendingSteps;
    public int getHomeDefendingSteps() { return homeDefendingSteps; }
    public void setHomeDefendingSteps(int homeDefendingSteps) { this.homeDefendingSteps = homeDefendingSteps; }
    public void changeHomeDefendingSteps(int homeDefendingSteps) {
        this.homeDefendingSteps = homeDefendingSteps;
        CareerSavedData.getInstance().updateObject(this);
    }

    private int awayAttackingSteps;
    public int getAwayAttackingSteps() { return awayAttackingSteps; }
    public void setAwayAttackingSteps(int awayAttackingSteps) { this.awayAttackingSteps = awayAttackingSteps; }
    public void changeAwayAttackingSteps(int attackingSteps) {
        this.awayAttackingSteps = attackingSteps;
        CareerSavedData.getInstance().updateObject(this);
    }

    private int awayDefendingSteps;
    public int getAwayDefendingSteps() { return awayDefendingSteps; }
    public void setAwayDefendingSteps(int awayDefendingSteps) { this.awayDefendingSteps = awayDefendingSteps; }
    public void changeAwayDefendingSteps(int awayDefendingSteps) {
        this.awayDefendingSteps = awayDefendingSteps;
        CareerSavedData.getInstance().updateObject(this);
    }


    private int week;
    public int getWeek() { return week; }
    public void setWeek(int week) {this.week = week; }
    public void changeWeek(int week) {
        this.week = week;
        CareerSavedData.getInstance().updateObject(this);
    }

    private Date date;
    public Date getDate() { return  date; }
    public void setDate(Date date) { this.date = date; }
    public void updateDate(Date date) {
        this.date = date;
        CareerSavedData.getInstance().updateObject(this);
    }

    private int league;
    public int getLeague() { return  league; }
    public void setLeague(int league) {this.league = league; }
    public void changeLeague(int league) {
        this.league = league;
        CareerSavedData.getInstance().updateObject(this);
    }
    public boolean isTeamInvolved(int ID) { return this.homeTeamID == ID || this.awayTeamID == ID; }

    public int getMatchResultForTeam(int teamID) {
        if (homeScore < 0) { return -1; }
        if (homeScore == awayScore) { return MatchResult.DRAW; }
        boolean homeTeamWon = homeScore > awayScore;

        if (teamID == homeTeamID) { return homeTeamWon ? MatchResult.WIN : MatchResult.LOSE; }
        else { return homeTeamWon ? MatchResult.LOSE : MatchResult.WIN; }

    }

    public int getSteps(int teamID) {
        if (teamID == this.homeTeamID) {
            return homeAttackingSteps + homeDefendingSteps;
        }

        return awayAttackingSteps + awayDefendingSteps;
    }

    public boolean matchPlayed() {
        return homeScore >= 0;
    }

    @Override
    public int compareTo(Fixture f) {
        return this.getDate().compareTo(f.getDate());
    }
}
