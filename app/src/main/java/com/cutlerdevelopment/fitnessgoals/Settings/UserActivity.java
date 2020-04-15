package com.cutlerdevelopment.fitnessgoals.Settings;

import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

import java.util.Date;


@Entity
public class UserActivity {


    @Ignore
    public UserActivity(Date date, int steps) {

        this.date = date;
        this.steps = steps;

        //TODO: Save

    }

    /**
     * Used when loading a player for the solo career. Doesn't take any variables as takes them from RoomDB
     */
    public UserActivity() {

    }

    @PrimaryKey
    private Date date;
    public Date getDate() { return  date; }
    public void setDate(Date date) { this.date = date; }
    public void changeDate(Date date) {
        this.date = date;

    }

    private int steps;
    public int getSteps() { return steps; }
    public void setSteps(int steps) { this.steps = steps; }
    public void changeSteps(int steps) {
        this.steps = steps;

    }

    private int activeMinutes;
    public int getActiveMinutes() { return  activeMinutes; }
    public void setActiveMinutes(int mins) {this.activeMinutes = mins; }
    public void changeActiveMinutes(int mins) {
        this.activeMinutes = mins;

    }
}
