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
import com.cutlerdevelopment.fitnessgoals.Settings.AppSettings;

import java.util.Arrays;
import java.util.List;

public class AppSettingsSavedData {

    public static void createAppSettingsSavedData(Context c) {
        new AppSettingsSavedData(c);
    }
    private static AppSettingsSavedData instance = null;

    public static AppSettingsSavedData getInstance() {
        if (instance != null) {
            return instance;
        }
        return null;
    }

    private AppSettingsSavedData(Context context) {
        instance = this;
        //TODO: stop allowing Main Thread Queries and figure out a better way to load a game
        db = Room.databaseBuilder(context, AppDatabase.class, Words.APPSETTINGS_SAVED_DATA_DB_NAME).allowMainThreadQueries().build();
    }

    /**
     * App Database class is the Room Database that contains the instances of the Dao interfaces.
     */
    @Database(entities = {AppSettings.class}, version = 1)
    public static abstract class AppDatabase extends RoomDatabase {
        public abstract MyDao getDao();
    }

    private static AppDatabase db;

    @Dao
    public interface MyDao {

        @Insert(onConflict = OnConflictStrategy.REPLACE)
        void insertData(AppSettings data);
        @Update
        void updateData(AppSettings data);
        @Delete
        void deleteData(AppSettings data);

        @Query("SELECT * FROM AppSettings")
        AppSettings[] selectAllData();
    }

    public void saveObject(AppSettings data) {
        db.getDao().insertData(data);
    }

    public void updateObject(AppSettings data) {
        db.getDao().updateData(data);
    }


    public void deleteObject(AppSettings data) {
        db.getDao().deleteData(data);
    }

    public AppSettings loadSettings() {
        if (db.getDao().selectAllData().length > 0) {
            return Arrays.asList(db.getDao().selectAllData()).get(0);
        }
        return null;
    }
    public List<AppSettings> getAllData() {
        return Arrays.asList(db.getDao().selectAllData());
    }
    public void resetDB() {
        db.clearAllTables();
    }
}
