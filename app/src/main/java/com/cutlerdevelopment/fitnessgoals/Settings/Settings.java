package com.cutlerdevelopment.fitnessgoals.Settings;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

import com.cutlerdevelopment.fitnessgoals.Constants.GameModes;
import com.cutlerdevelopment.fitnessgoals.SavedData.SettingsSavedData;

@Entity
public class Settings {


    private static Settings instance = null;

    public static Settings getInstance() {
        if (instance != null) {
            return instance;
        }
        return new Settings();
    }

    public Settings() {

        instance = this;
    }

    public void setupTeamGame(int stepMode, int stepTarget, int daysBetween) {

        this.gameMode = GameModes.TEAM_MODE;
        this.stepMode = stepMode;
        this.stepTarget = stepTarget;
        this.daysBetween = daysBetween;

        SettingsSavedData.getInstance().saveObject(this);

    }

    @PrimaryKey
    private int fitnessApp;
    public int getFitnessApp() { return fitnessApp; }
    public void setFitnessApp(int fitnessApp) { this.fitnessApp = fitnessApp; }
    public void changeFitnessApp(int fitnessApp) {
        this.fitnessApp = fitnessApp;
        //TODO: Save update
    }

    private int gameMode;
    public int getGameMode() { return gameMode; }
    public void setGameMode(int gameMode) { this.gameMode = gameMode; }
    public void changeGameMode(int gameMode) {
        this.gameMode = gameMode;
        SettingsSavedData.getInstance().saveObject(this);

    }

    private int stepMode;
    public int getStepMode() { return stepMode; }
    public void setStepMode(int stepMode) { this.stepMode = stepMode; }
    public void changeStepMode(int stepMode) {
        this.stepMode = stepMode;
        SettingsSavedData.getInstance().saveObject(this);

    }

    private int stepTarget;
    public int getStepTarget() { return stepTarget; }
    public void setStepTarget(int stepTarget) { this.stepTarget = stepTarget; }
    public void changeStepTarget(int stepTarget) {
        this.stepTarget = stepTarget;
        SettingsSavedData.getInstance().saveObject(this);
    }

    private int daysBetween;
    public int getDaysBetween() { return daysBetween; }
    public void setDaysBetween(int daysBetween) { this.daysBetween = daysBetween; }
    public void changeDaysBetween(int daysBetween) {
        this.daysBetween = daysBetween;
        SettingsSavedData.getInstance().saveObject(this);
    }
}
