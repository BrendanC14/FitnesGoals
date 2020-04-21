package com.cutlerdevelopment.fitnessgoals.Settings;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

import com.cutlerdevelopment.fitnessgoals.Constants.Defaults;
import com.cutlerdevelopment.fitnessgoals.Constants.Leagues;
import com.cutlerdevelopment.fitnessgoals.Constants.Numbers;
import com.cutlerdevelopment.fitnessgoals.Models.Fixture;
import com.cutlerdevelopment.fitnessgoals.Models.Team;
import com.cutlerdevelopment.fitnessgoals.SavedData.CareerSavedData;
import com.cutlerdevelopment.fitnessgoals.Utils.DateHelper;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;

@Entity
public class CareerSettings {



    private static CareerSettings instance = null;

    public static CareerSettings getInstance() {
        if (instance != null) {
            return instance;
        }
        return new CareerSettings();
    }

    public CareerSettings() {

        instance = this;

    }

    public void createNewCareerSettings(int daysBetween) {
        this.daysBetween = daysBetween;

        this.pointsForWin = Defaults.DEFAULT_POINTS_FOR_WIN;
        this.pointsForDraw = Defaults.DEFAULT_POINTS_FOR_DRAW;
        this. pointsForLoss = Defaults.DEFAULT_POINTS_FOR_LOSS;

        CareerSavedData.getInstance().saveObject(this);
    }

    @PrimaryKey
    private int teamID;
    public int getTeamID() { return teamID; }
    public void setTeamID(int teamID) { this.teamID = teamID; }
    public void changeTeamID(int teamID) {
        this.teamID = teamID;
        CareerSavedData.getInstance().saveObject(this);
    }


    private int daysBetween;
    public int getDaysBetween() { return daysBetween; }
    public void setDaysBetween(int daysBetween) { this.daysBetween = daysBetween; }
    public void changeDaysBetween(int daysBetween) {
        this.daysBetween = daysBetween;
        CareerSavedData.getInstance().saveObject(this);
    }

    private Date startDate;
    public Date getStartDate() { return startDate; }
    public void setStartDate(Date startDate) { this.startDate = startDate; }
    public void changeStartDate(Date startDate) {
        this.startDate = startDate;
        CareerSavedData.getInstance().saveObject(this);
    }

    private int season;
    public int getSeason() { return season; }
    public void setSeason(int season) { this.season = season; }

    private int pointsForWin;
    public int getPointsForWin() { return  pointsForWin; }
    public void setPointsForWin(int pts) { this.pointsForWin = pts; }
    public void changePointsForWin(int pts) {
        this.pointsForWin = pts;
        CareerSavedData.getInstance().updateObject(this);
    }

    private int pointsForDraw;
    public int getPointsForDraw() { return  pointsForDraw; }
    public void setPointsForDraw(int pts) { this.pointsForDraw = pts; }
    public void changePointsForDraw(int pts) {
        this.pointsForDraw = pts;
        CareerSavedData.getInstance().updateObject(this);
    }

    private int pointsForLoss;
    public int getPointsForLoss() { return  pointsForLoss; }
    public void setPointsForLoss(int pts) { this.pointsForLoss = pts; }
    public void changePointsForLoss(int pts) {
        this.pointsForLoss = pts;
        CareerSavedData.getInstance().updateObject(this);
    }


    public void startNewSeason() {
        season++;
        startDate = new Date();
        CareerSavedData.getInstance().saveObject(this);
        createFixtures();
    }

    void createFixtures() {
        for (int league = Leagues.TOP_LEAGUE; league <= Leagues.BOTTOM_LEAGUE; league++) {
            List<Team> teamsInLeague = CareerSavedData.getInstance().getAllTeamsInLeague(league);
            int numTeamsInLeague = teamsInLeague.size();
            List<Integer> availableWeeks = new ArrayList<>();

            for (int i = 1; i < numTeamsInLeague; i++) {
                availableWeeks.add(i);
            }

            Collections.shuffle(availableWeeks);
            int round;

            for (int gameweek = 0; gameweek < numTeamsInLeague - 1; gameweek++) {
                round = availableWeeks.get(0);
                availableWeeks.remove(0);
                for (int match = 0; match < numTeamsInLeague / 2; match++) {
                    int home = ((gameweek + match) % (numTeamsInLeague - 1));
                    int away = ((gameweek - match + numTeamsInLeague - 1) % (numTeamsInLeague - 1)) ;

                    if (match == 0) {
                        away = numTeamsInLeague - 1;
                    }

                    int homeTeam = teamsInLeague.get(home).getID();
                    int awayTeam = teamsInLeague.get(away).getID();
                    int numDays = round * CareerSettings.getInstance().getDaysBetween() + 1;
                    Date matchDate = DateHelper.addDays(startDate, numDays);
                    new Fixture(CareerSavedData.getInstance().getNumRowsFromFixtureTable(),
                            homeTeam,
                            awayTeam,
                            AppSettings.getInstance().getStepTarget(),
                            round,
                            matchDate,
                            league);
                }
            }

            availableWeeks = new ArrayList<>();

            for (int i = numTeamsInLeague; i < (numTeamsInLeague * 2) -1; i++) {
                availableWeeks.add(i);
            }
            Collections.shuffle(availableWeeks);

            for (int gameweek = 0; gameweek < numTeamsInLeague - 1; gameweek++) {
                round = availableWeeks.get(0);
                availableWeeks.remove(0);
                for (int match = 0; match < numTeamsInLeague / 2; match++) {
                    int home = ((gameweek - match + numTeamsInLeague - 1) % (numTeamsInLeague - 1)) ;
                    int away = ((gameweek + match) % (numTeamsInLeague - 1)) ;

                    if (match == 0) {
                        home = numTeamsInLeague - 1;
                    }

                    int homeTeam = teamsInLeague.get(home).getID();
                    int awayTeam = teamsInLeague.get(away).getID();
                    int numDays = round * CareerSettings.getInstance().getDaysBetween() + 1;
                    Date matchDate = DateHelper.addDays(startDate, numDays);
                    new Fixture(CareerSavedData.getInstance().getNumRowsFromFixtureTable(),
                            homeTeam,
                            awayTeam,
                            AppSettings.getInstance().getStepTarget(),
                            round,
                            matchDate,
                            league);
                }
            }
        }
    }
}
