package com.cutlerdevelopment.fitnessgoals.Constants;

public class FilterOptions {

    public static final int YESTERDAY = 1;
    public static final int LAST_7_DAYS = 2;
    public static final int LAST_MONTH = 3;

    public static final int LAST_MATCH = 4;
    public static final int SEASON = 5;

    private static final String YESTERDAY_TAB_STRING = "Yest";
    private static final String LAST_7_TAB_STRING = "7 days";
    private static final String LAST_MONTH_TAB_STRING = "Month";
    private static final String LAST_MATCH_TAB_STRING = "Match";
    private static final String SEASON_TAB_STRING = "Season";

    public static String getTabName(int filterChoice) {
        switch (filterChoice) {
            case YESTERDAY: return YESTERDAY_TAB_STRING;
            case LAST_7_DAYS: return LAST_7_TAB_STRING;
            case LAST_MONTH: return LAST_MONTH_TAB_STRING;
            case LAST_MATCH: return LAST_MATCH_TAB_STRING;
            case SEASON: return SEASON_TAB_STRING;
            default: return "Unknown";
        }
    }

    public static int getFilterOption(String filterChoice) {
        switch (filterChoice) {
            case YESTERDAY_TAB_STRING: return YESTERDAY;
            case LAST_7_TAB_STRING: return LAST_7_DAYS;
            case LAST_MONTH_TAB_STRING: return LAST_MONTH;
            case LAST_MATCH_TAB_STRING: return LAST_MATCH;
            case SEASON_TAB_STRING: return SEASON;
            default: return 0;
        }
    }
}
