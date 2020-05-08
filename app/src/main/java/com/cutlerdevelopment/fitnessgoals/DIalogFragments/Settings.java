package com.cutlerdevelopment.fitnessgoals.DIalogFragments;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.view.accessibility.AccessibilityViewCommand;
import androidx.fragment.app.DialogFragment;

import com.cutlerdevelopment.fitnessgoals.Constants.Colours;
import com.cutlerdevelopment.fitnessgoals.Constants.Leagues;
import com.cutlerdevelopment.fitnessgoals.Constants.Numbers;
import com.cutlerdevelopment.fitnessgoals.Constants.StepModes;
import com.cutlerdevelopment.fitnessgoals.Data.AppData;
import com.cutlerdevelopment.fitnessgoals.Data.GameData;
import com.cutlerdevelopment.fitnessgoals.Models.Team;
import com.cutlerdevelopment.fitnessgoals.R;
import com.cutlerdevelopment.fitnessgoals.SavedData.GameDBHandler;
import com.cutlerdevelopment.fitnessgoals.Utils.JamesStep;
import com.cutlerdevelopment.fitnessgoals.Utils.StringHelper;
import com.cutlerdevelopment.fitnessgoals.ViewAdapters.ColourDisplaySmallCardAdapter;
import com.cutlerdevelopment.fitnessgoals.ViewAdapters.LeagueStepsItemAdapter;
import com.cutlerdevelopment.fitnessgoals.ViewAdapters.SpinnerItemAdapter;
import com.cutlerdevelopment.fitnessgoals.ViewItems.ColourDisplaySmallCard;
import com.cutlerdevelopment.fitnessgoals.ViewItems.LeagueStepsItem;
import com.google.android.material.textfield.TextInputLayout;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.List;

import io.opencensus.tags.propagation.TagContextBinarySerializer;

public class Settings extends DialogFragment {

    private List<JamesStep> jamesSteps;
    int currentStepMode;
    ColourDisplaySmallCard currentColourItem;
    private LeagueStepsItemAdapter adapter;

    private TextInputLayout teamNameInput;
    private Button switchStepMode;
    private Button whatIsStepMode;
    private Spinner targetSpinner;
    private Spinner daysBetweenSpinner;
    private ListView leagueStepsList;

    public interface SettingsListener {
        public void settingsChanged(boolean refreshMenu);
    }
    SettingsListener listener;

    @Override public void onAttach(Context context) {
        super.onAttach(context);
        listener = (SettingsListener) context;
    }
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = requireActivity().getLayoutInflater();
        View settingsView = inflater.inflate(R.layout.settings, null);

        ConstraintLayout background = settingsView.findViewById(R.id.settingsBackground);
        TextView title = settingsView.findViewById(R.id.settingsTitle);
        Button closeButton = settingsView.findViewById(R.id.settingsCloseButton);

        TextView teamHeader = settingsView.findViewById(R.id.settingsTeamHeader);
        final LinearLayout teamSettings = settingsView.findViewById(R.id.settingsTeamParent);
        TextView teamIntro = settingsView.findViewById(R.id.settingsTeamIntro);
        TextView teamNameHeader = settingsView.findViewById(R.id.settingsTeamNameHeader);
        teamNameInput = settingsView.findViewById(R.id.settingsTeamNameInputText);
        TextView teamColourHeader = settingsView.findViewById(R.id.settingsTeamColourHeader);
        GridView teamColourGrid = settingsView.findViewById(R.id.settingsTeamColourGrid);

        TextView gameHeader = settingsView.findViewById(R.id.settingsGameHeader);
        final LinearLayout gameSettings = settingsView.findViewById(R.id.settingsGameParent);
        TextView gameIntro = settingsView.findViewById(R.id.settingsGameIntro);
        switchStepMode = settingsView.findViewById(R.id.settingsSwitchStepMode);
        whatIsStepMode = settingsView.findViewById(R.id.settingsWhatIsStepMode);
        TextView stepTargetHeader = settingsView.findViewById(R.id.settingsStepTargetHeader);
        targetSpinner = settingsView.findViewById(R.id.settingsStepTargetSpinner);
        TextView target000s = settingsView.findViewById(R.id.settingsStep000sString);
        TextView daysBetweenHeader = settingsView.findViewById(R.id.settingsDaysBetweenHeader);
        daysBetweenSpinner = settingsView.findViewById(R.id.settingsDaysBetweenSpinner);

        TextView numbersHeader = settingsView.findViewById(R.id.settingsNumberHeader);
        final LinearLayout numberSettings = settingsView.findViewById(R.id.settingsNumberParent);
        TextView numbersIntro = settingsView.findViewById(R.id.settingsNumberIntro);
        TextView leagueStepsHeader = settingsView.findViewById(R.id.settingsLeagueNumberHeader);
        leagueStepsList = settingsView.findViewById(R.id.settingsLeagueStepsList);

