package com.cutlerdevelopment.fitnessgoals.SavedData;

import android.content.Context;

import androidx.room.Dao;
import androidx.room.Database;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.room.TypeConverters;
import androidx.room.Update;

import com.cutlerdevelopment.fitnessgoals.Constants.Words;
import com.cutlerdevelopment.fitnessgoals.Models.Fixture;
import com.cutlerdevelopment.fitnessgoals.Models.Team;
import com.cutlerdevelopment.fitnessgoals.Data.GameData;
import com.cutlerdevelopment.fitnessgoals.Utils.Converters;

import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.List;

public class GameDBHandler {

    public static void createGameDBHandler(Context c) { new GameDBHandler(c); }
    private static GameDBHandler instance = null;


    public static GameDBHandler getInstance() {
        if (instance != null) {
            return  instance;
        }
        return null;
    }

    private GameDBHandler(Context context) {
        instance = this;
        //TODO: stop allowing Main Thread Queries and figure out a better way to load a game
        db = Room.databaseBuilder(context, AppDatabase.class, Words.CAREER_SETTINGS_SAVED_DATA_DB_NAME).allowMainThreadQueries().build();

    }
    private static AppDatabase db;
    @Database(entities = {Team.class, Fixture.class, GameData.class}, version = 1)
    @TypeConverters({Converters.class})
    public static abstract class AppDatabase extends RoomDatabase {
        public abstract GameDataDao dataDao();
        public abstract TeamDao teamDao();
        public abstract FixtureDao fixtureDao();
    }

    public void saveObject(Object obj) {
        if (obj instanceof  Team) {
            Team team = (Team) obj;
            db.teamDao().insertTeams(team);
        }
        else if (obj instanceof Fixture) {
            Fixture fix = (Fixture) obj;
            db.fixtureDao().insertFixtures(fix);
        }
        else if (obj instanceof GameData) {
            GameData settings = (GameData) obj;
            db.dataDao().insertSettings(settings);
        }
    }

    public void updateObject(Object obj) {
        if (obj instanceof Team) {
            Team team = (Team) obj;
            db.teamDao().updateTeam(team);
        }
        else if (obj instanceof Fixture) {
            Fixture fix = (Fixture) obj;
            db.fixtureDao().updateFixture(fix);
        }
        else if (obj instanceof GameData) {
            GameData settings = (GameData) obj;
            db.dataDao().updateSettings(settings);
        }
    }


    public GameData loadSettings() {
        GameData settings[] = db.dataDao().selectAllData();
        if (settings.length >0) {
            return settings[0];
        }
        return null;
    }

    public List<Team> getAllTeams() { return Arrays.asList(db.teamDao().selectAllTeams()); }
    public List<Team> getAllTeamsInLeague(int league) { return Arrays.asList(db.teamDao().selectAllTeamsInLeague(league));}
    public Team getTeamFromID(int ID) { return db.teamDao().selectTeamFromID(ID); }
    public Team getTeamFromName(String name) { return db.teamDao().selectTeamFromName(name); }
    public int getIDFromName(String name) {
        return db.teamDao().selectTeamFromName(name).getID();
    }
    public int getNumRowsFromTeamTable() {
        return db.teamDao().getRowCount();
    }


