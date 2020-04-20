package com.cutlerdevelopment.fitnessgoals.Integrations;

import com.cutlerdevelopment.fitnessgoals.Constants.FitnessApps;
import com.cutlerdevelopment.fitnessgoals.Constants.Numbers;
import com.cutlerdevelopment.fitnessgoals.Integrations.FitbitIntegrations.FitbitAPI;
import com.cutlerdevelopment.fitnessgoals.Integrations.GoogleFitIntegrations.GoogleFitAPI;
import com.cutlerdevelopment.fitnessgoals.Settings.Settings;

import java.util.Date;

public class IntegrationConnectionHandler implements GoogleFitAPI.GoogleFitListener,
                                                    FitbitAPI.FitbitListener {

    public interface IntegrationListener {
        void getAverageSteps(int average);
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
        if (Settings.getInstance().getFitnessApp() == FitnessApps.MOCKED) {
            listener.getAverageSteps(Numbers.MOCKED_AVERAGE_STEPS);
        }
        if (Settings.getInstance().getFitnessApp() == FitnessApps.GOOGLE_FIT) {
            GoogleFitAPI.getInstance().setListener(this);
            GoogleFitAPI.getInstance().getAverageFromDates(dateFrom, dateTo);
        }
        if (Settings.getInstance().getFitnessApp() == FitnessApps.FITBIT) {
            FitbitAPI.getInstance().setListener(this);
            FitbitAPI.getInstance().getAverageFromDates(dateFrom, dateTo);
        }
    }

    @Override
    public void getAverage(int average) {
        listener.getAverageSteps(average);
    }
}
