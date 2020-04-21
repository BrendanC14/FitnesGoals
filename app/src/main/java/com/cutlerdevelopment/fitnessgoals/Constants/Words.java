package com.cutlerdevelopment.fitnessgoals.Constants;

import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Words {

    //RoomDB Words
    public static final String FITIBIT_SAVED_DATA_DB_NAME = "FitbitStringsSavedData";
    public static final String APPSETTINGS_SAVED_DATA_DB_NAME = "AppSettingsSavedData";
    public static final String CAREER_SETTINGS_SAVED_DATA_DB_NAME = "CareerSettingsSavedData";
    private Map<String, Integer> teamLeagueMap;



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


    public static String getNumberWithCommas(int num) {
        DecimalFormat df = new DecimalFormat();
        DecimalFormatSymbols dfs = new DecimalFormatSymbols();
        dfs.setGroupingSeparator(',');
        df.setDecimalFormatSymbols(dfs);
        return df.format(num);
    }

    public static String getLeagueName(int leagueID) {
        switch (leagueID) {
            case 1: return "Premier League";
            case 2: return "Championship";
            case 3: return "League 1";
            case 4: return "League 2";
            default: return "League";
        }
    }

    public static String getDateInFitbitFormat(Date date) {

        DateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
        return formatter.format(date);
    }

    public static String getNumberWithDateSuffix(int num) {
        if (num >=11 && num <= 13) {
            return num + "th";
        }

        switch (num % 10) {
            case 1: return num + "st";
            case 2: return num +"nd";
            case 3: return num + "rd";
            default: return num + "th";
        }
    }
    public static final List<String> teamNames = Arrays.asList("Arsenal", "Liverpool", "Manchester City", "Leicester City", "Chelsea",
            "Manchester United", "Wolves", "Sheffield United", "Tottenham Hotspur", "Burnley", "Crystal Palace", "Everton",
            "Newcastle United", "Southampton", "Brighton", "West Ham", "Watford", "Bournemouth", "Aston Villa", "Norwich",
            "Leeds", "West Brom", "Fullham", "Brentford", "Nottingham Forest", "Preston North End", "Bristol City", "Milwall", "Cardiff",
            "Blackburn", "Swansea", "Derby County", "QPR", "Reading");

    public Map<String, Integer> getNewTeamLeagueMap() {
        teamLeagueMap = new HashMap<>();
        teamLeagueMap.put("Liverpool", Leagues.PREMIER_LEAGUE);
        teamLeagueMap.put("Manchester City", Leagues.PREMIER_LEAGUE);
        teamLeagueMap.put("Leicester City", Leagues.PREMIER_LEAGUE);
        teamLeagueMap.put("Chelsea", Leagues.PREMIER_LEAGUE);
        teamLeagueMap.put("Manchester United", Leagues.PREMIER_LEAGUE);
        teamLeagueMap.put("Arsenal", Leagues.PREMIER_LEAGUE);
        teamLeagueMap.put("Wolves", Leagues.PREMIER_LEAGUE);
        teamLeagueMap.put("Sheffield United", Leagues.PREMIER_LEAGUE);
        teamLeagueMap.put("Tottenham Hotspur", Leagues.PREMIER_LEAGUE);
        teamLeagueMap.put("Burnley", Leagues.PREMIER_LEAGUE);
        teamLeagueMap.put("Crystal Palace", Leagues.PREMIER_LEAGUE);
        teamLeagueMap.put("Everton", Leagues.PREMIER_LEAGUE);
        teamLeagueMap.put("Newcastle United", Leagues.PREMIER_LEAGUE);
        teamLeagueMap.put("Southampton", Leagues.PREMIER_LEAGUE);
        teamLeagueMap.put("Brighton", Leagues.PREMIER_LEAGUE);
        teamLeagueMap.put("West Ham", Leagues.PREMIER_LEAGUE);
        teamLeagueMap.put("Watford", Leagues.PREMIER_LEAGUE);
        teamLeagueMap.put("Bournemouth", Leagues.PREMIER_LEAGUE);
        teamLeagueMap.put("Aston Villa", Leagues.PREMIER_LEAGUE);
        teamLeagueMap.put("Norwich", Leagues.PREMIER_LEAGUE);

        teamLeagueMap.put("Leeds United", Leagues.CHAMPIONSHIP);
        teamLeagueMap.put("West Bromwich Albion", Leagues.CHAMPIONSHIP);
        teamLeagueMap.put("Fulham", Leagues.CHAMPIONSHIP);
        teamLeagueMap.put("Brentford", Leagues.CHAMPIONSHIP);
        teamLeagueMap.put("Nottingham Forest", Leagues.CHAMPIONSHIP);
        teamLeagueMap.put("Preston North End", Leagues.CHAMPIONSHIP);
        teamLeagueMap.put("Bristol City", Leagues.CHAMPIONSHIP);
        teamLeagueMap.put("Millwall", Leagues.CHAMPIONSHIP);
        teamLeagueMap.put("Cardiff", Leagues.CHAMPIONSHIP);
        teamLeagueMap.put("Blackburn", Leagues.CHAMPIONSHIP);
        teamLeagueMap.put("Swansea", Leagues.CHAMPIONSHIP);
        teamLeagueMap.put("Derby", Leagues.CHAMPIONSHIP);
        teamLeagueMap.put("Queens Park Rangers", Leagues.CHAMPIONSHIP);
        teamLeagueMap.put("Reading", Leagues.CHAMPIONSHIP);
        teamLeagueMap.put("Sheffield Wednesday", Leagues.CHAMPIONSHIP);
        teamLeagueMap.put("Birmingham", Leagues.CHAMPIONSHIP);
        teamLeagueMap.put("Stoke", Leagues.CHAMPIONSHIP);
        teamLeagueMap.put("Huddersfield", Leagues.CHAMPIONSHIP);
        teamLeagueMap.put("Middlesbrough", Leagues.CHAMPIONSHIP);
        teamLeagueMap.put("Wigan", Leagues.CHAMPIONSHIP);
        teamLeagueMap.put("Hull", Leagues.CHAMPIONSHIP);
        teamLeagueMap.put("Charlton", Leagues.CHAMPIONSHIP);
        teamLeagueMap.put("Luton", Leagues.CHAMPIONSHIP);
        teamLeagueMap.put("Barnsley", Leagues.CHAMPIONSHIP);

        teamLeagueMap.put("Coventry", Leagues.LEAGUE1);
        teamLeagueMap.put("Rotherham", Leagues.LEAGUE1);
        teamLeagueMap.put("Oxford United", Leagues.LEAGUE1);
        teamLeagueMap.put("Portsmouth", Leagues.LEAGUE1);
        teamLeagueMap.put("Fleetwood", Leagues.LEAGUE1);
        teamLeagueMap.put("Peterborough", Leagues.LEAGUE1);
        teamLeagueMap.put("Sunderland", Leagues.LEAGUE1);
        teamLeagueMap.put("Wycombe", Leagues.LEAGUE1);
        teamLeagueMap.put("Doncaster", Leagues.LEAGUE1);
        teamLeagueMap.put("Ipswich", Leagues.LEAGUE1);
        teamLeagueMap.put("Gillingham", Leagues.LEAGUE1);
        teamLeagueMap.put("Burton", Leagues.LEAGUE1);
        teamLeagueMap.put("Blackpool", Leagues.LEAGUE1);
        teamLeagueMap.put("Bristol Rovers", Leagues.LEAGUE1);
        teamLeagueMap.put("Lincoln", Leagues.LEAGUE1);
        teamLeagueMap.put("Shrewsbury", Leagues.LEAGUE1);
        teamLeagueMap.put("Accrington Stanley", Leagues.LEAGUE1);
        teamLeagueMap.put("MK Dons", Leagues.LEAGUE1);
        teamLeagueMap.put("Rochdale", Leagues.LEAGUE1);
        teamLeagueMap.put("AFC Wimbledon", Leagues.LEAGUE1);
        teamLeagueMap.put("Tranmere", Leagues.LEAGUE1);
        teamLeagueMap.put("Southend", Leagues.LEAGUE1);
        teamLeagueMap.put("Bolton", Leagues.LEAGUE1);
        teamLeagueMap.put("Bury", Leagues.LEAGUE1);

        teamLeagueMap.put("Crewe", Leagues.LEAGUE2);
        teamLeagueMap.put("Swindon", Leagues.LEAGUE2);
        teamLeagueMap.put("Plymouth", Leagues.LEAGUE2);
        teamLeagueMap.put("Exeter", Leagues.LEAGUE2);
        teamLeagueMap.put("Cheltenham", Leagues.LEAGUE2);
        teamLeagueMap.put("Colchester", Leagues.LEAGUE2);
        teamLeagueMap.put("Northampton", Leagues.LEAGUE2);
        teamLeagueMap.put("Port Vale", Leagues.LEAGUE2);
        teamLeagueMap.put("Bradford", Leagues.LEAGUE2);
        teamLeagueMap.put("Salford", Leagues.LEAGUE2);
        teamLeagueMap.put("Forest Green Rovers", Leagues.LEAGUE2);
        teamLeagueMap.put("Crawley", Leagues.LEAGUE2);
        teamLeagueMap.put("Grimsby", Leagues.LEAGUE2);
        teamLeagueMap.put("Walsall", Leagues.LEAGUE2);
        teamLeagueMap.put("Newport", Leagues.LEAGUE2);
        teamLeagueMap.put("Cambridge", Leagues.LEAGUE2);
        teamLeagueMap.put("Leyton Orient", Leagues.LEAGUE2);
        teamLeagueMap.put("Carlisle", Leagues.LEAGUE2);
        teamLeagueMap.put("Oldham", Leagues.LEAGUE2);
        teamLeagueMap.put("Scunthorpe", Leagues.LEAGUE2);
        teamLeagueMap.put("Mansfield", Leagues.LEAGUE2);
        teamLeagueMap.put("Macclesfield", Leagues.LEAGUE2);
        teamLeagueMap.put("Morecambe", Leagues.LEAGUE2);
        teamLeagueMap.put("Bury", Leagues.LEAGUE2);


        return teamLeagueMap;
    }
}