        Button saveButton = settingsView.findViewById(R.id.settingsSaveButton);

        int primaryColour =Colours.getUsersPrimaryColour();
        int secondaryColour =Colours.getUsersSecondaryColour();

        background.setBackgroundColor(primaryColour);
        title.setTextColor(secondaryColour);
        closeButton.setTextColor(secondaryColour);

        teamHeader.setTextColor(secondaryColour);
        teamIntro.setTextColor(secondaryColour);
        teamNameHeader.setTextColor(secondaryColour);
        teamNameInput.getEditText().setTextColor(secondaryColour);
        teamNameInput.getEditText().setText(GameData.getInstance().getUsersTeam().getName());
        teamColourHeader.setTextColor(secondaryColour);

        gameHeader.setTextColor(secondaryColour);
        gameIntro.setTextColor(secondaryColour);
        switchStepMode.setBackgroundColor(secondaryColour);
        switchStepMode.setTextColor(primaryColour);
        whatIsStepMode.setTextColor(secondaryColour);
        stepTargetHeader.setTextColor(secondaryColour);
        targetSpinner.setBackgroundColor(primaryColour);

        target000s.setTextColor(secondaryColour);
        daysBetweenHeader.setTextColor(secondaryColour);
        daysBetweenSpinner.setBackgroundColor(primaryColour);

        numbersHeader.setTextColor(secondaryColour);
        numbersIntro.setTextColor(secondaryColour);
        leagueStepsHeader.setTextColor(secondaryColour);


        saveButton.setBackgroundColor(secondaryColour);
        saveButton.setTextColor(primaryColour);

