package com.cutlerdevelopment.fitnessgoals.Models;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

import com.cutlerdevelopment.fitnessgoals.SavedData.AppSavedData;

import java.util.Date;

@Entity
public class Badge {



    @PrimaryKey
    private int ID;
    public int getID() { return ID; }
    public void setID(int ID) { this.ID = ID; }

    private String name;
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public void changeName(String name) {
        this.name = name;
        AppSavedData.getInstance().updateObject(this);
    }

    private String description;
    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    private Date dateEarned;
    public Date getDateEarned() { return dateEarned; }
    public void setDateEarned(Date dateEarned) { this.dateEarned = dateEarned; }
}
