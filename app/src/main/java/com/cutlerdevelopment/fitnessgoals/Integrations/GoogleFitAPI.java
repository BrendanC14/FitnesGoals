package com.cutlerdevelopment.fitnessgoals.Integrations;

import android.app.Activity;
import android.content.Context;

import com.cutlerdevelopment.fitnessgoals.Settings.UserActivity;
import com.cutlerdevelopment.fitnessgoals.Utils.DateHelper;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.fitness.Fitness;
import com.google.android.gms.fitness.FitnessOptions;
import com.google.android.gms.fitness.data.Bucket;
import com.google.android.gms.fitness.data.DataPoint;
import com.google.android.gms.fitness.data.DataSet;
import com.google.android.gms.fitness.data.DataType;
import com.google.android.gms.fitness.data.Field;
import com.google.android.gms.fitness.request.DataReadRequest;
import com.google.android.gms.fitness.result.DataReadResponse;
import com.google.android.gms.tasks.OnSuccessListener;

import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;

public class GoogleFitAPI {

    private FitnessOptions fitnessOptions;
    private GoogleSignInAccount account;
    private Context c;

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
                .addDataType(DataType.AGGREGATE_STEP_COUNT_DELTA, FitnessOptions.ACCESS_READ)
                .build();

        c = context;
        account = GoogleSignIn.getAccountForExtension(c, fitnessOptions);

        instance = this;
    }

    public Boolean hasPermissions(Activity activity) {

        if (!GoogleSignIn.hasPermissions(account, fitnessOptions)) {
            GoogleSignIn.requestPermissions(
                    activity,
                    1234,
                    account,
                    fitnessOptions);
            return false;
        }
        return true;
    }

    public void getStepsForProof(Date date) {
        long endTime = date.getTime();
        long startTime = DateHelper.addDays(date, - 1).getTime();

        DataReadRequest readRequest = new DataReadRequest.Builder()
                .aggregate(DataType.TYPE_STEP_COUNT_DELTA, DataType.AGGREGATE_STEP_COUNT_DELTA)
                .setTimeRange(startTime, endTime, TimeUnit.MILLISECONDS)
                .bucketByTime(1, TimeUnit.DAYS)
                .build();

        Fitness.getHistoryClient(c, account)
                .readData(readRequest).addOnSuccessListener(new OnSuccessListener<DataReadResponse>() {
            @Override
            public void onSuccess(DataReadResponse response) {
                // Use response data here
                if (response.getBuckets().size() > 0) {
                    for (Bucket bucket :response.getBuckets()) {
                        List<DataSet> dataSets = bucket.getDataSets();
                        for (DataSet dataSet : dataSets) {
                            for (DataPoint dp : dataSet.getDataPoints()) {
                                for (Field field : dp.getDataType().getFields()) {
                                    Date startDate = new Date(bucket.getStartTime(TimeUnit.MILLISECONDS));
                                    int a = dp.getValue(field).asInt();
                                    new UserActivity(startDate,a);
                                }
                            }
                        }
                    }
                }
            }
        });
    }
}
