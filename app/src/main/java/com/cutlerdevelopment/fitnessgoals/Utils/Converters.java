package com.cutlerdevelopment.fitnessgoals.Utils;

import androidx.room.TypeConverter;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class Converters {
    @TypeConverter
    public static Date fromTimestamp(Long value) {
        return value == null ? null : new Date(value);
    }
    @TypeConverter
    public static Long dateToTimestamp(Date date) {
        return date ==null ? null : date.getTime();
    }

    @TypeConverter
    public static String fromArrayList(List<Integer> list) {
        List<String> stringList = new ArrayList<>();
        for (Integer i : list) {
            stringList.add(String.valueOf(i));
        }
        Gson gson = new Gson();
        return gson.toJson(stringList);
    }

    @TypeConverter
    public static List<Integer> fromString(String value) {
        List<Integer> intList = new ArrayList<>();
        List<String> stringList = new Gson().fromJson(value, new TypeToken<List<String>>() {}.getType());
        for (String s : stringList) {
            intList.add(Integer.parseInt(s));
        }
        return intList;
    }
}
