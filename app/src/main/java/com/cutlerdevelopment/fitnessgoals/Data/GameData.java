package com.cutlerdevelopment.fitnessgoals.Data;

import android.database.AbstractWindowedCursor;

import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

import com.cutlerdevelopment.fitnessgoals.Constants.BadgeCategories;
import com.cutlerdevelopment.fitnessgoals.Constants.Defaults;
import com.cutlerdevelopment.fitnessgoals.Constants.FilterOptions;
import com.cutlerdevelopment.fitnessgoals.Constants.Leagues;
import com.cutlerdevelopment.fitnessgoals.Constants.MatchResult;
import com.cutlerdevelopment.fitnessgoals.Integrations.IntegrationConnectionHandler;
import com.cutlerdevelopment.fitnessgoals.Models.Badge;
import com.cutlerdevelopment.fitnessgoals.Models.Fixture;
import com.cutlerdevelopment.fitnessgoals.Models.MatchEngine;
import com.cutlerdevelopment.fitnessgoals.Models.Team;
import com.cutlerdevelopment.fitnessgoals.SavedData.AppDBHandler;
import com.cutlerdevelopment.fitnessgoals.SavedData.GameDBHandler;
import com.cutlerdevelopment.fitnessgoals.SavedData.GameDBHandler;
import com.cutlerdevelopment.fitnessgoals.Utils.DateHelper;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Entity
public class GameData {



    private static GameData instance = null;

    public static GameData getInstance() {
        if (instance != null) {
            return instance;
        }
        return new GameData();
    }

    public GameData() {

        instance = this;

    }

    public void createNewGameData(int daysBetween) {
        this.daysBetween = daysBetween;

        filterChoices = new ArrayList<>();
        filterChoices.add(FilterOptions.YESTERDAY);
        filterChoices.add(FilterOptions.LAST_MATCH);
        filterChoices.add(FilterOptions.LAST_7_DAYS);
        this.pointsForWin = Defaults.DEFAULT_POINTS_FOR_WIN;
        this.pointsForDraw = Defaults.DEFAULT_POINTS_FOR_DRAW;
        this. pointsForLoss = Defaults.DEFAULT_POINTS_FOR_LOSS;

        GameDBHandler.getInstance().saveObject(this);
    }
    public interface BadgeListener {
        public void badgeUnlocked(Badge badgeUnlocked);
    }
    @Ignore
    BadgeListener listener;
    public void setListener(BadgeListener listener) { this.listener = listener; }

    @PrimaryKey
    private int teamID;
    public int getTeamID() { return teamID; }
    public void setTeamID(int teamID) { this.teamID = teamID; }
    public void changeTeamID(int teamID) {
        this.teamID = teamID;
        GameDBHandler.getInstance().saveObject(this);
    }
    public Team getUsersTeam() {
        return GameDBHandler.getInstance().getTeamFromID(teamID);
    }

    private List<Integer> filterChoices;
    public List<Integer> getFilterChoices() { return filterChoices; }
    public void setFilterChoices(List<Integer> filterChoices) { this.filterChoices = filterChoices; }
    public void updateFilterChoices(List<Integer> filterChoices) {
        this.filterChoices = filterChoices;
        GameDBHandler.getInstance().updateObject(this);
    }

