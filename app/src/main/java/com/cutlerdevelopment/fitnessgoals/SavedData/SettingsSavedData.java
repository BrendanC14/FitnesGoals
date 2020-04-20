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
import androidx.room.Update;

import com.cutlerdevelopment.fitnessgoals.Constants.Words;
import com.cutlerdevelopment.fitnessgoals.Settings.Settings;

import java.util.Arrays;
import java.util.List;

public class SettingsSavedData {

    public static void createSettingsSavedData(Context c) {
        new SettingsSavedData(c);
    }
    private static SettingsSavedData instance = null;

    public static SettingsSavedData getInstance() {
        if (instance != null) {
            return instance;
        }
        return null;
    }

    private SettingsSavedData(Context context) {
        instance = this;
        //TODO: stop allowing Main Thread Queries and figure out a better way to load a game
        db = Room.databaseBuilder(context, AppDatabase.class, Words.APPSETTINGS_SAVED_DATA_DB_NAME).allowMainThreadQueries().build();
    }

    /**
     * App Database class is the Room Database that contains the instances of the Dao interfaces.
     */
    @Database(entities = {Settings.class}, version = 1)
    public static abstract class AppDatabase extends RoomDatabase {
        public abstract MyDao getDao();
    }

    private static AppDatabase db;

    @Dao
    public interface MyDao {

        @Insert(onConflict = OnConflictStrategy.REPLACE)
        void insertData(Settings data);
        @Update
        void updateData(Settings data);
        @Delete
        void deleteData(Settings data);

        @Query("SELECT * FROM FitbitStrings")
        Settings[] selectAllData();
    }

    public void saveObject(Settings data) {
        db.getDao().insertData(data);
    }

    public void updateObject(Settings data) {
        db.getDao().updateData(data);
    }


    public void deleteObject(Settings data) {
        db.getDao().deleteData(data);
    }

    public Settings getTopData() {
        if (db.getDao().selectAllData().length > 0) {
            return Arrays.asList(db.getDao().selectAllData()).get(0);
        }
        return null;
    }
    public List<Settings> getAllData() {
        return Arrays.asList(db.getDao().selectAllData());
    }
    public void resetDB() {
        db.clearAllTables();
    }
}
