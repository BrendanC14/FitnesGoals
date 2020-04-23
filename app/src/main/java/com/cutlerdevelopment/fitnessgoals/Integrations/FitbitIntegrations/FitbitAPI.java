package com.cutlerdevelopment.fitnessgoals.Integrations.FitbitIntegrations;

import android.app.Activity;
import android.content.ComponentName;
import android.net.Uri;

import androidx.browser.customtabs.CustomTabsClient;
import androidx.browser.customtabs.CustomTabsIntent;
import androidx.browser.customtabs.CustomTabsServiceConnection;
import androidx.browser.customtabs.CustomTabsSession;
import androidx.core.content.ContextCompat;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.cutlerdevelopment.fitnessgoals.Constants.FitnessApps;
import com.cutlerdevelopment.fitnessgoals.R;
import com.cutlerdevelopment.fitnessgoals.Utils.DateHelper;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import static com.cutlerdevelopment.fitnessgoals.Constants.Words.getDateInFitbitFormat;

@Entity
public class FitbitAPI {

    @PrimaryKey
    private static final String CLIENT_ID = "22BNWX";
    private static final String CLIENT_SECRET = "cc7290baf51817b20a7bcbed82dad566";

    Activity act;

    public interface FitbitListener {
        void getAverage(int average);
        void getSteps(HashMap<Date, Integer> map);
    }

    FitbitListener listener;
    public void setListener(FitbitListener listener) {this.listener = listener; }
    private static final String CUSTOM_TAB_PACKAGE_NAME = "com.android.chrome";
    private CustomTabsClient mClient;
    private CustomTabsSession mCustomTabsSession;
    private CustomTabsServiceConnection mCustomTabsServiceConnection;
    private CustomTabsIntent customTabsIntent;

    public static void createFitbitAPIInstance(Activity activity) {
        new FitbitAPI(activity);
    }
    private static FitbitAPI instance = null;

    public static FitbitAPI getInstance() {
        if (instance != null) {
            return instance;
        }
        return null;
    }

    private FitbitAPI(Activity activity) {
        this.act = activity;
        instance = this;
    }

    public void requestFitbitPermission(Activity activity) {

        speedUpChromeTabs();
        CustomTabsClient.bindCustomTabsService(activity, CUSTOM_TAB_PACKAGE_NAME, mCustomTabsServiceConnection);


        customTabsIntent = new CustomTabsIntent.Builder(mCustomTabsSession)
                .setToolbarColor(ContextCompat.getColor(activity, R.color.colorPrimary))
                .setShowTitle(true)
                .setStartAnimations(activity, R.anim.slide_in_right, R.anim.slide_out_left)
                .setExitAnimations(activity, R.anim.slide_out_left, R.anim.slide_in_right)
                .build();

        String url = "https://www.fitbit.com/oauth2/authorize?" +
                "response_type=token" +
                "&client_id=" + CLIENT_ID +
                "&expires_in=31536000" +
                "&scope=activity" +
                "&redirect_uri=my-fitbit-app://my.fitbit.app/handle_auth" +
                "&prompt=login";
        customTabsIntent.launchUrl(activity, Uri.parse(url));

        activity.startActivityForResult(customTabsIntent.intent, FitnessApps.FITBIT_INIT_REQUEST_CODE);

    }

    private void speedUpChromeTabs() {
        mCustomTabsServiceConnection = new CustomTabsServiceConnection() {
            @Override
            public void onCustomTabsServiceConnected(ComponentName componentName, CustomTabsClient customTabsClient) {
                //Pre-warming
                mClient = customTabsClient;
                mClient.warmup(0L);
                mCustomTabsSession = mClient.newSession(null);
            }

            @Override
            public void onServiceDisconnected(ComponentName name) {
                mClient = null;
            }
        };
    }

    public void getStepsFromDates(Date startDate, Date endDate) {

        String startDateString = getDateInFitbitFormat(startDate);
        String endDateString = getDateInFitbitFormat(endDate);

        RequestQueue queue = Volley.newRequestQueue(act);
        String url = "https://api.fitbit.com/1/user/" +
                "-/" +
                "activities/steps/date/" +
                startDateString + "/" +
                endDateString +".json";



        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        HashMap<Date, Integer> map = new HashMap<>();
                        try {
                            JSONObject object = new JSONObject(response);
                            JSONArray jsonArray = object.getJSONArray("activities-steps");
                            for (int i = 0; i < jsonArray.length(); i++) {
                                JSONObject dateObject = jsonArray.getJSONObject(i);
                                String dateString = dateObject.getString("dateTime");
                                String steps = dateObject.getString("value");
                                Date date = DateHelper.formatStringToDate(dateString);
                                map.put(date, Integer.valueOf(steps));
                            }




                        } catch (JSONException | ParseException e) {
                            e.printStackTrace();
                        }


                        listener.getSteps(map);
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                    }
                }) {
            /** Passing some request headers* */
            @Override
            public Map getHeaders() throws AuthFailureError {
                HashMap headers = new HashMap();
                headers.put("Authorization", "Bearer  " + FitbitStrings.getInstance().getAccessToken());
                return headers;
            }



        };

        queue.add(stringRequest);
    }

    public void getAverageFromDates(Date startDate, Date endDate) {

        String startDateString = getDateInFitbitFormat(startDate);
        String endDateString = getDateInFitbitFormat(endDate);

        RequestQueue queue = Volley.newRequestQueue(act);
        String url = "https://api.fitbit.com/1/user/" +
                "-/" +
                "activities/steps/date/" +
                startDateString + "/" +
                endDateString +".json";



        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        int steps = 0;
                        int numDays = 0;

                        try {
                            JSONObject object = new JSONObject(response);
                            JSONArray jsonArray = object.getJSONArray("activities-steps");
                            for (int i = 0; i < jsonArray.length(); i++) {
                                JSONObject dateObject = jsonArray.getJSONObject(i);
                                String stepString = dateObject.getString("value");
                                steps += Integer.valueOf(stepString);
                                numDays++;
                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }


                        listener.getAverage(steps / numDays);
                    }
                },
                new Response.ErrorListener() {
                  @Override
                  public void onErrorResponse(VolleyError error) {
                        listener.getAverage(0);
                    }
        }) {
            /** Passing some request headers* */
            @Override
            public Map getHeaders() throws AuthFailureError {
                HashMap headers = new HashMap();
                headers.put("Authorization", "Bearer  " + FitbitStrings.getInstance().getAccessToken());
                return headers;
            }



        };

        queue.add(stringRequest);
    }

}