    private int daysBetween;
    public int getDaysBetween() { return daysBetween; }
    public void setDaysBetween(int daysBetween) { this.daysBetween = daysBetween; }
    public void changeDaysBetween(int daysBetween) {

        int differenceInDays = daysBetween - this.daysBetween;

        Map<Date, Fixture> newFixturesDate = new HashMap<>();
        List<Fixture> usersFixtures = GameDBHandler.getInstance().getAllUpcomingFixturesForTeam(GameData.getInstance().getTeamID());
        Collections.sort(usersFixtures);
        if (differenceInDays > 0) {
            //Because the number of days in between is increasing, we need to start at the end so as not to move the same fixture multiple times
            for (int i = usersFixtures.size() - 1; i >= 0; i--) {
                Fixture usersFix = usersFixtures.get(i);
                Date currentDate = usersFix.getDate();
                for (Fixture fix : GameDBHandler.getInstance().getAllFixturesOnDate(currentDate)) {
                    fix.changeDate(DateHelper.addDays(currentDate, differenceInDays * (i + 1)));
                }
            }
        }
        else {
            for (int i = 0; i < usersFixtures.size(); i++) {
                Fixture usersFix = usersFixtures.get(i);
                Date currentDate = usersFix.getDate();
                for (Fixture fix : GameDBHandler.getInstance().getAllFixturesOnDate(currentDate)) {
                    fix.changeDate(DateHelper.addDays(currentDate, differenceInDays * (i + 1)));
                }
            }
        }

        this.daysBetween = daysBetween;
        GameDBHandler.getInstance().saveObject(this);
    }

    private Date startDate;
    public Date getStartDate() { return startDate; }
    public void setStartDate(Date startDate) { this.startDate = startDate; }
    public void changeStartDate(Date startDate) {
        this.startDate = startDate;
        GameDBHandler.getInstance().saveObject(this);
    }
    private Date seasonStartDate;
    public Date getSeasonStartDate() { return seasonStartDate; }
    public void setSeasonStartDate(Date seasonStartDate) { this.seasonStartDate = seasonStartDate; }
    public void changeSeasonStartDate(Date seasonStartDate) {
        this.seasonStartDate = seasonStartDate;
        GameDBHandler.getInstance().updateObject(this);
    }
    public int getDaysBetweenSeasonStartAndNextFixture() {
        Fixture nextFixture = GameDBHandler.getInstance().getNextFixtureForTeam(teamID);
        Date nextDate = nextFixture != null ? nextFixture.getDate() : new Date();
        return DateHelper.getDaysBetween(nextDate, seasonStartDate);
    }

    private int season;
    public int getSeason() { return season; }
    public void setSeason(int season) { this.season = season; }

    private int careerGamesPlayed;
    public int getCareerGamesPlayed() {
        return careerGamesPlayed;
    }
    public void setCareerGamesPlayed(int careerGamesPlayed) { this.careerGamesPlayed = careerGamesPlayed; }
    public void addGamesPlayed(int matchResult, int goals) {
        careerGamesPlayed++;
        checkNextBadge(BadgeCategories.GAMES, careerGamesPlayed);
        if (matchResult == MatchResult.WIN) { addCareerWin(); addUnbeatenRecord(); }
        else if (matchResult == MatchResult.DRAW) { addUnbeatenRecord(); }
        else { resetUnbeatenRecord(); }
        if (goals > 0) { addCareerGoals(goals); }
        GameDBHandler.getInstance().updateObject(this);
    }

    private int careerWins;
    public int getCareerWins() { return careerWins; }
    public void setCareerWins(int careerWins) { this.careerWins = careerWins; }
    public void addCareerWin() {
        careerWins++;
        checkNextBadge(BadgeCategories.WINS, careerWins);
        GameDBHandler.getInstance().updateObject(this);
    }

    private int currentUnbeatenRecord;
    public int getCurrentUnbeatenRecord() { return currentUnbeatenRecord; }
    public void setCurrentUnbeatenRecord(int currentUnbeatenRecord) { this.currentUnbeatenRecord = currentUnbeatenRecord; }
    public void addUnbeatenRecord() {
        currentUnbeatenRecord++;
        checkNextBadge(BadgeCategories.UNBEATEN, currentUnbeatenRecord);
        GameDBHandler.getInstance().updateObject(this);
    }
    public void resetUnbeatenRecord() { this.currentUnbeatenRecord = 0; }

    private int careerGoals;
    public int getCareerGoals() { return careerGoals; }
    public void setCareerGoals(int careerGoals) { this.careerGoals = careerGoals; }
    public void addCareerGoals(int goals) {
        this.careerGoals += goals;
        checkNextBadge(BadgeCategories.GOALS, careerGoals);
        GameDBHandler.getInstance().updateObject(this);
    }