    public List<Fixture> getAllFixtures() { return Arrays.asList(db.fixtureDao().selectAllFixtures());}
    public Fixture getFixtureFromID(int ID) {return db.fixtureDao().selectFixtureFromID(ID); }
    public List<Fixture> getWeeksFixtureFromLeague(Date date, int league) { return Arrays.asList(db.fixtureDao().selectAllFixturesInLeagueFromDate(date, league)); }
    public List<Fixture> getWeeksResultsFromLeague(Date date, int league) { return Arrays.asList(db.fixtureDao().selectAllResultsInLeagueFromDate(date, league)); }
    public List<Fixture> getFixturesForTeam(int teamID) { return Arrays.asList(db.fixtureDao().selectAllFixturesFromTeam(teamID)); }
    public Fixture getTeamsFixtureForWeek(int week, int teamID) { return db.fixtureDao().selectFixtureForWeekWithTeam(week, teamID); }
    public int getNumRowsFromFixtureTable() { return db.fixtureDao().getRowCount(); }
    public List<Fixture> getAllUnplayedFixtures() { return Arrays.asList(db.fixtureDao().selectAllUnplayedFixtures()); }
    public List<Fixture> getAllUpcomingFixturesForTeam(int teamID) { return Arrays.asList(db.fixtureDao().selectAllUpcomingFixturesFromTeam(teamID)); }
    public List<Fixture> getAllResultsForTeam(int teamID) { return Arrays.asList(db.fixtureDao().selectAllResultsFromTeam(teamID)); }
    public Fixture getLastResultForTeam(int teamID) {
        List<Fixture> results = Arrays.asList(db.fixtureDao().selectAllResultsFromTeam(teamID));
        if (results.size() > 0) {
            return results.get(results.size() - 1);
        }
        return null;
    }
    public Fixture getNextFixtureForTeam(int teamID) {
        List<Fixture> fixes = Arrays.asList(db.fixtureDao().selectAllUpcomingFixturesFromTeam(teamID));
        Collections.sort(fixes);

        if (fixes.size() > 0) {
            return fixes.get(0);
        }
        return null;
    }
    public void removeAllFixtures() { db.fixtureDao().nukeTable();}

    @Dao
    public interface GameDataDao {

        @Insert(onConflict = OnConflictStrategy.REPLACE)
        void insertSettings(GameData settings);
        @Update
        void updateSettings(GameData settings);
        @Delete
        void deleteSettings(GameData settings);

        @Query("SELECT * FROM GameData")
        GameData[] selectAllData();

    }

    @Dao
    interface TeamDao {
        @Insert(onConflict = OnConflictStrategy.REPLACE)
        void insertTeams(Team teams);
        @Update
        void updateTeam(Team teams);
        @Delete
        void deleteTeam(Team teams);

        @Query("SELECT * FROM Team")
        Team[] selectAllTeams();
        @Query("SELECT * FROM Team WHERE league = :league")
        Team[] selectAllTeamsInLeague(int league);
        @Query("SELECT * FROM Team WHERE id = :teamID")
        Team selectTeamFromID(int teamID);
        @Query("SELECT * FROM Team WHERE name = :teamName")
        Team selectTeamFromName(String teamName);
        @Query("SELECT COUNT(id) FROM Team")
        int getRowCount();

    }


    @Dao
    interface  FixtureDao {
        @Insert(onConflict = OnConflictStrategy.REPLACE)
        void insertFixtures(Fixture fixture);
        @Update
        void updateFixture(Fixture fixture);
        @Delete
        void deleteFixture(Fixture fixture);

        @Query("SELECT * FROM Fixture")
        Fixture[] selectAllFixtures();
        @Query("SELECT * FROM Fixture WHERE id = :fixtureID")
        Fixture selectFixtureFromID(int fixtureID);
        @Query("SELECT * FROM Fixture WHERE homeScore = -1 AND date = :date AND league = :league")
        Fixture[] selectAllFixturesInLeagueFromDate(Date date, int league);
        @Query("SELECT * FROM Fixture WHERE homeScore = -1")
        Fixture[] selectAllUnplayedFixtures();
        @Query("SELECT * FROM Fixture WHERE homeScore > -1 AND date = :date AND league = :league")
        Fixture[] selectAllResultsInLeagueFromDate(Date date, int league);
        @Query("SELECT * FROM Fixture WHERE homeTeamID = :teamID OR awayTeamID = :teamID")
        Fixture[] selectAllFixturesFromTeam(int teamID);
        @Query("SELECT * FROM Fixture WHERE homeScore = -1 AND (homeTeamID = :teamID OR awayTeamID = :teamID)")
        Fixture[] selectAllUpcomingFixturesFromTeam(int teamID);
        @Query("SELECT * FROM Fixture WHERE homeScore > -1 AND (homeTeamID = :teamID OR awayTeamID = :teamID) ORDER BY date")
        Fixture[] selectAllResultsFromTeam(int teamID);
        @Query("SELECT * FROM Fixture WHERE week = :week AND (homeTeamID = :teamID OR awayTeamID = :teamID)")
        Fixture selectFixtureForWeekWithTeam(int week, int teamID);
        @Query("SELECT COUNT(id) FROM Fixture")
        int getRowCount();
        @Query("DELETE FROM Fixture")
        void nukeTable();
    }



    public void resetDB() {
        db.clearAllTables();
    }

}
