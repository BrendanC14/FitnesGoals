package com.cutlerdevelopment.fitnessgoals.Integrations;

import android.app.Activity;
import android.content.Context;

import com.cutlerdevelopment.fitnessgoals.Constants.FitnessApps;
import com.cutlerdevelopment.fitnessgoals.Constants.Numbers;
import com.cutlerdevelopment.fitnessgoals.Data.GameData;
import com.cutlerdevelopment.fitnessgoals.Integrations.FitbitIntegrations.FitbitAPI;
import com.cutlerdevelopment.fitnessgoals.Integrations.FitbitIntegrations.FitbitStringsSavedData;
import com.cutlerdevelopment.fitnessgoals.Integrations.GoogleIntegrations.GoogleFirestoreConnector;
import com.cutlerdevelopment.fitnessgoals.Integrations.GoogleIntegrations.GoogleFitAPI;
import com.cutlerdevelopment.fitnessgoals.Data.AppData;
import com.cutlerdevelopment.fitnessgoals.Data.UserActivity;
import com.cutlerdevelopment.fitnessgoals.Integrations.SHealthIntegrations.SHealthAPI;
import com.cutlerdevelopment.fitnessgoals.Utils.DateHelper;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

public class IntegrationConnectionHandler implements GoogleFitAPI.GoogleFitListener,
                                                    FitbitAPI.FitbitListener,
                                                    SHealthAPI.SHealthFitListener,
        GoogleFirestoreConnector.FirestoreListener {

    public interface SetupListener {
        void getAverageSteps(int average);
        void teamsRetrieved();
    }
    SetupListener setupListener;
    public void setSetupListener(SetupListener listener) {this.setupListener = listener; }

    public interface TMListener {
        void gotStepMap();
    }
    TMListener tmListener;
    public void setTmListener(TMListener listener) {this.tmListener = listener; }

    private static IntegrationConnectionHandler instance = null;

    public static IntegrationConnectionHandler getInstance() {
        if (instance != null) {
            return instance;
        }
        return new IntegrationConnectionHandler();
    }

    public IntegrationConnectionHandler() {
        instance = this;
    }

    public void initialiseFitnessAppConnection(Activity activity, Context context) {

        int chosenApp = AppData.getInstance().getFitnessApp();
        if (chosenApp == FitnessApps.MOCKED) {
            GameData.getInstance().refreshPlayerActivity();
        }
        if (chosenApp == FitnessApps.GOOGLE_FIT) {
            GoogleFitAPI.createGoogleFitAPIInstance(context);
            GameData.getInstance().refreshPlayerActivity();
        }
        else if (chosenApp == FitnessApps.FITBIT) {
            FitbitAPI.createFitbitAPIInstance(activity);
            FitbitStringsSavedData.createFitbitStringsSavedDataInstance(context);
            FitbitStringsSavedData.getInstance().getTopData();
            GameData.getInstance().refreshPlayerActivity();
        }
        else if (chosenApp == FitnessApps.SHEALTH) {
            SHealthAPI.createSHealthAPIInstance(activity);
            SHealthAPI.getInstance().setListener(this);
        }

    }

    public void getAverageStepsFrom(Date startDate, Date endDate) {
        int chosenApp = AppData.getInstance().getFitnessApp();
        if (chosenApp == FitnessApps.MOCKED) {
            setupListener.getAverageSteps(Numbers.MOCKED_AVERAGE_STEPS);
        }
        else if (chosenApp == FitnessApps.GOOGLE_FIT) {
            GoogleFitAPI.getInstance().setListener(this);
            GoogleFitAPI.getInstance().getAverageFromDates(startDate, endDate);
        }
        else if (chosenApp == FitnessApps.FITBIT) {
            FitbitAPI.getInstance().setListener(this);
            FitbitAPI.getInstance().getAverageFromDates(startDate, endDate);
        }
        else if (chosenApp == FitnessApps.SHEALTH) {
            SHealthAPI.getInstance().setListener(this);
            SHealthAPI.getInstance().getAverageFromDates(startDate, endDate);

        }
    }

    public void getTeamsForNewGame() {
        GoogleFirestoreConnector.getInstance().setListener(this);
        GoogleFirestoreConnector.populateNewGameTeamsFromFirestore();
    }

    public void refreshStepActivity(Date startDate, Date endDate) {

        int chosenApp = AppData.getInstance().getFitnessApp();
        if (chosenApp == FitnessApps.MOCKED) {
            Random r = new Random();
            int daysBetween = DateHelper.getDaysBetween(endDate, startDate);
            HashMap<Date, Integer> map = new HashMap<>();
            for (int i = 0; i <= daysBetween; i++) {
                new UserActivity(
                        DateHelper.addDays(startDate, i),
                        r.nextInt((10000) - (6000)) + 6000
                );
                if (tmListener != null) { tmListener.gotStepMap(); }
            }

        }
        else if (chosenApp == FitnessApps.GOOGLE_FIT) {
            GoogleFitAPI.getInstance().setListener(this);
            GoogleFitAPI.getInstance().getStepsFromDates(startDate, endDate);
        }
        else if (chosenApp == FitnessApps.FITBIT) {
            FitbitAPI.getInstance().setListener(this);
            FitbitAPI.getInstance().getStepsFromDates(startDate, endDate);
        }
        else if (chosenApp == FitnessApps.SHEALTH) {
            //S Health is exclusive of end date, but want inclusive so adding a day on
            endDate = DateHelper.addDays(endDate, 1);
            SHealthAPI.getInstance().setListener(this);
            SHealthAPI.getInstance().getStepsFromDates(startDate, endDate);
        }
    }

    @Override
    public void getSteps(HashMap<Date, Integer> map, Date startDate, Date endDate) {

        int daysBetwwen = DateHelper.getDaysBetween(endDate, startDate);
        for (int i = 0; i <= daysBetwwen; i++) {
            Date thisDate = DateHelper.addDays(startDate, i);
            int steps = 0;
            if (map.containsKey(thisDate)) {
                steps = map.get(thisDate);
            }
            new UserActivity(
                    thisDate,
                    steps
            );
        }
        if (tmListener != null) { tmListener.gotStepMap(); }

    }

    @Override
    public void getAverage(int average) {
        setupListener.getAverageSteps(average);
    }

    @Override
    public void gotTeamsFromFirestore() {
        setupListener.teamsRetrieved();
    }
}
