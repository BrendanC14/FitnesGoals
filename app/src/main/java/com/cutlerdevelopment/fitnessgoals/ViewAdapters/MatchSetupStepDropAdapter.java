package com.cutlerdevelopment.fitnessgoals.ViewAdapters;

import android.content.ClipData;
import android.content.ClipDescription;
import android.content.Context;
import android.view.DragEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.GridView;
import android.widget.TextView;

import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.DialogFragment;

import com.cutlerdevelopment.fitnessgoals.DIalogFragments.MatchSetup;
import com.cutlerdevelopment.fitnessgoals.R;
import com.cutlerdevelopment.fitnessgoals.ViewItems.MatchSetupDropItem;

import java.util.ArrayList;

public class MatchSetupStepDropAdapter extends BaseAdapter implements View.OnDragListener, View.OnLongClickListener {

    private ArrayList<MatchSetupDropItem> singleRow;
    private LayoutInflater thisInflater;
    private Context context;
    private View newView;
    static View itemPressed;

    public interface MatchSetupInterface {
        public void itemDropped();
    }

    MatchSetupInterface listener;

    public MatchSetupStepDropAdapter(Context context, ArrayList<MatchSetupDropItem> aRow, MatchSetupInterface listener) {
        this.singleRow = aRow;
        this.context = context;
        this.listener = listener;
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
        newView = convertView;
        if (newView == null) {
            newView = thisInflater.inflate(R.layout.match_setup_step_drop_item, parent, false);
        }

        TextView stepsText = newView.findViewById(R.id.matchSetupItemButton);

        final MatchSetupDropItem currentItem = (MatchSetupDropItem) getItem(i);

        stepsText.setTag(String.valueOf(i));
        stepsText.setOnDragListener(this);
        stepsText.setOnLongClickListener(this);

        stepsText.setText(currentItem.getSteps());

        parent.setOnDragListener(this);

        return newView;

    }


    @Override
    public boolean onDrag(View viewDroppedOn, DragEvent event) {
        switch (event.getAction()) {
            case DragEvent.ACTION_DRAG_STARTED:
                // Ignore this event
                return true;
            case DragEvent.ACTION_DRAG_ENTERED:
                // Ignore this event
                return true;
            case DragEvent.ACTION_DRAG_EXITED:
                // Ignore this event
                return true;
            case DragEvent.ACTION_DRAG_LOCATION:
                // Ignore this event
                return true;
            case DragEvent.ACTION_DROP:// The item will be Dropped
                viewDroppedOn.invalidate();
                ViewGroup currentOwner = (ViewGroup) itemPressed.getParent();

                ConstraintLayout newOwner = null;
                Button buttonToSwap = null;
                if (viewDroppedOn instanceof Button) {
                    newOwner = (ConstraintLayout) viewDroppedOn.getParent();
                    buttonToSwap = (Button) viewDroppedOn;
                }
                else if (viewDroppedOn instanceof GridView) {
                    //Loop through each ConstraintLayout in the Grid
                    for (int i = 0; i < ((GridView) viewDroppedOn).getChildCount(); i++) {
                        ConstraintLayout layout = (ConstraintLayout) ((GridView) viewDroppedOn).getChildAt(i);
                        Button view = (Button) layout.getChildAt(0);
                        if (view.getText() == "") {
                            newOwner = layout;
                            buttonToSwap = view;
                        }
                    }
                    if (newOwner == null || buttonToSwap == null) {
                        return false;
                    }
                }
                else {
                    return false;
                }

                currentOwner.removeView(itemPressed);
                newOwner.removeView(buttonToSwap);
                currentOwner.addView(buttonToSwap);
                newOwner.addView(itemPressed);
                listener.itemDropped();
                return true;

            case DragEvent.ACTION_DRAG_ENDED:
                return true;

        }
        return false;
    }


    @Override
    public boolean onLongClick(View view) {
        if (view instanceof Button) {
            if (((Button) view).getText() == "") {
                return false;
            }
        }
        ClipData.Item item = new ClipData.Item((String) view.getTag());
        ClipData clipData = new ClipData((CharSequence) view.getTag(),
                new String[] { ClipDescription.MIMETYPE_TEXT_PLAIN }, item);
        view.startDrag(clipData, new View.DragShadowBuilder(view), null, 0);
        itemPressed = view;
        return true;
    }

}
