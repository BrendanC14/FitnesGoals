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

import com.cutlerdevelopment.fitnessgoals.Constants.BadgeCategories;
import com.cutlerdevelopment.fitnessgoals.Constants.Words;
import com.cutlerdevelopment.fitnessgoals.Models.Badge;
import com.cutlerdevelopment.fitnessgoals.Data.AppData;
import com.cutlerdevelopment.fitnessgoals.Data.UserActivity;
import com.cutlerdevelopment.fitnessgoals.Utils.Converters;
import com.cutlerdevelopment.fitnessgoals.Utils.DateHelper;

import java.util.Arrays;
import java.util.Date;
import java.util.List;

public class AppDBHandler {

    public static void createAppDBHandler(Context c) {
        new AppDBHandler(c);
    }
    private static AppDBHandler instance = null;

    public static AppDBHandler getInstance() {
        if (instance != null) {
            return instance;
        }
        return null;
    }

    private AppDBHandler(Context context) {
        instance = this;
        //TODO: stop allowing Main Thread Queries and figure out a better way to load a game
        db = Room.databaseBuilder(context, AppDatabase.class, Words.APPSETTINGS_SAVED_DATA_DB_NAME).allowMainThreadQueries().build();
    }

    /**
     * App Database class is the Room Database that contains the instances of the Dao interfaces.
     */
    @Database(entities = {AppData.class, UserActivity.class, Badge.class}, version = 1)
    @TypeConverters({Converters.class})
    public static abstract class AppDatabase extends RoomDatabase {
        public abstract AppDataDao dataDao();
        public abstract UserDao userDao();
        public abstract BadgeDao badgeDao();
    }
    public void saveObject(Object obj) {
        if (obj instanceof AppData) {
            AppData data = (AppData) obj;
            db.dataDao().insertData(data);
        }
        else if (obj instanceof UserActivity) {
            UserActivity act = (UserActivity) obj;
            db.userDao().insertUserActivity(act);
        }
        else if (obj instanceof Badge) {
            Badge badge = (Badge) obj;
            db.badgeDao().insertData(badge);
        }
    }

    public void updateObject(Object obj) {
        if (obj instanceof AppData) {
            AppData data = (AppData) obj;
            db.dataDao().updateData(data);
        }
        else if (obj instanceof UserActivity) {
            UserActivity act = (UserActivity) obj;
            db.userDao().updateUserActivity(act);
        }
        else if (obj instanceof Badge) {
            Badge badge = (Badge) obj;
            db.badgeDao().updateData(badge);
        }
    }


    public void deleteObject(AppData data) {

    }

    public AppData loadSettings() {
        if (db.dataDao().selectAllData().length > 0) {
            return Arrays.asList(db.dataDao().selectAllData()).get(0);
        }
        return null;
    }
    public List<AppData> getAllData() {
        return Arrays.asList(db.dataDao().selectAllData());
    }
    public List<UserActivity> getAllActivities() {
        return Arrays.asList(db.userDao().selectAllUserActivity());
    }
    public UserActivity getActivityOnDate(Date date) {
        return db.userDao().selectUserActivityWithDate(date, (DateHelper.addDays(date, 1)));
    }
    public List<UserActivity> getActivityOnAllDates(Date startDate, Date endDate) {
        return Arrays.asList(db.userDao().selectUserActivityBetweenDates(startDate, endDate));
    }
    public UserActivity getLastAddedActivity() {
        return db.userDao().selectLastAddedActivity();
    }
    public UserActivity getFirstAddedActivity() {
        return db.userDao().selectFirstAddedActivity();
    }

    public List<Badge> getAllBadges() { return Arrays.asList(db.badgeDao().selectAllBadges()); }
    public List<Badge> getBadgesWhereUnlockedIs(boolean unlocked) { return Arrays.asList(db.badgeDao().selectBadgesWhereUnlocked(unlocked)); }
    public Badge getNextLockedBadgeIn(int category) {
        return Arrays.asList(db.badgeDao().selectAllBadgesWhereUnlockedFromCategory(false, category)).get(0);
    }

    public List<Badge> getDailyStepBadgesWhereUnlockedIs(boolean unlocked) {
        return Arrays.asList(db.badgeDao().selectAllBadgesWhereUnlockedFromCategory(unlocked, BadgeCategories.DAILY_STEP));
    }
    public List<Badge> getMatchBadgesWhereUnlockedIs(boolean unlocked) {
        return Arrays.asList(db.badgeDao().selectAllBadgesWhereUnlockedFromCategory(unlocked, BadgeCategories.GAMES));
    }
    public List<Badge> getTargetBadgesWhereUnlockedIs(boolean unlocked) {
        return Arrays.asList(db.badgeDao().selectAllBadgesWhereUnlockedFromCategory(unlocked, BadgeCategories.TARGET));
    }
    public List<Badge> getTotalStepBadgesWhereUnlockedIs(boolean unlocked) {
        return Arrays.asList(db.badgeDao().selectAllBadgesWhereUnlockedFromCategory(unlocked, BadgeCategories.TOTAL_STEP));
    }
    public List<Badge> getWinBadgesWhereUnlockedIs(boolean unlocked) {
        return Arrays.asList(db.badgeDao().selectAllBadgesWhereUnlockedFromCategory(unlocked, BadgeCategories.WINS));
    }
    public List<Badge> getUnbeatenBadgesWhereUnlockedIs(boolean unlocked) {
        return Arrays.asList(db.badgeDao().selectAllBadgesWhereUnlockedFromCategory(unlocked, BadgeCategories.UNBEATEN));
    }
    public List<Badge> getGoalBadgesWhereUnlockedIs(boolean unlocked) {
        return Arrays.asList(db.badgeDao().selectAllBadgesWhereUnlockedFromCategory(unlocked, BadgeCategories.GOALS));
    }


    private static AppDatabase db;

    @Dao
    public interface AppDataDao {

        @Insert(onConflict = OnConflictStrategy.REPLACE)
        void insertData(AppData data);
        @Update
        void updateData(AppData data);
        @Delete
        void deleteData(AppData data);

        @Query("SELECT * FROM AppData")
        AppData[] selectAllData();
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
        @Query("SELECT * FROM UserActivity ORDER BY date ASC LIMIT 1")
        UserActivity selectFirstAddedActivity();
        @Query("SELECT * FROM UserActivity WHERE date BETWEEN :date1 AND :date2")
        UserActivity[] selectUserActivityBetweenDates(Date date1, Date date2);
    }

    @Dao public interface BadgeDao {
        @Insert(onConflict = OnConflictStrategy.REPLACE)
        void insertData(Badge badge);
        @Update
        void updateData(Badge badge);
        @Delete
        void deleteData(Badge badge);

        @Query("SELECT * FROM Badge")
        Badge[] selectAllBadges();
        @Query("SELECT * FROM Badge WHERE unlocked = :unlocked")
        Badge[] selectBadgesWhereUnlocked(boolean unlocked);
        @Query("SELECT * FROM badge WHERE unlocked = :unlocked AND category = :category ORDER BY target ASC")
        Badge[] selectAllBadgesWhereUnlockedFromCategory(boolean unlocked, int category);
    }


    public void resetDB() {
        db.clearAllTables();
    }
}
