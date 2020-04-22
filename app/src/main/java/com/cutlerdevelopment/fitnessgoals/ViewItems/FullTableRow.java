package com.cutlerdevelopment.fitnessgoals.ViewItems;

public class FullTableRow {


    private String position;
    private String teamName;
    private String gamesPlayed;
    private String wins;
    private String draws;
    private String losses;
    private String scored;
    private String conceded;
    private String goalDifference;
    private String points;
    private boolean boldTeam;
    private boolean promotionPlace;
    private boolean relegationPlace;

    public String getPosition() { return position; }
    public void setPosition(String pos) { position = pos; }

    public String getTeamName() { return teamName; }
    public void setTeamName(String name) { teamName = name; }

    public String getGamesPlayed() { return gamesPlayed; }
    public void setGamesPlayed(String played) { gamesPlayed = played; }

    public String getWins() { return wins; }
    public void setWins(String wins) { this.wins = wins; }

    public String getDraws() { return draws; }
    public void setDraws(String draws) { this.draws = draws; }

    public String getLosses() { return losses; }
    public void setLosses(String losses) { this.losses = losses; }

    public String getScored() { return scored; }
    public void setScored(String scored) { this.scored = scored; }

    public String getConceded() { return conceded; }
    public void setConceded(String conceded) { this.conceded = conceded; }

    public String getGoalDifference() { return goalDifference; }
    public void setGoalDifference(String goalDifference) { this.goalDifference = goalDifference; }

    public String getPoints() { return points; }
    public void setPoints(String pts) { points = pts; }

    public boolean isBoldTeam() { return boldTeam; }
    public void setBoldTeam(boolean boldTeam) { this.boldTeam = boldTeam; }

    public boolean isPromotionPlace() { return promotionPlace; }
    public void setPromotionPlace(boolean promotionPlace) { this.promotionPlace = promotionPlace; }

    public boolean isRelegationPlace() { return relegationPlace; }
    public void setRelegationPlace(boolean relegationPlace) { this.relegationPlace = relegationPlace; }
}
