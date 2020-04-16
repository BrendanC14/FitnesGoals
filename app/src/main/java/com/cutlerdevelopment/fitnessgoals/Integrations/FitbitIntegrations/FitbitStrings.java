package com.cutlerdevelopment.fitnessgoals.Integrations.FitbitIntegrations;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

@Entity
public class FitbitStrings {

    private static FitbitStrings instance = null;

    public static FitbitStrings getInstance() {
        if (instance != null) {
            return instance;
        }
        return null;
    }

    @Ignore
    public FitbitStrings(String userID, String accessToken, String tokenType) {
        this.userID = userID;
        this.accessToken = accessToken;
        this.tokenType = tokenType;

        FitbitSavedData.getInstance().saveObject(this);

        instance = this;
    }

    private FitbitStrings() {
        instance = this;
    }

    @PrimaryKey
    @NonNull
    private String userID;
    public String getUserID() { return userID; }
    public void setUserID(String userID) { this.userID = userID; }

    private String accessToken;
    public String getAccessToken() { return accessToken; }
    public void setAccessToken(String accessToken) { this.accessToken = accessToken; }
    public void changeAccessToken(String accessToken) {
        this.accessToken = accessToken;
        FitbitSavedData.getInstance().updateObject(this);
    }

    private String tokenType;
    public String getTokenType() { return tokenType; }
    public void setTokenType(String tokenType) { this.tokenType = tokenType; }
    public void changeTokenType(String tokenType) {
        this.tokenType = tokenType;
        FitbitSavedData.getInstance().updateObject(this);
    }
}
