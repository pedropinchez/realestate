package com.example.realestate.util;



import com.example.realestate.R;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class DateTimeUtils {
    public static String getCurrentSqlTimestamp() {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        return sdf.format(new Date());
    }

    public static Date sqlTimestampToDate(String timestamp) {
        Date ret = null;
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

        try {
            ret = sdf.parse(timestamp);

        } catch (ParseException e) {
            e.printStackTrace();
        }

        return ret;
    }

    public static String formatSqlTimestamp(String timestamp, int dateStyle, int timeStyle) {
        String ret = timestamp;
        SimpleDateFormat srcFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        DateFormat targetFormat = DateFormat.getDateTimeInstance(dateStyle, timeStyle);

        try {
            ret = targetFormat.format(srcFormat.parse(timestamp));
        }
        catch (ParseException e) {
            e.printStackTrace();
        }

        return ret;
    }

    public static String formatSqlTimestamp(String timestamp) {
        return formatSqlTimestamp(timestamp, DateFormat.MEDIUM, DateFormat.SHORT);
    }

    public static String formatDateTime(Date date, int dateStyle, int timeStyle) {
        DateFormat df = DateFormat.getDateTimeInstance(dateStyle, timeStyle);
        return df.format(date);
    }

    public static String formatDatetime(Date date) {
        return formatDateTime(date, DateFormat.MEDIUM, DateFormat.SHORT);
    }

    public static int getDueIndicatorColor(String timestamp) {
        Date date = sqlTimestampToDate(timestamp);

        if (date == null) {
            return 0;
        }

        long dueMills = date.getTime();
        long diff = dueMills - System.currentTimeMillis();

        // 4 hours
        if (diff <= 4 * 60 * 60 * 1000) {
            return R.color.branding_red;
        }
        else if (diff > 4 * 60 * 60 * 1000 && diff < 24 * 60 * 60 * 1000) {
            return R.color.branding_yellow_dark;
        }

        return R.color.branding_blue;
    }
}
