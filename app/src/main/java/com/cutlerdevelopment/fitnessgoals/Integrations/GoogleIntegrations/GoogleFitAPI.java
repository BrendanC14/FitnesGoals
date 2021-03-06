package com.cutlerdevelopment.fitnessgoals.Integrations.GoogleIntegrations;

import android.app.Activity;
import android.content.Context;

import androidx.annotation.NonNull;

import com.cutlerdevelopment.fitnessgoals.Constants.FitnessApps;
import com.cutlerdevelopment.fitnessgoals.Utils.DateHelper;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.fitness.Fitness;
import com.google.android.gms.fitness.FitnessOptions;
import com.google.android.gms.fitness.data.Bucket;
import com.google.android.gms.fitness.data.DataPoint;
import com.google.android.gms.fitness.data.DataSet;
import com.google.android.gms.fitness.data.DataSource;
import com.google.android.gms.fitness.data.DataType;
import com.google.android.gms.fitness.data.Field;
import com.google.android.gms.fitness.request.DataReadRequest;
import com.google.android.gms.fitness.result.DataReadResponse;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.TimeUnit;

public class GoogleFitAPI {

    private FitnessOptions fitnessOptions;
    private GoogleSignInAccount account;
    private Context c;

    public interface GoogleFitListener {
        void getAverage(int average);
        void getSteps(HashMap<Date, Integer> map, Date startDate, Date endDate);
    }

    GoogleFitListener listener;
    public void setListener(GoogleFitListener listener) {this.listener = listener; }

    public static void createGoogleFitAPIInstance(Context context) {
        new GoogleFitAPI(context);
    }
    private static GoogleFitAPI instance = null;

    public static GoogleFitAPI getInstance() {
        if (instance != null) {
            return instance;
        }
        return null;
    }

    private GoogleFitAPI(Context context) {

        fitnessOptions = FitnessOptions.builder()
                .addDataType(DataType.TYPE_STEP_COUNT_DELTA, FitnessOptions.ACCESS_READ)
                .build();

        c = context;
        account = GoogleSignIn.getAccountForExtension(c, fitnessOptions);

        instance = this;
    }

    public Boolean hasPermissions(Activity activity) {

        if (!GoogleSignIn.hasPermissions(account, fitnessOptions)) {
            GoogleSignIn.requestPermissions(
                    activity,
                    FitnessApps.GOOGLE_FIT_INIT_REQUEST_CODE,
                    account,
                    fitnessOptions);
            return false;
        }
        return true;
    }

    public String getName() {
        return account.getDisplayName();
    }

    public void getStepsFromDates(final Date startDate, final Date endDate) {
        long endTime = DateHelper.addDays(endDate, 1).getTime();
        long startTime =startDate.getTime();

        DataSource ESTIMATED_STEP_DELTAS = new DataSource.Builder()
                .setDataType(DataType.TYPE_STEP_COUNT_DELTA)
                .setType(DataSource.TYPE_DERIVED)
                .setStreamName("estimated_steps")
                .setAppPackageName("com.cutlerdevelopment.fitnessgoals.gms")
                .build();
        DataReadRequest readRequest = new DataReadRequest.Builder()
                .aggregate(DataType.TYPE_STEP_COUNT_DELTA,   DataType.TYPE_STEP_COUNT_DELTA)
                .bucketByTime(1, TimeUnit.DAYS)
                .setTimeRange(startTime, endTime, TimeUnit.MILLISECONDS)
                .build();

        Fitness.getHistoryClient(c, account)
                .readData(readRequest).addOnSuccessListener(new OnSuccessListener<DataReadResponse>() {
            @Override
            public void onSuccess(DataReadResponse response) {
                // Use response data here
                HashMap<Date, Integer> map = new HashMap<>();
                if (response.getBuckets().size() > 0) {
                    for (Bucket bucket :response.getBuckets()) {
                        List<DataSet> dataSets = bucket.getDataSets();
                        for (DataSet dataSet : dataSets) {
                            for (DataPoint dp : dataSet.getDataPoints()) {
                                for (Field field : dp.getDataType().getFields()) {
                                    map.put(
                                    new Date(bucket.getStartTime(TimeUnit.MILLISECONDS)),
                                    dp.getValue(field).asInt());
                                }
                            }
                        }
                    }
                }
                listener.getSteps(map, startDate, endDate);

            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                e.getCause();
            }
        });
    }


    public void getAverageFromDates(Date startDate, Date endDate) {
        long endTime = endDate.getTime();
        long startTime =startDate.getTime();

        DataReadRequest readRequest = new DataReadRequest.Builder()
                .aggregate(DataType.TYPE_STEP_COUNT_DELTA, DataType.AGGREGATE_STEP_COUNT_DELTA)
                .setTimeRange(startTime, endTime, TimeUnit.MILLISECONDS)
                .bucketByTime(1, TimeUnit.DAYS)
                .enableServerQueries()
                .build();

        Fitness.getHistoryClient(c, account)
                .readData(readRequest).addOnSuccessListener(new OnSuccessListener<DataReadResponse>() {
            @Override
            public void onSuccess(DataReadResponse response) {
                // Use response data here
                int totalSteps = 0;
                int numDays = 0;
                if (response.getBuckets().size() > 0) {
                    for (Bucket bucket :response.getBuckets()) {
                        List<DataSet> dataSets = bucket.getDataSets();
                        for (DataSet dataSet : dataSets) {
                            for (DataPoint dp : dataSet.getDataPoints()) {
                                for (Field field : dp.getDataType().getFields()) {
                                    numDays++;
                                    totalSteps += dp.getValue(field).asInt();
                                }
                            }
                        }
                    }
                }
                if (totalSteps > 0) { listener.getAverage(totalSteps / numDays); }
                else { listener.getAverage(0); }

            }
        });
    }
}
