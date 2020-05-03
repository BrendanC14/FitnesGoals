package com.cutlerdevelopment.fitnessgoals.Models;

import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

import com.cutlerdevelopment.fitnessgoals.SavedData.AppDBHandler;
import com.cutlerdevelopment.fitnessgoals.Utils.DateHelper;

import java.util.Date;

@Entity
public class Badge {

    @Ignore
    public Badge(int ID, int category, int target, String name, String description) {
        this.ID = ID;
        this.category = category;
        this.target = target;
        this.name = name;
        this. description = description;
        this.unlocked = false;
        this.dateEarned = null;

        AppDBHandler.getInstance().saveObject(this);
    }

    public Badge() {

    }



    @PrimaryKey
    private int ID;
    public int getID() { return ID; }
    public void setID(int ID) { this.ID = ID; }

    private int target;
    public int getTarget() { return target; }
    public void setTarget(int target) { this.target = target; }

    private String name;
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public void changeName(String name) {
        this.name = name;
        AppDBHandler.getInstance().updateObject(this);
    }

    private String description;
    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    private int category;
    public int getCategory() { return category; }
    public void setCategory(int category) { this.category = category; }

    private boolean unlocked;
    public boolean isUnlocked() { return unlocked; }
    public void setUnlocked(boolean unlocked) { this.unlocked = unlocked; }
    public void unlockBadge() {
        unlocked = true;
        dateEarned = DateHelper.cleanDate(new Date());
        AppDBHandler.getInstance().updateObject(this);
    }

    private Date dateEarned;
    public Date getDateEarned() { return dateEarned; }
    public void setDateEarned(Date dateEarned) { this.dateEarned = dateEarned; }


}
