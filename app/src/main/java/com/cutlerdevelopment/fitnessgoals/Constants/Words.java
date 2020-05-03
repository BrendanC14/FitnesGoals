package com.cutlerdevelopment.fitnessgoals.Constants;

import com.cutlerdevelopment.fitnessgoals.Models.Team;
import com.cutlerdevelopment.fitnessgoals.SavedData.GameDBHandler;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class Words {

    //RoomDB Words
    public static final String FITIBIT_SAVED_DATA_DB_NAME = "FitbitStringsSavedData";
    public static final String APPSETTINGS_SAVED_DATA_DB_NAME = "AppSettingsSavedData";
    public static final String CAREER_SETTINGS_SAVED_DATA_DB_NAME = "CareerSettingsSavedData";

    public static final List<String> firstNames = Arrays.asList("James", "John", "James", "Robert", "Michael", "William", "David",
            "Richard", "Joseph", "Thomas", "Charles", "Christopher", "Daniel", "Matthew", "Anthony", "Donald", "Mark", "Paul", "Steven", "Andrew",
            "Kenneth", "Joshua", "George", "Kevin", "Brian", "Edward", "Ronald", "Timothy", "Jason", "Jeffrey", "Ryan", "Jacob", "Gary",
            "Nicholas", "Eric", "Stephen", "Jonathan", "Larry", "Justin", "Scott", "Brandon", "Frank", "Benjamin", "Gregory", "Samuel",
            "Raymond", "Patrick", "Alexander", "Jack", "Dennis", "Jerry", "Tyler", "Aaron", "Jose", "Henry", "Douglas", "Adam",
            "Peter", "Nathan", "Zachary", "Walter", "Kyle", "Harold", "Carl", "Jeremy", "Keith", "Gerald", "Ethan", "Arthur",
            "Terry", "Christian", "Sean", "Lawrence", "Austin", "Joe", "Noah", "Jesse", "Albert", "Bryan", "Billy", "Bruce",
            "Jordan", "Dylan", "Alan", "Ralph", "Gabriel", "Roy", "Wayne", "Logan", "Randy", "Louis", "Russell", "Vincent",
            "Philip", "Bobby", "Johnny", "Bradley");
    public static final List<String> surnames = Arrays.asList("Smith", "Jones", "Williams", "Taylor", "Brown", "Davies", "Evans", "Wilson",
            "Thomas", "Johnson", "Roberts", "Robinson", "Thompson", "Wright", "Walker", "White", "Edwards", "Hughes", "Green", "Hall", "Lewis",
            "Harris", "Clarke", "Patel", "Griffiths", "Jackson", "Wood", "Turner", "Martin", "Cooper", "Hill", "Ward", "Morris", "Moore",
            "Clark", "Allen", "Davis", "Mitchell", "Carter", "Shaw", "Jones", "Ellis", "Rogers", "Gray", "Holmes", "Mills", "Jenkins",
            "Lee", "James", "Parker", "Kelly", "Richardson", "Murphy", "Simpson", "Adams", "Webb", "Mason", "Palmer", "Barnes", "Stevens",
            "King", "Scott", "Price", "Cook", "Bailey", "Miller", "Anderson", "Singh", "Powell", "Ali", "Owen", "Knight", "Fisher",
            "Baker", "Phillips", "Bennett", "Collins", "Cox", "Marshall", "Begum", "Chapman", "Hunt", "Matthews", "Lloyd", "Barker", "Murray",
            "Harrison", "Watson", "Young", "Bell", "Richards", "Khan", "Wilkinson", "Foster", "Hussain", "Campbell", "Butler", "Russell", "Dixon",
            "Morgan", "Harvey");


    public static List<String> getAllTeamNames() {
        List<String> teamNames = new ArrayList<>();
        for (Team t : GameDBHandler.getInstance().getAllTeams()) {
            teamNames.add(t.getName());
        }

        Collections.sort(teamNames);
        return teamNames;
    }



}
