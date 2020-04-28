package com.cutlerdevelopment.fitnessgoals.ViewAdapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.cutlerdevelopment.fitnessgoals.R;
import com.cutlerdevelopment.fitnessgoals.ViewItems.ResultItem;

import java.util.ArrayList;

public class ResultItemRowAdapter extends BaseAdapter {


    private ArrayList<ResultItem> singleRow;
    private LayoutInflater thisInflater;
    private Context context;


    public ResultItemRowAdapter(Context context, ArrayList<ResultItem> aRow) {
        this.singleRow = aRow;
        this.context = context;
        thisInflater = (thisInflater.from(context));
    }

    @Override
    public int getCount() {
        return singleRow.size();
    }

    @Override
    public Object getItem(int i) {
        return singleRow.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }


    @Override
    public View getView(int i, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = thisInflater.inflate(R.layout.result_item, parent, false);
        }

        TextView homeTeam = convertView.findViewById(R.id.resultItemHomeTeam);
        TextView homeScore = convertView.findViewById(R.id.resultItemHomeScore);
        TextView awayScore = convertView.findViewById(R.id.resultItemAwayScore);
        TextView awayTeam = convertView.findViewById(R.id.resultItemAwayTeam);

        final ResultItem currentItem = (ResultItem) getItem(i);

        homeTeam.setText(currentItem.getHomeTeam());
        homeScore.setText(currentItem.getHomeScore());
        awayScore.setText(currentItem.getAwayScore());
        awayTeam.setText(currentItem.getAwayTeam());

        return convertView;

    }
}
