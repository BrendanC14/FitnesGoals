package com.cutlerdevelopment.fitnessgoals.Settings;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

import com.cutlerdevelopment.fitnessgoals.Constants.GameModes;
import com.cutlerdevelopment.fitnessgoals.SavedData.AppSavedData;

@Entity
public class AppSettings {


    private static AppSettings instance = null;

    public static AppSettings getInstance() {
        if (instance != null) {
            return instance;
        }
        return new AppSettings();
    }

    public AppSettings() {

        instance = this;
    }

    public void setupTeamGame(int stepMode, int stepTarget) {

        this.gameMode = GameModes.TEAM_MODE;
        this.stepMode = stepMode;
        this.stepTarget = stepTarget;

        AppSavedData.getInstance().saveObject(this);

    }

    @PrimaryKey
    private int fitnessApp;
    public int getFitnessApp() { return fitnessApp; }
    public void setFitnessApp(int fitnessApp) { this.fitnessApp = fitnessApp; }
    public void changeFitnessApp(int fitnessApp) {
        this.fitnessApp = fitnessApp;
        AppSavedData.getInstance().updateObject(this);
    }

    private int gameMode;
    public int getGameMode() { return gameMode; }
    public void setGameMode(int gameMode) { this.gameMode = gameMode; }
    public void changeGameMode(int gameMode) {
        this.gameMode = gameMode;
        AppSavedData.getInstance().updateObject(this);

    }

    private int stepMode;
    public int getStepMode() { return stepMode; }
    public void setStepMode(int stepMode) { this.stepMode = stepMode; }
    public void changeStepMode(int stepMode) {
        this.stepMode = stepMode;
        AppSavedData.getInstance().updateObject(this);

    }

    private int stepTarget;
    public int getStepTarget() { return stepTarget; }
    public void setStepTarget(int stepTarget) { this.stepTarget = stepTarget; }
    public void changeStepTarget(int stepTarget) {
        this.stepTarget = stepTarget;
        AppSavedData.getInstance().updateObject(this);
    }
}
