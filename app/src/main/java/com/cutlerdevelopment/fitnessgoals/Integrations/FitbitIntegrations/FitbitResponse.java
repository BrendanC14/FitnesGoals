package com.cutlerdevelopment.fitnessgoals.Integrations.FitbitIntegrations;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;

import com.cutlerdevelopment.fitnessgoals.Activities.FirstMenu;
import com.cutlerdevelopment.fitnessgoals.Integrations.FitbitIntegrations.FitbitStrings;

public class FitbitResponse extends AppCompatActivity {

    String string;

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        string = intent.getDataString();

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        onNewIntent(getIntent());
        if (!string.contains("error_description")) {
            String accessToken = string.substring(string.indexOf("&access_token") + 14);
            String userId = string.substring(string.indexOf("&user_id") + 9, string.indexOf("&token_type"));
            String tokenType = string.substring(string.indexOf("&token_type") + 12, string.indexOf("&expires_in"));
            new FitbitStrings(userId, accessToken, tokenType);
        }
        Intent intent = new Intent(this, FirstMenu.class);
        startActivity(intent);
        finish();

    }
}
