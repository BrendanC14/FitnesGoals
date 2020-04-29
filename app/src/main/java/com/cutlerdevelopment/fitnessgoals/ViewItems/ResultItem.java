package com.cutlerdevelopment.fitnessgoals.ViewItems;

import android.widget.ScrollView;

import com.cutlerdevelopment.fitnessgoals.Constants.MatchResult;

public class ResultItem {

    private String date;
    private String homePosition;
    private String homeTeam;
    private String homeScore;
    private String awayScore;
    private String awayTeam;
    private String awayPosition;

    private int result;

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

    public int getResult() { return result; }
    public void setResult(int result) { this.result = result; }
}