    private int pointsForWin;
    public int getPointsForWin() { return  pointsForWin; }
    public void setPointsForWin(int pts) { this.pointsForWin = pts; }
    public void changePointsForWin(int pts) {
        this.pointsForWin = pts;
        GameDBHandler.getInstance().updateObject(this);
    }

    private int pointsForDraw;
    public int getPointsForDraw() { return  pointsForDraw; }
    public void setPointsForDraw(int pts) { this.pointsForDraw = pts; }
    public void changePointsForDraw(int pts) {
        this.pointsForDraw = pts;
        GameDBHandler.getInstance().updateObject(this);
    }

    private int pointsForLoss;
    public int getPointsForLoss() { return  pointsForLoss; }
    public void setPointsForLoss(int pts) { this.pointsForLoss = pts; }
    public void changePointsForLoss(int pts) {
        this.pointsForLoss = pts;
        GameDBHandler.getInstance().updateObject(this);
    }

    void checkNextBadge(int badgeCategory, int numAchieved) {
        Badge badgeToWin = AppDBHandler.getInstance().getNextLockedBadgeIn(badgeCategory);
        if (badgeToWin != null) {
            if (numAchieved >= badgeToWin.getTarget()) {
                badgeToWin.unlockBadge();
                if (listener != null) {
                    listener.badgeUnlocked(badgeToWin);
                }
            }
        }
    }

    public void startNewSeason() {
        season = 1;
        startDate = DateHelper.addDays(new Date(), - 20);
        //startDate = DateHelper.cleanDate(new Date());
        seasonStartDate = startDate;
        GameDBHandler.getInstance().saveObject(this);
        createFixtures();
    }
    public void startNextSeason() {

        seasonStartDate = DateHelper.cleanDate(new Date());

        for (Fixture f : GameDBHandler.getInstance().getAllUnplayedFixtures()) {
            MatchEngine.playAIGame(f);
        }
        season++;
        for (Team promotedTeam : Leagues.getAllTeamsForPromotion()) {
            promotedTeam.promote();
        }
        for (Team relegatedTeam: Leagues.getAllTeamsForRelegation()) {
            relegatedTeam.relegate();
        }
        for (Team t : GameDBHandler.getInstance().getAllTeams()) {
            t.startNewSeason();
        }
        GameDBHandler.getInstance().removeAllFixtures();
        createFixtures();
        GameDBHandler.getInstance().updateObject(this);
    }


    public void refreshPlayerActivity() {
        Date lastActivityDate = startDate;

        UserActivity lastActivity = AppDBHandler.getInstance().getLastAddedActivity();
        if (lastActivity != null) {
            lastActivityDate = lastActivity.getDate();
        }

        Date yesterday = DateHelper.addDays(new Date(), -1);
        if (lastActivityDate.before(yesterday)) {
            IntegrationConnectionHandler.getInstance().refreshStepActivity(DateHelper.addDays(lastActivityDate, 1), yesterday);
        }
    }




    void createFixtures() {
        for (int league = Leagues.TOP_LEAGUE; league <= Leagues.BOTTOM_LEAGUE; league++) {
            List<Team> teamsInLeague = GameDBHandler.getInstance().getAllTeamsInLeague(league);
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
                    int numDays = round * GameData.getInstance().getDaysBetween() + 1;
                    Date matchDate = DateHelper.addDays(seasonStartDate, numDays);
                    new Fixture(GameDBHandler.getInstance().getNumRowsFromFixtureTable(),
                            homeTeam,
                            awayTeam,
                            AppData.getInstance().getStepTarget(),
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
                    int numDays = round * GameData.getInstance().getDaysBetween() + 1;
                    Date matchDate = DateHelper.addDays(seasonStartDate, numDays);
                    new Fixture(GameDBHandler.getInstance().getNumRowsFromFixtureTable(),
                            homeTeam,
                            awayTeam,
                            AppData.getInstance().getStepTarget(),
                            round,
                            matchDate,
                            league);
                }
            }
        }
    }
}
