package com.cutlerdevelopment.fitnessgoals.Utils;

import com.cutlerdevelopment.fitnessgoals.Constants.BadgeCategories;
import com.cutlerdevelopment.fitnessgoals.Constants.Leagues;
import com.cutlerdevelopment.fitnessgoals.Models.Badge;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class BadgeHelper {

    public static final int TOTAL_NUM_BADGES = 49;

    public static void createFirstTimeBadges(int ID) {
        switch (ID) {
            case 0:
                new Badge(
                        ID,
                        BadgeCategories.DAILY_STEP,
                        5000,
                        "5k",
                        "You've done 5,000 steps in one day!"
                );
                break;
            case 1:
                new Badge(
                        ID,
                        BadgeCategories.DAILY_STEP,
                        10000,
                        "10k",
                        "You've done 10,000 steps in one day!"
                        );
                break;
            case 2:
                new Badge(
                        ID,
                        BadgeCategories.DAILY_STEP,
                        15000,
                        "15k",
                        "You've done 15,000 steps in one day!"
                );
                break;
            case 3:
                new Badge(
                        ID,
                        BadgeCategories.DAILY_STEP,
                        20000,
                        "20k",
                        "You've done 20,000 steps in one day!"
                );
                break;
            case 4:
                new Badge(
                        ID,
                        BadgeCategories.DAILY_STEP,
                        25000,
                        "25k",
                        "You've done 25,000 steps in one day!"
                );
                break;
            case 5:
                new Badge(
                        ID,
                        BadgeCategories.TOTAL_STEP,
                        29250,
                        "Half Marathon",
                        "You've done 29,250 steps since you've had the app! This is roughly a Half-Marathon!"
                );
                break;
            case 6:
                new Badge(
                        ID,
                        BadgeCategories.DAILY_STEP,
                        50000,
                        "50k",
                        "You've done 29,250 steps since you've had the app! This is roughly a Half-Marathon!"
                );
                break;
            case 7:
                new Badge(
                        ID,
                        BadgeCategories.TOTAL_STEP,
                        58500,
                        "Marathon",
                        "You've done 58,500 steps since you've had the app! This is roughly a Marathon!"
                );
                break;
            case 8:
                new Badge(
                        ID,
                        BadgeCategories.TOTAL_STEP,
                        100000,
                        "100k",
                        "You've done 100,000 steps since you've had the app!"
                );
                break;
            case 9:
                new Badge(
                        ID,
                        BadgeCategories.TOTAL_STEP,
                        137000,
                        "1k Pitches",
                        "You've done 137,000 steps since you've had the app! This is roughly 1,000 football pitches!"
                );
                break;
            case 10:
                new Badge(
                        ID,
                        BadgeCategories.TOTAL_STEP,
                        168750,
                        "75 miles",
                        "You've done 168,750 steps since you've had the app! This is roughly 75 miles!"
                );
                break;
            case 11:
                new Badge(
                        ID,
                        BadgeCategories.TOTAL_STEP,
                        225000,
                        "100 miles",
                        "You've done 225,000 steps since you've had the app! This is roughly 100 miles!"
                );
                break;
            case 12:
                new Badge(
                        ID,
                        BadgeCategories.TOTAL_STEP,
                        250000,
                        "250k",
                        "You've done 250,000 steps since you've had the app!"
                );
                break;
            case 13:
                new Badge(
                        ID,
                        BadgeCategories.TOTAL_STEP,
                        500000,
                        "500k",
                        "You've done 500,000 steps since you've had the app!"
                );
                break;
            case 14:
                new Badge(
                        ID,
                        BadgeCategories.TOTAL_STEP,
                        1000000,
                        "Milli",
                        "You've done a MILLION steps since you've had the app!"
                );
                break;
            case 15:
                new Badge(
                        ID,
                        BadgeCategories.TOTAL_STEP,
                        1370000,
                        "10k pitches",
                        "You've done 1,370,000 steps since you've had the app! This is roughly 10,000 football pitches!"
                );
                break;
            case 16:
                new Badge(
                        ID,
                        BadgeCategories.TOTAL_STEP,
                        2250000,
                        "1k miles",
                        "You've done 2,250,000 steps since you've had the app! This is roughly 1,000 miles!"
                );
                break;
            case 17:
                new Badge(
                        ID,
                        BadgeCategories.TOTAL_STEP,
                        5000000,
                        "5 milli",
                        "You've done 5 MILLION steps since you've had the app!"
                );
                break;
            case 18:
                new Badge(
                        ID,
                        BadgeCategories.TOTAL_STEP,
                        10000000,
                        "10 milli",
                        "You've done 10 MILLION steps since you've had the app!"
                );
                break;
            case 19:
                new Badge(
                        ID,
                        BadgeCategories.WINS,
                        1,
                        "1st Win",
                        "Well done, you've won your first match!"
                );
                break;
            case 20:
                new Badge(
                        ID,
                        BadgeCategories.WINS,
                        5,
                        "5 Wins",
                        "Well done, you've won five matches!"
                );
                break;
            case 21:
                new Badge(
                        ID,
                        BadgeCategories.UNBEATEN,
                        5,
                        "5 Unbeaten",
                        "Well done, you've gone five games unbeaten!"
                );
                break;
            case 22:
                new Badge(
                        ID,
                        BadgeCategories.WINS,
                        10,
                        "10 Wins",
                        "Well done, you've won ten matches!"
                );
                break;
            case 23:
                new Badge(
                        ID,
                        BadgeCategories.UNBEATEN,
                        10,
                        "10 Unbeaten",
                        "Well done, you've gone ten games unbeaten!"
                );
                break;
            case 24:
                new Badge(
                        ID,
                        BadgeCategories.WINS,
                        25,
                        "25 Wins",
                        "Well done, you've won twenty five matches!"
                );
                break;
            case 25:
                new Badge(
                        ID,
                        BadgeCategories.UNBEATEN,
                        25,
                        "25 Unbeaten",
                        "Well done, you've gone twenty five games unbeaten!"
                );
                break;
            case 26:
                new Badge(
                        ID,
                        BadgeCategories.WINS,
                        50,
                        "50 Wins",
                        "Well done, you've won fifty matches!"
                );
                break;
            case 27:
                new Badge(
                        ID,
                        BadgeCategories.UNBEATEN,
                        50,
                        "50 Unbeaten",
                        "Well done, you've gone fifty games unbeaten!"
                );
                break;
            case 28:
                new Badge(
                        ID,
                        BadgeCategories.GAMES,
                        100,
                        "100 Matches",
                        "You've now played a hundred matches"
                );
                break;
            case 29:
                new Badge(
                        ID,
                        BadgeCategories.WINS,
                        100,
                        "100 Wins",
                        "Well done, you've won a hundred matches!"
                );
                break;
            case 30:
                new Badge(
                        ID,
                        BadgeCategories.GAMES,
                        250,
                        "250 Matches",
                        "You've now played 250 matches"
                );
                break;
            case 31:
                new Badge(
                        ID,
                        BadgeCategories.WINS,
                        250,
                        "250 Wins",
                        "Well done, you've won 250 matches!"
                );
                break;
            case 32:
                new Badge(
                        ID,
                        BadgeCategories.GAMES,
                        500,
                        "500 Matches",
                        "You've now played 500 matches"
                );
                break;
            case 33:
                new Badge(
                        ID,
                        BadgeCategories.GAMES,
                        500,
                        "500 Matches",
                        "You've now played 500 matches"
                );
                break;
            case 34:
                new Badge(
                        ID,
                        BadgeCategories.GOALS,
                        10,
                        "10 Goals",
                        "You've now scored ten goals!"
                );
                break;
            case 35:
                new Badge(
                        ID,
                        BadgeCategories.GOALS,
                        50,
                        "50 Goals",
                        "You've now scored fifty goals!"
                );
                break;
            case 36:
                new Badge(
                        ID,
                        BadgeCategories.GOALS,
                        100,
                        "100 Goals",
                        "You've now scored a hundred goals!"
                );
                break;
            case 37:
                new Badge(
                        ID,
                        BadgeCategories.GOALS,
                        250,
                        "250 Goals",
                        "You've now scored 250 goals!"
                );
                break;
            case 38:
                new Badge(
                        ID,
                        BadgeCategories.GOALS,
                        500,
                        "500 Goals",
                        "You've now scored 500 goals!"
                );
                break;
            case 39:
                new Badge(
                        ID,
                        BadgeCategories.GOALS,
                        1000,
                        "1k Goals",
                        "You've now scored a THOUSAND goals!"
                );
                break;
            case 40:
                new Badge(
                        ID,
                        BadgeCategories.TARGET,
                        5,
                        "Target Reached x5",
                        "You've reached your target 5 times!"
                );
                break;
            case 41:
                new Badge(
                        ID,
                        BadgeCategories.TARGET,
                        10,
                        "Target Reached x10",
                        "You've reached your target 10 times!"
                );
                break;
            case 42:
                new Badge(
                        ID,
                        BadgeCategories.TARGET,
                        25,
                        "Target Reached x25",
                        "You've reached your target 25 times!"
                );
                break;
            case 43:
                new Badge(
                        ID,
                        BadgeCategories.TARGET,
                        50,
                        "Target Reached x50",
                        "You've reached your target 50 times!"
                );
                break;
            case 44:
                new Badge(
                        ID,
                        BadgeCategories.TARGET,
                        100,
                        "Target Reached x100",
                        "You've reached your target 100 times!"
                );
                break;
            case 45:
                new Badge(
                        ID,
                        BadgeCategories.TARGET,
                        250,
                        "Target Reached x250",
                        "You've reached your target 250 times!"
                );
                break;
            case 46:
                new Badge(
                        ID,
                        BadgeCategories.TARGET,
                        500,
                        "Target Reached x500",
                        "You've reached your target 500 times!"
                );
                break;
            case 47:
                new Badge(
                        ID,
                        BadgeCategories.TARGET,
                        1000,
                        "Target Reached x1000",
                        "You've reached your target 1000 times!"
                );
                break;
        }
    }

}
