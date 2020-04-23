package com.cutlerdevelopment.fitnessgoals.Utils;

import android.icu.util.ULocale;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class DateHelper {

    /**
     * Checks the number of days between date1 and date 2.
     * If date1 is before date 2 number will be negative.
     * If date1 is after date 2 number will be positive.
     * @param date1 the date
     * @param date2 the date to compare it to
     * @return an int that tells you the number of days between the two dates
     */
    public static int getDaysBetween(Date date1, Date date2) {
        //TODO: Consider joda time, gotta be better than this
        return (int)( (date1.getTime() - date2.getTime()) / (1000 * 60 * 60 * 24));
        // return date1.compareTo(date2);
    }
    public static Date addDays(Date date, int numDays) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        cal.add(Calendar.DATE, numDays);
        cal.set(Calendar.HOUR_OF_DAY, 0);
        cal.set(Calendar.MINUTE, 0);
        cal.set(Calendar.SECOND, 0);
        cal.set(Calendar.MILLISECOND, 0);
        return cal.getTime();
    }

    public static String formatDateToString(Date date) {
        DateFormat formatter = new SimpleDateFormat("dd/MM/yyyy", Locale.ENGLISH);
        return formatter.format(date);
    }

    public static Date formatStringToDate(String date) throws ParseException {
        DateFormat formatter = new SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH);
        return formatter.parse(date);

    }
}
