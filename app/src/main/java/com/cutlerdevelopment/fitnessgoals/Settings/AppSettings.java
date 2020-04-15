package com.cutlerdevelopment.fitnessgoals.Settings;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

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

    @PrimaryKey
    private int fitnessApp;
    public int getFitnessApp() { return fitnessApp; }
    public void setFitnessApp(int fitnessApp) { this.fitnessApp = fitnessApp; }
    public void changeFitnessApp(int fitnessApp) {
        this.fitnessApp = fitnessApp;
        //TODO: Save update
    }
}
