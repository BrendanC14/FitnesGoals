package com.cutlerdevelopment.fitnessgoals.Settings;

import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

import com.cutlerdevelopment.fitnessgoals.SavedData.AppSavedData;

import java.util.Date;


@Entity
public class UserActivity {


    @Ignore
    public UserActivity(Date date, int steps) {

        this.date = date;
        this.steps = steps;

        AppSavedData.getInstance().saveObject(this);
    }

    public UserActivity() {

    }

    @PrimaryKey
    private Date date;
    public Date getDate() { return  date; }
    public void setDate(Date date) { this.date = date; }
    public void changeDate(Date date) {
        this.date = date;
        AppSavedData.getInstance().updateObject(this);
    }

    private int steps;
    public int getSteps() { return steps; }
    public void setSteps(int steps) { this.steps = steps; }
    public void changeSteps(int steps) {
        this.steps = steps;
        AppSavedData.getInstance().updateObject(this);

    }

    private int activeMinutes;
    public int getActiveMinutes() { return  activeMinutes; }
    public void setActiveMinutes(int mins) {this.activeMinutes = mins; }
    public void changeActiveMinutes(int mins) {
        this.activeMinutes = mins;
        AppSavedData.getInstance().updateObject(this);

    }

    private boolean targetAchieved;
    public boolean isTargetAchieved() { return targetAchieved; }
    public void setTargetAchieved(boolean targetAchieved) { this.targetAchieved = targetAchieved; }
    public void changeTargetAchieved(boolean targetAchieved) {
        this.targetAchieved = targetAchieved;
        AppSavedData.getInstance().updateObject(this);
    }
}