        teamSettings.setVisibility(View.GONE);
        teamHeader.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                expandCollapseSettings(teamSettings);
            }
        });
        gameSettings.setVisibility(View.GONE);
        gameHeader.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                expandCollapseSettings(gameSettings);
            }
        });
        numberSettings.setVisibility(View.GONE);
        numbersHeader.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                expandCollapseSettings(numberSettings);
            }
        });

        populateTeamColourGrid(teamColourGrid);

        currentStepMode = AppData.getInstance().getStepMode();
        updateStepModeViews();
        switchStepMode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                updateCurrentStepMode();
                updateStepModeViews();
            }
        });
        whatIsStepMode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                whatIsStepModePressed();
            }
        });
        populateStepTargetSpinner();
        populateDaysBetweenSpinner();
        populateLeaguesStepTarget();

        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                confirmSettings();
            }
        });



        closeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dismiss();
            }
        });


        builder.setView(settingsView);
        return builder.create();
    }

    void expandCollapseSettings(View settings) {
        if (settings.getVisibility() == View.GONE) {
            settings.setVisibility(View.VISIBLE);

        }
        else {
            settings.setVisibility(View.GONE);
        }

    }

    void populateTeamColourGrid(GridView colourGrid) {

        String usersTeamColour = GameData.getInstance().getUsersTeam().getPrimaryColour();
        final ArrayList<ColourDisplaySmallCard> colourItems = new ArrayList<>();
        for (String c : Colours.getAllTeamColours()) {
            ColourDisplaySmallCard item = new ColourDisplaySmallCard();
            item.setTeamColour(c);
            if (c.equals(usersTeamColour)) {
                item.setUsersColour(true);
                currentColourItem = item;
            }
            else {
                item.setUsersColour(false);
            }
            colourItems.add(item);
        }


        final ColourDisplaySmallCardAdapter adapter = new ColourDisplaySmallCardAdapter(getContext(), colourItems);
        colourGrid.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                ColourDisplaySmallCard item = (ColourDisplaySmallCard) adapterView.getItemAtPosition(i);
                item.setUsersColour(true);


                if (currentColourItem != null && currentColourItem != item) {
                    currentColourItem.setUsersColour(false);
                }

                currentColourItem = item;
                adapter.notifyDataSetChanged();
            }
        });
        colourGrid.setAdapter(adapter);
        colourGrid.setNumColumns(4);

    }

    void updateCurrentStepMode() {
        if (currentStepMode == StepModes.TARGETED_MODE) { currentStepMode = StepModes.SCALED_MODE; }
        else { currentStepMode = StepModes.TARGETED_MODE; }
    }

    void updateStepModeViews() {
        if (currentStepMode == StepModes.TARGETED_MODE) {
            switchStepMode.setText(getString(R.string.settings_switch_to_scaled_mode));
            whatIsStepMode.setText(getString(R.string.what_is_scaled_mode));
        }
        else {
            switchStepMode.setText(getString(R.string.settings_switch_to_targeted_mode));
            whatIsStepMode.setText(getString(R.string.what_is_target_mode));

        }

    }


    void whatIsStepModePressed() {
        jamesSteps = new ArrayList<>();
        if (currentStepMode == StepModes.TARGETED_MODE) {
            jamesSteps.add(new JamesStep(getString(R.string.scaled_mode_details)));
        }
        else {
            jamesSteps.add(new JamesStep(getString(R.string.target_mode_details)));
        }
        JamesTutorial james = new JamesTutorial(jamesSteps);
        james.show(getFragmentManager(), null);
    }

    void populateStepTargetSpinner() {
        ArrayList<String> spinnerList = new ArrayList<>();
        for (int i = 1; i <= Numbers.MAX_NUM_STEPS_TARGET; i++) {
            spinnerList.add(String.valueOf(i));
        }

        SpinnerItemAdapter stepAdapter = new SpinnerItemAdapter(
                getContext(), spinnerList);

        targetSpinner.setAdapter(stepAdapter);
        int chosenSteps = AppData.getInstance().getStepTarget() / 1000;
        targetSpinner.setSelection(chosenSteps - 1);
    }

    void populateDaysBetweenSpinner() {
        ArrayList<String> spinnerList = new ArrayList<>();
        for (int i = 2; i <= Numbers.MAX_NUM_DAYS_BETWEEN; i+=2) {
            spinnerList.add(String.valueOf(i));
        }
        SpinnerItemAdapter daysAdapter = new SpinnerItemAdapter(
                getContext(), spinnerList);
        daysBetweenSpinner.setAdapter(daysAdapter);
        daysBetweenSpinner.setSelection(spinnerList.indexOf(String.valueOf(GameData.getInstance().getDaysBetween())));
    }

    void populateLeaguesStepTarget() {

        final ArrayList<LeagueStepsItem> leagueItems = new ArrayList<>();

        for (int i = Leagues.TOP_LEAGUE; i <= Leagues.BOTTOM_LEAGUE; i++) {

            LeagueStepsItem item = new LeagueStepsItem();
            item.setLeagueName(Leagues.getLeagueName(i));
            item.setLeagueSteps(StringHelper.getNumberWithCommas(Leagues.getAverageStepsInLeague(i)));
            leagueItems.add(item);
        }


        adapter = new LeagueStepsItemAdapter(getContext(), leagueItems);
        leagueStepsList.setAdapter(adapter);
    }

    public void confirmSettings() {
        boolean refreshMainMenu = false;
        Team usersTeam = GameData.getInstance().getUsersTeam();
        String typedTeamName = teamNameInput.getEditText().getText().toString();
        if (!typedTeamName.equals(usersTeam.getName())) {
            usersTeam.changeName(typedTeamName);
            refreshMainMenu = true;
        }
        if (!currentColourItem.getTeamColour().equals(usersTeam.getPrimaryColour())) {
            usersTeam.changePrimaryColour(currentColourItem.getTeamColour());
            usersTeam.changeSecondaryColour(Colours.getSecondaryColour(currentColourItem.getTeamColour()));
            refreshMainMenu = true;
        }
        if (currentStepMode != AppData.getInstance().getStepMode()) {
            AppData.getInstance().changeStepMode(currentStepMode);
        }
        int target = Integer.parseInt(String.valueOf(targetSpinner.getSelectedItem()));
        if (target != AppData.getInstance().getStepTarget() / 1000) {
            AppData.getInstance().changeStepTarget(target * 1000);
        }

        int days = Integer.parseInt(String.valueOf(daysBetweenSpinner.getSelectedItem()));
        if (days != GameData.getInstance().getDaysBetween()) {
            GameData.getInstance().changeDaysBetween(days);
            refreshMainMenu = true;
        }


        for (int i = Leagues.TOP_LEAGUE; i <= Leagues.BOTTOM_LEAGUE; i++) {
            int leagueAverage = Leagues.getAverageStepsInLeague(i);

            LeagueStepsItem leagueItem = (LeagueStepsItem) adapter.getItem(i - 1);
            int newAverage = Integer.parseInt(StringHelper.removeCommaFromString(leagueItem.getLeagueSteps()));
            if (newAverage != leagueAverage) {
                updateLeagueAverage(i, leagueAverage, newAverage);
            }
        }


        listener.settingsChanged(refreshMainMenu);
        dismiss();
    }

    void updateLeagueAverage(int league, int currAverage, int newAverage) {
        int difference = newAverage - currAverage;
        for (Team t : GameDBHandler.getInstance().getAllTeamsInLeague(league)) {
            t.changeMinNumberOfSteps(difference);
            t.changeMaxNumberOfSteps(difference);
            int stepsToAdd = difference * GameData.getInstance().getDaysBetweenSeasonStartAndNextFixture();
            t.addAttackingSteps(stepsToAdd);
            t.addDefendingSteps(stepsToAdd);
        }
    }

}
