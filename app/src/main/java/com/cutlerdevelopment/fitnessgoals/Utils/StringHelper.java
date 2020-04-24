package com.cutlerdevelopment.fitnessgoals.Utils;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;

public class StringHelper {

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


    public static String getNumberWithCommas(int num) {
        DecimalFormat df = new DecimalFormat();
        DecimalFormatSymbols dfs = new DecimalFormatSymbols();
        dfs.setGroupingSeparator(',');
        df.setDecimalFormatSymbols(dfs);
        return df.format(num);
    }

    public static String removeCommaFromString(String number) {
        return number.replaceAll(",","");
    }
}
