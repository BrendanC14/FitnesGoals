package com.cutlerdevelopment.fitnessgoals.ViewItems;

import android.widget.ScrollView;

public class ResultItem {

    public String date;
    public String homePosition;
    public String homeTeam;
    public String homeScore;
    public String awayScore;
    public String awayTeam;
    public String awayPosition;

    public String getDate() { return date; }
    public void setDate(String date) { this.date = date; }

    public String getHomePosition() { return homePosition; }
    public void setHomePosition(String homePosition) { this.homePosition = homePosition; }

    public String getHomeTeam() { return homeTeam; }
    public void setHomeTeam(String homeTeam) { this.homeTeam = homeTeam; }

    public String getHomeScore() { return homeScore; }
    public void setHomeScore(String homeScore) { this.homeScore = homeScore; }

    public String getAwayScore() { return awayScore; }
    public void setAwayScore(String awayScore) { this.awayScore = awayScore; }

    public String getAwayTeam() { return awayTeam; }
    public void setAwayTeam(String awayTeam) { this.awayTeam = awayTeam; }

    public String getAwayPosition() { return awayPosition; }
    public void setAwayPosition(String awayPosition) { this.awayPosition = awayPosition; }
}
