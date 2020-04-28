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
import com.cutlerdevelopment.fitnessgoals.Settings.AppSettings;
import com.cutlerdevelopment.fitnessgoals.Settings.UserActivity;
import com.cutlerdevelopment.fitnessgoals.Utils.Converters;
import com.cutlerdevelopment.fitnessgoals.Utils.DateHelper;

import java.util.Arrays;
import java.util.Date;
import java.util.List;

public class AppSavedData {

    public static void createAppSavedData(Context c) {
        new AppSavedData(c);
    }
    private static AppSavedData instance = null;

    public static AppSavedData getInstance() {
        if (instance != null) {
            return instance;
        }
        return null;
    }

    private AppSavedData(Context context) {
        instance = this;
        //TODO: stop allowing Main Thread Queries and figure out a better way to load a game
        db = Room.databaseBuilder(context, AppDatabase.class, Words.APPSETTINGS_SAVED_DATA_DB_NAME).allowMainThreadQueries().build();
    }

    /**
     * App Database class is the Room Database that contains the instances of the Dao interfaces.
     */
    @Database(entities = {AppSettings.class, UserActivity.class}, version = 1)
    @TypeConverters({Converters.class})
    public static abstract class AppDatabase extends RoomDatabase {
        public abstract SettingsDao settingsDao();
        public abstract UserDao userActivityDao();
    }
    public void saveObject(Object obj) {
        if (obj instanceof AppSettings) {
            AppSettings data = (AppSettings) obj;
            db.settingsDao().insertData(data);
        }
        else if (obj instanceof UserActivity) {
            UserActivity act = (UserActivity) obj;
            db.userActivityDao().insertUserActivity(act);
        }
    }

    public void updateObject(Object obj) {
        if (obj instanceof AppSettings) {
            AppSettings data = (AppSettings) obj;
            db.settingsDao().updateData(data);
        }
        else if (obj instanceof UserActivity) {
            UserActivity act = (UserActivity) obj;
            db.userActivityDao().updateUserActivity(act);
        }
    }


    public void deleteObject(AppSettings data) {

    }

    public AppSettings loadSettings() {
        if (db.settingsDao().selectAllData().length > 0) {
            return Arrays.asList(db.settingsDao().selectAllData()).get(0);
        }
        return null;
    }
    public List<AppSettings> getAllData() {
        return Arrays.asList(db.settingsDao().selectAllData());
    }
    public List<UserActivity> getAllActivities() {
        return Arrays.asList(db.userActivityDao().selectAllUserActivity());
    }
    public UserActivity getActivityOnDate(Date date) {
        return db.userActivityDao().selectUserActivityWithDate(date, (DateHelper.addDays(date, 1)));
    }
    public List<UserActivity> getActivityOnAllDates(Date startDate, Date endDate) {
        return Arrays.asList(db.userActivityDao().selectUserActivityBetweenDates(startDate, endDate));
    }
    public UserActivity getLastAddedActivity() {
        return db.userActivityDao().selectLastAddedActivity();
    }


    private static AppDatabase db;

    @Dao
    public interface SettingsDao {

        @Insert(onConflict = OnConflictStrategy.REPLACE)
        void insertData(AppSettings data);
        @Update
        void updateData(AppSettings data);
        @Delete
        void deleteData(AppSettings data);

        @Query("SELECT * FROM AppSettings")
        AppSettings[] selectAllData();
    }

    @Dao public interface UserDao {
        @Insert(onConflict = OnConflictStrategy.REPLACE)
        void insertUserActivity(UserActivity activity);
        @Update
        void updateUserActivity(UserActivity activity);
        @Delete
        void deleteUserActivity(UserActivity activity);

        @Query("SELECT * FROM UserActivity")
        UserActivity[] selectAllUserActivity();
        @Query("SELECT * FROM UserActivity WHERE date BETWEEN :date AND :date2")
        UserActivity selectUserActivityWithDate(Date date, Date date2);
        @Query("SELECT * FROM UserActivity ORDER BY date DESC LIMIT 1")
        UserActivity selectLastAddedActivity();
        @Query("SELECT * FROM UserActivity WHERE date BETWEEN :date1 AND :date2")
        UserActivity[] selectUserActivityBetweenDates(Date date1, Date date2);
    }


    public void resetDB() {
        db.clearAllTables();
    }
}
