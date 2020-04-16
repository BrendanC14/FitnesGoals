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

import com.cutlerdevelopment.fitnessgoals.Constants.FitnessApps;
import com.cutlerdevelopment.fitnessgoals.R;

@Entity
public class FitbitAPI {

    @PrimaryKey
    private static final String CLIENT_ID = "22BNWX";
    private static final String CLIENT_SECRET = "cc7290baf51817b20a7bcbed82dad566";

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
        instance = this;
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
}
