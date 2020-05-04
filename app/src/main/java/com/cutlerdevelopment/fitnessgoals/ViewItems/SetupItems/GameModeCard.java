package com.cutlerdevelopment.fitnessgoals.ViewItems.SetupItems;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;

import androidx.constraintlayout.widget.ConstraintLayout;

import com.cutlerdevelopment.fitnessgoals.R;

public class GameModeCard {

    private Context mContext;
    private View mCustomView;
    private LayoutInflater mInflater;
    public GameModeCard(Context context) {
        // TODO Auto-generated constructor stub
        mContext = context;
        mInflater = (LayoutInflater)mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    public View getView() {
        if(mCustomView==null) {
            mCustomView = mInflater.inflate(R.layout.game_mode_card, null);
            //initialize all the child here
        }
        return mCustomView;
    }
}
