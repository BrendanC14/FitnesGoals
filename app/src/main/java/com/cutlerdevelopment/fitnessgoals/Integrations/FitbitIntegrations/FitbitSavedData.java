package com.cutlerdevelopment.fitnessgoals.Integrations.FitbitIntegrations;

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

import java.util.Arrays;
import java.util.List;

public class FitbitSavedData {

    public static void createFitbitSavedDataInstance(Context c) {
        new FitbitSavedData(c);
    }
    private static FitbitSavedData instance = null;

    public static FitbitSavedData getInstance() {
        if (instance != null) {
            return instance;
        }
        return null;
    }

    private FitbitSavedData(Context context) {
        instance = this;
        //TODO: stop allowing Main Thread Queries and figure out a better way to load a game
        db = Room.databaseBuilder(context, AppDatabase.class, Words.FITIBIT_SAVED_DATA_DB_NAME).allowMainThreadQueries().build();
    }

    /**
     * App Database class is the Room Database that contains the instances of the Dao interfaces.
     */
    @Database(entities = {FitbitStrings.class}, version = 1)
    public static abstract class AppDatabase extends RoomDatabase {
        public abstract MyDao getDao();
    }

    private static AppDatabase db;

    @Dao
    public interface MyDao {

        @Insert(onConflict = OnConflictStrategy.REPLACE)
        void insertData(FitbitStrings data);
        @Update
        void updateData(FitbitStrings data);
        @Delete
        void deleteData(FitbitStrings data);

        @Query("SELECT * FROM FitbitStrings")
        FitbitStrings[] selectAllData();
    }

    public void saveObject(FitbitStrings data) {
        db.getDao().insertData(data);
    }

    public void updateObject(FitbitStrings data) {
        db.getDao().updateData(data);
    }


    public void deleteObject(FitbitStrings data) {
        db.getDao().deleteData(data);
    }

    public FitbitStrings getTopData() {
        if (db.getDao().selectAllData().length > 0) {
            return Arrays.asList(db.getDao().selectAllData()).get(0);
        }
        return null;
    }
    public List<FitbitStrings> getAllData() {
        return Arrays.asList(db.getDao().selectAllData());
    }
    public void resetDB() {
        db.clearAllTables();
    }
}
