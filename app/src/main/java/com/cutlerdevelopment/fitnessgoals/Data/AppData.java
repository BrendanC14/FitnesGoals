package com.cutlerdevelopment.fitnessgoals.Data;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

import com.cutlerdevelopment.fitnessgoals.Constants.BadgeCategories;
import com.cutlerdevelopment.fitnessgoals.Constants.GameModes;
import com.cutlerdevelopment.fitnessgoals.Constants.Numbers;
import com.cutlerdevelopment.fitnessgoals.Constants.Words;
import com.cutlerdevelopment.fitnessgoals.Models.Badge;
import com.cutlerdevelopment.fitnessgoals.SavedData.AppDBHandler;
import com.cutlerdevelopment.fitnessgoals.Utils.BadgeHelper;

@Entity
public class AppData {


    private static AppData instance = null;

    public static AppData getInstance() {
        if (instance != null) {
            return instance;
        }
        return new AppData();
    }

    public AppData() {

        instance = this;
    }

    public void setupTeamGame(int stepMode, int stepTarget) {

        this.gameMode = GameModes.TEAM_MODE;
        this.stepMode = stepMode;
        this.stepTarget = stepTarget;
        for (int i = 0; i < BadgeHelper.TOTAL_NUM_BADGES; i++) {
            BadgeHelper.createFirstTimeBadges(i);
        }

        AppDBHandler.getInstance().saveObject(this);

    }

    @PrimaryKey
    private int fitnessApp;
    public int getFitnessApp() { return fitnessApp; }
    public void setFitnessApp(int fitnessApp) { this.fitnessApp = fitnessApp; }
    public void changeFitnessApp(int fitnessApp) {
        this.fitnessApp = fitnessApp;
        AppDBHandler.getInstance().updateObject(this);
    }

    private int gameMode;
    public int getGameMode() { return gameMode; }
    public void setGameMode(int gameMode) { this.gameMode = gameMode; }
    public void changeGameMode(int gameMode) {
        this.gameMode = gameMode;
        AppDBHandler.getInstance().updateObject(this);

    }

    private int stepMode;
    public int getStepMode() { return stepMode; }
    public void setStepMode(int stepMode) { this.stepMode = stepMode; }
    public void changeStepMode(int stepMode) {
        this.stepMode = stepMode;
        AppDBHandler.getInstance().updateObject(this);

    }

    private int stepTarget;
    public int getStepTarget() { return stepTarget; }
    public void setStepTarget(int stepTarget) { this.stepTarget = stepTarget; }
    public void changeStepTarget(int stepTarget) {
        this.stepTarget = stepTarget;
        AppDBHandler.getInstance().updateObject(this);
    }

    private int totalSteps;
    public int getTotalSteps() { return totalSteps; }
    public void setTotalSteps(int totalSteps) { this.totalSteps = totalSteps; }
    public void addStepsToTotal(int steps) {
        this.totalSteps += steps;
        GameData.getInstance().checkNextBadge(BadgeCategories.TOTAL_STEP, totalSteps);
    }


    private int timesHitTarget;
    public int getTimesHitTarget() { return timesHitTarget; }
    public void setTimesHitTarget(int timesHitTarget) { this.timesHitTarget = timesHitTarget; }
    public void addTimeHitTarget() {
        timesHitTarget++;
        GameData.getInstance().checkNextBadge(BadgeCategories.TARGET, timesHitTarget);
    }
}
