package com.cutlerdevelopment.fitnessgoals.Integrations;

import com.cutlerdevelopment.fitnessgoals.Constants.FitnessApps;
import com.cutlerdevelopment.fitnessgoals.Constants.Numbers;
import com.cutlerdevelopment.fitnessgoals.Integrations.FitbitIntegrations.FitbitAPI;
import com.cutlerdevelopment.fitnessgoals.Integrations.GoogleIntegrations.GoogleFirestoreConnector;
import com.cutlerdevelopment.fitnessgoals.Integrations.GoogleIntegrations.GoogleFitAPI;
import com.cutlerdevelopment.fitnessgoals.Settings.AppSettings;

import java.util.Date;

public class IntegrationConnectionHandler implements GoogleFitAPI.GoogleFitListener,
                                                    FitbitAPI.FitbitListener,
        GoogleFirestoreConnector.FirestoreListener {

    public interface IntegrationListener {
        void getAverageSteps(int average);
        void teamsRetrieved();
    }

    IntegrationListener listener;
    public void setListener(IntegrationListener listener) {this.listener = listener; }

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

    public void getAverageStepsFrom(Date dateFrom, Date dateTo) {
        if (AppSettings.getInstance().getFitnessApp() == FitnessApps.MOCKED) {
            listener.getAverageSteps(Numbers.MOCKED_AVERAGE_STEPS);
        }
        if (AppSettings.getInstance().getFitnessApp() == FitnessApps.GOOGLE_FIT) {
            GoogleFitAPI.getInstance().setListener(this);
            GoogleFitAPI.getInstance().getAverageFromDates(dateFrom, dateTo);
        }
        if (AppSettings.getInstance().getFitnessApp() == FitnessApps.FITBIT) {
            FitbitAPI.getInstance().setListener(this);
            FitbitAPI.getInstance().getAverageFromDates(dateFrom, dateTo);
        }
    }

    public void getTeamsForNewGame() {
        GoogleFirestoreConnector.getInstance().setListener(this);
        GoogleFirestoreConnector.populateNewGameTeamsFromFirestore();
    }

    @Override
    public void getAverage(int average) {
        listener.getAverageSteps(average);
    }

    @Override
    public void gotTeamsFromFirestore() {
        listener.teamsRetrieved();
    }
}
