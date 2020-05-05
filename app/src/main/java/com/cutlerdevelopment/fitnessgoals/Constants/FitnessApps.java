package com.cutlerdevelopment.fitnessgoals.Constants;

import com.cutlerdevelopment.fitnessgoals.R;

public class FitnessApps {

    public static final int MOCKED = 0;
    public static final int GOOGLE_FIT = 1;
    public static final int FITBIT = 2;
    public static final int SHEALTH = 3;
    public static final int NUM_OF_APPS = 4;

    public static final int GOOGLE_FIT_INIT_REQUEST_CODE = 1234;
    public static final int FITBIT_INIT_REQUEST_CODE = 4321;
    public static final int SHEALTH_INIT_REQUEST_CODE = 4567;

    private static final String GOOGLE_FIT_STRING = "Google Fit";
    private static final String FITBIT_STRING = "FitBit";
    private static final String SHEALTH_STRING = "S Health";
    private static final String MOCKED_STRING = "Mocked";

    public static String getFitnessAppName(int app) {
        switch (app) {
            case GOOGLE_FIT: return GOOGLE_FIT_STRING;
            case FITBIT: return FITBIT_STRING;
            case SHEALTH: return SHEALTH_STRING;
            default: return MOCKED_STRING;
        }
    }

    public static int getFitnessAppImage(int app) {
        switch (app) {
            case GOOGLE_FIT: return R.drawable.googlefitlogo;
            case FITBIT: return R.drawable.fitbitlogo;
            case SHEALTH: return R.drawable.shealth;
            default: return R.drawable.football;
        }
    }
    public static int getFitnessAppFromName(String name) {
        switch (name) {
            case GOOGLE_FIT_STRING: return GOOGLE_FIT;
            case FITBIT_STRING: return FITBIT;
            case SHEALTH_STRING: return SHEALTH;
            default: return MOCKED;
        }
    }
}
