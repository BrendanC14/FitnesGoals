package com.cutlerdevelopment.fitnessgoals.Integrations.SHealthIntegrations;

import android.app.Activity;
import android.database.Cursor;
import android.util.Log;

import com.cutlerdevelopment.fitnessgoals.Utils.DateHelper;
import com.samsung.android.sdk.healthdata.HealthConnectionErrorResult;
import com.samsung.android.sdk.healthdata.HealthConstants;
import com.samsung.android.sdk.healthdata.HealthData;
import com.samsung.android.sdk.healthdata.HealthDataResolver;
import com.samsung.android.sdk.healthdata.HealthDataStore;
import com.samsung.android.sdk.healthdata.HealthPermissionManager;
import com.samsung.android.sdk.healthdata.HealthResultHolder;

import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

public class SHealthAPI {
    private HealthDataStore mStore;
    private Activity act;

    public interface InitialConnectionListener {
        void connectionResult(int resultCode);
    }

    public interface SHealthFitListener {
        void getAverage(int average);
        void getSteps(HashMap<Date, Integer> map);
    }

    SHealthFitListener listener;
    InitialConnectionListener initListener;

    public void setListener(SHealthFitListener listener) {
        this.listener = listener;
    }

    public static void createSHealthAPIInstance(Activity context) {
        new SHealthAPI(context);
    }

    private static SHealthAPI instance = null;

    public static SHealthAPI getInstance() {
        if (instance != null) {
            return instance;
        }
        return null;
    }

    private SHealthAPI(Activity activity) {

        act = activity;
        initListener = (InitialConnectionListener) activity;
        mStore = new HealthDataStore(act, new HealthDataStore.ConnectionListener() {
            @Override
            public void onConnected() {
                requestPermissions();

            }

            @Override
            public void onConnectionFailed(HealthConnectionErrorResult error) {
                error.getErrorCode();
                initListener.connectionResult(error.getErrorCode());

            }

            @Override
            public void onDisconnected() {

            }
        });

        mStore.connectService();

        instance = this;
    }

    private void requestPermissions() {

        Set<HealthPermissionManager.PermissionKey> mKeys = new HashSet<HealthPermissionManager.PermissionKey>();
        HealthPermissionManager pmsManager = new HealthPermissionManager(mStore);
        mKeys.add(new HealthPermissionManager.PermissionKey(HealthConstants.StepCount.HEALTH_DATA_TYPE, HealthPermissionManager.PermissionType.READ));
        try {
            pmsManager.requestPermissions(mKeys, act).setResultListener(new HealthResultHolder.ResultListener<HealthPermissionManager.PermissionResult>() {
                @Override
                public void onResult(HealthPermissionManager.PermissionResult permissionResult) {
                    Map<HealthPermissionManager.PermissionKey, Boolean> resultMap = permissionResult.getResultMap();

                    if (resultMap.values().contains(Boolean.FALSE)) {
                        initListener.connectionResult(1);
                    } else {
                        initListener.connectionResult(0);
                    }

                }
            });
        }
        catch (Exception e) {
            e.getMessage();
        }
    }

    private final HealthResultHolder.ResultListener<HealthDataResolver.ReadResult> mListener =
            new HealthResultHolder.ResultListener<HealthDataResolver.ReadResult>() {
                @Override
                public void onResult(HealthDataResolver.ReadResult result) {
                    int count = 0;
                    try {
                        Iterator<HealthData> iterator = result.iterator();

                        if (iterator.hasNext()) {
                            HealthData data = iterator.next();
                            count += data.getInt(HealthConstants.StepCount.COUNT);
                        }
                    } finally {
                        result.close();
                    }
                    listener.getAverage(count);

                }
            };
    public void getAverageFromDates(Date startDate, Date endDate) {
        long startTime = DateHelper.addDays(new Date(), -1).getTime();
        long ONE_DAY_IN_MILLIS = 24 * 60 * 60 * 1000L;
        long endTime = startTime + ONE_DAY_IN_MILLIS;

        HealthDataResolver resolver = new HealthDataResolver(mStore, null);
        Log.d("TEST", "start "+startTime);
        Log.d("TEST", "end "+endTime);
        HealthDataResolver.Filter filter =
                HealthDataResolver.Filter.greaterThanEquals(HealthConstants.StepCount.START_TIME, startTime)
                        .lessThan(HealthConstants.StepCount.END_TIME, endTime);

        HealthDataResolver.AggregateRequest request = new HealthDataResolver.AggregateRequest.Builder()
                .setDataType(HealthConstants.StepCount.HEALTH_DATA_TYPE)
                .setFilter(filter)
                .addFunction(HealthDataResolver.AggregateRequest.AggregateFunction.SUM,
                        HealthConstants.StepCount.COUNT, "sum")
                .setTimeGroup(HealthDataResolver.AggregateRequest.TimeGroupUnit.DAILY, 1,
                        HealthConstants.StepCount.TIME_OFFSET,
                        HealthConstants.StepCount.END_TIME, "day")
                .build();

        try {
            resolver.aggregate(request).setResultListener(mStepAggrResult);
        } catch (Exception e) {
            Log.d("TEST", "Aggregating health data fails.");
        }
    }

    private final HealthResultHolder.ResultListener<HealthDataResolver.AggregateResult> mStepAggrResult=
            new HealthResultHolder.ResultListener<HealthDataResolver.AggregateResult>() {

                @Override
                public void onResult(HealthDataResolver.AggregateResult result) {
// Checks the result
                    Cursor c = result.getResultCursor();

                    int count = 0;
                    int total = 0;
                    if (c != null) {
                        while (c.moveToNext()) {
                            String day = c.getString(c.getColumnIndex("day"));
                            float sum = c.getInt(c.getColumnIndex("sum"));

                            count++;
                            total += sum;
                            Log.d("TEST", "sum "+sum);
                            Log.d("TEST", "day "+day);
                        }
                        c.close();
                    }
                    else {
                        Log.d("TEST", "There is no result.");
                        listener.getAverage(0);
                    }
                    if (count == 0 || total == 0) {
                        listener.getAverage(0);
                    }
                    else {
                        listener.getAverage(total / count);
                    }
                }
            };
}
