package com.cutlerdevelopment.fitnessgoals.Data;

import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

import com.cutlerdevelopment.fitnessgoals.Constants.BadgeCategories;
import com.cutlerdevelopment.fitnessgoals.SavedData.AppDBHandler;

import java.util.Date;


@Entity
public class UserActivity {


    @Ignore
    public UserActivity(Date date, int steps) {

        this.date = date;
        this.steps = steps;

        AppData data = AppData.getInstance();

        GameData.getInstance().checkNextBadge(BadgeCategories.DAILY_STEP, steps);
        data.addStepsToTotal(steps);
        if (steps > data.getStepTarget()) { data.addTimeHitTarget(); }
        AppDBHandler.getInstance().saveObject(this);
    }

    public UserActivity() {

    }

    @PrimaryKey
    private Date date;
    public Date getDate() { return  date; }
    public void setDate(Date date) { this.date = date; }
    public void changeDate(Date date) {
        this.date = date;
        AppDBHandler.getInstance().updateObject(this);
    }

    private int steps;
    public int getSteps() { return steps; }
    public void setSteps(int steps) { this.steps = steps; }
    public void changeSteps(int steps) {
        this.steps = steps;
        AppDBHandler.getInstance().updateObject(this);

    }

    private int activeMinutes;
    public int getActiveMinutes() { return  activeMinutes; }
    public void setActiveMinutes(int mins) {this.activeMinutes = mins; }
    public void changeActiveMinutes(int mins) {
        this.activeMinutes = mins;
        AppDBHandler.getInstance().updateObject(this);

    }

    private boolean targetAchieved;
    public boolean isTargetAchieved() { return targetAchieved; }
    public void setTargetAchieved(boolean targetAchieved) { this.targetAchieved = targetAchieved; }
    public void changeTargetAchieved(boolean targetAchieved) {
        this.targetAchieved = targetAchieved;
        AppDBHandler.getInstance().updateObject(this);
    }
}
